/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller;

import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.dao.TeamDAOI;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Participant;
import de.appsolve.padelcampus.db.model.ParticipantI;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;
import de.appsolve.padelcampus.utils.Msg;
import de.appsolve.padelcampus.utils.RequestUtil;
import de.appsolve.padelcampus.utils.SessionUtil;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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
    Msg msg;
    
    @Autowired
    SessionUtil sessionUtil;
    
    @RequestMapping()
    public ModelAndView getIndex(){
        return getIndexView();
    }
    
    @RequestMapping("/all")
    public ModelAndView getAll(){
        List<Event> allEvents = eventDAO.findAll();
        Set<Player> players = new TreeSet<>();
        for(Event event: allEvents){
            Set<Participant> participants = event.getParticipants();
            for (Participant participant : participants) {
                if (participant instanceof Player){
                    Player player = (Player) participant;
                    players.add(player);
                } else if (participant instanceof Team){
                    Team team = (Team) participant;
                    players.addAll(team.getPlayers());
                }
            }
        }
        return getPlayersView(setToList(players));
    }
    
    @RequestMapping("/player/{id}")
    public ModelAndView addToContacts(@PathVariable("id") Long playerId, HttpServletRequest request){
        return getPlayerView(playerDAO.findById(playerId));
    }
    
    @RequestMapping(value = "/player/{id}/vcard.vcf")
    public void getPlayer(@PathVariable("id") Long playerId, HttpServletRequest request, HttpServletResponse response) throws IOException{
        // Alternatively: application/octet-stream
        // Depending on the desired browser behaviour
        // Be sure to test thoroughly cross-browser
        Player player = playerDAO.findById(playerId);
        StringBuilder sb = new StringBuilder();
        response.setHeader("Content-type", "text/x-vcard; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=\""+player.getDisplayName()+".vcf\";");
        sb.append("BEGIN:VCARD\n");
        sb.append("VERSION:3.0\n");
        sb.append("N:").append(player.getLastName()).append(";").append(player.getFirstName()).append(";;;\n");
        sb.append("FN:").append(player.getDisplayName()).append("\n");
        sb.append("EMAIL;type=INTERNET;type=WORK;type=pref:").append(player.getEmail()).append("\n");
        sb.append("TEL;type=CELL;type=VOICE;type=pref:").append(player.getPhone()).append("\n");
        sb.append("NOTE:Added by ").append(msg.get("ProjectName")).append("\n");
        sb.append("URL:").append(RequestUtil.getBaseURL(request)).append("/players/player/").append(player.getId()).append("\n");
        sb.append("END:VCARD");
        response.getOutputStream().write(sb.toString().getBytes(Charset.forName("UTF-8")));
        response.getOutputStream().flush();
    }
    
    @RequestMapping("/team/{teamId}")
    public ModelAndView getByTeam(@PathVariable("teamId") Long teamId){
        Team team = teamDAO.findById(teamId);
        return getPlayersView(setToList(team.getPlayers()));
    }
    
    @RequestMapping("/event/{id}")
    public ModelAndView getByEvent(@PathVariable("id") Long eventId){
        Event event = eventDAO.findById(eventId);
        Set<ParticipantI> participants = new LinkedHashSet<>();
        Set<Team> teams = event.getTeams();
        for (Team team: teams){
            Set<Player> players = team.getPlayers();
            participants.addAll(players);
        }
        return getPlayersView(setToList(participants));
    }

    private ModelAndView getPlayersView(List<? extends ParticipantI> players) {
        return new ModelAndView("players/players", "Players", players);
    }

    private ModelAndView getPlayerView(Player player) {
        ModelAndView mav = new ModelAndView("players/player", "Player", player);
        return mav;
    }

    private ModelAndView getIndexView() {
        ModelAndView mav = new ModelAndView("players/index");
        mav.addObject("Events", eventDAO.findAll());
        return mav;
    }
    
    private List<? extends ParticipantI> setToList(Set<? extends ParticipantI> participants){
        ParticipantI[] participantArray = new ParticipantI[participants.size()];
        return Arrays.asList(((Set<ParticipantI>)participants).toArray(participantArray));
    }
}
