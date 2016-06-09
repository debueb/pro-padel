/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.api.controllers;

import de.appsolve.padelcampus.api.data.Option;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.utils.SessionUtil;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author dominik
 */
@Controller
@RequestMapping("/api/players")
public class ApiPlayersController {
    
    @Autowired
    PlayerDAOI playerDAO;
    
    @Autowired
    SessionUtil sessionUtil;
    
    @RequestMapping(method = GET, value = "/options", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<Option> getOptions(HttpServletRequest request, @RequestParam("q") String q){
        Player user = sessionUtil.getUser(request);
        if (user == null){
            return null;
        }
        List<Option> options = new ArrayList<>();
        if (!StringUtils.isEmpty(q)){
            Page<Player> page = playerDAO.findAllByFuzzySearch(q);
            for (Player player: page.getContent()){
                Option option = new Option();
                option.setValue(player.getUUID());
                option.setText(player.toString());
                options.add(option);
            }
        }
        return options;
    }
}
