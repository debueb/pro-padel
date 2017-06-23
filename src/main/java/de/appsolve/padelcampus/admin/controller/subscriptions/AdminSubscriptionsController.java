/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.subscriptions;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.dao.SubscriptionDAOI;
import de.appsolve.padelcampus.db.model.Subscription;
import de.appsolve.padelcampus.spring.PlayerCollectionEditor;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/subscriptions")
public class AdminSubscriptionsController extends AdminBaseController<Subscription> {
    
    @Autowired
    SubscriptionDAOI subscriptionDAO;
    
    @Autowired
    PlayerCollectionEditor playerCollectionEditor;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Set.class, "players", playerCollectionEditor);
    }
    
    @Override
    public BaseEntityDAOI getDAO() {
        return subscriptionDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/subscriptions";
    }
}
