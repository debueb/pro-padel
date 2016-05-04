/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.ranking;

import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.TeamDAOI;
import de.appsolve.padelcampus.db.model.Participant;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;
import de.appsolve.padelcampus.utils.RankingUtil;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
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
@RequestMapping("/ranking")
public class RankingController extends BaseController {
    
    @Autowired
    RankingUtil rankingUtil;
    
    @Autowired
    TeamDAOI teamDAO;
    
    @RequestMapping
    public ModelAndView getIndex(){
        return new ModelAndView("ranking/index");
    }
    
    @RequestMapping("{gender}/{category}")
    public ModelAndView getRanking(@PathVariable("gender") Gender gender, @PathVariable("category") String category){
        ModelAndView mav = new ModelAndView("ranking/ranking");
        mav.addObject("gender", gender);
        mav.addObject("category", category);
        SortedMap<? extends Participant, BigDecimal> rankings = null;
        switch (category){
            case "singles":
                rankings = rankingUtil.getRanking(gender);
                break;
            case "doubles":
                List<Team> allTeams = teamDAO.findAllFetchWithPlayers();
                Iterator<Team> teams = allTeams.iterator();
                while (teams.hasNext()){
                    Team team = teams.next();
                    switch (category){
                        case "doubles":
                            boolean genderMatches = true;
                            for (Player player: team.getPlayers()){
                                if (!player.getGender().equals(gender)){
                                    genderMatches = false;
                                    break;
                                }
                            }
                            if (!genderMatches){
                                teams.remove();
                            }
                        break;
                    }
                }
                rankings = rankingUtil.getTeamRanking(gender, allTeams);
                break;
        }
        mav.addObject("Rankings", rankings);
        return mav;
    }
}
