/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.data;

import static de.appsolve.padelcampus.constants.Constants.MAIL_NOREPLY_SENDER_NAME;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.utils.RequestUtil;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author dominik
 */
public class Mail {
    
    /**
     * default constructor
     */
    public Mail(){    
    }
    
    /**
     * convenience constructor. sender will automatically be set to no-reply@host.tld
     * @param request
     */
    public Mail(HttpServletRequest request){
        this.from = MAIL_NOREPLY_SENDER_NAME + "@" + RequestUtil.getMailHostName(request);
    }
    
    @NotEmpty(message = "{NotEmpty.email}")
    @Email(message = "{Email}")
    private String from;
    
    @Email(message = "{Email}")
    private String replyTo;
    
    @NotEmpty(message = "{NotEmpty.subject}")
    private String subject;
    
    @NotEmpty(message = "{NotEmpty.body}")
    private String body;
    
    private List<EmailContact> recipients;
    
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<EmailContact> getRecipients() {
        return recipients == null ? new ArrayList<EmailContact>() : recipients;
    }

    public void setRecipients(List<EmailContact> contacts) {
        this.recipients = contacts;
    }

    public void addRecipient(EmailContact contact) {
        List<EmailContact> contacts = getRecipients();
        contacts.add(contact);
        setRecipients(contacts);
    }
    
    @Override
    public String toString(){
        return String.format("[from: %s, to: %s, subject: %s]", getFrom(), getRecipients(), getSubject());
    }
}
