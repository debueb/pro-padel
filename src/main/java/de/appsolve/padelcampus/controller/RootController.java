/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller;

import de.appsolve.padelcampus.db.dao.NewsDAOI;
import de.appsolve.padelcampus.db.model.News;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/")
public class RootController extends BaseController{
    
    @Autowired
    NewsDAOI newsDAO;
    
    @RequestMapping()
    public ModelAndView getIndex(){
        List<News> allNews = newsDAO.findAll();
        Iterator<News> iterator = allNews.iterator();
        while (iterator.hasNext()){
            News news = iterator.next();
            if (!news.getShowOnHomepage()){
                iterator.remove();
            }
        }
        return new ModelAndView("index", "AllNews", allNews);
    }
}
