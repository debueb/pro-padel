/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.players;

import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.dao.TeamDAOI;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.ParticipantI;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;
import de.appsolve.padelcampus.utils.RequestUtil;
import de.appsolve.padelcampus.utils.SessionUtil;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/players")
public class PlayersController extends BaseController {
    
    @Autowired
    PlayerDAOI playerDAO;
    
    @Autowired
    TeamDAOI teamDAO;
    
    @Autowired
    EventDAOI eventDAO;
    
    @Autowired
    SessionUtil sessionUtil;
    
    @RequestMapping("/player/{UUID}")
    public ModelAndView getPlayer(@PathVariable("UUID") String UUID, HttpServletRequest request){
        return getPlayerView(playerDAO.findByUUID(UUID));
    }
    
    @RequestMapping(value = "/player/{UUID}/vcard.vcf")
    public void addToContacts(@PathVariable("UUID") String UUID, HttpServletRequest request, HttpServletResponse response) throws IOException{
        // Alternatively: application/octet-stream
        // Depending on the desired browser behaviour
        // Be sure to test thoroughly cross-browser
        Player player = playerDAO.findByUUID(UUID);
        StringBuilder sb = new StringBuilder();
        response.setHeader("Content-type", "text/x-vcard; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=\""+player.toString()+".vcf\";");
        sb.append("BEGIN:VCARD\n");
        sb.append("VERSION:3.0\n");
        sb.append("N:").append(player.getLastName()).append(";").append(player.getFirstName()).append(";;;\n");
        sb.append("FN:").append(player.toString()).append("\n");
        sb.append("EMAIL;type=INTERNET;type=WORK;type=pref:").append(player.getEmail()).append("\n");
        sb.append("TEL;type=CELL;type=VOICE;type=pref:").append(player.getPhone()).append("\n");
        sb.append("NOTE:Added by ").append(sessionUtil.getCustomer(request)).append("\n");
        sb.append("URL:").append(RequestUtil.getBaseURL(request)).append("/players/player/").append(player.getUUID()).append("\n");
        sb.append("END:VCARD");
        response.getOutputStream().write(sb.toString().getBytes(Charset.forName("UTF-8")));
        response.getOutputStream().flush();
    }
    
    @RequestMapping("/event/{id}")
    public ModelAndView getByEvent(@PathVariable("id") Long eventId){
        Event event = eventDAO.findByIdFetchWithParticipantsAndPlayers(eventId);
        Set<ParticipantI> participants = new LinkedHashSet<>();
        Set<Team> teams = event.getTeams();
        for (Team team: teams){
            Set<Player> players = team.getPlayers();
            participants.addAll(players);
        }
        participants.addAll(event.getPlayers());
        
        return getPlayersView(event, new ArrayList<>(participants), msg.get("PlayersIn", new Object[]{event.getName()}));
    }

    private ModelAndView getPlayersView(Event event, List<? extends ParticipantI> players, String title){
        ModelAndView mav = new ModelAndView("players/players", "Players", players);
        mav.addObject("title", title);
        mav.addObject("Model", event);
        return mav;
    }

    private ModelAndView getPlayerView(Player player) {
        ModelAndView mav = new ModelAndView("players/player", "Player", player);
        return mav;
    }
}
