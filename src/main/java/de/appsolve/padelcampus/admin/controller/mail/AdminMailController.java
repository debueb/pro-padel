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
import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.data.EmailContact;
import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.data.MailResult;
import de.appsolve.padelcampus.db.dao.*;
import de.appsolve.padelcampus.db.model.*;
import de.appsolve.padelcampus.exceptions.MailException;
import de.appsolve.padelcampus.spring.PlayerCollectionEditor;
import de.appsolve.padelcampus.utils.MailUtils;
import de.appsolve.padelcampus.utils.Msg;
import de.appsolve.padelcampus.utils.RequestUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
    CommunityDAOI communityDAO;

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

    @Autowired
    EmailConfirmationDAOI emailConfirmationDAO;

    @Autowired
    Validator validator;

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

    @RequestMapping(method = GET, value = "/community/{communityId}")
    public ModelAndView mailCommunity(HttpServletRequest request, @PathVariable("communityId") Long communityId) {
        Community community = communityDAO.findById(communityId);
        return getMailView(community.getPlayers(), request);
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

    @RequestMapping(method = GET, value = "/all/unsubscribed")
    public ModelAndView mailAllUnsubscribed(HttpServletRequest request) {
        List<Player> players = playerDAO.findPlayersNotRegisteredForEmails();
        LOG.info(String.format("Found %s players not subscribed for emails", players.size()));
        Set<Player> allPlayers = Sets.newHashSet(players);
        return getMailView(allPlayers, request);
    }

    @RequestMapping(method = POST)
    public ModelAndView postMail(
            HttpServletRequest request,
            @ModelAttribute("Model") Mail mail,
            BindingResult result) {
        if (mail.getTemplateId() != null && (mail.getTemplateId().equals("TextEmail") || mail.getTemplateId().equals("HTMLEmail"))) {
            validator.validate(mail, result);
        }
        if (result.hasErrors()) {
            return getMailView(mail);
        }
        try {
            if (mail.getTemplateId() != null && !mail.getTemplateId().equals("TextEmail") && !mail.getTemplateId().equals("HTMLEmail")) {
                for (EmailContact contact : mail.getRecipients()) {
                    Map<String, Object> substitutionData = new HashMap<>();

                    //create uuid per contact
                    String uuid = UUID.randomUUID().toString();

                    //save in db
                    EmailConfirmation emailConfirmation = new EmailConfirmation();
                    emailConfirmation.setUuid(uuid);
                    emailConfirmation.setEmail(contact.getEmailAddress());
                    emailConfirmationDAO.saveOrUpdate(emailConfirmation);

                    //add to map
                    substitutionData.put("CONFIRM_EMAIL_LINK", RequestUtil.getBaseURL(request) + "/email/confirm/" + uuid);
                    substitutionData.put("UNSUBSCRIBE_EMAIL_LINK", RequestUtil.getBaseURL(request) + "/email/unsubscribe/" + uuid);
                    substitutionData.put("USERNAME", contact.getEmailDisplayName());
                    substitutionData.put("SALUTATION", contact.getGender().equals(Gender.male) ? "Lieber" : "Liebe");
                    contact.setSubstitutionData(substitutionData);
                }
            }

            MailResult mailResult = mailUtils.send(mail, request);
            return new ModelAndView("admin/mail-success", "MailResult", mailResult);
        } catch (IOException | MailException e) {
            result.addError(new ObjectError("*", e.getMessage()));
            return getMailView(mail);
        }
    }

    @RequestMapping(value = "export", method = POST)
    public HttpEntity<byte[]> exportEmails(@ModelAttribute("Model") Mail mail) {
        List<String> emails = new ArrayList<>();
        for (EmailContact player : mail.getRecipients()) {
            emails.add(player.getEmailAddress());
        }
        byte[] data = StringUtils.join(emails, ",").getBytes(StandardCharsets.UTF_8);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("text", "csv"));
        header.set("Content-Disposition", "attachment; filename=email-export.csv");
        header.setContentLength(data.length);
        return new HttpEntity<>(data, header);
    }

    private ModelAndView getMailView(Set<Player> players, HttpServletRequest request) {
        Mail mail = new Mail();
        mail.setFrom(mailUtils.getDefaultSender(request));
        mail.setRecipients(players);
        mail.setBody(msg.get("MailAllPlayersBody", new Object[]{RequestUtil.getBaseURL(request) + "/account/profile"}));
        mail.setHtmlBody(msg.get("MailAllPlayersBody", new Object[]{RequestUtil.getBaseURL(request) + "/account/profile"}));
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
