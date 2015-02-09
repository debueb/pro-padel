package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.model.NotificationSetting;
import de.appsolve.padelcampus.db.model.Player;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class NotificationSettingDAO extends GenericDAO<NotificationSetting> implements NotificationSettingDAOI{

    @Override
    public NotificationSetting findBy(Player player) {
        return findByAttribute("player", player);
    }
}
