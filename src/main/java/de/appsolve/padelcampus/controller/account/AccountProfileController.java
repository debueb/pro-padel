/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller.account;

import com.drew.imaging.ImageProcessingException;
import static de.appsolve.padelcampus.constants.Constants.DATA_DIR_PROFILE_PICTURES;
import static de.appsolve.padelcampus.constants.Constants.PROFILE_PICTURE_HEIGHT;
import static de.appsolve.padelcampus.constants.Constants.PROFILE_PICTURE_WIDTH;
import de.appsolve.padelcampus.constants.SkillLevel;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.model.Image;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.utils.SessionUtil;
import de.appsolve.padelcampus.utils.imaging.ImageUtilI;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 *
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

    @RequestMapping()
    public ModelAndView getIndex(HttpServletRequest request) {
        Player user = sessionUtil.getUser(request);
        if (user == null){
            return getLoginRequiredView(request, msg.get("Profile"));
        }
        return getIndexView(user);
    }

    @RequestMapping(method = POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ModelAndView postIndex(@ModelAttribute("Model") @Valid Player player, BindingResult bindingResult, HttpServletRequest request) {
        ModelAndView profileView = getIndexView(player);
        if (bindingResult.hasErrors()) {
            return profileView;
        }
        //make sure nobody changes another player's account
        Player user = sessionUtil.getUser(request);
        if (user == null){
            return new ModelAndView("include/loginrequired");
        }
        if (user.getId() != null && user.getId().equals(player.getId())) {
            //make sure not to overwrite any existing data
            Player persistedPlayer = playerDAO.findById(player.getId());
            persistedPlayer.setFirstName(player.getFirstName());
            persistedPlayer.setLastName(player.getLastName());
            persistedPlayer.setEmail(player.getEmail());
            persistedPlayer.setPhone(player.getPhone());
            persistedPlayer.setGender(player.getGender());
            persistedPlayer.setSkillLevel(player.getSkillLevel());
            persistedPlayer.setEnableMatchNotifications(player.getEnableMatchNotifications());
            persistedPlayer.setNotificationSkillLevels(player.getNotificationSkillLevels());
            persistedPlayer.setAllowEmailContact(player.getAllowEmailContact());

            //resize Image
            try {
                MultipartFile pictureMultipartFile = player.getProfileImageMultipartFile();
                if (!pictureMultipartFile.isEmpty()) {
                    
                    //delete old picture if it exists
                    Image profileImage = persistedPlayer.getProfileImage();
                    if (profileImage!=null){
                        File profileFile = new File(profileImage.getFilePath());
                        if (profileFile.exists()){
                            boolean deleteSuccess = profileFile.delete();
                            if (!deleteSuccess){
                                LOG.warn("Unale to delete file "+profileFile.getAbsolutePath());
                            }
                        }
                    }
                    
                    Image image = imageUtil.saveImage(pictureMultipartFile.getContentType(), pictureMultipartFile.getBytes(), PROFILE_PICTURE_WIDTH, PROFILE_PICTURE_HEIGHT, DATA_DIR_PROFILE_PICTURES);
                    persistedPlayer.setProfileImage(image);
                }
            } catch (IOException | ImageProcessingException e) {
                LOG.warn("Error while resiging image: " + e.getMessage());
                bindingResult.addError(new ObjectError("pictureMultipartFile", msg.get("ErrorWhileResizingImage")));
                return profileView;
            }
            playerDAO.saveOrUpdate(persistedPlayer);
            sessionUtil.setUser(request, persistedPlayer);
        }
        String redirectPath = sessionUtil.getProfileRedirectPath(request);
        if (redirectPath==null){
            redirectPath = "/";
        } else {
            sessionUtil.setProfileRedirectPath(request, null);
        }
        return new ModelAndView("redirect:"+redirectPath);
    }

    private ModelAndView getIndexView(Player user) {
        ModelAndView mav = new ModelAndView("account/profile/index", "Model", user);
        mav.addObject("SkillLevels", SkillLevel.values());
        return mav;
    }
}
