/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.general;

import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.model.CssAttribute;
import de.appsolve.padelcampus.utils.HtmlResourceUtil;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
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
    
    @RequestMapping()
    public ModelAndView getIndex() {
        return getIndexView();
    }
    
    @RequestMapping(method = POST)
    public ModelAndView postIndex(HttpServletRequest request) throws Exception {
        Set<CssAttribute> atts = new HashSet<>();
        CssAttribute att = new CssAttribute();
        att.setName("primaryColor");
        att.setCssDefaultValue("#613815");
        att.setCssValue("#123456");
        atts.add(att);
        String rootPath = request.getServletContext().getRealPath("/");
        HtmlResourceUtil.updateCss(rootPath, atts);
        return getIndexView();
    }

    private ModelAndView getIndexView() {
        ModelAndView mav = new ModelAndView("admin/general/design/index", "Models", null);
        return mav;
    }
}
