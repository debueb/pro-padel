/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.spring;

import de.appsolve.padelcampus.db.dao.TeamDAOI;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class TeamCollectionEditor extends CustomCollectionEditor{
    
    @Autowired
    TeamDAOI teamDAO;
    
    public TeamCollectionEditor() {
        super(Set.class);
    }
    
    @Override
    protected Object convertElement(Object element) {
        if (element == null || !(element instanceof String)){
            return null;
        }
        return teamDAO.findByUUID((String)element);
    }
}
