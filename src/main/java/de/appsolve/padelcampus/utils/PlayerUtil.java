/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.dao.TeamDAOI;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author dominik
 */
@Component
public class PlayerUtil {

    @Autowired
    PlayerDAOI playerDAO;

    @Autowired
    TeamDAOI teamDAO;

    public static String getAccountVerificationLink(HttpServletRequest request, Player player) {
        return RequestUtil.getBaseURL(request) + "/login/confirm/" + player.getUUID();
    }

    public boolean isPasswordValid(Player player, String password) {
        if (player == null || StringUtils.isEmpty(player.getPasswordHash())) {
            return false;
        }
        if (!player.getSalted()) {
            String hash = DigestUtils.sha512Hex(password);
            return hash.equals(player.getPasswordHash());
        } else {
            return BCrypt.checkpw(password, player.getPasswordHash());
        }
    }

    public void markAsDeleted(Player user) {
        user.setDeleted(Boolean.TRUE);
        user.setAllowEmailContact(Boolean.FALSE);
        List<Team> teams = teamDAO.findByPlayer(user);
        if (teams != null) {
            for (Team team : teams) {
                team.setName(TeamUtil.getTeamName(team));
                teamDAO.saveOrUpdate(team);
            }
        }
        playerDAO.saveOrUpdate(user);
    }
}
