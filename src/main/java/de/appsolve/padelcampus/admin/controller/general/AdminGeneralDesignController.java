/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.general;

import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.model.CssAttribute;
import de.appsolve.padelcampus.utils.HtmlResourceUtil;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
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
    
    @RequestMapping()
    public ModelAndView getIndex() {
        return getIndexView(getDefaultAttributes());
    }
    
    @RequestMapping(method = POST)
    public ModelAndView postIndex(HttpServletRequest request) throws Exception {
        List<CssAttribute> atts = getDefaultAttributes();
        for (CssAttribute att: atts){
            att.setCssValue(request.getParameter(att.getName()));
        }
        
        htmlResourceUtil.updateCss(request.getServletContext(), atts);
        return getIndexView(atts);
    }

    private ModelAndView getIndexView(List<CssAttribute> atts) {
        ModelAndView mav = new ModelAndView("admin/general/design/index", "Models", atts);
        return mav;
    }

    private List<CssAttribute> getDefaultAttributes() {
        List<CssAttribute> atts = new ArrayList<>();
        atts.add(getCssAttribute("bgColor", "#94cfea"));
        atts.add(getCssAttribute("primaryColor", "#613815"));
        atts.add(getCssAttribute("primaryLinkColor", "#31708f"));
        return atts;
    }

    private CssAttribute getCssAttribute(String name, String cssDefaultValue) {
        CssAttribute att = new CssAttribute();
        att.setName(name);
        att.setCssDefaultValue(cssDefaultValue);
        return att;
    }
}
