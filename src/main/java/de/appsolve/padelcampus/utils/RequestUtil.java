/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dominik
 */
public class RequestUtil {
    
    public static String getBaseURL(HttpServletRequest request){
        return request.getScheme()+"://"+request.getServerName()+request.getContextPath();
    }

    public static String getMailHostName(HttpServletRequest request) {
        return request.getServerName().equals("localhost") ? "padelcampus-appsolve.rhcloud.com" : request.getServerName();
    }
    
}
