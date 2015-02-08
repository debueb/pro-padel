package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.model.Image;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class ImageDAO extends GenericDAO<Image> implements ImageDAOI{

    @Override
    public Image findBySha256(String sha256) {
        return findByAttribute("sha256", sha256);
    }
}
