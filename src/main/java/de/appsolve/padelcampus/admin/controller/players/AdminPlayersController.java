/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.players;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.db.dao.*;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.*;
import de.appsolve.padelcampus.utils.CustomerUtil;
import de.appsolve.padelcampus.utils.GameUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/players")
public class AdminPlayersController extends AdminBaseController<Player> {

    @Autowired
    PlayerDAOI playerDAO;

    @Autowired
    BookingDAOI bookingDAO;

    @Autowired
    TeamDAOI teamDAO;

    @Autowired
    EventDAOI eventDAO;

    @Autowired
    GameDAOI gameDAO;

    @Autowired
    GameSetDAOI gameSetDAO;

    @Autowired
    GameUtil gameUtil;

    @Override
    public ModelAndView showIndex(HttpServletRequest request, @PageableDefault(size = 10, sort = "firstName", direction = Sort.Direction.ASC) Pageable pageable, @RequestParam(required = false, name = "search") String search) {
        return super.showIndex(request, pageable, search);
    }

    @Override
    public ModelAndView postEditView(@ModelAttribute("Model") Player model, HttpServletRequest request, BindingResult result) {
        validator.validate(model, result);
        if (result.hasErrors()) {
            return getEditView(model);
        }
        //make sure not to overwrite passwordHash, verified etc.
        Player player;
        if (model.getId() != null) {
            player = playerDAO.findById(model.getId());
        } else {
            Player existingPlayer = playerDAO.findByEmail(model.getEmail());
            if (existingPlayer != null) {
                result.addError(new ObjectError("email", msg.get("EmailAlreadyRegistered")));
                return getEditView(model);
            }
            player = new Player();
        }
        player.setEmail(model.getEmail());
        player.setFirstName(model.getFirstName());
        player.setLastName(model.getLastName());
        player.setPhone(model.getPhone());
        player.setGender(model.getGender());
        player.setInitialRanking(model.getInitialRanking());
        player.setAllowEmailContact(model.getAllowEmailContact());
        playerDAO.saveOrUpdate(player);
        return redirectToIndex(request);
    }

    @RequestMapping("/exportemails")
    @ResponseBody
    public HttpEntity<byte[]> exportEmails() {
        List<Player> players = playerDAO.findPlayersRegisteredForEmails();
        List<String> emails = new ArrayList<>();
        for (Player player : players) {
            emails.add(player.getEmail());
        }
        byte[] data = StringUtils.join(emails, ",").getBytes(StandardCharsets.UTF_8);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("text", "csv"));
        header.set("Content-Disposition", "attachment; filename=players-that-allow-email-contact.csv");
        header.setContentLength(data.length);
        return new HttpEntity<>(data, header);
    }

    @Override
    protected ModelAndView getDeleteView(Player player) {
        ModelAndView mav = new ModelAndView(getModuleName() + "/delete", "Model", player);
        List<Team> teams = teamDAO.findByPlayer(player);

        if (!player.getCustomer().equals(CustomerUtil.getCustomer())) {
            mav.addObject("error", msg.get("PlayerOriginallySignedUpWithWarning", new Object[]{player, player.getCustomer(), player.getCustomer().getDomainName()}));
        }
        //TODO: handle case user has references to another customer...

        mav.addObject("Bookings", bookingDAO.findAllBookingsByPlayer(player));
        mav.addObject("Games", gameUtil.getGames(player, teams));
        mav.addObject("Teams", teams);
        mav.addObject("Events", getEvents(player, teams));
        mav.addObject("moduleName", getModuleName());
        return mav;
    }

    @Override
    public ModelAndView postDelete(HttpServletRequest request, @PathVariable("id") Long id) {
        Player player = playerDAO.findById(id);
        if (player == null) {
            return getNotFoundView();
        }
        try {
            //delete bookings
            List<Booking> bookings = bookingDAO.findAllBookingsByPlayer(player);
            bookingDAO.delete(bookings);

            //delete games (including active gamesets)
            List<Team> teams = teamDAO.findByPlayer(player);
            Set<Game> games = gameUtil.getGames(player, teams);
            gameDAO.delete(games);

            //remove game sets that may not have been removed when a gameset was removed from game
            Set<GameSet> gameSets = getGameSets(player, teams);
            gameSetDAO.delete(gameSets);

            //remove from events
            Set<Event> events = getEvents(player, teams);
            for (Event event : events) {
                event.getParticipants().remove(player);
                for (Team team : teams) {
                    event.getParticipants().remove(team);
                }
                eventDAO.saveOrUpdate(event);
            }

            //delete teams
            teamDAO.delete(teams);

            //delete player
            playerDAO.deleteById(player.getId());
        } catch (DataIntegrityViolationException e) {
            LOG.warn("Attempt to delete " + player + " failed due to " + e);
            ModelAndView deleteView = getDeleteView(player);
            deleteView.addObject("error", msg.get("CannotDeleteDueToRefrence", new Object[]{player.toString()}));
            return deleteView;
        }
        return redirectToIndex(request);
    }

    @Override
    public BaseEntityDAOI getDAO() {
        return playerDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/players";
    }

    private Set<Event> getEvents(Player player, List<Team> teams) {
        Set<Event> events = new TreeSet<>(eventDAO.findByParticipant(player));
        for (Team team : teams) {
            events.addAll(eventDAO.findByParticipant(team));
        }
        return events;
    }

    private Set<GameSet> getGameSets(Player player, List<Team> teams) {
        Set<GameSet> gameSets = new TreeSet<>(gameSetDAO.findByParticipant(player));
        for (Team team : teams) {
            gameSets.addAll(gameSetDAO.findByParticipant(team));
        }
        return gameSets;
    }
}
