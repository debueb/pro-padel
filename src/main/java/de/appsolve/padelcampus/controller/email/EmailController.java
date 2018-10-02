package de.appsolve.padelcampus.controller.email;

import de.appsolve.padelcampus.db.dao.EmailConfirmationDAOI;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.model.EmailConfirmation;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EmailController {

    @Autowired
    EmailConfirmationDAOI emailConfirmationDAO;

    @Autowired
    PlayerDAOI playerDAO;

    @RequestMapping("/email/confirm/{uuid}")
    public ModelAndView confirm(@PathVariable String uuid) {
        EmailConfirmation emailConfirmation = emailConfirmationDAO.findByAttribute("uuid", uuid);
        if (emailConfirmation == null) {
            throw new ResourceNotFoundException();
        }
        Player player = playerDAO.findByEmail(emailConfirmation.getEmail());
        if (player == null) {
            throw new ResourceNotFoundException();
        }
        player.setAllowEmailContact(Boolean.TRUE);
        player.setVerified(Boolean.TRUE);
        playerDAO.saveOrUpdate(player);
        return new ModelAndView("email/confirm/success");
    }

    @RequestMapping("/email/unsubscribe/{uuid}")
    public ModelAndView unsubscribe(@PathVariable String uuid) {
        EmailConfirmation emailConfirmation = emailConfirmationDAO.findByAttribute("uuid", uuid);
        if (emailConfirmation == null) {
            throw new ResourceNotFoundException();
        }
        Player player = playerDAO.findByEmail(emailConfirmation.getEmail());
        if (player == null) {
            throw new ResourceNotFoundException();
        }
        player.setAllowEmailContact(Boolean.FALSE);
        player.setVerified(Boolean.TRUE);
        playerDAO.saveOrUpdate(player);
        return new ModelAndView("email/confirm/unsubscribe");
    }
}
