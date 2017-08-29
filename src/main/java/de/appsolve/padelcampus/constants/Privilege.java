/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.constants;

import java.util.regex.Pattern;

/**
 * @author dominik
 */
public enum Privilege {

    AccessAdminInterface("/admin[\\/]?"),
    ManageCustomers("/admin/customers.*"),
    ManageGeneral("/admin/general.*"),
    ManageEvents("/admin/event.*"),
    ManageCommunities("/admin/communities.*"),
    ManagePlayers("/admin/players.*"),
    ManageTeams("/admin/teams.*"),
    ManageSubscriptions("/admin/subscriptions.*"),
    ManageBookings("/admin/bookings.*"),
    ManageContact("/admin/contact.*"),
    ManageStaff("/admin/staff.*"),
    ManageFiles("/admin/files.*"),
    SendMails("/admin/mail.*"),
    ViewReports("/admin/reports.*");

    private final Pattern pathPattern;

    private Privilege(String path) {
        pathPattern = Pattern.compile(path);
    }

    public Pattern getPathPattern() {
        return pathPattern;
    }
}
