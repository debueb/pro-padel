/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.events;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.constants.EventType;
import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.data.EventGroups;
import de.appsolve.padelcampus.data.GameData;
import de.appsolve.padelcampus.data.ScoreEntry;
import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.dao.GameDAOI;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    BaseEntityDAOI<Participant> participantDAO;
    
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
    
        binder.registerCustomEditor(Set.class, new CustomCollectionEditor(Set.class) {
            @Override
            protected Object convertElement(Object element) {
                Long id = Long.parseLong((String) element);
                return participantDAO.findById(id);
            }
        });
    }
    
    @Override
    public ModelAndView showIndex(HttpServletRequest request, @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable, @RequestParam(required = false, name = "search") String search){
        return super.showIndex(request, pageable, search);
    }
    
    @Override
    @RequestMapping(value={"add", "edit/{modelId}"}, method=POST)
    public ModelAndView postEditView(@ModelAttribute("Model") Event model, HttpServletRequest request, BindingResult result){
        validator.validate(model, result);
        
        if (model.getId()!=null){
            
            //prevent removal of a team if it has already played a game
            Event existingEvent = eventDAO.findByIdFetchWithParticipants(model.getId());
            if (!existingEvent.getParticipants().equals(model.getParticipants())){
                for (Participant participant: existingEvent.getParticipants()){
                    if (!model.getParticipants().contains(participant)){
                        List<Game> existingGames = gameDAO.findByParticipantAndEventWithScoreOnly(participant, model);
                        if (!existingGames.isEmpty()){
                            result.reject("TeamHasAlreadyPlayedInEvent", new Object[]{participant.toString(), existingGames.size(), model.toString()}, null);
                            break;
                        }
                    }
                }
            }
            
            //prevent switching from one event type to another
            if (!existingEvent.getEventType().equals(model.getEventType())){
                result.reject("CannotModifyEventTypeOfExistingEvent");
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
                removeGamesWithoutGameSets(eventGames);
                
                createMissingGames(model, eventGames, model.getParticipants(), null);
                
                break;
                
            case GroupKnockout:
                return redirectToGroupDraws(model);
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
                ArrayList<Participant> participants =  getRankedParticipants(model);
                
                eventsUtil.createKnockoutGames(model, participants);
                
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
    
    @RequestMapping(value={"edit/{eventId}/groupdraws"}, method=GET)
    public ModelAndView getGroupDraws(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        
        return getGroupDrawsView(event, getDefaultEventGroups(event));
    }
    
    @RequestMapping(value={"edit/{eventId}/groupdraws"}, method=POST)
    public ModelAndView postGroupDraws(@PathVariable("eventId") Long eventId, @ModelAttribute("Model") @Valid EventGroups eventGroups, BindingResult bindingResult, HttpServletRequest request){
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        Iterator<Map.Entry<Integer, Set<Participant>>> iterator = eventGroups.getGroupParticipants().entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Integer, Set<Participant>> entry = iterator.next();
            if (entry.getValue() == null){
                bindingResult.reject("PleaseSelectParticipantsForEachGroup");
            }
        }
        
        if (bindingResult.hasErrors()){
            return getGroupDrawsView(event, eventGroups);
        }
        
        //remove games that have not been played yet
        removeGamesWithoutGameSets(event.getGames());
        
        //remove games with teams that are no longer part of a group
        Iterator<Game> gameIterator = event.getGames().iterator();
        while (gameIterator.hasNext()){
            Game game = gameIterator.next();
            Integer groupNumber = game.getGroupNumber();
            if (groupNumber != null){
                Set<Participant> groupParticipants = eventGroups.getGroupParticipants().get(groupNumber);
                if (!groupParticipants.containsAll(game.getParticipants())){
                    gameIterator.remove();
                    gameDAO.deleteById(eventId);
                }
            }
        }
                
        //create missing games
        for (int groupNumber=0; groupNumber<event.getNumberOfGroups(); groupNumber++){
            Set<Participant> groupParticipants = eventGroups.getGroupParticipants().get(groupNumber);
            createMissingGames(event, event.getGames(), groupParticipants, groupNumber);
        }
        
        return redirectToIndex(request);
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
            for (Game game: event.getGames()){
                game.getGameSets().clear();
            }
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
    
     @RequestMapping(method=GET, value="event/{eventId}/groupgamesend")
    public ModelAndView getEventGroupGamesEnd(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findById(eventId);
        return getGroupGamesEndView(event);
    }
    
    @RequestMapping(method=POST, value="event/{eventId}/groupgamesend")
    public ModelAndView saveEventGroupGamesEnd(@PathVariable("eventId") Long eventId, @ModelAttribute("Model") Event dummy, BindingResult result){
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        
        SortedMap<Integer, List<Game>> roundGames = eventsUtil.getRoundGames(event);
        if (!roundGames.isEmpty()){
            result.reject("GroupGamesAlreadyEnded");
            return getGroupGamesEndView(dummy);
        }
        
        SortedMap<Integer, List<Game>> groupGames = eventsUtil.getGroupGames(event);
        Iterator<Map.Entry<Integer, List<Game>>> iterator = groupGames.entrySet().iterator();
        
        //determine best participants of each group
        Map<Integer, List<Participant>> rankedGroupParticipants = new TreeMap<>();
        while (iterator.hasNext()){
            Map.Entry<Integer, List<Game>> entry = iterator.next();
            Integer groupNumber = entry.getKey();
            List<Game> games = entry.getValue();
            
            //determine participant based on games to filter out participants who did not play
            Set<Participant> participants = new HashSet<>();
            List<Game> playedGames = new ArrayList<>();
            for (Game game: games){
                if (!game.getGameSets().isEmpty()){
                    participants.addAll(game.getParticipants());
                    playedGames.add(game);
                }
            }
            
            if (participants.isEmpty() || playedGames.isEmpty()){
                result.reject("CannotEndGroupGames");
                return getGroupGamesEndView(dummy);
            }
            
            //get list of score entries sorted by rank
            List<ScoreEntry> scoreEntries =  rankingUtil.getScores(participants, playedGames);
            for (int groupPos=0; groupPos<event.getNumberOfWinnersPerGroup(); groupPos++){
                List<Participant> rankedParticipants = rankedGroupParticipants.get(groupNumber);
                if (rankedParticipants == null){
                    rankedParticipants = new ArrayList<>();
                }
                Participant p = null;
                try {
                    p = scoreEntries.get(groupPos).getParticipant();
                } catch (IndexOutOfBoundsException e){
                    //could happen when not enough games were played in one group
                }
                rankedParticipants.add(p);
                rankedGroupParticipants.put(groupNumber, rankedParticipants);
            }
        }
        
        //sort participants so that group winners are first
        List<Participant> rankedParticipants = new ArrayList<>();
        for (int groupPos=0; groupPos<event.getNumberOfWinnersPerGroup(); groupPos++){
            for (int group=0; group<event.getNumberOfGroups(); group++){
                rankedParticipants.add(rankedGroupParticipants.get(group).get(groupPos));
            }
        }
        
        eventsUtil.createKnockoutGames(event, rankedParticipants);
        return redirectToDraws(event);
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
    
    private ModelAndView getGroupDrawsView(Event event, EventGroups eventGroups){
        ModelAndView mav = new ModelAndView("admin/events/groupdraws");
        mav.addObject("Event", event);
        mav.addObject("Model", eventGroups);
        return mav;
    }
    
    @Override
    public BaseEntityDAOI<Event> getDAO() {
        return eventDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/events";
    }
    
    @Override
    public Page<Event> findAll(Pageable pageable){
        return eventDAO.findAllFetchWithParticipants(pageable);
    }
    
    @Override
    protected Page<Event> findAllByFuzzySearch(String search) {
        return eventDAO.findAllByFuzzySearch(search, "participants");
    }
    
    @Override
    public Event findById(Long id){
        return eventDAO.findByIdFetchWithParticipants(id);
    }

    private ModelAndView redirectToDraws(Event model) {
        return new ModelAndView("redirect:/admin/events/edit/"+model.getId()+"/draws");
    }

    private ArrayList<Participant> getRankedParticipants(Event model) {
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
        return new ArrayList<>(ranking.keySet());
    }

    private void removeGamesWithoutGameSets(Collection<Game> eventGames) {
        Iterator<Game> eventGameIterator = eventGames.iterator();
        while (eventGameIterator.hasNext()){
            Game game = eventGameIterator.next();
            if (game.getGameSets().isEmpty()){                
                eventGameIterator.remove();
                gameDAO.deleteById(game.getId());
            }
        }
    }

    private ModelAndView redirectToGroupDraws(Event model) {
        return new ModelAndView("redirect:/admin/events/edit/"+model.getId()+"/groupdraws");
    }

    private EventGroups getDefaultEventGroups(Event event) {
        //initialize participant map
        Map<Integer, Set<Participant>> participantMap = new TreeMap<>();
        for (int i=0; i<event.getNumberOfGroups(); i++){
            participantMap.put(i, new TreeSet<Participant>());
        }
        
        //fill participant map from existing games if possible
        for (Game game: event.getGames()){
            Integer groupNumber = game.getGroupNumber();
            if (groupNumber != null){
                Set<Participant> groupParticipants = participantMap.get(groupNumber);
                groupParticipants.addAll(game.getParticipants());
                participantMap.put(groupNumber, groupParticipants);
            }
        }
        EventGroups eventGroups = new EventGroups();
        eventGroups.setGroupParticipants(participantMap);
        return eventGroups;
    }

    private void createMissingGames(Event event, Collection<Game> existingGames, Set<Participant> participants, Integer groupNumber) {
        for (Participant firstParticipant: participants){
            for (Participant secondParticipant: participants){
                if (!firstParticipant.equals(secondParticipant)){
                    boolean gameExists = false;
                    for (Game game: existingGames){
                        if (game.getParticipants().contains(firstParticipant) && game.getParticipants().contains(secondParticipant)){
                            gameExists = true;
                            break;
                        }
                    }
                    if (!gameExists){
                        Game game = new Game();
                        game.setEvent(event);
                        if (groupNumber != null){
                            game.setGroupNumber(groupNumber);
                        }
                        Set<Participant> gameParticipants = new LinkedHashSet<>();
                        gameParticipants.add(firstParticipant);
                        gameParticipants.add(secondParticipant);
                        game.setParticipants(gameParticipants);
                        gameDAO.saveOrUpdate(game);
                        existingGames.add(game);
                    }
                }
            }
        }
    }
    
    private ModelAndView getGroupGamesEndView(Event event) {
        ModelAndView mav = new ModelAndView("events/groupknockout/groupgamesend", "Model", event);
        return mav;
    }
}