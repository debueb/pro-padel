/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.data;

import java.util.Set;

/**
 * @author dominik
 */
public interface CustomerI {

    String getName();

    String getDefaultEmail();

    String getDefaultLanguage();

    Set<String> getSupportedLanguages();
}
