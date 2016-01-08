/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import de.appsolve.padelcampus.constants.ModuleType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author dominik
 */
@Entity
public class Module extends SortableEntity{
    
    @Transient
    private static final long serialVersionUID = 1L;
    
    @Column
    @Enumerated(EnumType.STRING)
    private ModuleType moduleType;
    
    @Column
    private String iconName;
    
    @Column
    @NotEmpty(message = "{NotEmpty.title}")
    @Pattern(regexp = "^[öÖäÄüÜßa-zA-Z0-9\\s\\-]*$", message = "{RegExp.AlphaNum}") 
    private String title;
    
    @Column
    private Boolean showInMenu;
    
    @Column
    private Boolean showInFooter;
    
    public ModuleType getModuleType() {
        return moduleType;
    }

    public void setModuleType(ModuleType moduleType) {
        this.moduleType = moduleType;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getShowInMenu() {
        return showInMenu == null ? Boolean.TRUE : showInMenu;
    }

    public void setShowInMenu(Boolean showInMenu) {
        this.showInMenu = showInMenu;
    }

    public Boolean getShowInFooter() {
        return showInFooter == null ? Boolean.TRUE : showInFooter;
    }

    public void setShowInFooter(Boolean showInFooter) {
        this.showInFooter = showInFooter;
    }
    
    public String getUrl(){
        switch (getModuleType()){
            case Page:
                return "/"+getModuleType().name().toLowerCase() + "/" + getTitle();
            default:
               return "/"+getModuleType().name().toLowerCase();
        }
    }

    @Override
    public String toString() {
        return title;
    }
}