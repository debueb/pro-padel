/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import com.sparkpost.Client;
import com.sparkpost.exception.SparkPostException;
import com.sparkpost.model.AddressAttributes;
import com.sparkpost.model.RecipientAttributes;
import com.sparkpost.model.TemplateContentAttributes;
import com.sparkpost.model.TransmissionWithRecipientArray;
import com.sparkpost.resources.ResourceTransmissions;
import com.sparkpost.transport.RestConnection;
import de.appsolve.padelcampus.data.EmailContact;
import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.exceptions.MailException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * @author dominik
 */
public class MailUtils {
    
    private static final Logger LOG                 = Logger.getLogger(MailUtils.class);
    private static final String API_KEY             = "3def953e23ed9c354a77cb6465c65cb34b32f2b4";
    private static final String DEFAULT_FROM_EMAIL  = "noreply@pro-padel.de";
        
    public static void send(Mail mail) throws MailException, IOException {
        
        Client client = new Client(API_KEY);

        try {
            TransmissionWithRecipientArray transmission = new TransmissionWithRecipientArray();

            List<RecipientAttributes> recipientArray = new ArrayList<>();
            for (String recpient : getRecipients(mail)) {
                RecipientAttributes recipientAttribs = new RecipientAttributes();
                recipientAttribs.setAddress(new AddressAttributes(recpient));
                recipientArray.add(recipientAttribs);
            }
            transmission.setRecipientArray(recipientArray);

            TemplateContentAttributes contentAttributes = new TemplateContentAttributes();

            contentAttributes.setFrom(new AddressAttributes(DEFAULT_FROM_EMAIL));
            contentAttributes.setReplyTo(mail.getFrom());
            contentAttributes.setSubject(mail.getSubject());
            contentAttributes.setHtml(getHTML(mail.getBody()));
            contentAttributes.setText(mail.getBody());
            transmission.setContentAttributes(contentAttributes);

            RestConnection connection = new RestConnection(client);
            ResourceTransmissions.create(connection, 0, transmission);

        
        } catch (SparkPostException e){
            throw new MailException(e.getMessage(), e);
        }
    }
    
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

    public static String getMailTo(Set<? extends EmailContact> emailContacts) {
        StringBuilder emails = new StringBuilder();
        int i=0;
        for (EmailContact p: emailContacts){
            emails.append(p.getEmailAddress());
            if (i<emailContacts.size()-1){
                emails.append(",");
            }
            i++;
        }
        return emails.toString();
    }
}
