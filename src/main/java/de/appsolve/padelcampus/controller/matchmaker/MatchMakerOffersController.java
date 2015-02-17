/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller.matchmaker;

import static de.appsolve.padelcampus.constants.Constants.BOOKING_DEFAULT_VALID_FROM_HOUR;
import de.appsolve.padelcampus.constants.SkillLevel;
import de.appsolve.padelcampus.controller.BaseEntityController;
import de.appsolve.padelcampus.db.dao.GenericDAOI;
import de.appsolve.padelcampus.db.dao.MatchOfferDAOI;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.model.MatchOffer;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import de.appsolve.padelcampus.utils.SessionUtil;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/matchmaker/offers")
public class MatchMakerOffersController extends BaseEntityController<MatchOffer> {

    private static final Logger log = Logger.getLogger(MatchMakerOffersController.class);
    
    @Autowired
    PlayerDAOI playerDAO;
    
    @Autowired
    SessionUtil sessionUtil;
    
    @Autowired
    MatchOfferDAOI matchOfferDAO;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));
    
        binder.registerCustomEditor(Set.class, "players", new CustomCollectionEditor(Set.class) {
            @Override
            protected Object convertElement(Object element) {
                Long id = Long.parseLong((String) element);
                return playerDAO.findById(id);
            }
        });
    }
    
    @RequestMapping()
    public ModelAndView getIndex(HttpServletRequest request){
        Player user = sessionUtil.getUser(request);
        List<MatchOffer> offers = matchOfferDAO.findBy(user);
        ModelAndView mav = new ModelAndView("matchmaker/offers/index", "Models", offers);
        return mav;
    }

    @RequestMapping(value = "edit")
    public ModelAndView getEdit(HttpServletRequest request) {
        Player user = sessionUtil.getUser(request);
        if (user == null){
            return new ModelAndView("include/loginrequired", "title", msg.get("NewOffer"));
        }
        MatchOffer matchOffer = new MatchOffer();
        matchOffer.setStartDate(new LocalDate().plusDays(1));
        matchOffer.setStartTimeHour(BOOKING_DEFAULT_VALID_FROM_HOUR);
        Set<Player> players = new HashSet<>();players.add(user);
        matchOffer.setPlayers(players);
        if (user.getSkillLevel()!=null){
            Set<SkillLevel> skillLevels = new HashSet<>();
            skillLevels.add(user.getSkillLevel());
            matchOffer.setSkillLevels(skillLevels);
        }
        return getEditView(matchOffer);
    }
    
    @RequestMapping(value = "edit/{id}")
    public ModelAndView getEdit(HttpServletRequest request, @PathVariable("id") Long id) {
        MatchOffer offer = matchOfferDAO.findById(id);
        return getEditView(offer);
    }
    
    @RequestMapping(value = {"edit", "edit/{id}"}, method=POST)
    public ModelAndView postEdit(HttpServletRequest request, @ModelAttribute("Model") MatchOffer model, BindingResult result) {
        Player user = sessionUtil.getUser(request);
        if (user == null){
            return new ModelAndView("include/loginrequired", "title", msg.get("NewOffer"));
        }
        //ToDo: make sure court is bookable
        if (result.hasErrors()){
            ModelAndView editView = getEditView(model);
            return editView;
        }
        model.setOwner(user);
        matchOfferDAO.saveOrUpdate(model);
        return new ModelAndView("redirect:/matchmaker");
    }
    
    @RequestMapping(value = "offer/{id}")
    public ModelAndView getShow(HttpServletRequest request, @PathVariable("id") Long id) {
        MatchOffer offer = matchOfferDAO.findById(id);
        return getShowView(offer);
    }
    
    private ModelAndView getEditView(MatchOffer model) {
        ModelAndView mav = new ModelAndView("matchmaker/offers/edit", "Model", model);
        mav.addObject("SkillLevels", SkillLevel.values());
        mav.addObject("Players", playerDAO.findAll());
        return mav;
    }
    
    private ModelAndView getShowView(MatchOffer model) {
        ModelAndView mav = new ModelAndView("matchmaker/offers/offer", "Model", model);
        return mav;
    }
    
    @Override
    public GenericDAOI getDAO() {
        return matchOfferDAO;
    }

    @Override
    public String getModuleName() {
        return "matchmaker";
    }
}
