/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.api.controllers;

import de.appsolve.padelcampus.api.data.Option;
import de.appsolve.padelcampus.db.model.Player;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author dominik
 */
@Controller
@RequestMapping("/api/players")
public class ApiPlayersController extends ApiBaseController<Player>{

    @Override
    Option getOption(Player model) {
        Option option = new Option();
        option.setValue(model.getUUID());
        option.setText(model.toString());
        return option;
    }
}
