/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.external.openshift.model;

import java.util.List;

/**
 *
 * @author dominik
 */
public class OpenshiftApiResponse {
    
    private String status;
    private String type;
    private List<Message> messages;
    private List<Double> supported_api_versions;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Double> getSupported_api_versions() {
        return supported_api_versions;
    }

    public void setSupported_api_versions(List<Double> supported_api_versions) {
        this.supported_api_versions = supported_api_versions;
    }
}
