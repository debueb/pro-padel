/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.appsolve.padelcampus.constants.ModuleType;
import de.appsolve.padelcampus.data.JSEvent;
import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.dao.ModuleDAOI;
import de.appsolve.padelcampus.db.dao.PageEntryDAOI;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Module;
import de.appsolve.padelcampus.db.model.PageEntry;
import de.appsolve.padelcampus.exceptions.ResourceNotFoundException;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static de.appsolve.padelcampus.constants.Constants.BLOG_PAGE_SIZE;
import static de.appsolve.padelcampus.constants.Constants.PATH_HOME;

/**
 * @author dominik
 */
@Controller()
@RequestMapping()
public class RootController extends BaseController {

    private static final Logger LOG = Logger.getLogger(RootController.class);

    @Autowired
    ModuleDAOI moduleDAO;

    @Autowired
    EventDAOI eventDAO;

    @Autowired
    PageEntryDAOI pageEntryDAO;

    @Autowired
    ObjectMapper objectMapper;

    @RequestMapping("/")
    public ModelAndView getIndex(HttpServletRequest request) {
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
        if (!StringUtils.isEmpty(userAgent) && userAgent.startsWith("ProPadel")) {
            return getHomePage();
        }
        HttpSession session = request.getSession(true);
        Object landingPageChecked = session.getAttribute("LANDINGPAGE_PAGE_CHECKED");
        if (landingPageChecked == null) {
            List<Module> modules = moduleDAO.findByModuleType(ModuleType.LandingPage);
            if (!modules.isEmpty()) {
                Module landingPageModule = modules.get(0);
                List<PageEntry> rootEntries = pageEntryDAO.findByModule(landingPageModule);
                if (!rootEntries.isEmpty()) {
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

    @GetMapping("/{moduleId}")
    public ModelAndView getIndex(@PathVariable("moduleId") String moduleTitle, @PageableDefault(size = BLOG_PAGE_SIZE) Pageable pageable) {
        switch (moduleTitle) {
            case "autodisover":
            case "serviceworker":
            case "apple-app-site-association":
            case ".well-known":
                return new ModelAndView("error/404");
            case PATH_HOME:
            case "netbeans-tomcat-status-test":
                return getHomePage();
            default:
                return getModuleView(moduleTitle, pageable);
        }
    }

    @PostMapping("/{moduleId}")
    public ModelAndView postIndex(HttpServletRequest request, @PageableDefault(size = BLOG_PAGE_SIZE) Pageable pageable, @PathVariable("moduleId") String moduleTitle, @ModelAttribute("Mail") Mail mail, BindingResult bindingResult) {
        ModelAndView defaultView = getModuleView(moduleTitle, pageable);
        mail.setReplyTo(mail.getFrom());
        mail.setFrom(mailUtils.getDefaultSender(request));
        return sendMail(request, defaultView, mail, bindingResult);
    }

    private ModelAndView getHomePage() {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("Mail", new Mail());
        List<Module> modules = moduleDAO.findByModuleType(ModuleType.HomePage);
        if (!modules.isEmpty()) {
            Module homePageModule = modules.get(0);
            mav.addObject("Module", homePageModule);
            addPageEntries(homePageModule, mav);
        }
        return mav;
    }

    private ModelAndView getModuleView(String moduleTitle, Pageable pageable) {
        Module module = moduleDAO.findByUrlTitle(moduleTitle);
        if (module == null) {
            LOG.error("Module does not exist: " + moduleTitle);
            throw new ResourceNotFoundException();
        }
        switch (module.getModuleType()) {
            case Blog:
                ModelAndView blogView = new ModelAndView("blog/index");
                Page<PageEntry> page = pageEntryDAO.findByModule(module, pageable);
                blogView.addObject("PageEntries", page.getContent());
                blogView.addObject("Page", page);
                blogView.addObject("Module", module);
                return blogView;
            default:
                ModelAndView mav = new ModelAndView("page/index");
                addPageEntries(module, mav);
                mav.addObject("Module", module);
                mav.addObject("Mail", new Mail());
                return mav;
        }
    }

    private void addPageEntries(Module module, ModelAndView mav) {
        List<PageEntry> pageEntries = pageEntryDAO.findByModule(module);
        mav.addObject("PageEntries", pageEntries);
        for (PageEntry pageEntry : pageEntries) {
            if (pageEntry.getShowEventCalendar()) {
                List<Event> events = eventDAO.findAllForEventOverviewActiveStartingWith(LocalDate.now());
                List<JSEvent> jsEvents = new ArrayList<>();
                for (Event event : events) {
                    jsEvents.add(new JSEvent(event));
                }
                try {
                    mav.addObject("Events", objectMapper.writeValueAsString(jsEvents));
                } catch (JsonProcessingException e) {
                    LOG.error(e);
                }
                break;
            }
            if (pageEntry.getShowEventOverview()) {
                LocalDate today = LocalDate.now();
                List<Event> currentEvents = eventDAO.findAllForEventOverviewActiveStartingWith(today);
                Map<Integer, ArrayList<Event>> eventMap = new TreeMap<>();
                for (Event event : currentEvents) {
                    int monthOfYear = event.getStartDate().getMonthOfYear() - 1;
                    if (eventMap.get(monthOfYear) == null) {
                        eventMap.put(monthOfYear, new ArrayList<>());
                    }
                    eventMap.get(monthOfYear).add(event);
                }

                // add passed events of current month AFTER upcoming events of current month
                LocalDate firstOfMonth = today.withDayOfMonth(1);
                int currentMonth = firstOfMonth.getMonthOfYear() - 1;
                List<Event> passedEvents = eventDAO.findAllActiveStartingWithEndingBefore(firstOfMonth, today);
                if (passedEvents != null && !passedEvents.isEmpty()) {
                    if (eventMap.get(currentMonth) == null) {
                        eventMap.put(currentMonth, new ArrayList<>());
                    }
                    eventMap.get(currentMonth).addAll(passedEvents);
                }

                mav.addObject("EventMap", eventMap);
                mav.addObject("Today", today);
            }
        }
    }
}
