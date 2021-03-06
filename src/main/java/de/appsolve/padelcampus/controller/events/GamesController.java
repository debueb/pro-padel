/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.events;

import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.data.ScoreEntry;
import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.dao.GameDAOI;
import de.appsolve.padelcampus.db.dao.GameSetDAOI;
import de.appsolve.padelcampus.db.dao.TeamDAOI;
import de.appsolve.padelcampus.db.model.*;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import de.appsolve.padelcampus.utils.GameUtil;
import de.appsolve.padelcampus.utils.RankingUtil;
import de.appsolve.padelcampus.utils.SessionUtil;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static de.appsolve.padelcampus.constants.Constants.FIRST_SET;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/games")
public class GamesController extends BaseController {

    @Autowired
    EventDAOI eventDAO;

    @Autowired
    TeamDAOI teamDAO;

    @Autowired
    GameDAOI gameDAO;

    @Autowired
    GameSetDAOI gameSetDAO;

    @Autowired
    SessionUtil sessionUtil;

    @Autowired
    RankingUtil rankingUtil;

    @Autowired
    GameUtil gameUtil;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));
    }

    @RequestMapping
    public ModelAndView getIndex() {
        ModelAndView mav = new ModelAndView("games/index");
        mav.addObject("Events", eventDAO.findAllActive());
        return mav;
    }

    @RequestMapping("/game/{gameId}")
    public ModelAndView getGame(@PathVariable("gameId") Long gameId) {
        Game game = gameDAO.findByIdFetchWithTeamsAndScoreReporter(gameId);
        ModelAndView indexView = new ModelAndView("games/game", "Game", game);
        indexView.addObject("GameResultMap", gameUtil.getGameResultMap(game));
        return indexView;
    }

    @RequestMapping("/game/{gameId}/edit")
    public ModelAndView editGame(@PathVariable("gameId") Long gameId, HttpServletRequest request) {
        Player user = sessionUtil.getUser(request);
        if (user == null) {
            return getLoginView(request);
        }
        return getEditView(gameId);
    }

    @RequestMapping(value = "/game/{gameId}/edit", method = POST)
    public ModelAndView postGame(
            @PathVariable Long gameId,
            @RequestParam(required = false) String redirectUrl,
            @RequestParam LocalDate startDate,
            HttpServletRequest request) {
        Player user = sessionUtil.getUser(request);
        if (user == null) {
            return getLoginView(request);
        }
        Game game = gameDAO.findByIdFetchWithEventAndNextGame(gameId);
        List<GameSet> gameSets = new ArrayList<>();
        Set<GameSet> gameSetsToRemove = new HashSet<>();
        for (int set = FIRST_SET; set <= game.getEvent().getNumberOfSets(); set++) {
            for (Participant participant : game.getParticipants()) {
                String setGames = request.getParameter(getKey(game, participant, set));
                Integer numSetGames = Integer.parseInt(setGames);
                GameSet gameSet = gameSetDAO.findBy(game, participant, set);
                if (numSetGames >= 0) {
                    if (gameSet == null) {
                        gameSet = new GameSet();
                        gameSet.setGame(game);
                        gameSet.setParticipant(participant);
                        gameSet.setSetNumber(set);
                        gameSet.setEvent(game.getEvent());
                    }
                    gameSet.setSetGames(numSetGames);
                    gameSets.add(gameSet);
                } else {
                    //mark set for deletion in case it exists and user changed it to "-"
                    if (gameSet != null) {
                        gameSetsToRemove.add(gameSet);
                    }
                }
            }
        }

        //save game sets
        for (GameSet gameSet : gameSets) {
            gameSetDAO.saveOrUpdate(gameSet);
        }

        //remove unwanted game sets
        if (gameSetsToRemove.size() > 0) {
            game.getGameSets().removeAll(gameSetsToRemove);
            gameDAO.saveOrUpdate(game);

            for (GameSet gameSet : gameSetsToRemove) {
                //if we do not delete by ID, we get an "removing detached entity exception" presumably because the session object that the entity was retrieved by has been closed
                gameSetDAO.deleteById(gameSet.getId());
            }
        }

        //save game
        game.setGameSets(new LinkedHashSet<>(gameSets));
        if (gameSets.isEmpty()) {
            //we use score reporter as an indicator that the game has been played
            game.setScoreReporter(null);
        } else {
            game.setScoreReporter(user);
        }
        game.setStartDate(startDate);
        gameDAO.saveOrUpdate(game);

        Game nextGame = game.getNextGame();
        if (nextGame != null) {
            //update next game

            //make sure next game does not contain any of the participants of the current game
            nextGame.getParticipants().removeAll(game.getParticipants());

            //determine winner
            Participant winner = null;
            for (Participant p : game.getParticipants()) {
                ScoreEntry score = rankingUtil.getScore(p, Arrays.asList(new Game[]{game}));
                if (score.getMatchesWon() == 1) {
                    winner = p;
                    break;
                }
            }
            if (winner != null) {
                nextGame.getParticipants().add(winner);
            }

            gameDAO.saveOrUpdate(nextGame);
        }
        if (StringUtils.isEmpty(redirectUrl)) {
            return new ModelAndView("redirect:/games/game/" + gameId);
        }
        return new ModelAndView("redirect:/" + redirectUrl);
    }

    @RequestMapping("/event/{eventId}")
    public ModelAndView getEvent(@PathVariable("eventId") Long eventId) {
        Event event = eventDAO.findByIdFetchWithParticipants(eventId);
        ModelAndView mav = new ModelAndView("games/event", "Model", event);
        return mav;
    }

    @RequestMapping("/event/{eventId}/all")
    public ModelAndView getAllGamesForEvent(@PathVariable("eventId") Long eventId) {
        Event event = eventDAO.findByIdFetchWithGames(eventId);
        Map<Participant, Map<Game, String>> participantGameResultMap = gameUtil.getParticipantGameResultMap(event.getGames(), false);
        ModelAndView mav = new ModelAndView("games/all", "ParticipantGameResultMap", participantGameResultMap);
        mav.addObject("title", msg.get("AllGamesIn", new Object[]{event.getName()}));
        mav.addObject("Model", event);
        return mav;
    }

    @RequestMapping("/event/{eventId}/team/{teamUUID}")
    public ModelAndView getTeamEvents(@PathVariable("teamUUID") String teamUUID, @PathVariable("eventId") Long eventId) {
        Event event = eventDAO.findById(eventId);
        Team team = teamDAO.findByUUID(teamUUID);
        List<Game> games = gameDAO.findByParticipantAndEvent(team, event);

        Map<Participant, Map<Game, String>> participantGameResultMap = gameUtil.getParticipantGameResultMap(games, true);
        Iterator<Participant> iterator = participantGameResultMap.keySet().iterator();
        while (iterator.hasNext()) {
            Participant p = iterator.next();
            if (!p.equals(team)) {
                iterator.remove();
            }
        }
        ModelAndView mav = new ModelAndView("games/teamgames", "ParticipantGameResultMap", participantGameResultMap);
        mav.addObject("title", msg.get("GamesWith", new Object[]{team.toString()}));
        mav.addObject("Model", event);
        return mav;
    }

    @RequestMapping("/team/{teamUUID}")
    public ModelAndView getTeamGames(@PathVariable("teamUUID") String teamUUID) {
        Team team = teamDAO.findByUUID(teamUUID);
        List<Game> games = gameDAO.findByParticipant(team);
        ModelAndView mav = new ModelAndView("games/games", "Games", games);
        String title = msg.get("AllGamesWith", new Object[]{team.getName()});
        mav.addObject("title", title);
        mav.addObject("GameResultMap", gameUtil.getGameResultMap(games));
        return mav;
    }

    @RequestMapping("/team/{teamUUID}/event/{eventId}")
    public ModelAndView getTeamGamesByEvent(@PathVariable("teamUUID") String teamUUID, @PathVariable("eventId") Long eventId) {
        Event event = eventDAO.findById(eventId);
        Team team = teamDAO.findByUUID(teamUUID);
        List<Game> games = gameDAO.findByParticipantAndEvent(team, event);
        ModelAndView mav = new ModelAndView("games/games", "Games", games);
        String title = msg.get("AllGamesWithTeamInEvent", new Object[]{team.getName(), event.getName()});
        mav.addObject("title", title);
        mav.addObject("Event", event);
        mav.addObject("GameResultMap", gameUtil.getGameResultMap(games));
        return mav;
    }

    private ModelAndView getEditView(Long gameId) {
        Game game = gameDAO.findByIdFetchWithEventAndTeamsAndScoreReporter(gameId);
        ModelAndView mav = new ModelAndView("games/edit", "Game", game);
        mav.addObject("GamesMap", getGamesMap(game));
        return mav;
    }

    private String getKey(Game game, Participant participant, int set) {
        return "game-" + game.getId() + "-participant-" + participant.getId() + "-set-" + set;
    }

    private Map<String, GameSet> getGamesMap(Game game) {
        Map<String, GameSet> gamesMap = new LinkedHashMap<>();
        for (Participant participant : game.getParticipants()) {
            for (int set = FIRST_SET; set <= game.getEvent().getNumberOfSets(); set++) {
                GameSet gameSet = gameSetDAO.findBy(game, participant, set);
                if (gameSet != null) {
                    gamesMap.put(getKey(game, participant, set), gameSet);
                }
            }
        }
        return gamesMap;
    }
}
