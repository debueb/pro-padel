package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.GenericDAO;
import de.appsolve.padelcampus.db.model.LoginCookie;
import org.springframework.stereotype.Component;

/**
 * @author dominik
 */
@Component
public class LoginCookieDAO extends GenericDAO<LoginCookie> implements LoginCookieDAOI {

    @Override
    public LoginCookie findByUUID(String UUID) {
        return super.findByAttribute("UUID", UUID);
    }
}
