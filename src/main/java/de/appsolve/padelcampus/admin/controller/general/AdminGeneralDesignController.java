/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.general;

import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.CssAttributeDAOI;
import de.appsolve.padelcampus.db.model.CssAttribute;
import de.appsolve.padelcampus.utils.HtmlResourceUtil;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @Autowired
    HtmlResourceUtil htmlResourceUtil;
    
    @Autowired
    CssAttributeDAOI cssAttributeDAO;
    
    @RequestMapping()
    public ModelAndView getIndex() {
        return getIndexView(getCssAttributes());
    }
    
    @RequestMapping(method = POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ModelAndView postIndex(HttpServletRequest request, @RequestParam("backgroundImage") MultipartFile file) throws Exception {
        List<CssAttribute> atts = getCssAttributes();
        for (CssAttribute att: atts){
            if (att.getName().equals("backgroundImage")){
                if (!file.isEmpty()){
                    StringBuilder sb = new StringBuilder("url(data:");
                    sb.append(file.getContentType());
                    sb.append(";base64,");
                    sb.append(Base64.encodeBase64String(file.getBytes()));
                    sb.append(")");
                    att.setCssValue(sb.toString());
                }
            } else {
                att.setCssValue(request.getParameter(att.getId()+""));
            }
            cssAttributeDAO.saveOrUpdate(att);
        }
        
        htmlResourceUtil.updateCss(request.getServletContext());
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
        
        Collections.sort(cssAttributes, new Comparator<CssAttribute>() {

            @Override
            public int compare(CssAttribute o1, CssAttribute o2) {
                Boolean endsWith1 = o1.getName().endsWith("Color");
                Boolean endsWith2 = o2.getName().endsWith("Color");
                return endsWith2.compareTo(endsWith1);
            }
        });
        return cssAttributes;
    }
}
