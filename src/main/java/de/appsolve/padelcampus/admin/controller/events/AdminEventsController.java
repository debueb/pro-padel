/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.events;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
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
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
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
        
        //prevent removal of a team it has already played a game
        if (model.getId()!=null){
            Event existingEvent = eventDAO.findByIdFetchWithParticipantsAndGames(model.getId());
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
        getDAO().saveOrUpdate(model);
        
        //remove games that have not been played yet
        List<Game> eventGames = gameDAO.findByEvent(model);
        Iterator<Game> eventGameIterator = eventGames.iterator();
        while (eventGameIterator.hasNext()){
            Game game = eventGameIterator.next();
            if (game.getGameSets().isEmpty()){                
                eventGameIterator.remove();
                gameDAO.deleteById(game.getId());
            }
        }
        
        //generate games
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
        return mav;
    }
    
    @Override
    public GenericDAOI getDAO() {
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
}
