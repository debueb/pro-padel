/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.constants;

import java.util.regex.Pattern;

/**
 *
 * @author dominik
 */
public enum Privilege {
    
    AccessAdminInterface("/admin"),
    ManageAdminGroups("/admin/admingroups.*"),
    ManageNews("/admin/news.*"),
    ManageEvents("/admin/events.*"),
    ManagePlayers("/admin/players.*"),
    ManageTeams("/admin/teams.*"),
    ManageBookings("/admin/bookings.*"),
    ManageContact("/admin/contact.*");
    
    private final Pattern pathPattern;

    private Privilege(String path){
        pathPattern = Pattern.compile(path);
    }
    
    public Pattern getPathPattern() {
        return pathPattern;
    }
}
