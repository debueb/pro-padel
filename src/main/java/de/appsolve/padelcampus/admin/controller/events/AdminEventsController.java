/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.events;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.constants.EventType;
import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.data.GameData;
import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.dao.GameDAOI;
import de.appsolve.padelcampus.db.dao.generic.GenericDAOI;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.dao.TeamDAOI;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Game;
import de.appsolve.padelcampus.db.model.Participant;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import de.appsolve.padelcampus.utils.EventsUtil;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import de.appsolve.padelcampus.utils.RankingUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/events")
public class AdminEventsController extends AdminBaseController<Event>{
    
    @Autowired
    EventDAOI eventDAO;
    
    @Autowired
    GameDAOI gameDAO;
    
    @Autowired
    GenericDAOI<Participant> participantDAO;
    
    @Autowired
    TeamDAOI teamDAO;
    
    @Autowired
    PlayerDAOI playerDAO;
    
    @Autowired
    RankingUtil rankingUtil;
    
    @Autowired
    EventsUtil eventsUtil;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));
    
        binder.registerCustomEditor(Set.class, "participants", new CustomCollectionEditor(Set.class) {
            @Override
            protected Object convertElement(Object element) {
                Long id = Long.parseLong((String) element);
                return participantDAO.findById(id);
            }
        });
    }
    
    @Override
    @RequestMapping(value={"add", "edit/{modelId}"}, method=POST)
    public ModelAndView postEditView(@ModelAttribute("Model") Event model, HttpServletRequest request, BindingResult result){
        validator.validate(model, result);
        
        //prevent removal of a team if it has already played a game
        if (model.getId()!=null){
            Event existingEvent = eventDAO.findByIdFetchWithParticipants(model.getId());
            if (!existingEvent.getParticipants().equals(model.getParticipants())){
                for (Participant participant: existingEvent.getParticipants()){
                    if (!model.getParticipants().contains(participant)){
                        List<Game> existingGames = gameDAO.findByParticipantAndEventWithScoreOnly(participant, model);
                        if (!existingGames.isEmpty()){
                            result.reject("TeamHasAlreadyPlayedInEvent", new Object[]{participant.toString(), existingGames.size(), model.toString()}, null);
                        }
                    }
                }
            }
        }
        
        if (result.hasErrors()){
            return getEditView(model);
        }
        model = getDAO().saveOrUpdate(model);
        
        
        //generate games
        List<Game> eventGames = gameDAO.findByEvent(model);
        switch (model.getEventType()){
            case SingleRoundRobin:
                
                //remove games that have not been played yet
                Iterator<Game> eventGameIterator = eventGames.iterator();
                while (eventGameIterator.hasNext()){
                    Game game = eventGameIterator.next();
                    if (game.getGameSets().isEmpty()){                
                        eventGameIterator.remove();
                        gameDAO.deleteById(game.getId());
                    }
                }
                
                
                for (Participant firstParticipant: model.getParticipants()){
                    for (Participant secondParticipant: model.getParticipants()){
                        if (!firstParticipant.equals(secondParticipant)){
                            boolean gameExists = false;
                            for (Game game: eventGames){
                                if (game.getParticipants().contains(firstParticipant) && game.getParticipants().contains(secondParticipant)){
                                    gameExists = true;
                                    break;
                                }
                            }
                            if (!gameExists){
                                Game game = new Game();
                                game.setEvent(model);
                                Set<Participant> gameParticipants = new LinkedHashSet<>();
                                gameParticipants.add(firstParticipant);
                                gameParticipants.add(secondParticipant);
                                game.setParticipants(gameParticipants);
                                gameDAO.saveOrUpdate(game);
                                eventGames.add(game);
                            }
                        }
                    }
                }
                break;
                
            case Knockout:
                //check min number of participants
                if (model.getParticipants().size()<3){
                    result.addError(new ObjectError("participants", msg.get("PleaseSelectAtLeast3Participants")));
                    return getEditView(model);
                }
                
                //determine number of games per round
                int numGamesPerRound = Integer.highestOneBit(model.getParticipants().size()-1);
                
                //handle udpate operations for existing events
                int numExistingGamesPerRound = 0;
                if (!eventGames.isEmpty()){
                    for (Game game: eventGames){
                        Integer round = game.getRound();
                        if (round!=null && round==0){
                            numExistingGamesPerRound++;
                        }
                    }
                    if (numExistingGamesPerRound!=numGamesPerRound){
                        result.addError(new ObjectError("id", msg.get("CannotChangeNumberOfGames")));
                        return getEditView(model);
                    } else {
                        return redirectToDraws(model);
                    }
                }
                
                //this is a new event
                
                //determine ranking
                Participant firstParticipant = model.getParticipants().iterator().next();
                SortedMap<Participant, BigDecimal> ranking = new TreeMap<>();
                if (firstParticipant instanceof Player){
                    ranking = rankingUtil.getRanking(model.getGender(), model.getPlayers());
                } else if (firstParticipant instanceof Team){
                    List<Team> teams = new ArrayList<>();
                    for (Participant p: model.getParticipants()){
                        Team team = (Team) p;
                        teams.add(teamDAO.findByIdFetchWithPlayers(team.getId()));
                    }
                    ranking = rankingUtil.getTeamRanking(model.getGender(), teams);
                }
                ArrayList<Participant> participants =  new ArrayList<>(ranking.keySet());
                
                //determine seed positions
                List<Integer> seedingPositions = getSeedPositions(participants);
                
                
                //fill up empty spots with bye's
                for (int i=model.getParticipants().size(); i< numGamesPerRound*2; i++){
                    participants.add(null);
                }
                
                //create games
                int round=0;
                SortedMap<Integer, List<Game>> roundGames = new TreeMap<>();
                while (numGamesPerRound>=1){
                    List<Game> games = new ArrayList<>();
                    for (int i=0; i<numGamesPerRound; i++){
                        Game game = new Game();
                        game.setEvent(model);
                        game.setRound(round);
                        game = gameDAO.saveOrUpdate(game);
                        
                        if (round==0){
                            //set participants
                            Set<Participant> gameParticipants = new HashSet<>();
                            addParticipants(gameParticipants, participants.get(seedingPositions.get(i*2)));
                            addParticipants(gameParticipants, participants.get(seedingPositions.get(i*2+1)));
                            game.setParticipants(gameParticipants);
                        } else {
                            //set game chain
                            List<Game> previousRoundGames = roundGames.get(round-1);
                            Game first = previousRoundGames.get(i*2);
                            Game second = previousRoundGames.get(i*2+1);
                            first.setNextGame(game);
                            second.setNextGame(game);
                            gameDAO.saveOrUpdate(first);
                            gameDAO.saveOrUpdate(second);
                            
                            
                            if (round==1){
                                //advance seeds that have bye's
                                if (first.getParticipants().size()==1){
                                    game.setParticipants(new HashSet<>(first.getParticipants()));
                                }
                                if (second.getParticipants().size()==1){
                                    Set<Participant> existingParticipants = game.getParticipants();
                                    if (existingParticipants == null){
                                        existingParticipants = new HashSet<>();
                                    }
                                    existingParticipants.addAll(new HashSet<>(second.getParticipants()));
                                    game.setParticipants(existingParticipants);
                                }
                            }
                            
                        }
                        
                        if (numGamesPerRound == 1){
                            //TODO: when we are in the final, check if we also play for third place
                        }
                        
                        game = gameDAO.saveOrUpdate(game);
                        games.add(game);
                    }
                    roundGames.put(round, games);
                    numGamesPerRound = numGamesPerRound/2;
                    round++;
                }
                return redirectToDraws(model);
            default:
                result.addError(new ObjectError("id", "Unsupported event type "+model.getEventType()));
                return getEditView(model);
        }
        
        
        return redirectToIndex(request);
    }
    
    @RequestMapping(value={"edit/{eventId}/draws"}, method=GET)
    public ModelAndView getDraws(@PathVariable("eventId") Long eventId, HttpServletRequest request){
        ModelAndView mav = getDrawsView(eventId);
        return mav;
    }
    
    @RequestMapping(value={"edit/{eventId}/draws/game/{gameId}"}, method=GET)
    public ModelAndView getDrawsGame(@PathVariable("eventId") Long eventId, @PathVariable("gameId") Long gameId, HttpServletRequest request){
        Game game = gameDAO.findById(gameId);
        GameData gameData = new GameData(game);
        ModelAndView mav = getDrawsGameView(eventId, gameData);
        return mav;
    }
    
    @RequestMapping(value={"edit/{eventId}/draws/game/{gameId}"}, method=POST)
    public ModelAndView postDrawsGame(@PathVariable("eventId") Long eventId, @RequestParam(value="id", required = true) Long id, @RequestParam(value="participant1", required = true) Long participant1, @RequestParam(value="participant2", required = false) Long participant2){
        Game game = gameDAO.findByIdFetchWithNextGame(id);
        
        Game nextGame = game.getNextGame();
        if (nextGame!=null){
            nextGame.getParticipants().clear();
            gameDAO.saveOrUpdate(nextGame);
        }
        
        game.getParticipants().clear();
        Participant p1 = participantDAO.findById(participant1);
        game.getParticipants().add(p1);
        if (participant2 != null){
            Participant p2 = participantDAO.findById(participant2);
            game.getParticipants().add(p2);
        } 
        
        if (nextGame!=null && game.getParticipants().size()==1){
            nextGame.getParticipants().add(p1);
            gameDAO.saveOrUpdate(nextGame);
        }
        
        gameDAO.saveOrUpdate(game);
        return new ModelAndView("redirect:/admin/events/edit/"+eventId+"/draws");
    }
    
    @Override
    protected ModelAndView getDeleteView(Event model) {
        return new ModelAndView("/admin/events/delete", "Model", model);
    }
    
    @Override
    @RequestMapping(value = "/{id}/delete", method = POST)
    public ModelAndView postDelete(HttpServletRequest request, @PathVariable("id") Long id){
        try {
            Event event = eventDAO.findByIdFetchWithGames(id);
            event.getGames().clear();
            eventDAO.saveOrUpdate(event);
            
            eventDAO.deleteById(event.getId());
        } catch (DataIntegrityViolationException e){
            Event model = (Event)getDAO().findById(id);
            log.warn("Attempt to delete "+model+" failed due to "+e);
            ModelAndView deleteView = getDeleteView(model);
            deleteView.addObject("error", msg.get("CannotDeleteDueToRefrence", new Object[]{model.toString()}));
            return deleteView;
        }
        return redirectToIndex(request);
    }
    
    @Override
    protected ModelAndView getEditView(Event event) {
        ModelAndView mav = new ModelAndView("admin/events/edit", "Model", event);
        mav.addObject("EventParticipants", event.getParticipants());
        List<Team> allTeams = teamDAO.findAll();
        allTeams.removeAll(event.getParticipants());
        mav.addObject("AllTeams", allTeams);
        List<Player> allPlayers = playerDAO.findAll();
        allPlayers.removeAll(event.getParticipants());
        mav.addObject("AllPlayers", allPlayers);
        mav.addObject("EventTypes", EventType.values());
        mav.addObject("Genders", Gender.values());
        return mav;
    }
    
    private ModelAndView getDrawsView(Long eventId) {
        Event event = eventDAO.findByIdFetchWithGames(eventId);
        SortedMap<Integer, List<Game>> roundGames = eventsUtil.getRoundGames(event);
        ModelAndView mav = new ModelAndView("admin/events/draws", "Model", event);
        mav.addObject("RoundGameMap", roundGames);
        return mav;
    }
    
    private ModelAndView getDrawsGameView(Long eventId, GameData gameData) {
        Event event = eventDAO.findByIdFetchWithParticipants(eventId);
        ModelAndView mav = new ModelAndView("admin/events/game");
        mav.addObject("Event", event);
        mav.addObject("Model", gameData);
        return mav;
    }
    
    @Override
    public GenericDAOI<Event> getDAO() {
        return eventDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/events";
    }
    
    @Override
    public List<Event> findAll(){
        return eventDAO.findAllFetchWithParticipants();
    }
    
    @Override
    public Event findById(Long id){
        return eventDAO.findByIdFetchWithParticipants(id);
    }

    private void addParticipants(Set<Participant> gameParticipants, Participant p) {
        if (p != null){
            gameParticipants.add(p);
        }
    }

    private List<Integer> getSeedPositions(List<Participant> participants) {
        Double numberOfDivisionRuns = Math.log(participants.size()) / Math.log(2)-1;
        List<Integer> seedingPositions = new ArrayList<>();
        seedingPositions.add(0);
        seedingPositions.add(1);
        for (int divisionRun=0; divisionRun<numberOfDivisionRuns; divisionRun++){
            int size = seedingPositions.size();
            List<Integer> newSeeedingPositions = new ArrayList<>();
            for (Integer position: seedingPositions){
                newSeeedingPositions.add(position);
                newSeeedingPositions.add(size*2-1-position);
            }
            seedingPositions = newSeeedingPositions;
        }
        return seedingPositions;
    }

    private ModelAndView redirectToDraws(Event model) {
        return new ModelAndView("redirect:/admin/events/edit/"+model.getId()+"/draws");
    }
}
