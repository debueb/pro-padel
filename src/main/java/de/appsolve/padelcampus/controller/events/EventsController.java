/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.events;

import de.appsolve.padelcampus.constants.BookingType;
import de.appsolve.padelcampus.constants.PaymentMethod;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.data.ScoreEntry;
import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.dao.ModuleDAOI;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.CalendarConfig;
import de.appsolve.padelcampus.db.model.Community;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Game;
import de.appsolve.padelcampus.db.model.GameSet;
import de.appsolve.padelcampus.db.model.Module;
import de.appsolve.padelcampus.db.model.Offer;
import de.appsolve.padelcampus.db.model.Participant;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;
import de.appsolve.padelcampus.utils.EventsUtil;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE;
import static de.appsolve.padelcampus.utils.FormatUtils.TIME_HUMAN_READABLE;
import de.appsolve.padelcampus.utils.GameUtil;
import de.appsolve.padelcampus.utils.RankingUtil;
import de.appsolve.padelcampus.utils.SessionUtil;
import de.appsolve.padelcampus.utils.SortUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.Minutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/events")
public class EventsController extends BaseController{
    
    @Autowired
    ModuleDAOI moduleDAO;
    
    @Autowired
    EventDAOI eventDAO;
    
    @Autowired
    EventsUtil eventsUtil;
    
    @Autowired
    GameUtil gameUtil;
    
    @Autowired
    RankingUtil rankingUtil;
    
    @Autowired
    SessionUtil sessionUtil;
    
    @Autowired
    PlayerDAOI playerDAO;
    
    @Autowired
    Validator validator;
    
    @RequestMapping("{moduleTitle}")
    public ModelAndView getEvent(@PathVariable("moduleTitle") String moduleTitle){
        Module module = moduleDAO.findByTitle(moduleTitle);
        List<Event> events = eventDAO.findAllActive();
        Iterator<Event> iterator = events.iterator();
        while (iterator.hasNext()){
            Event event = iterator.next();
            if (!module.getEventTypes().contains(event.getEventType())){
                iterator.remove();
            }
        }
        if (events.size()==1){
            return new ModelAndView("redirect:/events/event/"+events.get(0).getId());
        }
        return new ModelAndView("events/index", "Models", events);
    }
    
    @RequestMapping("event/{eventId}")
    public ModelAndView getEvent(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findByIdFetchWithCalendarConfig(eventId);
        ModelAndView mav = new ModelAndView("events/event", "Model", event);
        return mav;
    }
    
    @RequestMapping("event/{eventId}/participants")
    public ModelAndView getEventParticipants(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        SortedMap<Participant, BigDecimal> rankedParticipants = rankingUtil.getRankedParticipants(event);
        ModelAndView mav = new ModelAndView("events/participants", "Model", event);
        mav.addObject("RankedParticipants", rankedParticipants);
        return mav;
    }
    
    @RequestMapping("event/{eventId}/communities")
    public ModelAndView getEventCommunities(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        SortedMap<Participant, BigDecimal> rankedParticipants = rankingUtil.getRankedParticipants(event);
        Map<Community, SortedMap<Participant, BigDecimal>> communityMap = new HashMap<>();
        for (Participant participant: rankedParticipants.keySet()){
            if (participant instanceof Team){
                Team team = (Team) participant;
                if (team.getCommunity() != null){
                    SortedMap<Participant, BigDecimal> communityParticipantMap = communityMap.get(team.getCommunity());
                    if (communityParticipantMap == null){
                        communityParticipantMap = new TreeMap<>();
                    }
                    communityParticipantMap.put(team, rankedParticipants.get(team));
                    SortedMap<Participant, BigDecimal> sortedMap = SortUtil.sortMap(communityParticipantMap);
                    communityMap.put(team.getCommunity(), sortedMap);
                }
            }
        }
        ModelAndView mav = new ModelAndView("events/communityroundrobin/communities", "Model", event);
        mav.addObject("CommunityMap", communityMap);
        return mav;
    }
    
