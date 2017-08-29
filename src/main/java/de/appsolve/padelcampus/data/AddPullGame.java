/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.data;

import de.appsolve.padelcampus.db.model.Player;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.Set;

/**
 * @author dominik
 */
public class AddPullGame {

    @NotEmpty(message = "{Size.team}")
    @Size(min = 2, max = 2, message = "{Size.team}")
    private Set<Player> team1;

    @NotEmpty(message = "{Size.team}")
    @Size(min = 2, max = 2, message = "{Size.team}")
    private Set<Player> team2;

    public Set<Player> getTeam1() {
        return team1;
    }

    public void setTeam1(Set<Player> team1) {
        this.team1 = team1;
    }

    public Set<Player> getTeam2() {
        return team2;
    }

    public void setTeam2(Set<Player> team2) {
        this.team2 = team2;
    }
}
