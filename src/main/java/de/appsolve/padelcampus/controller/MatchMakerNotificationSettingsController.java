/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller;

import de.appsolve.padelcampus.constants.SkillLevel;
import de.appsolve.padelcampus.db.model.NotificationSetting;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
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

    @RequestMapping()
    public ModelAndView getIndex(HttpServletRequest request) {
        return getIndexView(new NotificationSetting());
    }
    
    @RequestMapping(method=POST)
    public ModelAndView postIndex(HttpServletRequest request, @ModelAttribute("Model") NotificationSetting model) {
        return getIndexView(model);
    }

    private ModelAndView getIndexView(NotificationSetting model) {
        ModelAndView mav = new ModelAndView("matchmaker/notificationsettings/index", "Model", model);
        mav.addObject("SkillLevels", SkillLevel.values());
        return mav;
    }
}