    @RequestMapping("event/{eventId}/communitygames")
    public ModelAndView getEventCommunityGames(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findByIdFetchWithGames(eventId);
        ModelAndView mav = new ModelAndView("events/communityroundrobin/communitygames", "Model", event);
        
        event = eventDAO.findByIdFetchWithGames(eventId);
        
        //Community // Participant // Game // GameResult
        SortedMap<Community, Map<Participant, Map<Game, String>>> communityParticipantGameResultMap = new TreeMap<>();
        
        Map<Participant, Map<Game, String>> participantGameResultMap = getParticipantGameResultMap(event.getGames());
        for (Participant p: participantGameResultMap.keySet()){
            if (p instanceof Team){
                Team team = (Team) p;
                Map<Participant, Map<Game, String>> participantMap = communityParticipantGameResultMap.get(team.getCommunity());
                if (participantMap == null){
                    participantMap = new HashMap<>();
                }
                participantMap.put(p, participantGameResultMap.get(p));
                communityParticipantGameResultMap.put(team.getCommunity(), participantMap);
            }
        }
        mav.addObject("GroupParticipantGameResultMap", communityParticipantGameResultMap);
        gameUtil.addGameResultMap(mav, event.getGames());
        return mav;
    }
    
    @RequestMapping("event/{eventId}/groupgames")
    public ModelAndView getEventGroupGames(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findByIdFetchWithGames(eventId);
        ModelAndView mav = new ModelAndView("events/groupknockout/groupgames", "Model", event);
        
        event = eventDAO.findByIdFetchWithGames(eventId);
        SortedMap<Integer, List<Game>> groupGameMap = eventsUtil.getGroupGameMap(event);
        SortedMap<Integer, List<Game>> roundGameMap = eventsUtil.getRoundGameMap(event);
        
        //Group // Participant // Game // GameResult
        SortedMap<Integer, Map<Participant, Map<Game, String>>> groupParticipantGameResultMap = new TreeMap<>();
        
        Iterator<Map.Entry<Integer, List<Game>>> iterator = groupGameMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer, List<Game>> entry = iterator.next();
            Map<Participant, Map<Game, String>> participantGameResultMap = getParticipantGameResultMap(entry.getValue());
            Integer group = entry.getKey();
            groupParticipantGameResultMap.put(group, participantGameResultMap);
        }
        mav.addObject("GroupParticipantGameResultMap", groupParticipantGameResultMap);
        mav.addObject("RoundGameMap", roundGameMap);
        gameUtil.addGameResultMap(mav, event.getGames());
        return mav;
    }
    
    @RequestMapping("event/{eventId}/knockoutgames")
    public ModelAndView getEventKnockoutGames(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findByIdFetchWithGames(eventId);
        SortedMap<Integer, List<Game>> groupGameMap = eventsUtil.getGroupGameMap(event);
        SortedMap<Integer, List<Game>> roundGameMap = eventsUtil.getRoundGameMap(event);
        if (roundGameMap.isEmpty()){
            return new ModelAndView("events/groupknockout/knockoutgamesend", "Model", event);
        }
        ModelAndView mav = getKnockoutView(event, roundGameMap);
        mav.addObject("GroupGameMap", groupGameMap);
        return mav;
    }
    
    @RequestMapping(method=GET, value="event/{eventId}/participate")
    public ModelAndView participate(@PathVariable("eventId") Long eventId, HttpServletRequest request){
        Player user = sessionUtil.getUser(request);
        if (user == null){
            return getLoginRequiredView(request, msg.get("Participate"));
        }
        return participateView(eventId, new Player());
    }
    
    @RequestMapping(method=POST, value="event/{eventId}/participate")
    public ModelAndView postParticipate(@PathVariable("eventId") Long eventId, HttpServletRequest request, final @ModelAttribute("Player") Player player, BindingResult bindingResult){
        ModelAndView participateView = participateView(eventId, player);
        try {
            Player user = sessionUtil.getUser(request);
            if (user == null){
                return getLoginRequiredView(request, msg.get("Participate"));
            }
            Player partner;
            if (player.getUUID()==null){
                validator.validate(player, bindingResult);
                if (bindingResult.hasErrors()){
                    return participateView;
                }
                if (playerDAO.findByEmail(player.getEmail()) != null){
                    throw new Exception(msg.get("EmailAlreadyRegistered"));
                }
                partner = playerDAO.saveOrUpdate(player);
            } else {
                partner = playerDAO.findByUUID(player.getUUID());
                if (partner == null){
                    throw new Exception(msg.get("ChoosePartner"));
                }
            }

            Event event = eventDAO.findByIdFetchWithParticipantsAndCalendarConfig(eventId);
            
            checkPlayerNotParticipating(event, user);
            checkPlayerNotParticipating(event, partner);

            CalendarConfig calendarConfig = event.getCalendarConfig();
            Offer offer = calendarConfig.getOffers().iterator().next();
            Set<Player> participants = new HashSet<>();
            //do not add user as this would cause duplicate key (player and players go into the same table)
            participants.add(partner);

            Booking booking = new Booking();
            booking.setPlayer(user);
            booking.setOffer(offer);
            booking.setBookingDate(calendarConfig.getStartDate());
            booking.setBookingTime(calendarConfig.getStartTime());
            booking.setAmount(calendarConfig.getBasePrice());
            booking.setCurrency(calendarConfig.getCurrency());
            booking.setDuration(0L+Minutes.minutesBetween(calendarConfig.getStartTime(), calendarConfig.getEndTime()).getMinutes());
            booking.setPaymentMethod(PaymentMethod.valueOf(request.getParameter("paymentMethod")));
            booking.setBookingType(BookingType.loggedIn);

            //extra fields
            booking.setEvent(event);
            booking.setPlayers(participants);

            sessionUtil.setBooking(request, booking);
            return new ModelAndView("redirect:/bookings/"+DATE_HUMAN_READABLE.print(calendarConfig.getStartDate())+"/"+TIME_HUMAN_READABLE.print(calendarConfig.getStartTime())+"/confirm");
        } catch (Exception e){
            bindingResult.addError(new ObjectError("*", e.getMessage()));
            return participateView;
        }
    }
    
    @RequestMapping(method=GET, value="event/{eventId}/score")
    public ModelAndView getScore(@PathVariable("eventId") Long eventId, HttpServletRequest request){
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        SortedMap<Community, ScoreEntry> communityScoreMap = new TreeMap<>();
        List<ScoreEntry> scores = rankingUtil.getScores(event.getParticipants(), event.getGames());
        for (ScoreEntry scoreEntry: scores){
            Participant p = scoreEntry.getParticipant();
            if (p instanceof Team){
                Team team = (Team) p;
                ScoreEntry communityScore = communityScoreMap.get(team.getCommunity());
                if (communityScore == null){
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
        while(gameList.hasNext()){
            List<Game> games = gameList.next();
            for (Game game: games){
                for (Participant p: game.getParticipants()){
                    Map<Game, List<GameSet>> gameGameMap = participantGameGameSetMap.get(p);
                    if (gameGameMap == null){
                        gameGameMap = new HashMap<>();
                    }
                    Set<GameSet> gameSets = game.getGameSets();
                    List<GameSet> participantGameSets = new ArrayList<>();
                    for (GameSet gs: gameSets){
                        if (gs.getParticipant().equals(p)){
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

    private ModelAndView participateView(Long eventId, Player player) {
        ModelAndView mav = new ModelAndView("events/participate/index");
        mav.addObject("Model", eventDAO.findByIdFetchWithCalendarConfig(eventId));
        mav.addObject("Player", player);
        return mav;
    }

    private void checkPlayerNotParticipating(Event event, Player user) throws Exception {
        for (Team team: event.getTeams()){
            if (team.getPlayers().contains(user)){
                throw new Exception(msg.get("AlreadyParticipatesInThisEvent", new Object[]{user}));
            }
        }
    }

    private Map<Participant, Map<Game, String>> getParticipantGameResultMap(Collection<Game> games) {
        Map<Participant, Map<Game, String>> participantGameResultMap = new HashMap<>();
        for (Game game: games){
            for (Participant p: game.getParticipants()){
                Map<Game, String> gameResultMap = participantGameResultMap.get(p);
                if (gameResultMap == null){
                    gameResultMap = new HashMap<>();
                }
                String result = gameUtil.getGameResult(game, p);
                gameResultMap.put(game, result);
                participantGameResultMap.put(p, gameResultMap);
            }
        }
        return participantGameResultMap;
    }

}
