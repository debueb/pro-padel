/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller;

import de.appsolve.padelcampus.data.Manifest;
import de.appsolve.padelcampus.data.ManifestIcon;
import de.appsolve.padelcampus.db.model.CssAttribute;
import de.appsolve.padelcampus.db.model.Customer;
import de.appsolve.padelcampus.exceptions.ResourceNotFoundException;
import de.appsolve.padelcampus.utils.HtmlResourceUtil;
import de.appsolve.padelcampus.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author dominik
 */
@Controller
@RequestMapping("/manifest")
public class ManifestJsonController {

    @Autowired
    SessionUtil sessionUtil;

    @Autowired
    HtmlResourceUtil htmlResourceUtil;

    @RequestMapping(value = "/manifest.json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Manifest get(HttpServletRequest request) {
        Customer customer = (Customer) sessionUtil.getCustomer(request);
        if (customer == null) {
            throw new ResourceNotFoundException();
        }
        Manifest manifest = new Manifest();
        manifest.setName(customer.getName());
        manifest.setShort_name(customer.getName());
        manifest.setDisplay("standalone");
        manifest.setStart_url("/home");
        List<CssAttribute> cssAttributes = htmlResourceUtil.getCssAttributes();
        for (CssAttribute att : cssAttributes) {
            if (att.getName().equals("primaryColor")) {
                manifest.setTheme_color(att.getCssValue());
                manifest.setBackground_color(att.getCssValue());
            }
        }
        Set<ManifestIcon> icons = new HashSet<>();
        ManifestIcon icon = new ManifestIcon();
        icon.setSrc(customer.getTouchIconPath());
        if (customer.getTouchIcon() == null) {
            icon.setType("image/png");
            icon.setSizes("192x192");
        } else {
            icon.setType(customer.getTouchIcon().getContentType());
            icon.setSizes(customer.getTouchIcon().getWidth() + "x" + customer.getTouchIcon().getHeight());
        }
        icons.add(icon);
        manifest.setIcons(icons);
        return manifest;
    }
}
