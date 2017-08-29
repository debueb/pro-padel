/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller;

import de.appsolve.padelcampus.utils.RequestUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dominik
 */
@Controller
@RequestMapping(("/robots.txt"))
public class RobotsTxtController {

    @RequestMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getRobotsTxt(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append("User-Agent: *\n");
        builder.append("Disallow: /players/\n");
        builder.append("Disallow: /teams/\n");
        builder.append("Disallow: /scores/\n");
        builder.append("Disallow: /games/\n");
        builder.append("Disallow: /matchoffers/\n");
        builder.append("Sitemap: ").append(RequestUtil.getBaseURL(request)).append("/sitemap.xml");
        return builder.toString();
    }
}
