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
        List<CssAttribute> atts = cssAttributeDAO.findAll();
        if (atts.isEmpty()){
            atts.add(getCssAttribute("bgColor", "#94cfea"));
            atts.add(getCssAttribute("primaryColor", "#613815"));
            atts.add(getCssAttribute("primaryLinkColor", "#31708f"));
            atts.add(getCssAttribute("backgroundImage", "url\\('\\/images\\/bg\\.jpg'\\)"));
        }
        return atts;
    }

    private CssAttribute getCssAttribute(String name, String cssDefaultValue) {
        CssAttribute att = new CssAttribute();
        att.setName(name);
        att.setCssDefaultValue(cssDefaultValue);
        att = cssAttributeDAO.saveOrUpdate(att);
        return att;
    }
}
