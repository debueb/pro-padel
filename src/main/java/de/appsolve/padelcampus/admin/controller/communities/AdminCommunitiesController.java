/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.communities;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.db.dao.CommunityDAOI;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.Community;
import de.appsolve.padelcampus.spring.CommunityPropertyEditor;
import de.appsolve.padelcampus.spring.SortedPlayerCollectionEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.SortedSet;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/communities")
public class AdminCommunitiesController extends AdminBaseController<Community> {

    @Autowired
    CommunityDAOI communityDAO;

    @Autowired
    SortedPlayerCollectionEditor sortedPlayerCollectionEditor;

    @Autowired
    CommunityPropertyEditor communityPropertyEditor;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(SortedSet.class, "players", sortedPlayerCollectionEditor);
        binder.registerCustomEditor(Community.class, communityPropertyEditor);
    }

    @Override
    public BaseEntityDAOI getDAO() {
        return communityDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/communities";
    }

    @Override
    protected ModelAndView redirectToIndex(HttpServletRequest request) {
        String redirectUrl = request.getParameter("redirectUrl");
        if (!StringUtils.isEmpty(redirectUrl)) {
            return new ModelAndView("redirect:" + redirectUrl);
        }
        return new ModelAndView("redirect:/" + getModuleName());
    }
}
