/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.api.controllers;

import de.appsolve.padelcampus.listener.SessionEventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author dominik
 */
@Controller
@RequestMapping("/api/sessions")
public class ApiSessionsController {

    @RequestMapping
    @ResponseBody
    public Integer getSessionCount() {
        return SessionEventListener.getActiveSessions();
    }
}
