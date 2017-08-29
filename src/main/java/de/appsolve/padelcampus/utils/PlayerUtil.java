/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.model.Player;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dominik
 */
@Component
public class PlayerUtil {

    @Autowired
    Msg msg;

    @Autowired
    PlayerDAOI playerDAO;

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
}
