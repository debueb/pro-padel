/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller;

import static de.appsolve.padelcampus.constants.Constants.BLOG_PAGE_SIZE;
import static de.appsolve.padelcampus.constants.Constants.PATH_HOME;
import de.appsolve.padelcampus.constants.ModuleType;
import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.db.dao.ModuleDAOI;
import de.appsolve.padelcampus.db.dao.PageEntryDAOI;
import de.appsolve.padelcampus.db.model.Module;
import de.appsolve.padelcampus.db.model.PageEntry;
import de.appsolve.padelcampus.exceptions.ResourceNotFoundException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping()
public class RootController extends BaseController{
    
    private static final Logger LOG = Logger.getLogger(RootController.class);
    
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
                    return mav;
                }
            }
        }
        return getHomePage();
    }
    
    @RequestMapping("/{moduleId}")
    public ModelAndView getIndex(@PathVariable("moduleId") String moduleTitle, @PageableDefault(size = BLOG_PAGE_SIZE) Pageable pageable){
        switch (moduleTitle){
            case PATH_HOME:
            case "netbeans-tomcat-status-test":
                return getHomePage();
            default:
                return getModuleView(moduleTitle, pageable);
        }
    }
    
    @RequestMapping(method=POST)
    public ModelAndView postIndex(HttpServletRequest request, @PageableDefault(size = BLOG_PAGE_SIZE) Pageable pageable, @PathVariable("moduleId") String moduleTitle, @ModelAttribute("Mail") Mail mail, BindingResult bindingResult){
        ModelAndView defaultView = getModuleView(moduleTitle, pageable);
        return sendMail(request, defaultView, mail, bindingResult);
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

    private ModelAndView getModuleView(String moduleTitle, Pageable pageable) {
        Module module = moduleDAO.findByUrlTitle(moduleTitle);
        if (module == null){
            LOG.error("Module does not exist: "+moduleTitle);
            throw new ResourceNotFoundException();
        }
        switch (module.getModuleType()){
            case Blog:
                ModelAndView blogView = new ModelAndView("blog/index");
                Page<PageEntry> page = pageEntryDAO.findByModule(module, pageable);
                blogView.addObject("PageEntries", page.getContent());
                blogView.addObject("Page", page);
                blogView.addObject("Module", module);
                return blogView;
            default:
                ModelAndView mav = new ModelAndView("page/index");
                mav.addObject("PageEntries", pageEntryDAO.findByModule(module));
                mav.addObject("Module", module);
                mav.addObject("Mail", new Mail());
                return mav;
        }
    }
}
