/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.data;

import de.appsolve.padelcampus.db.model.Team;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @author dominik
 */
public class AddTeamGame {

    public AddTeamGame() {
        this.teams = Arrays.asList(new Team(), new Team());
    }

    @Valid
    List<Team> teams;

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}
