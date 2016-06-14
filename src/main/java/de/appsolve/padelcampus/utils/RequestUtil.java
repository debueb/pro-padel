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
        String scheme = request.getScheme();
        String serverName = request.getServerName() ;
        String portStr = "";
        int port = request.getServerPort();
        if (scheme.equals("http") && port != 80){
            portStr = ":"+port;
        } else if (scheme.equals("https") && port != 443){
            portStr = ":"+port;
        }
        return scheme+"://"+serverName + portStr + request.getContextPath();
    }
}
