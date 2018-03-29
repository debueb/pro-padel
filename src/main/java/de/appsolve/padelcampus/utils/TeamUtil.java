/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;

import java.util.*;

/**
 * @author dominik
 */
public class TeamUtil {

    public static String getTeamName(Team model) {
        StringBuilder name = new StringBuilder();
        int i = 0;
        Set<Player> players = model.getPlayers();
        List<Player> sortedPlayers = new ArrayList<>(players);
        Collections.sort(sortedPlayers, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o1.getLastName().compareTo(o2.getLastName());
            }
        });
        for (Player player : sortedPlayers) {
            name.append(player.toString());
            if (i < model.getPlayers().size() - 1) {
                name.append(" / ");
            }
            i++;
        }
        return name.toString();
    }

    public static Gender getGender(Team team) {
        Gender teamGender = null;
        if (team.getPlayers() != null) {
            for (Player player : team.getPlayers()) {
                if (teamGender == null) {
                    teamGender = player.getGender();
                } else if (!teamGender.equals(player.getGender())) {
                    teamGender = Gender.mixed;
                }
            }
        }
        return teamGender;
    }
}
