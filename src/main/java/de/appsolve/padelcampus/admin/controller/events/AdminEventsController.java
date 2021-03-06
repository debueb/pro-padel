/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.events;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.constants.EventType;
import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.constants.PaymentMethod;
import de.appsolve.padelcampus.data.EventGroups;
import de.appsolve.padelcampus.data.GameData;
import de.appsolve.padelcampus.data.GameList;
import de.appsolve.padelcampus.data.ScoreEntry;
import de.appsolve.padelcampus.db.dao.*;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.*;
import de.appsolve.padelcampus.spring.*;
import de.appsolve.padelcampus.utils.BookingUtil;
import de.appsolve.padelcampus.utils.EventsUtil;
import de.appsolve.padelcampus.utils.GameUtil;
import de.appsolve.padelcampus.utils.RankingUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/events")
public class AdminEventsController extends AdminBaseController<Event> {

    private static final Integer ROUND_NONE = null;
    private static final Integer ROUND_ONE = 0;

    @Autowired
    EventDAOI eventDAO;

    @Autowired
    GameDAOI gameDAO;

    @Autowired
    BaseEntityDAOI<Participant> participantDAO;

    @Autowired
    CalendarConfigDAOI calendarConfigDAO;

    @Autowired
    EventGroupDAOI eventGroupDAO;

    @Autowired
    RankingUtil rankingUtil;

    @Autowired
    EventsUtil eventsUtil;

    @Autowired
    GameUtil gameUtil;

    @Autowired
    BookingUtil bookingUtil;

    @Autowired
    PlayerDAOI playerDAO;

    @Autowired
    ParticipantCollectionEditor participantCollectionEditor;

    @Autowired
    CommunitiesCollectionEditor communitiesCollectionEditor;

    @Autowired
    CalendarConfigPropertyEditor calendarConfigPropertyEditor;

    @Autowired
    EventGroupPropertyEditor eventGroupPropertyEditor;

