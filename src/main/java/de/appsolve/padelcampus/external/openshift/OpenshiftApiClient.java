/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.external.openshift;

import de.appsolve.padelcampus.external.openshift.model.Alias;
import de.appsolve.padelcampus.external.openshift.model.Application;
import de.appsolve.padelcampus.external.openshift.model.ApplicationsApiResponse;
import de.appsolve.padelcampus.external.openshift.model.OpenshiftApiResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author dominik
 */
@Component
public class OpenshiftApiClient {
    
    private final String OPENSHIFT_API_URL = "https://openshift.redhat.com/broker/rest/";
    
    @Autowired
    RestTemplate openshiftApiRestTemplate;
    
    public void addAlias(String aliasId, final String applicationUrl) throws OpenshiftApiException{
        ApplicationsApiResponse applicationsResponse = getApplications();
        List<Application> applications = applicationsResponse.getData();
        
        String appUrl = "http://"+applicationUrl;
        if (!appUrl.endsWith("/")){
            appUrl += "/";
        }
        for (Application application: applications){
            if (application.getApp_url().equalsIgnoreCase(appUrl)){
                boolean aliasExists = false;
                for (Alias existingAlias: application.getAliases()){
                    if (existingAlias.getId().equalsIgnoreCase(aliasId)){
                        aliasExists = true;
                        break;
                    }
                }
                if (!aliasExists){
                    postAlias(application.getId(), aliasId);
                }
                break;
            }
        }
    }
    
    private ApplicationsApiResponse getApplications() throws OpenshiftApiException{
        ApplicationsApiResponse response = openshiftApiRestTemplate.getForObject(OPENSHIFT_API_URL + "applications", ApplicationsApiResponse.class);
        if (response == null || response.getStatus() == null || !response.getStatus().equals("ok")){
            throw new OpenshiftApiException(response);
        }
        return response;
    }

    private void postAlias(String applicationId, String aliasId) throws OpenshiftApiException {
        Alias newAlias = new Alias();
        newAlias.setId(aliasId);
        OpenshiftApiResponse response = openshiftApiRestTemplate.postForObject(OPENSHIFT_API_URL + "application/" + applicationId + "/aliases", newAlias, OpenshiftApiResponse.class);
        if (response.getStatus() == null || !response.getStatus().equals("created")){
            throw new OpenshiftApiException(response);
        }
    }
    
}
