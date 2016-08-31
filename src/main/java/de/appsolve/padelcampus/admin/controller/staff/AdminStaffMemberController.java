/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.staff;

import com.drew.imaging.ImageProcessingException;
import de.appsolve.padelcampus.admin.controller.general.AdminSortableController;
import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.db.dao.StaffMemberDAOI;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.StaffMember;
import de.appsolve.padelcampus.utils.imaging.ImageUtilI;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/staff")
public class AdminStaffMemberController extends AdminSortableController<StaffMember> {
    
    @Autowired
    StaffMemberDAOI staffMemberDAO;
    
    @Autowired
    @Qualifier("TinifyImageUtil")
    ImageUtilI imageUtil;
    
    @Override
    public BaseEntityDAOI getDAO() {
        return staffMemberDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/staff";
    }
    
    @Override
    @RequestMapping(value={"add", "edit/{modelId}"}, method = POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ModelAndView postEditView(@ModelAttribute("Model") StaffMember model, HttpServletRequest request, BindingResult result){
        validator.validate(model, result);
        if (result.hasErrors()){
            return getEditView(model);
        }
        if (model.getProfileImageFile() != null && !model.getProfileImageFile().isEmpty()){
            try {
                model.setProfileImage(imageUtil.saveImage(model.getProfileImageFile().getContentType(), model.getProfileImageFile().getBytes(), Constants.STAFF_IMAGE_WIDTH, Constants.STAFF_IMAGE_HEIGHT, Constants.DATA_DIR_STAFF_IMAGES));
            } catch (IOException | ImageProcessingException ex) {
                LOG.error(ex);
            }
        }
        return super.postEditView(model, request, result);
    }
}
