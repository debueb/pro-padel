/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.admin.controller.general;

import com.drew.imaging.ImageProcessingException;
import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.ImageDAOI;
import de.appsolve.padelcampus.db.dao.MasterDataDAOI;
import de.appsolve.padelcampus.db.model.Image;
import de.appsolve.padelcampus.db.model.MasterData;
import de.appsolve.padelcampus.utils.CompanyLogoUtil;
import de.appsolve.padelcampus.utils.imaging.ImageUtilI;
import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/general/masterdata")
public class AdminGeneralMasterDataController extends BaseController {

    private final static Logger LOG = Logger.getLogger(AdminGeneralMasterDataController.class);

    @Autowired
    MasterDataDAOI masterDataDAO;
    
    @Autowired
    ImageDAOI imageDAO;

    @Autowired
    @Qualifier("TinifyImageUtil")
    ImageUtilI imageUtil;
    
    @Autowired
    CompanyLogoUtil companyLogoUtil;

    @RequestMapping()
    public ModelAndView getIndex() {
        ModelAndView mav = new ModelAndView(getModuleName() + "/index");
        mav.addObject("Models", masterDataDAO.findAll());
        mav.addObject("moduleName", getModuleName());
        return mav;
    }

    @RequestMapping(value = {"add"}, method = GET)
    public ModelAndView showAddView() {
        return getEditView(new MasterData());
    }

    @RequestMapping(value = "edit/{modelId}", method = GET)
    public ModelAndView showEditView(@PathVariable("modelId") Long modelId) {
        return getEditView(masterDataDAO.findById(modelId));
    }

    @RequestMapping(method = POST, value = {"add", "edit/{id}"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ModelAndView postEditView(HttpServletRequest request, @PathVariable("id") Long masterModelId, @Valid @ModelAttribute("Model") MasterData model, @RequestParam("companyLogoFile") MultipartFile companyLogoFile, BindingResult result) {
        ModelAndView editView = getEditView(model);
        try {
            if (result.hasErrors()) {
                return editView;
            }
            if (companyLogoFile.isEmpty()) {
                //transfer old logo if it exists
                if (masterModelId != null){
                    MasterData existing = masterDataDAO.findById(masterModelId);
                    model.setCompanyLogo(existing.getCompanyLogo());
                }
            } else {
                //delete old picture from FS if it exists. will be removed from DB automatically due to orphanRemoval=true
                if (masterModelId != null) {
                    MasterData masterData = masterDataDAO.findById(masterModelId);
                    if (masterData != null) {
                        Image logo = masterData.getCompanyLogo();
                        if (logo != null) {
                            File profileFile = new File(masterData.getCompanyLogo().getFilePath());
                            if (profileFile.exists()) {
                                boolean deleteSuccess = profileFile.delete();
                                if (!deleteSuccess) {
                                    LOG.warn("Unale to delete file " + profileFile.getAbsolutePath());
                                }
                            }
                            model.setCompanyLogo(null);
                            model = masterDataDAO.saveOrUpdate(model);
                            imageDAO.deleteById(logo.getId());
                        }
                    }
                }
                //save new image to FS and DB
                byte[] bytes = companyLogoFile.getBytes();
                //when using ImageUtil, do not resize, as it will lose the alpha channel
                //Image image = imageUtil.saveImage(bytes, Constants.DATA_DIR_COMPANY_LOGO_IMAGES);
                //when using TinifyImageUtil, we can resize the image
                Image image = imageUtil.saveImage(bytes, Constants.COMPANY_LOGO_HEIGHT, Constants.DATA_DIR_COMPANY_LOGO_IMAGES);
                model.setCompanyLogo(image);
                companyLogoUtil.reloadModules(request);
            }
            masterDataDAO.saveOrUpdate(model);
            return new ModelAndView("redirect:/admin/general/masterdata");
        } catch (IOException | ImageProcessingException | com.tinify.Exception e) {
            result.addError(new ObjectError("*", e.getMessage()));
            return editView;
        }
    }

    private String getModuleName() {
        return "admin/general/masterdata";
    }

    private ModelAndView getEditView(MasterData masterData) {
        return new ModelAndView(getModuleName() + "/edit", "Model", masterData);
    }

}
