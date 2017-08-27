package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.constants.ImageCategory;
import de.appsolve.padelcampus.db.dao.generic.GenericDAO;
import de.appsolve.padelcampus.db.model.Image;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dominik
 */
@Component
public class ImageDAO extends GenericDAO<Image> implements ImageDAOI {

    @Override
    public Image findBySha256(String sha256) {
        return findByAttribute("sha256", sha256);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Image> findAllWithContent(ImageCategory category) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.isNotNull("content"));
        criteria.add(Restrictions.isNotNull("contentLength"));
        criteria.add(Restrictions.eq("category", category));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<Image>) criteria.list();
    }
}
