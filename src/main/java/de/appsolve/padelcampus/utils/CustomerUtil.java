/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.db.model.Customer;
import java.io.File;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author dominik
 */
@Component
public class CustomerUtil {
    
    @Autowired
    Environment environment;
    
    public static Customer getCustomer(){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        if (request!=null){
            HttpSession session = request.getSession();
            if (session!=null){
                Object object = session.getAttribute(Constants.SESSION_CUSTOMER);
                if (object instanceof Customer){
                    return (Customer) object;
                }
            }
        }
        return null;
    }
    
    public File getCustomerDir(String customerName) {
       return new File(environment.getProperty(Constants.OPENSHIFT_DATA_DIR) + File.separator + customerName);
    }
}
