/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.data;

import de.appsolve.padelcampus.annotations.EmailWithTld;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author dominik
 */
public class Mail {
    
    @NotEmpty(message = "{NotEmpty.email}")
    @EmailWithTld
    private String from;
    
    @EmailWithTld
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

    public void setRecipients(List<? extends EmailContact> contacts) {
        List<EmailContact> list = new ArrayList<>(contacts);
        this.recipients = list;
    }
    
    public <T extends EmailContact> void addRecipient(T contact) {
        List<EmailContact> contacts = getRecipients();
        contacts.add(contact);
        setRecipients(contacts);
    }
    
    @Override
    public String toString(){
        return String.format("[from: %s, to: %s, subject: %s]", getFrom(), getRecipients(), getSubject());
    }
}
