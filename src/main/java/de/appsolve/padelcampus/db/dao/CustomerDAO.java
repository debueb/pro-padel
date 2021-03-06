package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAO;
import de.appsolve.padelcampus.db.model.Customer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dominik
 */
@Component
public class CustomerDAO extends BaseEntityDAO<Customer> implements CustomerDAOI {

    @Override
    public Customer findByName(String name) {
        return super.findByAttribute("name", name);
    }

    @Override
    public Customer findByDomainName(String domainName) {
        List<Customer> allCustomers = findAll();
        for (Customer customer : allCustomers) {
            for (String domain : customer.getDomainNames()) {
                if (domain.equalsIgnoreCase(domainName)) {
                    return customer;
                }
            }
        }
        return null;
    }
}