    @Autowired
    PlayerCollectionEditor playerCollectionEditor;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));
        binder.registerCustomEditor(Set.class, "communities", communitiesCollectionEditor);
        binder.registerCustomEditor(Set.class, "participants", participantCollectionEditor);
        binder.registerCustomEditor(Set.class, "groupParticipants", participantCollectionEditor);
        binder.registerCustomEditor(Set.class, "team1", playerCollectionEditor);
        binder.registerCustomEditor(Set.class, "team2", playerCollectionEditor);
        binder.registerCustomEditor(CalendarConfig.class, calendarConfigPropertyEditor);
        binder.registerCustomEditor(EventGroup.class, eventGroupPropertyEditor);
    }

    @Override
    public ModelAndView showIndex(HttpServletRequest request, Pageable pageable, @RequestParam(required = false, name = "search") String search) {
        return new ModelAndView(getModuleName() + "/index");
    }

    @RequestMapping(method = GET, value = "/{status}")
    public ModelAndView showEvents(HttpServletRequest request, Pageable pageable, @RequestParam(required = false, name = "search") String search, @PathVariable("status") String status) {
        Set<Criterion> criterions = new HashSet<>();
        LocalDate today = new LocalDate(Constants.DEFAULT_TIMEZONE);
        if (status.equals("current")) {
            criterions.add(Restrictions.ge("endDate", today));
        } else {
            criterions.add(Restrictions.lt("endDate", today));
        }
        Page<Event> page;
        if (!StringUtils.isEmpty(search)) {
            page = eventDAO.findAllByFuzzySearch(search, criterions, "participants", "participants.players");
        } else {
            page = eventDAO.findAllFetchWithParticipantsAndPlayers(pageable, criterions);
        }
        ModelAndView mav = new ModelAndView(getModuleName() + "/events");
        mav.addObject("Page", page);
        mav.addObject("Models", page.getContent());
        mav.addObject("moduleName", getModuleName());
        mav.addObject("status", status);
        return mav;
    }

    @Override
    protected Event findById(Long modelId) {
        return getDAO().findByIdFetchWithParticipants(modelId);
    }

    @Override
    @RequestMapping(value = {"add", "edit/{modelId}"}, method = POST)
    public ModelAndView postEditView(@ModelAttribute("Model") Event model, HttpServletRequest request, BindingResult result) {
        ModelAndView editView = getEditView(model);
        validator.validate(model, result);
        if (result.hasErrors()) {
            return editView;
        }
        try {
            if (model.getId() != null) {

                //prevent removal of a team if it has already played a game
                Event existingEvent = eventDAO.findByIdFetchWithParticipants(model.getId());
                if (!existingEvent.getParticipants().equals(model.getParticipants())) {
                    for (Participant participant : existingEvent.getParticipants()) {
                        if (!model.getParticipants().contains(participant)) {
                            List<Game> existingGames = gameDAO.findByParticipantAndEventWithScoreOnly(participant, model);
                            if (!existingGames.isEmpty()) {
                                throw new Exception(msg.get("TeamHasAlreadyPlayedInEvent", new Object[]{participant.toString(), existingGames.size(), model.toString()}), null);
                            }
                        }
                    }
                }

                //prevent switching from one event type to another
                if (!existingEvent.getEventType().equals(model.getEventType())) {
                    throw new Exception(msg.get("CannotModifyEventTypeOfExistingEvent"));
                }
            }

            //make sure end date is not before start date
            if (model.getEndDate().isBefore(model.getStartDate())) {
                model.setEndDate(model.getStartDate());
            }

            //if participants can sign up online, make sure price and payment methods are set
            if (model.getAllowSignup()) {
                if (model.getPaymentMethods() == null || model.getPaymentMethods().isEmpty()) {
                    throw new Exception(msg.get("SelectAPaymentMethod"));
                }
                if (model.getPrice() == null) {
                    throw new Exception(msg.get("SetAPrice"));
                }
            }

            switch (model.getEventType()) {

                case SingleRoundRobin:
                    model = getDAO().saveOrUpdate(model);

                    //remove games that have not been played yet
                    gameUtil.removeObsoleteGames(model);

                    gameUtil.createMissingGames(model, model.getParticipants());
                    return redirectToIndex(request);

                case GroupTwoRounds:
                    //remove games that have not been played yet
                    gameUtil.removeObsoleteGames(model);
                    //no break
                case GroupKnockout:
                    model = getDAO().saveOrUpdate(model);
                    if (!model.getParticipants().isEmpty()) {
                        return redirectToGroupDraws(model);
                    } else {
                        return redirectToIndex(request);
                    }

                case Knockout:
                    model = getDAO().saveOrUpdate(model);
                    if (!model.getParticipants().isEmpty()) {
                        return redirectToDraws(model);
                    } else {
                        return redirectToIndex(request);
                    }
                case CommunityRoundRobin:
                case PullRoundRobin:
                case FriendlyGames:
                    getDAO().saveOrUpdate(model);
                    return redirectToIndex(request);
                default:
                    throw new Exception("Unsupported event type " + model.getEventType());
            }
        } catch (Exception e) {
            result.addError(new ObjectError("id", e.getMessage()));
            return editView;
        }
    }

    @RequestMapping(value = {"edit/{eventId}/draws"}, method = GET)
    public ModelAndView getDraws(@PathVariable("eventId") Long eventId, HttpServletRequest request) {
        ModelAndView mav = getDrawsView(eventId);
        return mav;
    }

    @RequestMapping(value = {"edit/{eventId}/draws"}, method = POST)
    public ModelAndView postDraws(@PathVariable("eventId") Long eventId) {
        try {
            Event model = eventDAO.findByIdFetchWithParticipants(eventId);

            //prevent udpate operations for existing events
            List<Game> eventGames = gameDAO.findByEvent(model);
            if (!eventGames.isEmpty()) {
                throw new Exception(msg.get("DrawsAlreadyExist"));
            }

            //check min number of participants
            if (model.getParticipants().size() < 3) {
                throw new Exception(msg.get("PleaseSelectAtLeast3Participants"));
            }

            //determine ranking
            List<Ranking> ranking = rankingUtil.getRankedParticipants(model);
            List<Participant> rankedParticipants = new ArrayList<>();
            ranking.stream().forEach(r -> {
                rankedParticipants.add(r.getParticipant());
            });

            eventsUtil.createKnockoutGames(model, rankedParticipants);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ModelAndView mav = getDrawsView(eventId);
            mav.addObject("error", e.getMessage());
            return mav;
        }

        return new ModelAndView("redirect:/admin/events/edit/" + eventId + "/draws");
    }

    @RequestMapping(value = {"edit/{eventId}/draws/game/{gameId}"}, method = GET)
    public ModelAndView getDrawsGame(@PathVariable("eventId") Long eventId, @PathVariable("gameId") Long gameId, HttpServletRequest request) {
        Game game = gameDAO.findById(gameId);
        GameData gameData = new GameData(game);
        ModelAndView mav = getDrawsGameView(eventId, gameData);
        return mav;
    }

    @RequestMapping(value = {"edit/{eventId}/draws/game/{gameId}"}, method = POST)
    public ModelAndView postDrawsGame(@PathVariable("eventId") Long eventId, @RequestParam(value = "id", required = true) Long id, @RequestParam(value = "participant1", required = true) Long participant1, @RequestParam(value = "participant2", required = false) Long participant2) {
        Game game = gameDAO.findByIdFetchWithNextGame(id);

        Game nextGame = game.getNextGame();
        if (nextGame != null) {
            nextGame.getParticipants().removeAll(game.getParticipants());
            gameDAO.saveOrUpdate(nextGame);
        }

        game.getParticipants().clear();
        Participant p1 = participantDAO.findById(participant1);
        game.getParticipants().add(p1);
        if (participant2 != null) {
            Participant p2 = participantDAO.findById(participant2);
            game.getParticipants().add(p2);
        }

        if (nextGame != null && game.getParticipants().size() == 1) {
            nextGame.getParticipants().add(p1);
            gameDAO.saveOrUpdate(nextGame);
        }

        gameDAO.saveOrUpdate(game);
        return new ModelAndView("redirect:/admin/events/edit/" + eventId + "/draws");
    }

    @RequestMapping(value = {"edit/{eventId}/groupdraws"}, method = GET)
    public ModelAndView getGroupDraws(@PathVariable("eventId") Long eventId) {
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        return getGroupDrawsView(event, getDefaultEventGroups(event));
    }

    @RequestMapping(value = {"edit/{eventId}/groupdraws"}, method = POST)
    public ModelAndView postGroupDraws(@PathVariable("eventId") Long eventId, @ModelAttribute("Model") @Valid EventGroups eventGroups, BindingResult bindingResult, HttpServletRequest request) {
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        Iterator<Map.Entry<Integer, Set<Participant>>> iterator = eventGroups.getGroupParticipants().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Set<Participant>> entry = iterator.next();
            if (entry.getValue() == null) {
                bindingResult.reject("PleaseSelectParticipantsForEachGroup");
            }
        }

        //prevent modification of group draws if knockout round games have already begun
        if (event.getEventType().equals(EventType.Knockout)) {
            SortedMap<Integer, List<Game>> roundGames = eventsUtil.getRoundGameMap(event);
            if (!roundGames.isEmpty()) {
                bindingResult.reject("CannotModifyEventAfterGroupPhaseHasEnded");
            }
        }

        if (bindingResult.hasErrors()) {
            return getGroupDrawsView(event, eventGroups);
        }

        //remove games that have not been played yet
        gameUtil.removeObsoleteGames(event);

        //remove games with teams that are no longer part of a group
        Iterator<Game> gameIterator = event.getGames().iterator();
        while (gameIterator.hasNext()) {
            Game game = gameIterator.next();
            Integer groupNumber = game.getGroupNumber();
            if (groupNumber != null) {
                Set<Participant> groupParticipants = eventGroups.getGroupParticipants().get(groupNumber);
                if (!groupParticipants.containsAll(game.getParticipants())) {
                    gameDAO.deleteById(game.getId());
                    gameIterator.remove();
                }
            }
        }

        //create missing games
        for (int groupNumber = 0; groupNumber < event.getNumberOfGroups(); groupNumber++) {
            Set<Participant> groupParticipants = eventGroups.getGroupParticipants().get(groupNumber);
            Integer roundNumber = null;
            switch (event.getEventType()) {
                case GroupKnockout:
                    //for GroupKnockout events, only knockout games have a round
                    roundNumber = ROUND_NONE;
                    break;
                case GroupTwoRounds:
                    //for GroupTwoRound events, every round has a number
                    roundNumber = ROUND_ONE;
                    break;
            }
            gameUtil.createMissingGames(event, groupParticipants, groupNumber, roundNumber);
        }

        return new ModelAndView("redirect:/admin/events/edit/" + eventId + "/groupschedule");
    }

    @RequestMapping(value = {"edit/{eventId}/groupdraws/{round}"}, method = GET)
    public ModelAndView getGroupDrawsForRound(@PathVariable Long eventId, @PathVariable Integer round) {
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        try {
            int maxParticipantsPerGroup = (int) Math.ceil(event.getParticipants().size() / event.getNumberOfGroups());
            SortedMap<Integer, List<Game>> groupGames = eventsUtil.getGroupGameMap(event, round - 1);
            List<Participant> rankedParticipants = getRankedGroupParticipants(groupGames, event.getNumberOfGroups(), maxParticipantsPerGroup);
            EventGroups eventGroupsRoundTwo = getEventGroupsRoundTwo(event, rankedParticipants);
            return getGroupDrawsView(event, eventGroupsRoundTwo, round);
        } catch (Exception e) {
            return getGroupDrawsView(event, null, round);
        }
    }

    @RequestMapping(value = {"edit/{eventId}/groupdraws/{round}"}, method = POST)
    public ModelAndView postGroupDrawsForRound(@PathVariable Long eventId, @PathVariable Integer round, @ModelAttribute("Model") @Valid EventGroups eventGroups, BindingResult bindingResult, HttpServletRequest request) {
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        Iterator<Map.Entry<Integer, Set<Participant>>> iterator = eventGroups.getGroupParticipants().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Set<Participant>> entry = iterator.next();
            if (entry.getValue() == null) {
                bindingResult.reject("PleaseSelectParticipantsForEachGroup");
            }
        }

        if (bindingResult.hasErrors()) {
            return getGroupDrawsView(event, eventGroups, round);
        }

        //remove games of players who no longer participate
        gameUtil.removeObsoleteGames(event);

        //remove games with teams that are no longer part of a group in the current round
        Iterator<Game> gameIterator = event.getGames().iterator();
        while (gameIterator.hasNext()) {
            Game game = gameIterator.next();
            Integer groupNumber = game.getGroupNumber();
            if (groupNumber != null && game.getRound().equals(round)) {
                Set<Participant> groupParticipants = eventGroups.getGroupParticipants().get(groupNumber);
                if (!groupParticipants.containsAll(game.getParticipants())) {
                    gameIterator.remove();
                    gameDAO.deleteById(game.getId());
                }
            }
        }

        //create missing games
        for (int groupNumber = 0; groupNumber < event.getNumberOfGroupsSecondRound(); groupNumber++) {
            Set<Participant> groupParticipants = eventGroups.getGroupParticipants().get(groupNumber);
            gameUtil.createMissingGames(event, groupParticipants, groupNumber, round);
        }

        return new ModelAndView("redirect:/events/event/" + eventId + "/groupgames/" + round);
    }

    @RequestMapping(value = {"edit/{eventId}/groupschedule"}, method = GET)
    public ModelAndView getGroupSchedule(@PathVariable("eventId") Long eventId) {
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        return getGroupScheduleView(event);
    }

    @RequestMapping(value = {"edit/{eventId}/gameschedule"}, method = GET)
    public ModelAndView getGameSchedule(@PathVariable("eventId") Long eventId) {
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        return getGameScheduleView(event);
    }

    @RequestMapping(value = {"edit/{eventId}/schedule/{scheduleName}"}, method = POST)
    public ModelAndView postGroupSchedule(@PathVariable("eventId") Long eventId, @PathVariable("scheduleName") String scheduleName, @ModelAttribute("Model") GameList gameList, BindingResult bindingResult, HttpServletRequest request) {
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        if (bindingResult.hasErrors()) {
            switch (scheduleName) {
                case "groupschedule":
                    return getGroupScheduleView(event);
                case "gameschedule":
                default:
                    return getGameScheduleView(event);
            }
        }
        if (gameList.getList() != null) {
            for (Game game : gameList.getList()) {
                Game existingGame = gameDAO.findById(game.getId());
                existingGame.setStartDate(game.getStartDate());
                existingGame.setStartTimeHour(game.getStartTimeHour());
                existingGame.setStartTimeMinute(game.getStartTimeMinute());
                gameDAO.saveOrUpdate(existingGame);
            }
        }
        return redirectToIndex(request);
    }

    @Override
    protected ModelAndView getDeleteView(Event model) {
        return new ModelAndView(getModuleName() + "/delete", "Model", model);
    }

    @Override
    @RequestMapping(value = "/{id}/delete", method = POST)
    public ModelAndView postDelete(HttpServletRequest request, @PathVariable("id") Long id) {
        try {
            Event event = eventDAO.findByIdFetchWithGames(id);
            if (event == null) {
                return getNotFoundView();
            }
            for (Game game : event.getGames()) {
                game.getGameSets().clear();
            }
            event.getGames().clear();
            eventDAO.saveOrUpdate(event);

            eventDAO.deleteById(event.getId());
        } catch (DataIntegrityViolationException e) {
            Event model = (Event) getDAO().findById(id);
            LOG.warn("Attempt to delete " + model + " failed due to " + e);
            ModelAndView deleteView = getDeleteView(model);
            deleteView.addObject("error", msg.get("CannotDeleteDueToRefrence", new Object[]{model.toString()}));
            return deleteView;
        }
        return redirectToIndex(request);
    }

    @RequestMapping(method = GET, value = "event/{eventId}/groupgamesend")
    public ModelAndView getEventGroupGamesEnd(@PathVariable("eventId") Long eventId) {
        Event event = eventDAO.findById(eventId);
        return getGroupGamesEndView(event);
    }

    @RequestMapping(method = POST, value = "event/{eventId}/groupgamesend")
    public ModelAndView saveEventGroupGamesEnd(@PathVariable("eventId") Long eventId, @ModelAttribute("Model") Event dummy, BindingResult result) {
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        try {
            SortedMap<Integer, List<Game>> roundGames = eventsUtil.getRoundGameMap(event);
            if (!roundGames.isEmpty()) {
                throw new Exception(msg.get("GroupGamesAlreadyEnded"));
            }

            SortedMap<Integer, List<Game>> groupGames = eventsUtil.getGroupGameMap(event);
            if (groupGames.isEmpty()) {
                throw new Exception(msg.get("NoGroupGames"));
            }

            List<Participant> rankedParticipants = getRankedGroupParticipants(groupGames, event.getNumberOfGroups(), event.getNumberOfWinnersPerGroup());

            eventsUtil.createKnockoutGames(event, rankedParticipants);
            return redirectToDraws(event);
        } catch (Exception e) {
            result.addError(new ObjectError("*", e.getMessage()));
            return getGroupGamesEndView(dummy);
        }
    }

    @RequestMapping(method = GET, value = "event/{eventId}/groupgamesend/{round}")
    public ModelAndView getEventGroupGamesEndForRound(@PathVariable() Long eventId, @PathVariable Integer round) {
        Event event = eventDAO.findById(eventId);
        return getGroupGamesEndView(event, round);
    }

    @RequestMapping(method = POST, value = "event/{eventId}/groupgamesend/{round}")
    public ModelAndView saveEventGroupGamesEndForRound(@PathVariable() Long eventId, @PathVariable Integer round, @ModelAttribute("Model") Event dummy, BindingResult result) {
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        try {
            SortedMap<Integer, List<Game>> roundGames = eventsUtil.getRoundGameMap(event, round + 1);
            if (!roundGames.isEmpty()) {
                throw new Exception(msg.get("GroupGamesAlreadyEnded"));
            }

            SortedMap<Integer, List<Game>> groupGames = eventsUtil.getGroupGameMap(event, round);
            if (groupGames.isEmpty()) {
                throw new Exception(msg.get("NoGroupGames"));
            }
            getRankedGroupParticipants(groupGames, event.getNumberOfGroups(), event.getNumberOfWinnersPerGroup());

            return redirectToGroupDrawsRound(event, round + 1);
        } catch (Exception e) {
            result.addError(new ObjectError("*", e.getMessage()));
            return getGroupGamesEndView(dummy, round);
        }
    }

    @RequestMapping(method = GET, value = "/{eventId}/game/{gameId}/delete")
    public ModelAndView getDeleteGameView(@PathVariable("eventId") Long eventId, @PathVariable("gameId") Long gameId) {
        Game game = gameDAO.findById(gameId);
        return getDeleteGameView(game);
    }

    @RequestMapping(method = POST, value = "/{eventId}/game/{gameId}/delete")
    public ModelAndView postDeleteGameView(HttpServletRequest request, @PathVariable("gameId") Long gameId, @RequestParam(value = "redirectUrl", required = false) String redirectUrl) {
        try {
            gameDAO.deleteById(gameId);
        } catch (DataIntegrityViolationException e) {
            Game model = gameDAO.findById(gameId);
            LOG.warn("Attempt to delete " + model + " failed due to " + e);
            ModelAndView deleteView = getDeleteGameView(model);
            deleteView.addObject("error", msg.get("CannotDeleteDueToRefrence", new Object[]{model.toString()}));
            return deleteView;
        }
        if (!StringUtils.isEmpty(redirectUrl)) {
            return new ModelAndView("redirect:/" + redirectUrl);
        }
        return redirectToIndex(request);
    }

    @RequestMapping(method = GET, value = "/{eventId}/addguests")
    public ModelAndView getAddGuests(@PathVariable("eventId") Long eventId) {
        Event event = eventDAO.findById(eventId);
        return getAddGuestsView(event, new Player(), 1L);
    }

    @RequestMapping(method = POST, value = "/{eventId}/addguests")
    public ModelAndView postAddGuests(
            @PathVariable("eventId") Long eventId,
            @ModelAttribute("Player") Player player,
            @RequestParam("NumberOfGuests") Long numberOfGuests,
            HttpServletRequest request,
            BindingResult result) {
        Event event = eventDAO.findByIdFetchWithParticipants(eventId);
        try {
            Player primaryPlayer;
            if (StringUtils.isEmpty(player.getUUID())) {
                validator.validate(player, result);
                if (result.hasErrors()) {
                    return getAddGuestsView(event, player, numberOfGuests);
                }
                if (playerDAO.findByEmail(player.getEmail()) != null) {
                    throw new Exception(msg.get("EmailAlreadyRegistered"));
                }
                player.setAllowEmailContact(false);
                primaryPlayer = playerDAO.saveOrUpdate(player);
            } else {
                primaryPlayer = playerDAO.findByUUID(player.getUUID());
            }
            if (event.getParticipants().contains(primaryPlayer)) {
                throw new Exception(msg.get("AlreadyParticipatesInThisEvent", new Object[]{primaryPlayer}));
            }
            for (long guestNumber = 1L; guestNumber <= numberOfGuests; guestNumber++) {
                String domain = request.getServerName().equals("localhost") ? "localhost.local" : request.getServerName();
                String guestEmail = String.format("%s-%s-Gast-%s@%s", primaryPlayer.getFirstName(), primaryPlayer.getLastName(), guestNumber, domain).replace(" ", "-");
                Player guest = playerDAO.findByEmail(guestEmail);
                if (guest == null) {
                    guest = new Player();
                    guest.setFirstName(primaryPlayer.getFirstName());
                    guest.setLastName(String.format("%s Gast %s", primaryPlayer.getLastName(), guestNumber));
                    guest.setAllowEmailContact(false);
                    guest.setEmail(guestEmail);
                    guest.setPhone(primaryPlayer.getPhone());
                    guest.setGender(primaryPlayer.getGender());
                    guest = playerDAO.saveOrUpdate(guest);
                }
                event.getParticipants().add(guest);
            }
            event.getParticipants().add(primaryPlayer);
            eventDAO.saveOrUpdate(event);
            return new ModelAndView("redirect:/admin/events/edit/" + eventId);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            result.addError(new ObjectError("id", e.getMessage()));
            return getAddGuestsView(event, player, numberOfGuests);
        }
    }

    @Override
    protected ModelAndView getEditView(Event event) {
        ModelAndView mav = new ModelAndView("admin/events/edit", "Model", event);
        mav.addObject("EventTypes", EventType.values());
        mav.addObject("Genders", Gender.values());
        mav.addObject("CalendarConfigs", calendarConfigDAO.findAll());
        mav.addObject("EventGroups", eventGroupDAO.findAll());
        List<PaymentMethod> activePaymentMethods = bookingUtil.getActivePaymentMethods();
        activePaymentMethods.remove(PaymentMethod.Voucher);
        mav.addObject("PaymentMethods", activePaymentMethods);
        return mav;
    }

    @Override
    public EventDAOI getDAO() {
        return eventDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/events";
    }

    @Override
    public Page<Event> findAll(Pageable pageable) {
        Page<Event> page = eventDAO.findAllFetchWithParticipantsAndPlayers(pageable);
        return page;
    }

    private ModelAndView redirectToDraws(Event model) {
        return new ModelAndView("redirect:/admin/events/edit/" + model.getId() + "/draws");
    }

    private ModelAndView getDrawsView(Long eventId) {
        Event event = eventDAO.findByIdFetchWithGames(eventId);
        SortedMap<Integer, List<Game>> roundGames = eventsUtil.getRoundGameMap(event);
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

    private ModelAndView getGroupDrawsView(Event event, EventGroups eventGroups) {
        return getGroupDrawsView(event, eventGroups, null);
    }

    private ModelAndView getGroupDrawsView(Event event, EventGroups eventGroups, Integer round) {
        ModelAndView mav = new ModelAndView("admin/events/groupdraws");
        mav.addObject("Event", event);
        mav.addObject("Model", eventGroups);
        mav.addObject("Round", round);
        return mav;
    }

    private ModelAndView getGroupGamesEndView(Event event) {
        return getGroupGamesEndView(event, null);
    }

    private ModelAndView getGroupGamesEndView(Event event, Integer round) {
        ModelAndView mav = new ModelAndView("admin/events/groupgamesend", "Model", event);
        mav.addObject("Round", round);
        return mav;
    }

    private ModelAndView getGroupScheduleView(Event event) {
        ModelAndView mav = new ModelAndView("admin/events/groupschedule");
        mav.addObject("Event", event);
        SortedMap<Integer, List<Game>> groupGameMap = eventsUtil.getGroupGameMap(event);
        mav.addObject("GroupGameMap", groupGameMap);
        List<Game> games = new ArrayList<>();
        for (List<Game> list : groupGameMap.values()) {
            games.addAll(list);
        }
        GameList formList = new GameList();
        formList.setList(games);
        mav.addObject("Model", formList);
        return mav;
    }

    private ModelAndView getGameScheduleView(Event event) {
        ModelAndView mav = new ModelAndView("admin/events/gameschedule");
        mav.addObject("Event", event);
        List<Game> games = new ArrayList<>();
        games.addAll(event.getGames());
        GameList formList = new GameList();
        formList.setList(games);
        mav.addObject("Model", formList);
        return mav;
    }

    private ModelAndView getAddGuestsView(Event event, Player player, Long numberOfGuests) {
        ModelAndView mav = new ModelAndView("admin/events/addguests");
        mav.addObject("Event", event);
        mav.addObject("Player", player);
        mav.addObject("NumberOfGuests", numberOfGuests);
        return mav;
    }

    private ModelAndView redirectToGroupDraws(Event model) {
        return new ModelAndView("redirect:/admin/events/edit/" + model.getId() + "/groupdraws");
    }

    private ModelAndView redirectToGroupDrawsRound(Event model, Integer round) {
        return new ModelAndView("redirect:/admin/events/edit/" + model.getId() + "/groupdraws/" + round);
    }

    private EventGroups getDefaultEventGroups(Event event) {
        //initialize participant map
        Map<Integer, Set<Participant>> participantMap = new TreeMap<>();
        for (int i = 0; i < event.getNumberOfGroups(); i++) {
            participantMap.put(i, new TreeSet<Participant>());
        }

        //fill participant map from existing games if possible
        for (Game game : event.getGames()) {
            //ROUND_NONE for EventType.GroupKnockout, ROUND_ONE for EventType.GroupTwoRounds
            if (Objects.equals(game.getRound(), ROUND_NONE) || game.getRound().equals(ROUND_ONE)) {
                Integer groupNumber = game.getGroupNumber();
                if (groupNumber != null) {
                    Set<Participant> groupParticipants = participantMap.get(groupNumber);
                    groupParticipants.addAll(game.getParticipants());
                    participantMap.put(groupNumber, groupParticipants);
                }
            }
        }
        EventGroups eventGroups = new EventGroups();
        eventGroups.setGroupParticipants(participantMap);
        return eventGroups;
    }

    private EventGroups getEventGroupsRoundTwo(Event event, List<Participant> rankedGroupParticipants) {

        //initialize participant map
        Map<Integer, Set<Participant>> participantMap = new TreeMap<>();

        int grp = 0;
        for (Participant rankedGroupParticipant : rankedGroupParticipants) {
            Set<Participant> participants = participantMap.get(grp);
            if (participants == null) {
                participants = new TreeSet<>();
            }
            participants.add(rankedGroupParticipant);
            participantMap.put(grp, participants);

            if (grp + 1 >= event.getNumberOfGroupsSecondRound()) {
                grp = 0;
            } else {
                grp++;
            }
        }

        //fill participant map from existing games if possible
        for (Game game : event.getGames()) {
            if (game.getRound() != null && game.getRound() == 1) {
                Integer groupNumber = game.getGroupNumber();
                if (groupNumber != null) {
                    Set<Participant> groupParticipants = participantMap.get(groupNumber);
                    groupParticipants.addAll(game.getParticipants());
                    participantMap.put(groupNumber, groupParticipants);
                }
            }
        }
        EventGroups eventGroups = new EventGroups();
        eventGroups.setGroupParticipants(participantMap);
        return eventGroups;
    }

    private ModelAndView getDeleteGameView(Game game) {
        ModelAndView mav = new ModelAndView("include/delete");
        mav.addObject("Model", game);
        return mav;
    }

    private List<Participant> getRankedGroupParticipants(SortedMap<Integer, List<Game>> groupGames, Integer numberOfGroups, Integer maxWinnersPerGroup) throws Exception {
        Map<Integer, List<Participant>> rankedGroupParticipants = new TreeMap<>();
        Iterator<Map.Entry<Integer, List<Game>>> iterator = groupGames.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, List<Game>> entry = iterator.next();
            Integer groupNumber = entry.getKey();
            List<Game> games = entry.getValue();

            //determine participant based on games to filter out participants who did not play
            Set<Participant> participants = new HashSet<>();
            List<Game> playedGames = new ArrayList<>();
            for (Game game : games) {
                if (!game.getGameSets().isEmpty()) {
                    participants.addAll(game.getParticipants());
                    playedGames.add(game);
                }
            }

            if (participants.isEmpty() || playedGames.isEmpty()) {
                throw new Exception(msg.get("CannotEndGroupGames"));
            }

            //get list of score entries sorted by rank
            List<ScoreEntry> scoreEntries = rankingUtil.getScores(participants, playedGames);
            for (int groupPos = 0; groupPos < maxWinnersPerGroup; groupPos++) {
                List<Participant> rankedParticipants = rankedGroupParticipants.get(groupNumber);
                if (rankedParticipants == null) {
                    rankedParticipants = new ArrayList<>();
                }
                Participant p = null;
                try {
                    p = scoreEntries.get(groupPos).getParticipant();
                } catch (IndexOutOfBoundsException e) {
                    //could happen when not enough games were played in one group
                }
                rankedParticipants.add(p);
                rankedGroupParticipants.put(groupNumber, rankedParticipants);
            }
        }

        //sort participants so that group winners are first
        List<Participant> rankedParticipants = new ArrayList<>();
        for (int groupPos = 0; groupPos < maxWinnersPerGroup; groupPos++) {
            for (int group = 0; group < numberOfGroups; group++) {
                rankedParticipants.add(rankedGroupParticipants.get(group).get(groupPos));
            }
        }
        return rankedParticipants;
    }
}