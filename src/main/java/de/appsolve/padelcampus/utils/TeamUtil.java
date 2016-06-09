/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;

/**
 *
 * @author dominik
 */
public class TeamUtil {

    public static String getTeamName(Team model) {
        StringBuilder name = new StringBuilder();
        int i=0;
        for (Player player: model.getPlayers()){
            name.append(player.getLastName());
            if (i<model.getPlayers().size()-1){
                name.append(" / ");
            }
            i++;
        }
        return name.toString();
    }
}
