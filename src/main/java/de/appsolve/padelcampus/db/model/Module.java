/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import de.appsolve.padelcampus.constants.ModuleType;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Set;
import java.util.SortedSet;

import static de.appsolve.padelcampus.constants.Constants.PATH_HOME;

/**
 * @author dominik
 */
@Entity
public class Module extends SortableEntity {

    @Transient
    private static final long serialVersionUID = 1L;

    @Column
    @Enumerated(EnumType.STRING)
    private ModuleType moduleType;

    @Column
    private String iconName;

    @Column
    @NotEmpty(message = "{NotEmpty.title}")
    @Pattern(regexp = "^[öÖäÄüÜßa-zA-Z0-9\\s]*$", message = "{RegExp.AlphaNum}")
    private String title;

    @Column
    @Pattern(regexp = "^[öÖäÄüÜßa-zA-Z0-9\\s]*$", message = "{RegExp.AlphaNum}")
    private String seoTitle;

    @Column
    @Pattern(regexp = "^[öÖäÄüÜßa-zA-Z0-9\\s]*$", message = "{RegExp.AlphaNum}")
    private String urlTitle;

    @Column
    private String seoRobots;

    @Column
    private Boolean showInMenu;

    @Column
    private Boolean showInFooter;

    @Column
    private Boolean showOnHomepage;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<EventGroup> eventGroups;

    @Column
    private String shortDescription;

    @Lob
    private String description;

    @Column
    private String url;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("position")
    private SortedSet<Module> subModules;

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

    public String getSeoTitle() {
        return seoTitle == null ? title : seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getUrlTitle() {
        return urlTitle;
    }

    public void setUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }

    public String getSeoRobots() {
        return StringUtils.isEmpty(seoRobots) ? "index, follow" : seoRobots;
    }

    public void setSeoRobots(String seoRobots) {
        this.seoRobots = seoRobots;
    }

    public Boolean getShowOnHomepage() {
        return showOnHomepage == null ? Boolean.TRUE : showOnHomepage;
    }

    public void setShowOnHomepage(Boolean showOnHomepage) {
        this.showOnHomepage = showOnHomepage;
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

    public Set<EventGroup> getEventGroups() {
        return eventGroups;
    }

    public void setEventGroups(Set<EventGroup> eventGroups) {
        this.eventGroups = eventGroups;
    }

    public String getUrl() {
        if (moduleType == null) {
            return null;
        }
        switch (moduleType) {
            case LandingPage:
                return "/";
            case HomePage:
                return "/" + PATH_HOME;
            case Bookings:
                return "/bookings";
            case MatchOffers:
                return "/matchoffers";
            case Staff:
                return "/staff";
            case Ranking:
                return "/ranking";
            case Link:
                return url;
            case Events:
                String eventsName = "/events/" + getUrlTitle();
                eventsName = eventsName.replace(" ", "-");
                return eventsName;
            case Page:
            case Blog:
                String moduleName = "/" + getUrlTitle();
                moduleName = moduleName.replace(" ", "-");
                return moduleName;
            default:
                return "/";
        }

    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SortedSet<Module> getSubModules() {
        return subModules;
    }

    public void setSubModules(SortedSet<Module> subModules) {
        this.subModules = subModules;
    }

    @Override
    public String toString() {
        return title;
    }
}