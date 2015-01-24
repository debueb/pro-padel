/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.data;

import de.appsolve.padelcampus.db.model.Contact;
import java.util.List;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author dominik
 */
public class Mail {
    
    @NotEmpty(message = "{NotEmpty.email}")
    @Email(message = "{Email}")
    private String from;
    
    @NotEmpty(message = "{NotEmpty.subject}")
    private String subject;
    
    @NotEmpty(message = "{NotEmpty.body}")
    private String body;
    
    private List<Contact> recipients;
    
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
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

    public List<Contact> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<Contact> contacts) {
        this.recipients = contacts;
    }
}
