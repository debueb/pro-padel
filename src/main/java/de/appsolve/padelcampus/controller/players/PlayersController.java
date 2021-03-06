/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.players;

import de.appsolve.padelcampus.comparators.TeamByNameComparator;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.dao.GameDAOI;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.dao.TeamDAOI;
import de.appsolve.padelcampus.db.model.*;
import de.appsolve.padelcampus.exceptions.MailException;
import de.appsolve.padelcampus.exceptions.ResourceNotFoundException;
import de.appsolve.padelcampus.utils.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/players")
public class PlayersController extends BaseController {

    private static final Logger LOG = Logger.getLogger(PlayersController.class);

    @Autowired
    PlayerDAOI playerDAO;

    @Autowired
    TeamDAOI teamDAO;

    @Autowired
    EventDAOI eventDAO;

    @Autowired
    SessionUtil sessionUtil;

    @Autowired
    RankingUtil rankingUtil;

    @Autowired
    GameUtil gameUtil;

    @Autowired
    GameDAOI gameDAO;

    @RequestMapping(method = GET, value = "/player/{UUID}")
    public ModelAndView getPlayer(@PathVariable("UUID") String UUID, HttpServletRequest request) {
        return getPlayerView(playerDAO.findByUUID(UUID));
    }

    @RequestMapping(method = POST, value = "/player/{UUID}")
    public ModelAndView resendAccountVerificationEmail(@PathVariable("UUID") String UUID, HttpServletRequest request) throws MailException, IOException {
        Player player = sessionUtil.getUser(request);
        String accountVerificationLink = PlayerUtil.getAccountVerificationLink(request, player);
        Mail mail = new Mail();
        mail.addRecipient(player);
        mail.setSubject(msg.get("AccountVerificationEmailSubject"));
        mail.setBody(StringEscapeUtils.unescapeJava(msg.get("AccountVerificationEmailBody", new Object[]{player.toString(), accountVerificationLink, RequestUtil.getBaseURL(request)})));
        mailUtils.send(mail, request);
        ModelAndView mav = getPlayerView(playerDAO.findByUUID(UUID));
        mav.addObject("AccountVerificationLinkSent", true);
        return mav;
    }

    @RequestMapping(method = GET, value = "/player/{UUID}/games")
    public ModelAndView getGamesForPlayer(@PathVariable String UUID, @RequestParam(defaultValue = "date") String sortBy) {
        Player player = playerDAO.findByUUID(UUID);
        ModelAndView mav = new ModelAndView("players/games");
        mav.addObject("Player", player);
        mav.addObject("GameResultMap", gameUtil.getGameResultMap(player, sortBy));
        return mav;
    }

    @RequestMapping("/player/{UUID}/teams")
    public ModelAndView getTeamsForPlayer(@PathVariable String UUID) {
        Player player = playerDAO.findByUUID(UUID);
        List<Team> teams = teamDAO.findByPlayer(player);
        Collections.sort(teams, new TeamByNameComparator());
        Iterator<Team> iterator = teams.iterator();
        while (iterator.hasNext()) {
            boolean remove = true;
            Team team = iterator.next();
            List<Game> games = gameDAO.findByParticipant(team);
            for (Game game : games) {
                if (!game.getGameSets().isEmpty()) {
                    remove = false;
                    break;
                }
            }
            if (remove) {
                iterator.remove();
            }
        }
        ModelAndView mav = new ModelAndView("players/teams");
        mav.addObject("Player", player);
        mav.addObject("Teams", teams);
        return mav;
    }

    @RequestMapping("/event/{id}")
    public ModelAndView getByEvent(@PathVariable("id") Long eventId) {
        Event event = eventDAO.findByIdFetchWithParticipantsAndPlayers(eventId);
        if (event == null) {
            throw new ResourceNotFoundException();
        }
        Set<Player> participants = event.getAllPlayers();
        return getPlayersView(event, participants, msg.get("PlayersIn", new Object[]{participants.size(), event.getName()}));
    }

    private ModelAndView getPlayersView(Event event, Collection<Player> players, String title) {
        List<Ranking> ranking = rankingUtil.getPlayerRanking(players, new LocalDate());
        ModelAndView mav = new ModelAndView("players/players");
        mav.addObject("RankingMap", ranking);
        mav.addObject("title", title);
        mav.addObject("Model", event);
        return mav;
    }

    private ModelAndView getPlayerView(Player player) {
        if (player == null) {
            throw new ResourceNotFoundException();
        }
        if (player.getDeleted()) {
            return new ModelAndView("players/player-deleted");
        }
        List<Ranking> ranking = rankingUtil.getRanking(player.getGender(), new LocalDate());
        ModelAndView mav = new ModelAndView("players/player", "Player", player);
        Optional<Ranking> playerRanking = ranking.stream().filter(r -> r.getParticipant().equals(player)).findFirst();
        if (playerRanking.isPresent()) {
            mav.addObject("RankingValue", playerRanking.get().getValue());
        }
        return mav;
    }
}
