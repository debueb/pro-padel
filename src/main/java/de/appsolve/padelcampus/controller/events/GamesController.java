/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.events;

import de.appsolve.padelcampus.controller.BaseController;
import static de.appsolve.padelcampus.constants.Constants.FIRST_SET;
import static de.appsolve.padelcampus.constants.Constants.NUMBER_OF_SETS;
import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.dao.GameDAOI;
import de.appsolve.padelcampus.db.dao.GameSetDAOI;
import de.appsolve.padelcampus.db.dao.TeamDAOI;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Game;
import de.appsolve.padelcampus.db.model.GameSet;
import de.appsolve.padelcampus.db.model.Participant;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;
import de.appsolve.padelcampus.utils.SessionUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/games")
public class GamesController extends BaseController{
    
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
    
    @RequestMapping("/game/{gameId}")
    public ModelAndView getGame(@PathVariable("gameId") Long gameId){
        Game game = gameDAO.findById(gameId);
        ModelAndView indexView = new ModelAndView("games/game", "Game", game);
        addGameResultMap(indexView, game);
        return indexView;
    }
    
    @RequestMapping("/game/{gameId}/edit")
    public ModelAndView editGame(@PathVariable("gameId") Long gameId, HttpServletRequest request){
        Player user = sessionUtil.getUser(request);
        if (user == null){
            return getLoginView(request);
        }
        return getEditView(gameId);
    }
    
    @RequestMapping(value="/game/{gameId}/edit", method=POST)
    public ModelAndView postGame(@PathVariable("gameId") Long gameId,HttpServletRequest request){
        
        Player user = sessionUtil.getUser(request);
        if (user == null){
            return getLoginView(request);
        }
        Game game = gameDAO.findById(gameId);
        List<GameSet> gameSets = new ArrayList<>();
        Set<GameSet> gameSetsToRemove = new HashSet<>();
        for (Participant participant: game.getParticipants()){
            for (int set=FIRST_SET; set<=NUMBER_OF_SETS; set++){
                String setGames = request.getParameter(getKey(game, participant, set));
                Integer numSetGames = Integer.parseInt(setGames);
                GameSet gameSet = gameSetDAO.findBy(game, participant, set);
                if (numSetGames>=0){
                    if (gameSet == null){
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
                    if (gameSet!=null){
                        gameSetsToRemove.add(gameSet);
                    }
                }
            }
        }

        //save game sets
        for (GameSet gameSet: gameSets){
            gameSetDAO.saveOrUpdate(gameSet);
        }

        //remove unwanted game sets
        if (gameSetsToRemove.size()>0){
            game.getGameSets().removeAll(gameSetsToRemove);
            gameDAO.saveOrUpdate(game);

            for (GameSet gameSet: gameSetsToRemove){
                //if we do not delete by ID, we get an "removing detached entity exception" presumably because the session object that the entity was retrieved by has been closed
                gameSetDAO.deleteById(gameSet.getId());
            }
        }

        //save game
        game.setGameSets(new LinkedHashSet<>(gameSets));
        game.setScoreReporter(user);
        gameDAO.saveOrUpdate(game);
        return new ModelAndView("redirect:/games/game/"+gameId);
    }
    
    @RequestMapping("/event/{eventId}")
    public ModelAndView getEvent(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        ModelAndView mav = new ModelAndView("games/event", "Event", event);
        return mav;
    }
 
    @RequestMapping("/event/{eventId}/all")
    public ModelAndView getAllGamesForEvent(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        ModelAndView mav = new ModelAndView("games/games", "Games", event.getGames());
        mav.addObject("title", "Spiele "+event.getName());
        addGameResultMap(mav, event.getGames());
        return mav;
    }
 
    @RequestMapping("/event/{eventId}/team/{teamId}")
    public ModelAndView getTeamEvents(@PathVariable("teamId") Long teamId, @PathVariable("eventId") Long eventId){
        Event event = eventDAO.findById(eventId);
        Team team = teamDAO.findById(teamId);
        List<Game> games = gameDAO.findByParticipantAndEvent(team, event);
        ModelAndView mav = new ModelAndView("games/games", "Games", games);
        mav.addObject("title", event.getName());
        mav.addObject("subtitle", team.toString());
        addGameResultMap(mav, games);
        return mav;
    }
    
    @RequestMapping("/team/{teamId}")
    public ModelAndView getTeamEvents(@PathVariable("teamId") Long teamId){
        Team team = teamDAO.findById(teamId);
        List<Game> games = gameDAO.findByParticipant(team);
        ModelAndView mav = new ModelAndView("games/games", "Games", games);
        mav.addObject("title", "Spiele "+team.toString());
        addGameResultMap(mav, games);
        return mav;
    }

    private ModelAndView getEditView(Long gameId) {
        Game game = gameDAO.findById(gameId);
        ModelAndView mav = new ModelAndView("games/edit", "Game", game);
        mav.addObject("GamesMap", getGamesMap(game));
        return mav;
    }

    private String getKey(Game game, Participant participant, int set) {
        return "game-"+game.getId()+"-participant-"+participant.getId()+"-set-"+set;
    }

    private Map<String, GameSet> getGamesMap(Game game) {
        Map<String, GameSet> gamesMap = new LinkedHashMap<>();
        for (Participant participant: game.getParticipants()){
            for (int set=FIRST_SET; set<=NUMBER_OF_SETS; set++){
                GameSet gameSet = gameSetDAO.findBy(game, participant, set);
                if (gameSet!=null){
                    gamesMap.put(getKey(game, participant, set), gameSet);
                }
            }
        }
        return gamesMap;
    }

    private void addGameResultMap(ModelAndView mav, Game game) {
        addGameResultMap(mav, Arrays.asList(new Game[]{game}));
    }
    
    private void addGameResultMap(ModelAndView mav, Collection<Game> games) {
        Map<Game, String> map = new HashMap<>();
        for (Game game : games) {
            List<GameSet> gameSets = new ArrayList<>(game.getGameSets());
            StringBuilder result = new StringBuilder();
            
            Collections.sort(gameSets);
            int gameSetsDisplayed=0;
            for (int set=FIRST_SET; set<gameSets.size(); set++){
                int participantId = 0;
                for (Participant participant: game.getParticipants()){
                    String setGames = "-";
                    for (GameSet gs: gameSets){
                        if (gs.getSetNumber() == set && gs.getParticipant().equals(participant)){
                            setGames = gs.getSetGames()+"";
                            gameSetsDisplayed++;
                            break;
                        }
                    }
                    result.append(setGames);
                    result.append(participantId%2==0 ? ":" : " "); //separate games by colon, sets by space
                    participantId++;
                }
                if (gameSetsDisplayed == gameSets.size()){
                    break; //do not display third set if it was not played
                }
            }
            map.put(game, result.toString());
        }
        mav.addObject("GameResultMap", map);
    }

    private ModelAndView getLoginView(HttpServletRequest request) {
        sessionUtil.setLoginRedirectPath(request, request.getRequestURL().toString());
        return new ModelAndView("redirect:/login");
    }
}
