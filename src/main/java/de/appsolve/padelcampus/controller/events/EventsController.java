/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.events;

import de.appsolve.padelcampus.comparators.EventByStartDateComparator;
import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.constants.EventType;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.data.AddPullGame;
import de.appsolve.padelcampus.data.ScoreEntry;
import de.appsolve.padelcampus.db.dao.*;
import de.appsolve.padelcampus.db.model.*;
import de.appsolve.padelcampus.exceptions.ResourceNotFoundException;
import de.appsolve.padelcampus.spring.PlayerCollectionEditor;
import de.appsolve.padelcampus.utils.EventsUtil;
import de.appsolve.padelcampus.utils.GameUtil;
import de.appsolve.padelcampus.utils.RankingUtil;
import de.appsolve.padelcampus.utils.SessionUtil;
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
import java.util.Map.Entry;

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
    PlayerCollectionEditor playerCollectionEditor;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Set.class, "team1", playerCollectionEditor);
        binder.registerCustomEditor(Set.class, "team2", playerCollectionEditor);
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
        //for friendly games get the participants from the games
        if (event.getEventType().equals(EventType.FriendlyGames)) {
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
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        List<Ranking> rankedParticipants = rankingUtil.getRankedParticipants(event);
        Map<Community, List<Ranking>> communityMap = new HashMap<>();
        rankedParticipants.forEach(ranking -> {
            Participant participant = ranking.getParticipant();
            if (participant instanceof Team) {
                Team team = (Team) participant;
                if (team.getCommunity() != null) {
                    List<Ranking> communityRanking = communityMap.get(team.getCommunity());
                    if (communityRanking == null) {
                        communityRanking = new ArrayList<>();
                    }
                    Ranking teamRanking = rankedParticipants.stream().filter(r -> r.getParticipant().equals(team)).findFirst().get();
                    communityRanking.add(teamRanking);
                    Collections.sort(communityRanking);
                    communityMap.put(team.getCommunity(), communityRanking);
                }
            }
        });
        ModelAndView mav = new ModelAndView("events/communityroundrobin/communities", "Model", event);
        mav.addObject("CommunityMap", communityMap);
        return mav;
    }

    @RequestMapping("event/{eventId}/communitygames")
    public ModelAndView getEventCommunityGames(@PathVariable("eventId") Long eventId) {
        Event event = eventDAO.findByIdFetchWithGames(eventId);
        if (event == null) {
            throw new ResourceNotFoundException();
        }
        ModelAndView mav = new ModelAndView("events/communityroundrobin/communitygames", "Model", event);

        //Community // Participant // Game // GameResult
        SortedMap<Community, Map<Participant, Map<Game, String>>> communityParticipantGameResultMap = new TreeMap<>();

        Map<Participant, Map<Game, String>> participantGameResultMap = gameUtil.getParticipantGameResultMap(event.getGames(), false);
        for (Entry<Participant, Map<Game, String>> entry : participantGameResultMap.entrySet()) {
            Participant p = entry.getKey();
            if (p instanceof Team) {
                Team team = (Team) p;
                Map<Participant, Map<Game, String>> participantMap = communityParticipantGameResultMap.get(team.getCommunity());
                if (participantMap == null) {
                    participantMap = new HashMap<>();
                }
                participantMap.put(p, entry.getValue());
                communityParticipantGameResultMap.put(team.getCommunity(), participantMap);
            }
        }
        mav.addObject("GroupParticipantGameResultMap", communityParticipantGameResultMap);
        mav.addObject("GameResultMap", gameUtil.getGameResultMap(event.getGames()));
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
        mav.addObject("GameResultMap", gameUtil.getGameResultMap(event.getGames()));
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
        List<ScoreEntry> scores = rankingUtil.getScores(event.getParticipants(), event.getGames());
        for (ScoreEntry scoreEntry : scores) {
            Participant p = scoreEntry.getParticipant();
            if (p instanceof Team) {
                Team team = (Team) p;
                ScoreEntry communityScore = communityScoreMap.get(team.getCommunity());
                if (communityScore == null) {
                    communityScore = new ScoreEntry();
                }
                communityScore.add(scoreEntry);
                communityScoreMap.put(team.getCommunity(), communityScore);
            }
        }

        ModelAndView scoreView = new ModelAndView("events/communityroundrobin/score");
        scoreView.addObject("Model", event);
        scoreView.addObject("CommunityScoreMap", communityScoreMap);
        return scoreView;
    }

    @RequestMapping(value = {"edit/{eventId}/addpullgame"}, method = GET)
    public ModelAndView getAddPullGame(@PathVariable Long eventId, HttpServletRequest request) {
        Player user = sessionUtil.getUser(request);
        if (user == null) {
            return getLoginView(request, request.getRequestURI());
        }
        Event event = eventDAO.findByIdFetchWithParticipants(eventId);
        return getAddPullGameView(event, new AddPullGame());
    }

    @RequestMapping(value = {"edit/{eventId}/addpullgame"}, method = POST)
    public ModelAndView postAddPullGame(
            HttpServletRequest request,
            @PathVariable("eventId") Long eventId,
            @ModelAttribute("Model") AddPullGame addPullGame,
            @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
            BindingResult bindingResult) {
        Player user = sessionUtil.getUser(request);
        if (user == null) {
            return getLoginView(request, request.getRequestURI());
        }
        Event event = eventDAO.findByIdFetchWithParticipants(eventId);
        validator.validate(addPullGame, bindingResult);
        if (bindingResult.hasErrors()) {
            return getAddPullGameView(event, addPullGame);
        }
        if (!Collections.disjoint(addPullGame.getTeam1(), addPullGame.getTeam2())) {
            bindingResult.addError(new ObjectError("id", msg.get("ChooseDistinctPlayers")));
            return getAddPullGameView(event, addPullGame);
        }
        List<Participant> teams = new ArrayList<>();
        teams.add(teamDAO.findOrCreateTeam(addPullGame.getTeam1()));
        teams.add(teamDAO.findOrCreateTeam(addPullGame.getTeam2()));
        List<Game> eventGames = gameDAO.findByEventWithPlayers(event);
        for (Game game : eventGames) {
            if (game.getParticipants().containsAll(teams)) {
                bindingResult.addError(new ObjectError("id", msg.get("GameAlreadyExists")));
                return getAddPullGameView(event, addPullGame);
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
        return getAddFriendlyGameView(event, new AddPullGame());
    }

    @RequestMapping(value = {"edit/{eventId}/addfriendlygame"}, method = POST)
    public ModelAndView postAddFriendlyGame(
            @PathVariable("eventId") Long eventId,
            @ModelAttribute("Model") AddPullGame addPullGame,
            @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
            BindingResult bindingResult,
            HttpServletRequest request) {
        Player user = sessionUtil.getUser(request);
        if (user == null) {
            return getLoginView(request, request.getRequestURI());
        }
        Event event = eventDAO.findByIdFetchWithParticipants(eventId);
        validator.validate(addPullGame, bindingResult);
        if (bindingResult.hasErrors()) {
            return getAddFriendlyGameView(event, addPullGame);
        }
        if (!Collections.disjoint(addPullGame.getTeam1(), addPullGame.getTeam2())) {
            bindingResult.addError(new ObjectError("id", msg.get("ChooseDistinctPlayers")));
            return getAddFriendlyGameView(event, addPullGame);
        }
        List<Participant> teams = new ArrayList<>();
        teams.add(teamDAO.findOrCreateTeam(addPullGame.getTeam1()));
        teams.add(teamDAO.findOrCreateTeam(addPullGame.getTeam2()));

        saveGame(event, teams, request);

        if (!StringUtils.isEmpty(redirectUrl)) {
            return new ModelAndView("redirect:/" + redirectUrl);
        }
        return new ModelAndView("redirect:/events/event/" + event.getId() + "/pullgames");
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

    private ModelAndView getAddFriendlyGameView(Event event, AddPullGame game) {
        ModelAndView mav = new ModelAndView("events/friendlygames/addfriendlygame");
        mav.addObject("Event", event);
        mav.addObject("Model", game);
        return mav;
    }

    private ModelAndView getAddPullGameView(Event event, AddPullGame game) {
        ModelAndView mav = new ModelAndView("events/pullroundrobin/addpullgame");
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
            for (int teamNumber = 1; teamNumber <= teams.size(); teamNumber++) {
                try {
                    LOG.info("set-" + setNumber + "-team-" + teamNumber + 1);
                    Integer setGames = Integer.parseInt(request.getParameter("set-" + setNumber + "-team-" + teamNumber));
                    if (setGames != -1) {
                        GameSet gameSet = new GameSet();
                        gameSet.setEvent(event);
                        gameSet.setGame(game);
                        gameSet.setSetGames(setGames);
                        gameSet.setSetNumber(setNumber);
                        gameSet.setParticipant(teams.get(teamNumber - 1));
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
        rankingUtil.updateRanking();
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
}
