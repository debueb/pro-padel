/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller;

import de.appsolve.padelcampus.constants.ModuleType;
import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.db.dao.ModuleDAOI;
import de.appsolve.padelcampus.db.dao.PageEntryDAOI;
import de.appsolve.padelcampus.db.model.Module;
import de.appsolve.padelcampus.db.model.PageEntry;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping()
public class RootController extends BaseController{
    
    @Autowired
    ModuleDAOI moduleDAO;
    
    @Autowired
    PageEntryDAOI pageEntryDAO;
    
    @RequestMapping("/")
    public ModelAndView getIndex(HttpServletRequest request){
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
        if (!StringUtils.isEmpty(userAgent) && userAgent.startsWith("ProPadel")){
            return getHomePage();
        }
        HttpSession session = request.getSession(true);
        Object landingPageChecked = session.getAttribute("LANDINGPAGE_PAGE_CHECKED");
        if (landingPageChecked == null){
            List<Module> modules = moduleDAO.findByModuleType(ModuleType.LandingPage);
            if (!modules.isEmpty()){
                Module landingPageModule = modules.get(0);
                List<PageEntry> rootEntries = pageEntryDAO.findByModule(landingPageModule);
                if (!rootEntries.isEmpty()){
                    ModelAndView mav = new ModelAndView("root");
                    mav.addObject("Module", landingPageModule);
                    mav.addObject("PageEntries", rootEntries);
                    mav.addObject("skipNavbar", true);
                    mav.addObject("skipFooter", true);
                    return mav;
                }
            }
        }
        return getHomePage();
    }
    
    @RequestMapping(value={"/home", "index", "index.html"})
    public ModelAndView getHome(HttpServletRequest request){
        HttpSession session = request.getSession(true);
        session.setAttribute("LANDINGPAGE_PAGE_CHECKED", true);
        return getHomePage();       
    }

    private ModelAndView getHomePage() {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("Mail", new Mail());
        List<Module> modules = moduleDAO.findByModuleType(ModuleType.HomePage);
        if (!modules.isEmpty()){
            Module homePageModule = modules.get(0);
            mav.addObject("Module", homePageModule);
            List<PageEntry> pageEntries = pageEntryDAO.findByModule(homePageModule);
            mav.addObject("PageEntries", pageEntries);
        }
        return mav;
    }
}
