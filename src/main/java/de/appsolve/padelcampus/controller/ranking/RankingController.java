/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.ranking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.constants.ModuleType;
import de.appsolve.padelcampus.constants.RankingCategory;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.ParticipantDAOI;
import de.appsolve.padelcampus.db.model.Module;
import de.appsolve.padelcampus.db.model.Participant;
import de.appsolve.padelcampus.db.model.Ranking;
import de.appsolve.padelcampus.exceptions.ResourceNotFoundException;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/ranking")
public class RankingController extends BaseController {

    @Autowired
    RankingUtil rankingUtil;

    @Autowired
    ParticipantDAOI participantDAO;

    @Autowired
    ModuleUtil moduleUtil;

    @Autowired
    ObjectMapper objectMapper;

    @RequestMapping
    public ModelAndView getIndex(HttpServletRequest request) {
        Module module = moduleUtil.getCustomerModule(request, ModuleType.Ranking);
        return getIndexView(module.getTitle(), module.getDescription());
    }

    @RequestMapping("{gender}/{category}")
    public ModelAndView getRanking(
            @PathVariable Gender gender,
            @PathVariable RankingCategory category
    ) {
        return getRanking(gender, category, LocalDate.now());
    }

    @RequestMapping("{gender}/{category}/{date}")
    public ModelAndView getRanking(
            @PathVariable Gender gender,
            @PathVariable RankingCategory category,
            @PathVariable(required = false) LocalDate date
    ) {
        ModelAndView mav = new ModelAndView(getPath() + "ranking/ranking");
        mav.addObject("gender", gender);
        mav.addObject("category", category);
        List<Ranking> rankings = null;
        switch (category) {
            case individual:
                rankings = rankingUtil.getRanking(gender, date);
                break;
            case team:
                rankings = rankingUtil.getTeamRanking(gender, date);
                break;
            default:
                throw new NotImplementedException("unsupported category");
        }
        mav.addObject("Rankings", rankings);
        mav.addObject("path", getPath());
        return mav;
    }

    @RequestMapping("{participantUUID}/history")
    public ModelAndView getRankingHistory(
            @PathVariable() String participantUUID
    ) throws JsonProcessingException {
        Participant participant = participantDAO.findByUUID(participantUUID);
        if (participant == null) {
            throw new ResourceNotFoundException();
        }

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(365);
        Map<Gender, Map<Long, BigDecimal>> genderDateRankingMap = new TreeMap<>();
        for (Gender gender : EnumSet.of(Gender.male, Gender.female, Gender.unisex)) {
            List<Ranking> rankings = rankingUtil.getPlayerRanking(gender, participant, startDate, endDate);
            if (rankings != null && !rankings.isEmpty()) {
                Map<Long, BigDecimal> dateRankingMap = new TreeMap<>();
                for (Ranking ranking : rankings) {
                    LocalDate date = ranking.getDate();
                    Long millis = date.toDateTimeAtStartOfDay().getMillis();
                    dateRankingMap.put(millis, ranking.getValue().setScale(0, RoundingMode.HALF_UP));
                }
                genderDateRankingMap.put(gender, dateRankingMap);
            }
        }
        ModelAndView mav = new ModelAndView(getPath() + "ranking/history");
        mav.addObject("Participant", participant);
        mav.addObject("GenderDateRankingMap", genderDateRankingMap);
        mav.addObject("ChartMap", objectMapper.writeValueAsString(genderDateRankingMap));
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
