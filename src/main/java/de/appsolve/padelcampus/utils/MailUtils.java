/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import com.sparkpost.Client;
import com.sparkpost.exception.SparkPostException;
import de.appsolve.padelcampus.data.EmailContact;
import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.exceptions.MailException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author dominik
 */
public class MailUtils {
    
    private static final Logger LOG = Logger.getLogger(MailUtils.class);
    private final static String API_KEY = "3def953e23ed9c354a77cb6465c65cb34b32f2b4";
        
    public static void send(Mail mail) throws MailException, IOException {
        
        Client client = new Client(API_KEY);

        try {
            client.sendMessage(
                mail.getFrom(),
                getRecipients(mail),
                mail.getSubject(),
                mail.getBody(),
                getHTML(mail.getBody()));
        } catch (SparkPostException e){
            throw new MailException(e.getMessage(), e);
        }
    }
    
//    public static void send(Mail mail) throws MailException, IOException {
//        Mailin http = new Mailin("https://api.sendinblue.com/v2.0", "MHfRnvT7Vd2BEaCP");
//
//	Map <String, String> to = new HashMap<>();
//        for (EmailContact contact: mail.getRecipients()){
//            to.put(contact.getEmailAddress(), contact.getEmailDisplayName());
//        }
//        
//        Map <String, String> cc = new HashMap<>();
//        cc.put(mail.getFrom(), mail.getFrom());
//        
//        Map <String, String> headers = new HashMap<>();
//        //headers.put("Content-Type", "text/html; charset=UTF-8");
//        headers.put("Content-Type", "text/plain; charset=UTF-8");
//        
//        Map<String,Object > data = new HashMap<>();
//        data.put("to", to);
//        data.put("cc", cc);
//        data.put("replyto", new String [] {mail.getReplyTo(), mail.getReplyTo()});
//        data.put("from", new String [] {mail.getFrom(), mail.getFrom()});
//        data.put("subject", mail.getSubject());
//        data.put("html", getHTML(mail.getBody()));
//        data.put("text", mail.getBody());
//        data.put("headers", headers);
////        data.put("inline_image", new String [] {});
//
//        String response = http.send_email(data);
//        if (StringUtils.isEmpty(response)){
//            throw new MailException("SMTP backend did not return a response for mail: "+mail);
//        }
//        try {
//            Gson gson = new Gson();
//            MailinResponse mailinResponse = gson.fromJson(response, MailinResponse.class);
//            if (mailinResponse.isSuccessful()){
//                LOG.info("Successfully relayed mail: "+mail);
//            } else {
//                throw new MailException("SMTP backend returned "+response);
//            }
//        } catch (JsonSyntaxException e){
//            throw new MailException("SMTP backend returned invalid response: "+response);
//        }
//    }
    
    private static String getHTML(String string) {
        string = string.replaceAll("(\r\n|\n)", "<br />");
        return string;
    }

    private static List<String> getRecipients(Mail mail) {
        List<String> recipients = new ArrayList<>();
        for (EmailContact contact: mail.getRecipients()){
            recipients.add(contact.getEmailAddress());
        }
        return recipients;
    }
}
