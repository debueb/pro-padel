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
import de.appsolve.padelcampus.data.CustomerI;
import de.appsolve.padelcampus.data.EmailContact;
import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.exceptions.MailException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 *
 * @author dominik
 */
@Component
public class MailUtils {
    
    @Autowired
    SessionUtil sessionUtil;
    
    @Autowired
    Environment environment;
    
    public void send(Mail mail, HttpServletRequest request) throws MailException, IOException {
        
        String from = environment.getProperty("SPARKPOST_DEFAULT_SENDER");
        if (request != null){
            CustomerI customer = sessionUtil.getCustomer(request);
            if (customer != null){
                if (!StringUtils.isEmpty(customer.getDefaultEmail())){
                    from = customer.getDefaultEmail();
                }
            }
        }
        String replyTo = StringUtils.isEmpty(mail.getFrom()) ? from : mail.getFrom();
        
        Client client = new Client(environment.getProperty("SPARKPOST_API_KEY"));

        try {
            TransmissionWithRecipientArray transmission = new TransmissionWithRecipientArray();

            List<RecipientAttributes> recipientArray = new ArrayList<>();
            for (EmailContact contact: mail.getRecipients()) {
                if (EmailValidator.getInstance(false, true).isValid(contact.getEmailAddress())){
                    RecipientAttributes recipientAttribs = new RecipientAttributes();
                    recipientAttribs.setAddress(new AddressAttributes(contact.getEmailAddress()));
                    recipientArray.add(recipientAttribs);
                }
            }
            if (recipientArray.isEmpty()){
                throw new MailException("No recipient specified for mail "+mail);
            }
            transmission.setRecipientArray(recipientArray);

            TemplateContentAttributes contentAttributes = new TemplateContentAttributes();
            contentAttributes.setFrom(new AddressAttributes(from));
            contentAttributes.setReplyTo(replyTo);
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
    
    private String getHTML(String string) {
        string = string.replaceAll("(\r\n|\n)", "<br />");
        return string;
    }

    public static String getMailTo(Collection<? extends EmailContact> emailContacts) {
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
