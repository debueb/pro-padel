/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import static de.appsolve.padelcampus.constants.Constants.SESSION_ACCESS_LEVEL;
import static de.appsolve.padelcampus.constants.Constants.SESSION_BOOKING;
import static de.appsolve.padelcampus.constants.Constants.SESSION_LOGIN_REDIRECT_PATH;
import static de.appsolve.padelcampus.constants.Constants.SESSION_PRIVILEGES;
import static de.appsolve.padelcampus.constants.Constants.SESSION_PROFILE_REDIRECT_PATH;
import static de.appsolve.padelcampus.constants.Constants.SESSION_USER;
import de.appsolve.padelcampus.constants.Privilege;
import de.appsolve.padelcampus.db.dao.AdminGroupDAOI;
import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.model.AdminGroup;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Player;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static de.appsolve.padelcampus.constants.Constants.SESSION_CUSTOMER;
import de.appsolve.padelcampus.data.CustomerI;


/*
 * @author Dominik
 */
@Component
public class SessionUtil {

    private static final Logger log = Logger.getLogger(SessionUtil.class);
    
    @Autowired
    AdminGroupDAOI adminGroupDAO;
    
    @Autowired
    EventDAOI eventDAO;
    
    public void setUser(HttpServletRequest request, Player player) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.setAttribute(SESSION_USER, player);
            if (player!=null){
                List<AdminGroup> adminGroups = adminGroupDAO.findByPlayer(player);
                Set<Privilege> privileges = new HashSet<>();
                for (AdminGroup adminGroup: adminGroups){
                    privileges.addAll(adminGroup.getPrivileges());
                }
                session.setAttribute(SESSION_PRIVILEGES, privileges);
                
                //set access level
                String accessLevel = "loggedIn";
                List<Event> eventsWithParticipant = eventDAO.findAllWithParticipant(player);
                if (!eventsWithParticipant.isEmpty()){
                    accessLevel = "loggedInAndParticipant";
                }
                session.setAttribute(SESSION_ACCESS_LEVEL, accessLevel);
            }
        }
    }

    public Player getUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            return (Player) session.getAttribute(SESSION_USER);
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public Set<Privilege> getPrivileges(HttpServletRequest request){
        HttpSession session = request.getSession();
        if (session != null) {
            Object obj = session.getAttribute(SESSION_PRIVILEGES);
            if (obj != null){
                return (Set<Privilege>) obj;
            }
        }
        return new HashSet<>();
    }
    
    public void setBooking(HttpServletRequest request, Booking booking){
        setObject(request, SESSION_BOOKING, booking);
    }
    
    public Booking getBooking(HttpServletRequest request){
        HttpSession session = request.getSession();
        return getBooking(session);
    }
    
    public Booking getBooking(HttpSession session){
        if (session != null) {
            Object booking = session.getAttribute(SESSION_BOOKING);
            if (booking!=null){
                return (Booking) booking;
            }
        }
        return null;
    }
    
    public void setLoginRedirectPath(HttpServletRequest request, String path){
        setObject(request, SESSION_LOGIN_REDIRECT_PATH, path);
    }
    
    public String getLoginRedirectPath(HttpServletRequest request){
        return (String) getObject(request, SESSION_LOGIN_REDIRECT_PATH);
    }
    
    public void setProfileRedirectPath(HttpServletRequest request, String path){
        setObject(request, SESSION_PROFILE_REDIRECT_PATH, path);
    }
    
    public String getProfileRedirectPath(HttpServletRequest request){
        return (String) getObject(request, SESSION_PROFILE_REDIRECT_PATH);
    }
    
    public void invalidate(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session!=null){
            session.invalidate();
        }
    }
   
    private void setObject(HttpServletRequest request, String attributeName, Object value) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.setAttribute(attributeName, value);
        } else {
            log.warn("Unable to set session varible due to null session");
        }
    }

    private Object getObject(HttpServletRequest request, String path) {
        HttpSession session = request.getSession();
        if (session != null) {
            Object attribute = session.getAttribute(path);
            if (attribute!=null){
                return attribute;
            }
        }
        return null;
    }

    public void setCustomer(HttpServletRequest httpRequest, CustomerI customer) {
        setObject(httpRequest, SESSION_CUSTOMER, customer);
    }
    
    public CustomerI getCustomer(HttpServletRequest request){
        return (CustomerI) getObject(request, SESSION_CUSTOMER);
    }
}
