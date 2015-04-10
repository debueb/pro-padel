/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author dominik
 */
@Entity
public class FooterLink extends BaseEntity{
    
    @Transient
    private static final long serialVersionUID = 1L;
    
    @NotEmpty(message = "{NotEmpty.title}")
    @Column
    private String title;
    
    @NotEmpty(message = "{NotEmpty.message}")
    @Column(length=21584)
    private String message;
    

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
    
    @Override
    public String toString(){
        return title;
    }
}
