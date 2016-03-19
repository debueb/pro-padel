/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import de.appsolve.padelcampus.data.EmailContact;
import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.mail.MailException;
import de.appsolve.padelcampus.mail.Mailin;
import de.appsolve.padelcampus.mail.MailinResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

/**
 *
 * @author dominik
 */
public class MailUtils {
    
    private static final Logger LOG = Logger.getLogger(MailUtils.class);
    /**
     * source: https://github.com/mailin-api/mailin-api-java/blob/master/V2.0/examples/tutorial.java
     * @param mail
     * @throws MailException
     * @throws IOException 
     */
    public static void send(Mail mail) throws MailException, IOException {
        Mailin http = new Mailin("https://api.sendinblue.com/v2.0", "MHfRnvT7Vd2BEaCP");

	Map <String, String> to = new HashMap<>();
        for (EmailContact contact: mail.getRecipients()){
            to.put(contact.getEmailAddress(), contact.getEmailDisplayName());
        }
        
        Map <String, String> cc = new HashMap<>();
        cc.put(mail.getFrom(), mail.getFrom());
        
        Map <String, String> headers = new HashMap<>();
        //headers.put("Content-Type", "text/html; charset=UTF-8");
        headers.put("Content-Type", "text/plain; charset=UTF-8");
        
        Map<String,Object > data = new HashMap<>();
        data.put("to", to);
        data.put("cc", cc);
        data.put("replyto", new String [] {mail.getReplyTo(), mail.getReplyTo()});
        data.put("from", new String [] {mail.getFrom(), mail.getFrom()});
        data.put("subject", mail.getSubject());
        data.put("html", getHTML(mail.getBody()));
        data.put("text", mail.getBody());
        data.put("headers", headers);
//        data.put("inline_image", new String [] {});

        String response = http.send_email(data);
        if (StringUtils.isEmpty(response)){
            throw new MailException("SMTP backend did not return a response for mail: "+mail);
        }
        try {
            Gson gson = new Gson();
            MailinResponse mailinResponse = gson.fromJson(response, MailinResponse.class);
            if (mailinResponse.isSuccessful()){
                LOG.info("Successfully relayed mail: "+mail);
            } else {
                throw new MailException("SMTP backend returned "+response);
            }
        } catch (JsonSyntaxException e){
            throw new MailException("SMTP backend returned invalid response: "+response);
        }
    }
    
//    public static boolean send(Mail mail) throws MandrillApiError, IOException {
//        MandrillApi mandrillApi = new MandrillApi(MANDRILL_API_KEY);
//
//        MandrillMessage message = new MandrillMessage();
//        message.setSubject(mail.getSubject());
//        message.setText(mail.getBody());
//        message.setHtml(getHTML(mail.getBody()));
//        message.setFromEmail(mail.getFrom());
//        message.setFromName(mail.getFrom());
//        
//        if (!StringUtils.isEmpty(mail.getReplyTo())){
//            Map<String, String> headerMap = new HashMap<>();
//            headerMap.put("Reply-To", mail.getReplyTo());
//            message.setHeaders(headerMap);
//        }
//
//        // add recipients
//        ArrayList<MandrillMessage.Recipient> recipients = new ArrayList<>();
//        for (EmailContact contact: mail.getRecipients()){
//            MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
//            recipient.setEmail(contact.getEmailAddress());
//            recipient.setName(contact.getEmailDisplayName());
//            recipients.add(recipient);
//        }
//        message.setTo(recipients);
//        message.setPreserveRecipients(true);
//       
//        MandrillMessageStatus[] messageStatusReports = mandrillApi.messages().send(message, false);
//        if (messageStatusReports.length == mail.getRecipients().size()) {
//            for (MandrillMessageStatus messageStatusReport : messageStatusReports) {
//                String status = messageStatusReport.getStatus();
//                if (!(status.equals("sent") || status.equals("queued") || status.equals("scheduled"))) {
//                    throw new MandrillApiError("Mail backend returned status code '" + status +"' for recipient '"+messageStatusReport.getEmail()+"'. Reject reason: "+messageStatusReport.getRejectReason());
//                }
//            }
//        } else {
//            throw new MandrillApiError("Mail backend returned unexpected number of status reports: " + messageStatusReports.length);
//        }
//        return true;
//    }

    private static String getHTML(String string) {
        string = string.replaceAll("(\r\n|\n)", "<br />");
        return string;
    }
}
