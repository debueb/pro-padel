package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAO;
import de.appsolve.padelcampus.db.model.EmailConfirmation;
import org.springframework.stereotype.Component;

/**
 * @author dominik
 */
@Component
public class EmailConfirmationDAO extends BaseEntityDAO<EmailConfirmation> implements EmailConfirmationDAOI {

}
