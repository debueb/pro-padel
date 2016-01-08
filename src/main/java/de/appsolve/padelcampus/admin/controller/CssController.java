/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.admin.controller;

import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.utils.HtmlResourceUtil;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author dominik
 */
@Controller
@RequestMapping("css")
public class CssController extends BaseController{
    
    @Autowired
    HtmlResourceUtil htmlResourceUtil;
    
    @ResponseBody
    @RequestMapping(value="all.min.stylesheet", produces = "text/css")
    public String getAllMinCss(HttpServletRequest request) throws IOException{
        return htmlResourceUtil.getAllMinCss(request.getServletContext(), "");
    }
    
    @ResponseBody
    @RequestMapping(value="{customerName}/all.min.stylesheet", produces = "text/css")
    public String getAllMinCss(HttpServletRequest request, @PathVariable("customerName") String customerName) throws IOException{
        return htmlResourceUtil.getAllMinCss(request.getServletContext(), customerName);
    }
}
