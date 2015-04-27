/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
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
    
    @NotEmpty(message = "{NotEmpty.title}")
    @Column
    private String title;
    
    @NotEmpty(message = "{NotEmpty.message}")
    @Column(columnDefinition = "text")
    private String message;
    
    @Column
    private Boolean showOnHomepage;
    
    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Module.class, fetch = FetchType.EAGER)
    private Module module;

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate newsDte) {
        this.lastModified = newsDte;
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

    public Boolean getShowOnHomepage() {
        return showOnHomepage == null ? Boolean.FALSE : showOnHomepage;
    }

    public void setShowOnHomepage(Boolean showOnHomepage) {
        this.showOnHomepage = showOnHomepage;
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
