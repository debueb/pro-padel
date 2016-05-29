/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.external.openshift;

import de.appsolve.padelcampus.external.openshift.model.OpenshiftApiResponse;

/**
 *
 * @author dominik
 */
class OpenshiftApiException extends Exception {

    public OpenshiftApiException(OpenshiftApiResponse response) {
        super("Openshift API returned invalid response: [status: "+response.getStatus()+", messages"+response.getMessages()+"]");
    }
    
}
