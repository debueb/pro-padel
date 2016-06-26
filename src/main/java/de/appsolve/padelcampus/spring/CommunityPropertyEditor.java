/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.spring;

import de.appsolve.padelcampus.db.dao.CommunityDAOI;
import de.appsolve.padelcampus.db.model.Community;
import java.beans.PropertyEditorSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class CommunityPropertyEditor extends PropertyEditorSupport {

    @Autowired
    CommunityDAOI communityDAO;
    
    @Override
    public void setAsText(String text)
    {
        try {
            Long id = Long.parseLong(text);
            Community model = communityDAO.findById(id);
            setValue(model);
        } catch (NumberFormatException e){
            setValue(null);
        }
    }
}
