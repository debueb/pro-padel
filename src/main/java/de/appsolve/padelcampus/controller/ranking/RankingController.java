/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.ranking;

import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.constants.ModuleType;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.ParticipantDAOI;
import de.appsolve.padelcampus.db.dao.TeamDAOI;
import de.appsolve.padelcampus.db.model.Module;
import de.appsolve.padelcampus.db.model.Participant;
import de.appsolve.padelcampus.db.model.Ranking;
import de.appsolve.padelcampus.utils.ModuleUtil;
import de.appsolve.padelcampus.utils.RankingUtil;
import org.apache.commons.lang.NotImplementedException;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/ranking")
public class RankingController extends BaseController {

    @Autowired
    RankingUtil rankingUtil;

    @Autowired
    TeamDAOI teamDAO;

    @Autowired
    ParticipantDAOI participantDAO;

    @Autowired
    ModuleUtil moduleUtil;

    @RequestMapping
    public ModelAndView getIndex(HttpServletRequest request) {
        Module module = moduleUtil.getCustomerModule(request, ModuleType.Ranking);
        return getIndexView(module.getTitle(), module.getDescription());
    }

    @RequestMapping("{gender}/{category}")
    public ModelAndView getRanking(
            @PathVariable Gender gender,
            @PathVariable String category
    ) {
        return getRanking(gender, category, null, null);
    }

    @RequestMapping("{gender}/{category}/{participantUUID}")
    public ModelAndView getRanking(
            @PathVariable() Gender gender,
            @PathVariable() String category,
            @PathVariable() String participantUUID
    ) {
        return getRanking(gender, category, participantUUID, null);
    }

    @RequestMapping("{gender}/{category}/{participantUUID}/{date}")
    public ModelAndView getRanking(
            @PathVariable() Gender gender,
            @PathVariable() String category,
            @PathVariable() String participantUUID,
            @PathVariable(required = false) LocalDate date
    ) {
        ModelAndView mav = new ModelAndView(getPath() + "ranking/ranking");
        mav.addObject("gender", gender);
        mav.addObject("category", category);
        List<Ranking> rankings = null;
        switch (category) {
            case "individual":
                rankings = rankingUtil.getRanking(gender, date);
                break;
            case "team":
                rankings = rankingUtil.getTeamRanking(gender, date);
                break;
            default:
                throw new NotImplementedException("unsupported category");
        }
        if (participantUUID != null) {
            Participant participant = participantDAO.findByUUID(participantUUID);
            mav.addObject("SelectedParticipant", participant);
        }
        mav.addObject("Rankings", rankings);
        mav.addObject("path", getPath());
        return mav;
    }

    protected ModelAndView getIndexView(String title, String description) {
        ModelAndView mav = new ModelAndView(getPath() + "ranking/index");
        mav.addObject("path", getPath());
        mav.addObject("title", title);
        mav.addObject("description", description);
        return mav;
    }
}
