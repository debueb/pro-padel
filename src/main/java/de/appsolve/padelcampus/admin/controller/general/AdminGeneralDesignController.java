/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.general;

import de.appsolve.padelcampus.comparators.CssAttributeComparator;
import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.CssAttributeDAOI;
import de.appsolve.padelcampus.db.dao.CustomerDAOI;
import de.appsolve.padelcampus.db.dao.ImageDAOI;
import de.appsolve.padelcampus.db.model.CssAttribute;
import de.appsolve.padelcampus.db.model.Customer;
import de.appsolve.padelcampus.db.model.Image;
import de.appsolve.padelcampus.utils.HtmlResourceUtil;
import de.appsolve.padelcampus.utils.SessionUtil;
import de.appsolve.padelcampus.utils.imaging.ImageUtilI;
import java.io.File;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/general/design")
public class AdminGeneralDesignController extends BaseController{
    
    private static final Logger LOG = Logger.getLogger(AdminGeneralDesignController.class);
    
    @Autowired
    HtmlResourceUtil htmlResourceUtil;
    
    @Autowired
    CssAttributeDAOI cssAttributeDAO;
    
    @Autowired
    @Qualifier("TinifyImageUtil")
    ImageUtilI imageUtil;
    
    @Autowired
    SessionUtil sessionUtil;
    
    @Autowired
    CustomerDAOI customerDAO;
    
    @Autowired
    ImageDAOI imageDAO;
    
    @RequestMapping()
    public ModelAndView getIndex() {
        return getIndexView(getCssAttributes());
    }
    
    @RequestMapping(method = POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ModelAndView postIndex(
            HttpServletRequest request,
            @RequestParam("backgroundImage") MultipartFile backgroundImage, 
            @RequestParam(value = "backgroundImageRepeat", required = false, defaultValue = "false") Boolean backgroundImageRepeat, 
            @RequestParam(value = "backgroundSizeCover", required = false, defaultValue = "false") Boolean backgroundSizeCover, 
            @RequestParam(value = "loaderOpacity", required = false, defaultValue = "false") Boolean loaderOpacity, 
            @RequestParam("companyLogo") MultipartFile companyLogo, 
            @RequestParam("touchIcon") MultipartFile touchIcon) throws Exception {
        List<CssAttribute> atts = getCssAttributes();
        for (CssAttribute att: atts){
            switch (att.getName()){
                case "backgroundImage":
                    if (!backgroundImage.isEmpty()){
                        StringBuilder sb = new StringBuilder("url(data:");
                        sb.append(backgroundImage.getContentType());
                        sb.append(";base64,");
                        sb.append(Base64.encodeBase64String(backgroundImage.getBytes()));
                        sb.append(")");
                        att.setCssValue(sb.toString());
                    }
                    break;
                case "backgroundRepeat":
                    if (backgroundImageRepeat){
                        att.setCssValue("repeat");
                    } else {
                        att.setCssValue("no-repeat");
                    }
                    break;
                case "backgroundSize":
                    if (backgroundSizeCover){
                        att.setCssValue("cover");
                    } else {
                        att.setCssValue("inherit");
                    }
                    break;
                case "loaderOpacity":
                    if (loaderOpacity){
                        att.setCssValue("@loaderOpacity: 1");
                    } else {
                        att.setCssValue("@loaderOpacity: 0");
                    }
                    break;
                default:
                    att.setCssValue(request.getParameter(att.getName()+""));
            }
            cssAttributeDAO.saveOrUpdate(att);
        }
        Customer customer = (Customer) sessionUtil.getCustomer(request);
        if (!companyLogo.isEmpty()){
            
            //delete old picture from FS if it exists. will be removed from DB automatically due to orphanRemoval=true
            deleteImage(customer.getCompanyLogo());
            customer.setCompanyLogo(null);
            customerDAO.saveOrUpdate(customer);
            
            Image newImage = imageUtil.saveImage(companyLogo.getContentType(), companyLogo.getBytes(), Constants.DATA_DIR_COMPANY_LOGO_IMAGES);
            customer.setCompanyLogo(newImage);
            customer = customerDAO.saveOrUpdate(customer);
            sessionUtil.setCustomer(request, customer);
        }
        if (!touchIcon.isEmpty()){
            //delete old picture from FS
            deleteImage(customer.getTouchIcon());
            customer.setTouchIcon(null);
            customerDAO.saveOrUpdate(customer);
            
            Image image = imageUtil.saveImage(touchIcon.getContentType(), touchIcon.getBytes(), Constants.TOUCH_ICON_WIDTH, Constants.TOUCH_ICON_HEIGHT, Constants.DATA_DIR_TOUCH_ICON_IMAGE);
            customer.setTouchIcon(image);
            customer = customerDAO.saveOrUpdate(customer);
            sessionUtil.setCustomer(request, customer);
        }
        
        htmlResourceUtil.updateCss(request.getServletContext(), customer);
        return getIndexView(atts);
    }

    private ModelAndView getIndexView(List<CssAttribute> colors) {
        ModelAndView mav = new ModelAndView("admin/general/design/index", "Colors", colors);
        return mav;
    }

    private List<CssAttribute> getCssAttributes() {
        List<CssAttribute> cssAttributes = cssAttributeDAO.findAll();
        List<CssAttribute> defaultCssAttributes = htmlResourceUtil.getDefaultCssAttributes();
        for (CssAttribute defaultAttribute: defaultCssAttributes){
            boolean exists = false;
            for (CssAttribute att: cssAttributes){
                if (att.getName().equals(defaultAttribute.getName())){
                    exists = true;
                    break;
                }
            }
            if (!exists){
                cssAttributes.add(defaultAttribute);
            }
        }
        
        Collections.sort(cssAttributes, new CssAttributeComparator());
        return cssAttributes;
    }

    private void deleteImage(Image image) {
        if (image != null) {
            File profileFile = new File(image.getFilePath());
            if (profileFile.exists()) {
                boolean deleteSuccess = profileFile.delete();
                if (!deleteSuccess) {
                    LOG.warn("Unale to delete file " + profileFile.getAbsolutePath());
                }
            }
            imageDAO.deleteById(image.getId());
        }
    }
}
