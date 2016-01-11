package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAO;
import de.appsolve.padelcampus.db.model.Customer;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class CustomerDAO extends BaseEntityDAO<Customer> implements CustomerDAOI{

    @Override
    public Customer findByDomainName(String domainName) {
        List<Customer> allCustomers = findAll();
        for (Customer customer: allCustomers){
            for (String domain: customer.getDomainNames()){
                if (domain.equalsIgnoreCase(domainName)){
                    return customer;
                }
            }
        }
        return null;
    }

}
