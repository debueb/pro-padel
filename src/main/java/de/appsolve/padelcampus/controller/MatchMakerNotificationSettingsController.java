/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller;

import de.appsolve.padelcampus.constants.SkillLevel;
import de.appsolve.padelcampus.db.dao.NotificationSettingDAOI;
import de.appsolve.padelcampus.db.model.NotificationSetting;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.utils.Msg;
import de.appsolve.padelcampus.utils.SessionUtil;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/matchmaker/notificationsettings")
public class MatchMakerNotificationSettingsController extends BaseController {

    private static final Logger log = Logger.getLogger(MatchMakerNotificationSettingsController.class);
    
    @Autowired
    NotificationSettingDAOI notificationSettingDAO;

    @Autowired
    SessionUtil sessionUtil;
    
    @Autowired
    Msg msg;
    
    @RequestMapping()
    public ModelAndView getIndex(HttpServletRequest request) {
        Player user = sessionUtil.getUser(request);
        if (user==null){
            return new ModelAndView("include/loginrequired", "title", msg.get("NotificationSettings"));
        }
        //try to get existing setting
        NotificationSetting setting = notificationSettingDAO.findBy(user);
        //create a new one if none exists
        if (setting == null){
            setting = new NotificationSetting();
        }
        //if user has a skill level add presets
        if (user.getSkillLevel()!=null){
            Set<SkillLevel> skillLevels = new HashSet<>();
            skillLevels.add(user.getSkillLevel());
            setting.setSkillLevels(skillLevels);
        }
        return getIndexView(setting);
    }
    
    @RequestMapping(method=POST)
    public ModelAndView postIndex(HttpServletRequest request, @ModelAttribute("Model") NotificationSetting model, BindingResult result) {
        if (result.hasErrors()){
            return getIndexView(model);
        }
        notificationSettingDAO.saveOrUpdate(model);
        return new ModelAndView("redirect:/matchmaker");
    }

    private ModelAndView getIndexView(NotificationSetting model) {
        ModelAndView mav = new ModelAndView("matchmaker/notificationsettings/index", "Model", model);
        mav.addObject("SkillLevels", SkillLevel.values());
        return mav;
    }
}
