/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.admin.controller;

import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.utils.HtmlResourceUtil;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author dominik
 */
@Controller
@RequestMapping("/css")
public class CssController extends BaseController{
    
    @Autowired
    HtmlResourceUtil htmlResourceUtil;
    
    @ResponseBody
    @RequestMapping(value="all.min.stylesheet", produces = "text/css")
    public String getAllMinCss(HttpServletRequest request, HttpServletResponse response) throws IOException{
        addCacheControlResponseHeader(response);
        return htmlResourceUtil.getAllMinCss(request.getServletContext(), "");
    }
    
    @ResponseBody
    @RequestMapping(value="{customerName}/all.min.stylesheet", produces = "text/css")
    public String getAllMinCss(HttpServletRequest request, HttpServletResponse response, @PathVariable("customerName") String customerName) throws IOException{
        addCacheControlResponseHeader(response);
        return htmlResourceUtil.getAllMinCss(request.getServletContext(), customerName);
    }

    private void addCacheControlResponseHeader(HttpServletResponse response) {
        response.addHeader(HttpHeaders.CACHE_CONTROL, CacheControl.maxAge(4, TimeUnit.HOURS).getHeaderValue());
    }
}
