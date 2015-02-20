package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.model.MatchOffer;
import de.appsolve.padelcampus.db.model.NotificationSetting;
import de.appsolve.padelcampus.db.model.Player;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
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

    @Override
    public List<NotificationSetting> findFor(MatchOffer offer) {
        List<NotificationSetting> all = findAll();
        Iterator<NotificationSetting> iterator = all.iterator();
        while(iterator.hasNext()){
            NotificationSetting setting = iterator.next();
            if (Collections.disjoint(setting.getSkillLevels(), offer.getSkillLevels())){
                iterator.remove();
            }
        }
        return all;
    }
}
