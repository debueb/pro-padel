/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.admin.controller.general;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.SortableEntity;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author dominik
 * @param <T>
 */
public abstract class AdminSortableController<T extends SortableEntity> extends AdminBaseController<T> {
    
    @RequestMapping(value="/updatesortorder", method=POST)
    @ResponseStatus(OK)
    public void updateSortOrder(HttpServletRequest request, @ModelAttribute("Model") T model, @RequestBody List<Long> orderedIds){
        BaseEntityDAOI<T> dao = getDAO();
        Long position = 0L;
        for (Long id: orderedIds){
            T object = (T) dao.findById(id);
            object.setPosition(position);
            dao.saveOrUpdate(object);
            position++;
        }
    }
}
