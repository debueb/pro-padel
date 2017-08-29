/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller;

import de.appsolve.padelcampus.db.dao.ModuleDAOI;
import de.appsolve.padelcampus.db.dao.PageEntryDAOI;
import de.appsolve.padelcampus.db.model.Module;
import de.appsolve.padelcampus.db.model.PageEntry;
import de.appsolve.padelcampus.exceptions.ResourceNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static de.appsolve.padelcampus.constants.Constants.BLOG_PAGE_SIZE;

/**
 * @author dominik
 */
@Controller
public class BlogController {

    private static final Logger LOG = Logger.getLogger(BlogController.class);

    @Autowired
    ModuleDAOI moduleDAO;

    @Autowired
    PageEntryDAOI pageEntryDAO;

    /* {moduleTitle}/{pageEntryTable} mapping breaks static resource mapping, since @RequestMappings have precedence over static resource mappings */
    @RequestMapping("{moduleTitle}/{pageEntryTitle}")
    public ModelAndView getBlogEntry(@PathVariable String moduleTitle, @PathVariable String pageEntryTitle, @PageableDefault(size = BLOG_PAGE_SIZE) Pageable pageable) {
        Module module = moduleDAO.findByUrlTitle(moduleTitle);
        if (module == null) {
            LOG.error("Module does not exist: " + moduleTitle);
            throw new ResourceNotFoundException();
        }
        if (!StringUtils.isEmpty(pageEntryTitle)) {
            ModelAndView blogView = new ModelAndView("blog/index");
            Page<PageEntry> page = pageEntryDAO.findByTitle(pageEntryTitle, pageable);
            blogView.addObject("PageEntries", page.getContent());
            blogView.addObject("Page", page);
            blogView.addObject("Module", module);
            return blogView;
        }
        throw new ResourceNotFoundException();
    }
}
