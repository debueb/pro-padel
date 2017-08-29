/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.page;

import com.google.common.net.UrlEscapers;
import de.appsolve.padelcampus.controller.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/page/{moduleId}")
public class PageController extends BaseController {

    @RequestMapping()
    public ModelAndView getIndex(@PathVariable("moduleId") String moduleTitle) {
        RedirectView red = new RedirectView("/" + UrlEscapers.urlPathSegmentEscaper().escape(moduleTitle));
        red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        return new ModelAndView(red);
    }
}
