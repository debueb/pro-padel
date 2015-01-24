package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.model.Contact;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class ContactDAO extends SortedGenericDAO<Contact> implements ContactDAOI{

}
