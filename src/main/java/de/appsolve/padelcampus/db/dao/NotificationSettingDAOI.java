/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.model.MatchOffer;
import de.appsolve.padelcampus.db.model.NotificationSetting;
import de.appsolve.padelcampus.db.model.Player;
import java.util.List;

/**
 *
 * @author dominik
 */
public interface NotificationSettingDAOI extends GenericDAOI<NotificationSetting>{
    
    NotificationSetting findBy(Player player);
    List<NotificationSetting> findFor(MatchOffer offer);
}
