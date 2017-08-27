/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller;

import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import de.appsolve.padelcampus.db.dao.ModuleDAOI;
import de.appsolve.padelcampus.db.model.Module;
import de.appsolve.padelcampus.utils.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;

/**
 * @author dominik
 */
@Controller
@RequestMapping(("/sitemap.xml"))
public class SitemapXmlGenerator {

    @Autowired
    Environment environment;

    @Autowired
    ModuleDAOI moduleDAO;

    @RequestMapping(produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String getRobotsTxt(HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, IOException {
        String baseUrl = RequestUtil.getBaseURL(request);
        WebSitemapGenerator generator = new WebSitemapGenerator(baseUrl);
        List<Module> rootModules = moduleDAO.findAllRootModules();
        addUrls(generator, rootModules, baseUrl);
        List<String> sitemaps = generator.writeAsStrings();
        if (!sitemaps.isEmpty()) {
            return sitemaps.get(0);
        }
        return null;
    }

    private void addUrls(WebSitemapGenerator generator, Collection<Module> modules, String baseUrl) throws MalformedURLException {
        for (Module module : modules) {
            if (module.getSubModules() != null && !module.getSubModules().isEmpty()) {
                addUrls(generator, module.getSubModules(), baseUrl);
            } else {
                WebSitemapUrl.Options options = new WebSitemapUrl.Options(baseUrl + module.getUrl());
                options.priority(0.5);
                if (module.getLastUpdated() != null) {
                    options.lastMod(module.getLastUpdated().toDate());
                }
                switch (module.getModuleType()) {
                    case HomePage:
                        options = new WebSitemapUrl.Options(baseUrl + "/home");
                        options = options.priority(1.0);
                        break;
                    case LandingPage:
                        options = new WebSitemapUrl.Options(baseUrl + "/");
                        break;
                    case Bookings:
                        options.priority(0.8);
                        options.changeFreq(ChangeFreq.DAILY);
                        break;
                    case Staff:
                        options.changeFreq(ChangeFreq.MONTHLY);
                        break;
                    case Ranking:
                        break;
                }
                generator.addUrl(options.build());
            }
        }
    }
}
