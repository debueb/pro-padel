/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.account;

import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.utils.LoginUtil;
import de.appsolve.padelcampus.utils.PlayerUtil;
import de.appsolve.padelcampus.utils.SessionUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/account")
public class AccountController extends BaseController {

    @Autowired
    SessionUtil sessionUtil;

    @Autowired
    PlayerUtil playerUtil;

    @Autowired
    LoginUtil loginUtil;

    @RequestMapping()
    public ModelAndView getIndex() {
        return new ModelAndView("account/index");
    }

    @RequestMapping(method = GET, value = "/delete")
    public ModelAndView getDelete(HttpServletRequest request) {
        return getDeleteView(request);
    }

    @RequestMapping(method = POST, value = "/delete")
    public ModelAndView postDelete(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = getDeleteView(request);
        try {
            playerUtil.markAsDeleted(sessionUtil.getUser(request));
            loginUtil.deleteLoginCookie(request, response);
            sessionUtil.invalidate(request);
            return new ModelAndView("account/delete-success");
        } catch (Exception e) {
            mav.addObject("error", StringUtils.isEmpty(e.getMessage()) ? e.getClass().getSimpleName() : e.getMessage());
            return mav;
        }
    }

    private ModelAndView getDeleteView(HttpServletRequest request) {
        Player user = sessionUtil.getUser(request);
        if (user == null) {
            return getLoginRequiredView(request, msg.get("DeleteAccount"));
        }
        return new ModelAndView("account/delete");
    }
}
