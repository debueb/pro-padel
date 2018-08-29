package de.appsolve.padelcampus.db.dao;

import com.google.common.collect.ImmutableMap;
import de.appsolve.padelcampus.db.dao.generic.GenericDAO;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dominik
 */
@Component
public class TransactionDAO extends GenericDAO<Transaction> implements TransactionDAOI {

    @Override
    public List<Transaction> findByPlayer(Player player) {
        return super.findByAttributes(ImmutableMap.of("player", player));
    }
}
