/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller.account;

import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.utils.Msg;
import de.appsolve.padelcampus.utils.SessionUtil;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/account/changepassword")
public class AccountPasswordController extends BaseController {

    @Autowired
    PlayerDAOI playerDAO;

    @Autowired
    SessionUtil sessionUtil;

    @Autowired
    Msg msg;

    @RequestMapping()
    public ModelAndView getIndex(HttpServletRequest request) {
        return getIndexView(new ChangePasswordRequest());
    }

    @RequestMapping(method = POST)
    public ModelAndView postIndex(@Valid @ModelAttribute("Model") ChangePasswordRequest changePasswordRequest, BindingResult bindingResult, HttpServletRequest request) {
        ModelAndView mav = getIndexView(changePasswordRequest);
        if (bindingResult.hasErrors()) {
            return mav;
        }
        Player user = sessionUtil.getUser(request);
        String hash = DigestUtils.sha512Hex(changePasswordRequest.getOldPass());
        if (user.getPasswordHash().equals(hash)){
            if (changePasswordRequest.getNewPass().equals(changePasswordRequest.getNewPassRepeat())){
                user.setPasswordHash(DigestUtils.sha512Hex(changePasswordRequest.getNewPass()));
                playerDAO.saveOrUpdate(user);
                bindingResult.addError(new ObjectError("*", msg.get("PasswordWasChanged")));
            } else {
                bindingResult.addError(new ObjectError("*", msg.get("NewPasswordsDoNotMatch")));
            }
        } else {
            bindingResult.addError(new ObjectError("*", msg.get("InvalidPassword")));
        }
        return mav;
    }

    private ModelAndView getIndexView(ChangePasswordRequest request) {
        ModelAndView mav = new ModelAndView("account/changepassword/index", "Model", request);
        return mav;
    }
}
