/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.data;

import java.util.Set;

/**
 *
 * @author dominik
 */
public class Manifest {
    
    private String short_name;
    private String name;
    private Set<ManifestIcon> icons;
    private String start_url;
    private String display;
    private String background_color;
    private String theme_color;

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ManifestIcon> getIcons() {
        return icons;
    }

    public void setIcons(Set<ManifestIcon> icons) {
        this.icons = icons;
    }

    public String getStart_url() {
        return start_url;
    }

    public void setStart_url(String start_url) {
        this.start_url = start_url;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }

    public String getTheme_color() {
        return theme_color;
    }

    public void setTheme_color(String theme_color) {
        this.theme_color = theme_color;
    }
}
