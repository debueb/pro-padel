/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller;

import de.appsolve.padelcampus.db.dao.GenericDAOI;
import de.appsolve.padelcampus.db.dao.NewsDAOI;
import de.appsolve.padelcampus.db.model.News;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/news")
public class AdminNewsController extends AdminBaseController<News> {
    
    @Autowired
    NewsDAOI newsDAO;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));
    }
    
    @Override
    public GenericDAOI getDAO() {
        return newsDAO;
    }

    @Override
    public String getModuleName() {
        return "news";
    }
}
