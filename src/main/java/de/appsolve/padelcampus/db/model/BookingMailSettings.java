/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * @author dominik
 */
@Entity
public class BookingMailSettings extends CustomerEntity {

    @Transient
    private static final long serialVersionUID = 1L;

    @Column(length = 8000)
    @NotEmpty(message = "{NotEmpty.body}")
    private String htmlBodyTemplate;

    public String getHtmlBodyTemplate() {
        return htmlBodyTemplate;
    }

    public void setHtmlBodyTemplate(String htmlBodyTemplate) {
        this.htmlBodyTemplate = htmlBodyTemplate;
    }
}
