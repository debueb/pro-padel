/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;
import static de.appsolve.padelcampus.constants.Constants.MANDRILL_API_KEY;
import de.appsolve.padelcampus.data.EmailContact;
import de.appsolve.padelcampus.data.Mail;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

/**
 *
 * @author dominik
 */
public class MailUtils {
    
    private static final Logger log = Logger.getLogger(MailUtils.class);
    
    public static boolean send(Mail mail) throws MandrillApiError, IOException {
        MandrillApi mandrillApi = new MandrillApi(MANDRILL_API_KEY);

        MandrillMessage message = new MandrillMessage();
        message.setSubject(mail.getSubject());
        message.setText(mail.getBody());
        message.setHtml(getHTML(mail.getBody()));
        message.setFromEmail(mail.getFrom());
        message.setFromName(mail.getFrom());
        
        if (!StringUtils.isEmpty(mail.getReplyTo())){
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Reply-To", mail.getReplyTo());
            message.setHeaders(headerMap);
        }

        // add recipients
        ArrayList<MandrillMessage.Recipient> recipients = new ArrayList<>();
        for (EmailContact contact: mail.getRecipients()){
            MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
            recipient.setEmail(contact.getEmailAddress());
            recipient.setName(contact.getEmailDisplayName());
            recipients.add(recipient);
        }
        message.setTo(recipients);
        message.setPreserveRecipients(true);
       
        MandrillMessageStatus[] messageStatusReports = mandrillApi.messages().send(message, false);
        if (messageStatusReports.length == mail.getRecipients().size()) {
            for (MandrillMessageStatus messageStatusReport : messageStatusReports) {
                String status = messageStatusReport.getStatus();
                if (!(status.equals("sent") || status.equals("queued") || status.equals("scheduled"))) {
                    throw new MandrillApiError("Mail backend returned status code '" + status +"' for recipient '"+messageStatusReport.getEmail()+"'. Reject reason: "+messageStatusReport.getRejectReason());
                }
            }
        } else {
            throw new MandrillApiError("Mail backend returned unexpected number of status reports: " + messageStatusReports.length);
        }
        return true;
    }

    private static String getHTML(String string) {
        string = string.replaceAll("(\r\n|\n)", "<br />");
        return string;
    }
}
