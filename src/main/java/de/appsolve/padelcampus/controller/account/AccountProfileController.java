/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller.account;

import com.drew.imaging.ImageProcessingException;
import de.appsolve.padelcampus.constants.ImageCategory;
import de.appsolve.padelcampus.constants.SkillLevel;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.model.Image;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.spring.DaySchedulePropertyEditor;
import de.appsolve.padelcampus.utils.SessionUtil;
import de.appsolve.padelcampus.utils.imaging.ImageUtilI;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Set;

import static de.appsolve.padelcampus.constants.Constants.PROFILE_PICTURE_HEIGHT;
import static de.appsolve.padelcampus.constants.Constants.PROFILE_PICTURE_WIDTH;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/account/profile")
public class AccountProfileController extends BaseController {

    private static final Logger LOG = Logger.getLogger(AccountProfileController.class);

    @Autowired
    PlayerDAOI playerDAO;

    @Autowired
    SessionUtil sessionUtil;

    @Autowired
    @Qualifier("TinifyImageUtil")
    ImageUtilI imageUtil;

    @Autowired
    DaySchedulePropertyEditor daySchedulePropertyEditor;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Set.class, "daySchedules", daySchedulePropertyEditor);
    }

    @RequestMapping(method = GET)
    public ModelAndView getIndex(HttpServletRequest request) {
        Player user = sessionUtil.getUser(request);
        if (user == null) {
            return getLoginRequiredView(request, msg.get("Profile"));
        }
        user = playerDAO.findByUUIDWithDaySchedules(user.getUUID());
        return getIndexView(user);
    }

    /*
    TODO: use mixed-multipart
    http://stackoverflow.com/questions/21329426/spring-mvc-multipart-request-with-json
    
    e.g. 
    @RequestPart("Model") @Valid Player player,
    BindingResult bindingResult
    @RequestPart("file") MultipartFile file
    
    */
    @RequestMapping(method = POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ModelAndView postIndex(@ModelAttribute("Model") @Valid Player player, BindingResult bindingResult, HttpServletRequest request) {
        ModelAndView profileView = getIndexView(player);
        if (bindingResult.hasErrors()) {
            return profileView;
        }
        Player user = sessionUtil.getUser(request);
        if (user == null) {
            return new ModelAndView("include/loginrequired");
        }
        //make sure nobody changes another player's account
        if (user.getId() != null && user.getId().equals(player.getId())) {
            //make sure not to overwrite any existing data
            Player persistedPlayer = playerDAO.findByUUIDWithDaySchedules(user.getUUID());
            persistedPlayer.setFirstName(player.getFirstName());
            persistedPlayer.setLastName(player.getLastName());
            persistedPlayer.setEmail(player.getEmail());
            persistedPlayer.setPhone(player.getPhone());
            persistedPlayer.setGender(player.getGender());
            persistedPlayer.setEnableMatchNotifications(player.getEnableMatchNotifications());
            persistedPlayer.setNotificationSkillLevels(player.getNotificationSkillLevels());
            persistedPlayer.setAllowEmailContact(player.getAllowEmailContact());
            if (persistedPlayer.getDaySchedules() != null) {
                persistedPlayer.getDaySchedules().clear();
                persistedPlayer.getDaySchedules().addAll(player.getDaySchedules());
            } else {
                persistedPlayer.setDaySchedules(player.getDaySchedules());
            }
            playerDAO.saveOrUpdate(persistedPlayer);

            //resize Image
            try {
                MultipartFile pictureMultipartFile = player.getProfileImageMultipartFile();
                if (!pictureMultipartFile.isEmpty()) {
                    Image image = imageUtil.saveImage(pictureMultipartFile.getContentType(), pictureMultipartFile.getBytes(), PROFILE_PICTURE_WIDTH, PROFILE_PICTURE_HEIGHT, ImageCategory.profilePicture);
                    persistedPlayer.setProfileImage(image);
                    persistedPlayer = playerDAO.saveOrUpdateWithMerge(persistedPlayer);
                }
            } catch (IOException | ImageProcessingException e) {
                LOG.warn("Error while resizing image: " + e.getMessage());
                bindingResult.addError(new ObjectError("pictureMultipartFile", msg.get("ErrorWhileResizingImage")));
                return profileView;
            }
            sessionUtil.setUser(request, persistedPlayer);
        }
        String redirectPath = sessionUtil.getProfileRedirectPath(request);
        if (redirectPath == null) {
            redirectPath = "/home";
        } else {
            sessionUtil.setProfileRedirectPath(request, null);
        }
        return new ModelAndView("redirect:" + redirectPath);
    }

    private ModelAndView getIndexView(Player user) {
        ModelAndView mav = new ModelAndView("account/profile/index", "Model", user);
        mav.addObject("SkillLevels", SkillLevel.values());
        return mav;
    }
}
