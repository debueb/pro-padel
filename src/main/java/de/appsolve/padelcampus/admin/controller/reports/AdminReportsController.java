package de.appsolve.padelcampus.admin.controller.reports;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import de.appsolve.padelcampus.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/reports")
public class AdminReportsController extends BaseController {

    @RequestMapping()
    public ModelAndView getIndex() {
        return new ModelAndView("admin/reports/index");
    }
}
