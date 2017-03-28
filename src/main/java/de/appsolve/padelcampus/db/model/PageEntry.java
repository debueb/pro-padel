/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;

/**
 *
 * @author dominik
 */
@Entity
public class PageEntry extends SortableEntity{
    
    @Transient
    private static final long serialVersionUID = 1L;
    
    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate lastModified;
    
    @OneToOne
    private Player author;
    
    @NotEmpty(message = "{NotEmpty.title}")
    @Column
    private String title;
    
    @NotEmpty(message = "{NotEmpty.message}")
    @Lob
    private String message;
    
    @Column
    private Boolean fullWidth;
    
    @Column
    private Boolean showContactForm;
    
    @ManyToOne(targetEntity = Module.class, fetch = FetchType.EAGER)
    private Module module;

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate newsDte) {
        this.lastModified = newsDte;
    }

    public Player getAuthor() {
        return author;
    }

    public void setAuthor(Player author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getFullWidth() {
        return fullWidth == null ? Boolean.FALSE : fullWidth;
    }

    public void setFullWidth(Boolean fullWidth) {
        this.fullWidth = fullWidth;
    }

    public Boolean getShowContactForm() {
        return showContactForm == null ? Boolean.FALSE : showContactForm;
    }

    public void setShowContactForm(Boolean showContactForm) {
        this.showContactForm = showContactForm;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
    
    @Override
    public String toString(){
        return title;
    }
}
