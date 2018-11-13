/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.bookings;

import de.appsolve.padelcampus.constants.BookingMailVariables;
import de.appsolve.padelcampus.db.dao.BookingMailSettingsDAOI;
import de.appsolve.padelcampus.db.model.BookingMailSettings;
import de.appsolve.padelcampus.utils.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/bookings/mailsettings")
public class AdminBookingsMailSettingsController {

    @Autowired
    Msg msg;

    @Autowired
    BookingMailSettingsDAOI bookingMailSettingsDAO;

    @RequestMapping()
    public ModelAndView showSettings() {
        BookingMailSettings settings = bookingMailSettingsDAO.findFirst();
        if (settings == null) {
            settings = new BookingMailSettings();
            settings.setHtmlBodyTemplate(msg.get("BookingSuccessfulMailBodyHtml"));
        }
        return getSettingsView(settings);
    }

    @RequestMapping(method = POST)
    public ModelAndView saveSettings(@Valid @ModelAttribute("Model") BookingMailSettings config, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return getSettingsView(config);
        }
        String template = config.getHtmlBodyTemplate();
        for (BookingMailVariables variable : BookingMailVariables.values()) {
            if (!template.contains(variable.name())) {
                bindingResult.addError(new ObjectError("*", msg.get("BookingMailVariablesDesc")));
                return getSettingsView(config);
            }
        }

        bookingMailSettingsDAO.saveOrUpdate(config);
        return new ModelAndView("redirect:/admin/bookings/");

    }

    private ModelAndView getSettingsView(BookingMailSettings config) {
        ModelAndView configView = new ModelAndView("admin/bookings/mailsettings/index", "Model", config);
        configView.addObject("BookingMailVariables", BookingMailVariables.values());
        return configView;
    }
}
