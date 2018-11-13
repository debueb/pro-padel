package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.GenericDAO;
import de.appsolve.padelcampus.db.model.BookingMailSettings;
import org.springframework.stereotype.Component;

/**
 * @author dominik
 */
@Component
public class BookingMailSettingsDAO extends GenericDAO<BookingMailSettings> implements BookingMailSettingsDAOI {
}
