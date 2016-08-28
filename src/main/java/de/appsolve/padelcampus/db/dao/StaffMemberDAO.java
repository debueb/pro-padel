package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.SortedGenericDAO;
import de.appsolve.padelcampus.db.model.StaffMember;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class StaffMemberDAO extends SortedGenericDAO<StaffMember> implements StaffMemberDAOI{
    
    @Override
    protected Set<String> getIndexedProperties(){
       return new HashSet<>(Arrays.asList("name")); 
    }
}
