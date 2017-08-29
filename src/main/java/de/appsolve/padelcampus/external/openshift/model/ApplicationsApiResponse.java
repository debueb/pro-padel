/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.external.openshift.model;

import java.util.List;

/**
 * @author dominik
 */
public class ApplicationsApiResponse extends OpenshiftApiResponse {

    private List<Application> data;

    public List<Application> getData() {
        return data;
    }

    public void setData(List<Application> data) {
        this.data = data;
    }
}
