/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.admin.controller.mail;

import com.google.common.collect.Sets;
import com.sparkpost.Client;
import com.sparkpost.exception.SparkPostException;
import com.sparkpost.model.TemplateItem;
import com.sparkpost.model.responses.TemplateListResponse;
import com.sparkpost.resources.ResourceTemplates;
import com.sparkpost.transport.RestConnection;
import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.dao.TeamDAOI;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;
import de.appsolve.padelcampus.exceptions.MailException;
import de.appsolve.padelcampus.spring.PlayerCollectionEditor;
import de.appsolve.padelcampus.utils.MailUtils;
import de.appsolve.padelcampus.utils.Msg;
import de.appsolve.padelcampus.utils.RequestUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author dominik
 */
@Controller
@RequestMapping("/admin/mail")
public class AdminMailController {

    private static final Logger LOG = Logger.getLogger(AdminMailController.class);

    @Autowired
    PlayerDAOI playerDAO;

    @Autowired
    TeamDAOI teamDAO;

    @Autowired
    EventDAOI eventDAO;

    @Autowired
    PlayerCollectionEditor playerCollectionEditor;

    @Autowired
    MailUtils mailUtils;

    @Autowired
    Msg msg;

    @Autowired
    Environment environment;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Set.class, "recipients", playerCollectionEditor);
    }

    @RequestMapping(method = GET, value = "/player/{PlayerUUID}")
    public ModelAndView mailPlayer(HttpServletRequest request, @PathVariable("PlayerUUID") String playerUUID) {
        Set<Player> player = Sets.newHashSet(playerDAO.findByUUID(playerUUID));
        return getMailView(player, request);
    }

    @RequestMapping(method = GET, value = "/team/{teamId}")
    public ModelAndView mailTeam(HttpServletRequest request, @PathVariable("teamId") Long teamId) {
        Team team = teamDAO.findByIdFetchWithPlayers(teamId);
        return getMailView(team.getPlayers(), request);
    }

    @RequestMapping(method = GET, value = "/event/{eventId}")
    public ModelAndView mailEvent(HttpServletRequest request, @PathVariable("eventId") Long eventId) {
        Event event = eventDAO.findByIdFetchWithParticipantsAndPlayers(eventId);
        return getMailView(event.getAllPlayers(), request);
    }

    @RequestMapping(method = GET, value = "/all")
    public ModelAndView mailAll(HttpServletRequest request) {
        Set<Player> allPlayers = Sets.newHashSet(playerDAO.findPlayersRegisteredForEmails());
        return getMailView(allPlayers, request);
    }

    @RequestMapping(method = POST)
    public ModelAndView postMailAll(HttpServletRequest request, @ModelAttribute("Model") Mail mail, BindingResult result) {
        if (result.hasErrors()) {
            return getMailView(mail);
        }
        try {
            mailUtils.send(mail, request);
            return new ModelAndView("admin/mail-success");
        } catch (IOException | MailException e) {
            result.addError(new ObjectError("*", e.getMessage()));
            return getMailView(mail);
        }
    }

    private ModelAndView getMailView(Set<Player> players, HttpServletRequest request) {
        Mail mail = new Mail();
        mail.setRecipients(players);
        mail.setBody(msg.get("MailAllPlayersBody", new Object[]{RequestUtil.getBaseURL(request) + "/account/profile"}));
        return getMailView(mail);
    }

    private ModelAndView getMailView(Mail mail) {
        ModelAndView mav = new ModelAndView("admin/mail", "Model", mail);
        try {
            Client client = new Client(environment.getProperty("SPARKPOST_API_KEY"));
            RestConnection connection = new RestConnection(client);
            TemplateListResponse templateListResponse = ResourceTemplates.listAll(connection);
            List<TemplateItem> templateItems = templateListResponse.getResults();
            mav.addObject("Templates", templateItems);
        } catch (SparkPostException ex) {
            LOG.error(ex);
        }
        return mav;
    }
}
