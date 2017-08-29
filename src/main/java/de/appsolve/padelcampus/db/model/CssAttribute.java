/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * @author dominik
 */
@Entity
public class CssAttribute extends CustomerEntity {

    @Transient
    private static final long serialVersionUID = 1L;

    @Column
    private String name;

    @Column
    private String cssDefaultValue;

    @Column
    private String cssValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCssDefaultValue() {
        return cssDefaultValue;
    }

    public void setCssDefaultValue(String cssDefaultValue) {
        this.cssDefaultValue = cssDefaultValue;
    }

    public String getCssValue() {
        return cssValue;
    }

    public void setCssValue(String cssValue) {
        this.cssValue = cssValue;
    }

    @Override
    public String toString() {
        return name;
    }
}