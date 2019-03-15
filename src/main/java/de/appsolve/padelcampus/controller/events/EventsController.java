/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.events;

import de.appsolve.padelcampus.comparators.EventByStartDateComparator;
import de.appsolve.padelcampus.comparators.GameByStartDateComparator;
import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.constants.EventType;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.data.AddTeamGame;
import de.appsolve.padelcampus.data.ScoreEntry;
import de.appsolve.padelcampus.db.dao.*;
import de.appsolve.padelcampus.db.model.*;
import de.appsolve.padelcampus.exceptions.ResourceNotFoundException;
import de.appsolve.padelcampus.spring.CommunityPropertyEditor;
import de.appsolve.padelcampus.spring.PlayerPropertyEditor;
import de.appsolve.padelcampus.utils.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/events")
public class EventsController extends BaseController {

    private static final Logger LOG = Logger.getLogger(EventsController.class);
    @Autowired
    protected Validator validator;
    @Autowired
    ModuleDAOI moduleDAO;
    @Autowired
    EventDAOI eventDAO;
    @Autowired
    GameDAOI gameDAO;
    @Autowired
    GameSetDAOI gameSetDAO;
    @Autowired
    TeamDAOI teamDAO;
    @Autowired
    EventsUtil eventsUtil;
    @Autowired
    GameUtil gameUtil;
    @Autowired
    RankingUtil rankingUtil;
    @Autowired
    SessionUtil sessionUtil;
    @Autowired
    PlayerPropertyEditor playerPropertyEditorEditor;
    @Autowired
    CommunityPropertyEditor communityPropertyEditor;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Player.class, playerPropertyEditorEditor);
        binder.registerCustomEditor(Community.class, communityPropertyEditor);
    }

    @RequestMapping("{moduleTitle}")
    public ModelAndView getEvent(@PathVariable("moduleTitle") String moduleTitle) {
        Module module = moduleDAO.findByUrlTitle(moduleTitle);
        if (module == null) {
            throw new ResourceNotFoundException();
        }
        List<Event> events = eventDAO.findAllActive();
        List<Event> currentEvents = new ArrayList<>();
        List<Event> pastEvents = new ArrayList<>();
        Iterator<Event> iterator = events.iterator();
        while (iterator.hasNext()) {
            Event event = iterator.next();
            if (event.getEventGroup() == null || !module.getEventGroups().contains(event.getEventGroup())) {
                iterator.remove();
            } else if (new LocalDate(Constants.DEFAULT_TIMEZONE).compareTo(event.getEndDate()) <= 0) {
                currentEvents.add(event);
            } else {
                pastEvents.add(event);
            }
        }
        Collections.sort(currentEvents, new EventByStartDateComparator());
        Collections.sort(pastEvents, new EventByStartDateComparator(true));

        ModelAndView mav = new ModelAndView("events/index");
        if (module.getShowEventScores()) {
            List<Game> games = new ArrayList<>();
            for (Event evt : currentEvents) {
                if (evt.getEventType().equals(EventType.PullRoundRobin)) {
                    Event event = eventDAO.findByIdFetchWithGames(evt.getId());
                    games.addAll(event.getGames());
                }
            }
            mav.addObject("ScoreEntries", rankingUtil.getPullResults(games));
        }

        mav.addObject("CurrentEvents", currentEvents);
        mav.addObject("PastEvents", pastEvents);
        mav.addObject("Module", module);
        return mav;
    }

    @RequestMapping("event/{eventId}")
    public ModelAndView getEvent(@PathVariable("eventId") Long eventId) {
        Event event = eventDAO.findById(eventId);
        if (event == null) {
            throw new ResourceNotFoundException();
        }
        ModelAndView mav = new ModelAndView("events/event", "Model", event);
        return mav;
    }

    @RequestMapping("event/{eventId}/participants")
    public ModelAndView getEventParticipants(@PathVariable("eventId") Long eventId) {
        Event event = eventDAO.findByIdFetchWithParticipantsAndPlayers(eventId);
        if (event == null) {
            throw new ResourceNotFoundException();
        }
        if (event.getEventType().equals(EventType.FriendlyGames)) {
            //for friendly games get the participants from the games
            List<Game> eventGames = gameDAO.findByEventWithPlayers(event);
            for (Game game : eventGames) {
                event.getParticipants().addAll(game.getParticipants());
            }
        }
        List<Ranking> rankingMap = new ArrayList<>();
        if (!event.getParticipants().isEmpty()) {
            Participant participant = event.getParticipants().iterator().next();
            if (participant instanceof Player) {
                rankingMap = rankingUtil.getPlayerRanking(event.getPlayers(), LocalDate.now());
            } else {
                rankingMap = rankingUtil.getTeamRanking(event.getTeams(), LocalDate.now());
            }
        }
        ModelAndView mav = new ModelAndView("events/participants");
        mav.addObject("Model", event);
        mav.addObject("RankingMap", rankingMap);
        return mav;
    }

    @RequestMapping("event/{eventId}/communities")
    public ModelAndView getEventCommunities(@PathVariable("eventId") Long eventId) {
        Event event = eventDAO.findByIdFetchWithParticipantsAndCommunities(eventId);
        if (event == null) {
            throw new ResourceNotFoundException();
        }
        ModelAndView mav = new ModelAndView("events/communityroundrobin/participants");
        mav.addObject("Model", event);
        return mav;
    }

    @RequestMapping("event/{eventId}/communitygames")
    public ModelAndView getEventCommunityGames(@PathVariable("eventId") Long eventId) {
        Event event = eventDAO.findByIdFetchWithGames(eventId);
        if (event == null) {
            throw new ResourceNotFoundException();
        }
        Map<List<Community>, Map<Game, String>> communityGameMap = new HashMap<>();
        List<Game> allGames = gameDAO.findByEvent(event);
        allGames.forEach(game -> {
            Set<Participant> participants = game.getParticipants();
            List<Community> communities = new ArrayList<>();
            participants.forEach(participant -> {
                if (participant instanceof Team) {
                    Team team = (Team) participant;
                    if (team.getCommunity() != null) {
                        communities.add(team.getCommunity());
                    }
                }
            });
            if (communities.size() == 2) {
                Optional<List<Community>> existingCommunityList = communityGameMap.keySet().stream().filter(communities1 -> communities1.containsAll(communities)).findFirst();
                if (existingCommunityList.isPresent()) {
                    List<Community> communityList = existingCommunityList.get();
                    Map<Game, String> games = communityGameMap.get(communityList);
                    games.put(game, gameUtil.getGameResult(game, findFirstParticipant(participants, communityList), false));
                    communityGameMap.put(communityList, games);
                } else {
                    Map<Game, String> gameMap = new HashMap<>();
                    gameMap.put(game, gameUtil.getGameResult(game, findFirstParticipant(participants, communities), false));
                    communityGameMap.put(communities, gameMap);
                }
            }
        });
        ModelAndView mav = new ModelAndView("events/communityroundrobin/communitygames");
        mav.addObject("Model", event);
        mav.addObject("CommunityGameMap", communityGameMap);
        return mav;
    }

    @RequestMapping("event/{eventId}/pullgames")
    public ModelAndView getEventPullGames(@PathVariable("eventId") Long eventId) {
        Event event = eventDAO.findByIdFetchWithGames(eventId);
        if (event == null) {
            throw new ResourceNotFoundException();
        }
        ModelAndView mav = new ModelAndView("events/pullroundrobin/pullgames");
        mav.addObject("Model", event);
        mav.addObject("GameResultMap", gameUtil.getGameResultMap(event.getGames(), new GameByStartDateComparator(true)));
        return mav;
    }

    @RequestMapping("event/{eventId}/groupgames")
    public ModelAndView getEventGroupGames(@PathVariable Long eventId) {
        return getGroupGameView(eventId, null);
    }

    @RequestMapping("event/{eventId}/groupgames/{round}")
    public ModelAndView getEventGroupGames(@PathVariable Long eventId, @PathVariable Integer round) {
        return getGroupGameView(eventId, round);
    }

    @RequestMapping("event/{eventId}/knockoutgames")
    public ModelAndView getEventKnockoutGames(@PathVariable("eventId") Long eventId) {
        Event event = eventDAO.findByIdFetchWithGames(eventId);
        if (event == null) {
            throw new ResourceNotFoundException();
        }
        SortedMap<Integer, List<Game>> groupGameMap = eventsUtil.getGroupGameMap(event);
        SortedMap<Integer, List<Game>> roundGameMap = eventsUtil.getRoundGameMap(event);
        if (roundGameMap.isEmpty()) {
            return new ModelAndView("events/groupknockout/groupgamesendinfo", "Model", event);
        }
        ModelAndView mav = getKnockoutView(event, roundGameMap);
        mav.addObject("GroupGameMap", groupGameMap);
        return mav;
    }

    @RequestMapping(method = GET, value = "event/{eventId}/score")
    public ModelAndView getScore(@PathVariable("eventId") Long eventId, HttpServletRequest request) {
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        if (event == null) {
            throw new ResourceNotFoundException();
        }
        SortedMap<Community, ScoreEntry> communityScoreMap = new TreeMap<>();
        Set<Participant> teams = new HashSet<>();
        event.getGames().forEach(game -> game.getParticipants().forEach(participant -> {
            if (participant instanceof Team) {
                teams.add(participant);
            }
        }));
        List<ScoreEntry> scores = rankingUtil.getScores(teams, event.getGames());
        for (ScoreEntry scoreEntry : scores) {
            Team team = (Team) scoreEntry.getParticipant();
            ScoreEntry communityScore = communityScoreMap.get(team.getCommunity());
            if (communityScore == null) {
                communityScore = new ScoreEntry();
            }
            communityScore.add(scoreEntry);
            communityScoreMap.put(team.getCommunity(), communityScore);
        }
        SortedMap<Community, ScoreEntry> communityScoreMapSorted = SortUtil.sortMap(communityScoreMap, true);
        ModelAndView scoreView = new ModelAndView("events/communityroundrobin/score");
        scoreView.addObject("Model", event);
        scoreView.addObject("CommunityScoreMap", communityScoreMapSorted);
        return scoreView;
    }

    @RequestMapping(value = {"edit/{eventId}/addpullgame"}, method = GET)
    public ModelAndView getAddPullGame(@PathVariable Long eventId, HttpServletRequest request) {
        Player user = sessionUtil.getUser(request);
        if (user == null) {
            return getLoginView(request, request.getRequestURI());
        }
        Event event = eventDAO.findByIdFetchWithParticipants(eventId);
        return getAddPullGameView(event, new AddTeamGame());
    }

    @RequestMapping(value = {"edit/{eventId}/addpullgame"}, method = POST)
    public ModelAndView postAddPullGame(
            HttpServletRequest request,
            @PathVariable("eventId") Long eventId,
            @ModelAttribute("Model") AddTeamGame addTeamGame,
            @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
            BindingResult bindingResult) {
        Player user = sessionUtil.getUser(request);
        if (user == null) {
            return getLoginView(request, request.getRequestURI());
        }
        Event event = eventDAO.findByIdFetchWithParticipants(eventId);
        validator.validate(addTeamGame, bindingResult);
        if (bindingResult.hasErrors()) {
            return getAddPullGameView(event, addTeamGame);
        }
        if (!Collections.disjoint(addTeamGame.getTeams().get(0).getPlayers(), addTeamGame.getTeams().get(1).getPlayers())) {
            bindingResult.addError(new ObjectError("id", msg.get("ChooseDistinctPlayers")));
            return getAddPullGameView(event, addTeamGame);
        }
        List<Participant> teams = new ArrayList<>();
        teams.add(teamDAO.findOrCreateTeam(addTeamGame.getTeams().get(0).getPlayers()));
        teams.add(teamDAO.findOrCreateTeam(addTeamGame.getTeams().get(1).getPlayers()));
        List<Game> eventGames = gameDAO.findByEventWithPlayers(event);
        for (Game game : eventGames) {
            if (game.getParticipants().containsAll(teams)) {
                bindingResult.addError(new ObjectError("id", msg.get("GameAlreadyExists")));
                return getAddPullGameView(event, addTeamGame);
            }
        }
        saveGame(event, teams, request);

        if (!StringUtils.isEmpty(redirectUrl)) {
            return new ModelAndView("redirect:/" + redirectUrl);
        }
        return new ModelAndView("redirect:/events/event/" + event.getId() + "/pullgames");
    }

    @RequestMapping(value = {"edit/{eventId}/addfriendlygame"}, method = GET)
    public ModelAndView getAddFriendlyGame(@PathVariable("eventId") Long eventId, HttpServletRequest request) {
        Player user = sessionUtil.getUser(request);
        if (user == null) {
            return getLoginView(request, request.getRequestURI());
        }
        Event event = eventDAO.findByIdFetchWithParticipants(eventId);
        return getAddFriendlyGameView(event, new AddTeamGame());
    }

    @RequestMapping(value = {"edit/{eventId}/addfriendlygame"}, method = POST)
    public ModelAndView postAddFriendlyGame(
            @PathVariable("eventId") Long eventId,
            @ModelAttribute("Model") AddTeamGame addTeamGame,
            @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
            BindingResult bindingResult,
            HttpServletRequest request) {
        Player user = sessionUtil.getUser(request);
        if (user == null) {
            return getLoginView(request, request.getRequestURI());
        }
        Event event = eventDAO.findByIdFetchWithParticipants(eventId);
        validator.validate(addTeamGame, bindingResult);
        if (bindingResult.hasErrors()) {
            return getAddFriendlyGameView(event, addTeamGame);
        }
        if (!Collections.disjoint(addTeamGame.getTeams().get(0).getPlayers(), addTeamGame.getTeams().get(1).getPlayers())) {
            bindingResult.addError(new ObjectError("id", msg.get("ChooseDistinctPlayers")));
            return getAddFriendlyGameView(event, addTeamGame);
        }
        List<Participant> teams = new ArrayList<>();
        teams.add(teamDAO.findOrCreateTeam(addTeamGame.getTeams().get(0).getPlayers()));
        teams.add(teamDAO.findOrCreateTeam(addTeamGame.getTeams().get(1).getPlayers()));

        saveGame(event, teams, request);

        if (!StringUtils.isEmpty(redirectUrl)) {
            return new ModelAndView("redirect:/" + redirectUrl);
        }
        return new ModelAndView("redirect:/events/event/" + event.getId() + "/pullgames");
    }

    @RequestMapping(value = {"edit/{eventId}/addcommunitygame"}, method = GET)
    public ModelAndView getAddCommunityGame(@PathVariable Long eventId, HttpServletRequest request) {
        Player user = sessionUtil.getUser(request);
        if (user == null) {
            return getLoginView(request, request.getRequestURI());
        }
        Event event = eventDAO.findByIdFetchWithParticipantsAndCommunities(eventId);
        return getAddCommunityGameView(event, new AddTeamGame());
    }

    @RequestMapping(value = {"edit/{eventId}/addcommunitygame"}, method = POST)
    public ModelAndView postAddCommunityGame(
            HttpServletRequest request,
            @PathVariable("eventId") Long eventId,
            @ModelAttribute("Model") AddTeamGame addTeamGame,
            @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
            BindingResult bindingResult) {
        Event event = eventDAO.findByIdFetchWithParticipants(eventId);
        if (event == null) {
            throw new ResourceNotFoundException();
        }
        try {
            Player user = sessionUtil.getUser(request);
            if (user == null) {
                return getLoginView(request, request.getRequestURI());
            }
            validator.validate(addTeamGame, bindingResult);
            if (bindingResult.hasErrors()) {
                return getAddCommunityGameView(event, addTeamGame);
            }
            Team team0 = addTeamGame.getTeams().get(0);
            Team team1 = addTeamGame.getTeams().get(1);
            if (team0.getPlayers().size() != 2 || team1.getPlayers().size() != 2) {
                throw new Exception(msg.get("ChooseTwoPlayersPerTeam"));
            }
            if (!Collections.disjoint(team0.getPlayers(), team1.getPlayers())) {
                throw new Exception(msg.get("ChooseDistinctPlayers"));
            }
            if (team0.getCommunity().equals(team1.getCommunity())) {
                throw new Exception(msg.get("ChooseDistinctCommunities"));
            }
            List<Participant> teams = new ArrayList<>();
            teams.add(findOrUpdateTeam(team0));
            teams.add(findOrUpdateTeam(team1));
            List<Game> eventGames = gameDAO.findByEventWithPlayers(event);
            for (Game game : eventGames) {
                if (game.getParticipants().containsAll(teams)) {
                    bindingResult.addError(new ObjectError("id", msg.get("GameAlreadyExists")));
                    return getAddCommunityGameView(event, addTeamGame);
                }
            }
            saveGame(event, teams, request);

            if (!StringUtils.isEmpty(redirectUrl)) {
                return new ModelAndView("redirect:/" + redirectUrl);
            }
            return new ModelAndView("redirect:/events/event/" + event.getId() + "/communitygames");
        } catch (Exception e) {
            bindingResult.addError(new ObjectError("id", e.getMessage()));
            return getAddCommunityGameView(event, addTeamGame);
        }
    }

    private Participant findOrUpdateTeam(Team team) {
        Team newOrExistingTeam = teamDAO.findOrCreateTeam(team.getPlayers());
        newOrExistingTeam.setCommunity(team.getCommunity());
        return teamDAO.saveOrUpdate(newOrExistingTeam);
    }

    private ModelAndView getKnockoutView(Event event, SortedMap<Integer, List<Game>> roundGameMap) {
        ModelAndView mav = new ModelAndView("events/knockout/knockoutgames");
        mav.addObject("Model", event);
        mav.addObject("RoundGameMap", roundGameMap);
        mav.addObject("ParticipantGameGameSetMap", getParticipantGameGameSetMap(roundGameMap));
        return mav;
    }

    private Map<Participant, Map<Game, List<GameSet>>> getParticipantGameGameSetMap(SortedMap<Integer, List<Game>> roundGameMap) {
        Map<Participant, Map<Game, List<GameSet>>> participantGameGameSetMap = new HashMap<>();
        Iterator<List<Game>> gameList = roundGameMap.values().iterator();
        while (gameList.hasNext()) {
            List<Game> games = gameList.next();
            for (Game game : games) {
                for (Participant p : game.getParticipants()) {
                    Map<Game, List<GameSet>> gameGameMap = participantGameGameSetMap.get(p);
                    if (gameGameMap == null) {
                        gameGameMap = new HashMap<>();
                    }
                    Set<GameSet> gameSets = game.getGameSets();
                    List<GameSet> participantGameSets = new ArrayList<>();
                    for (GameSet gs : gameSets) {
                        if (gs.getParticipant().equals(p)) {
                            participantGameSets.add(gs);
                        }
                    }
                    Collections.sort(participantGameSets);
                    gameGameMap.put(game, participantGameSets);
                    participantGameGameSetMap.put(p, gameGameMap);
                }
            }
        }
        return participantGameGameSetMap;
    }

    private ModelAndView getAddFriendlyGameView(Event event, AddTeamGame game) {
        ModelAndView mav = new ModelAndView("events/friendlygames/addfriendlygame");
        mav.addObject("Event", event);
        mav.addObject("Model", game);
        return mav;
    }

    private ModelAndView getAddPullGameView(Event event, AddTeamGame game) {
        ModelAndView mav = new ModelAndView("events/pullroundrobin/addpullgame");
        mav.addObject("Event", event);
        mav.addObject("Model", game);
        return mav;
    }

    private ModelAndView getAddCommunityGameView(Event event, AddTeamGame game) {
        ModelAndView mav = new ModelAndView("events/communityroundrobin/addcommunitygame");
        mav.addObject("Event", event);
        mav.addObject("Model", game);
        return mav;
    }

    private void saveGame(Event event, List<Participant> teams, HttpServletRequest request) {
        Game game = new Game();
        game.setEvent(event);
        game.setParticipants(new HashSet<>(teams));
        game = gameDAO.saveOrUpdate(game);

        Set<GameSet> gameSets = new HashSet<>();
        for (int setNumber = 1; setNumber <= event.getNumberOfSets(); setNumber++) {
            for (int teamNumber = 0; teamNumber < teams.size(); teamNumber++) {
                try {
                    LOG.info("set-" + setNumber + "-team-" + teamNumber);
                    Integer setGames = Integer.parseInt(request.getParameter("set-" + setNumber + "-team-" + teamNumber));
                    if (setGames != -1) {
                        GameSet gameSet = new GameSet();
                        gameSet.setEvent(event);
                        gameSet.setGame(game);
                        gameSet.setSetGames(setGames);
                        gameSet.setSetNumber(setNumber);
                        gameSet.setParticipant(teams.get(teamNumber));
                        gameSet = gameSetDAO.saveOrUpdate(gameSet);
                        gameSets.add(gameSet);
                    }
                } catch (NumberFormatException e) {
                    LOG.info(e);
                }
            }
        }
        game.setGameSets(new HashSet<>(gameSets));
        if (gameSets.isEmpty()) {
            //we use score reporter as an indicator that the game has been played
            game.setScoreReporter(null);
        } else {
            game.setScoreReporter(sessionUtil.getUser(request));
        }
        game.setGameSets(gameSets);
        game.setStartDate(new LocalDate());
        gameDAO.saveOrUpdate(game);
    }

    private ModelAndView getGroupGameView(Long eventId, Integer roundNumber) {
        Event event = eventDAO.findByIdFetchWithGames(eventId);
        if (event == null) {
            throw new ResourceNotFoundException();
        }
        ModelAndView mav = new ModelAndView("events/groupknockout/groupgames", "Model", event);

        event = eventDAO.findByIdFetchWithGames(eventId);
        SortedMap<Integer, List<Game>> groupGameMap = eventsUtil.getGroupGameMap(event, roundNumber);
        SortedMap<Integer, List<Game>> roundGameMap;

        if (roundNumber == null) {
            roundGameMap = eventsUtil.getRoundGameMap(event);
        } else {
            roundGameMap = eventsUtil.getRoundGameMap(event, roundNumber + 1);
        }

        //Group // Participant // Game // GameResult
        SortedMap<Integer, Map<Participant, Map<Game, String>>> groupParticipantGameResultMap = new TreeMap<>();

        Iterator<Map.Entry<Integer, List<Game>>> iterator = groupGameMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, List<Game>> entry = iterator.next();
            Map<Participant, Map<Game, String>> participantGameResultMap = gameUtil.getParticipantGameResultMap(entry.getValue(), false);
            Integer group = entry.getKey();
            groupParticipantGameResultMap.put(group, participantGameResultMap);
        }
        mav.addObject("GroupParticipantGameResultMap", groupParticipantGameResultMap);
        mav.addObject("RoundGameMap", roundGameMap);
        mav.addObject("Round", roundNumber);
        mav.addObject("GameResultMap", gameUtil.getGameResultMap(event.getGames()));
        return mav;
    }

    private Participant findFirstParticipant(Set<Participant> participants, List<Community> communities) {
        for (Participant participant : participants) {
            if (((Team) participant).getCommunity().equals(communities.get(0))) {
                return participant;
            }
        }
        return null;
    }
}
