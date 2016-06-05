/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.data.CustomerI;
import de.appsolve.padelcampus.db.dao.MasterDataDAOI;
import de.appsolve.padelcampus.db.dao.ModuleDAOI;
import de.appsolve.padelcampus.db.model.MasterData;
import de.appsolve.padelcampus.db.model.Module;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class CompanyLogoUtil {
    
    @Autowired
    MasterDataDAOI masterDataDAO;
    
    @Autowired
    SessionUtil sessionUtil;
    
    public void initModules(HttpServletRequest request) {
        CustomerI customer = sessionUtil.getCustomer(request);
        
        ServletContext context = request.getServletContext();
        Map<String, String> companyLogoMap = (Map<String, String>) context.getAttribute(Constants.APPLICATION_COMPANY_LOGO_PATH);
        if (companyLogoMap == null){
            companyLogoMap = new HashMap<>();
        }
        String companyLogoPath = companyLogoMap.get(customer.getName());
        if (StringUtils.isEmpty(companyLogoPath)){
            MasterData masterData = masterDataDAO.findFirst();
            if (masterData == null){
                companyLogoPath = "/images/logo.png";
            } else {
                companyLogoPath = masterData.getCompanyLogoPath();
            }
            companyLogoMap.put(customer.getName(), companyLogoPath);
            context.setAttribute(Constants.APPLICATION_COMPANY_LOGO_PATH, companyLogoMap);
        }
    } 
    
    public void reloadModules(HttpServletRequest request) {
        ServletContext context = request.getServletContext();
        context.setAttribute(Constants.APPLICATION_COMPANY_LOGO_PATH, null);
        initModules(request);
    }
}
