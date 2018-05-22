/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.data;

import java.util.Map;

/**
 * @author dominik
 */
public interface EmailContact {

    String getEmailAddress();

    String getEmailDisplayName();

    Map<String, Object> getSubstitutionData();

    void setSubstitutionData(Map<String, Object> substitutionData);

}
