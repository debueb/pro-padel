/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.data;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author dominik
 */
public class DatePickerDayConfig {

    private Boolean selectable;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cssClass;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tooltip;

    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }
}
