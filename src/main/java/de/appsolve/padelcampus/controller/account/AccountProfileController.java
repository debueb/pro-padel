/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller.account;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import static de.appsolve.padelcampus.constants.Constants.PROFILE_PICTURE_WIDTH;
import de.appsolve.padelcampus.constants.SkillLevel;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.model.Image;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.utils.FileUtil;
import de.appsolve.padelcampus.utils.Msg;
import de.appsolve.padelcampus.utils.SessionUtil;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final Logger log = Logger.getLogger(AccountProfileController.class);

    @Autowired
    PlayerDAOI playerDAO;

    @Autowired
    SessionUtil sessionUtil;

    @Autowired
    Msg msg;

    @Autowired
    FileUtil fileUtil;

    @RequestMapping()
    public ModelAndView getIndex(HttpServletRequest request) {
        Player user = sessionUtil.getUser(request);
        if (user == null){
            return new ModelAndView("include/loginrequired", "title", msg.get("Profile"));
        }
        return getIndexView(user);
    }

    @RequestMapping(method = POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ModelAndView postIndex(@ModelAttribute("Model") Player player, BindingResult bindingResult, HttpServletRequest request) {
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
            persistedPlayer.setSkillLevel(player.getSkillLevel());

            //resize Image
            BufferedImage originalImage = null;
            BufferedImage resizedImage = null;
            try {
                MultipartFile pictureMultipartFile = player.getProfileImageMultipartFile();
                if (!pictureMultipartFile.isEmpty()) {
                    byte[] bytes = pictureMultipartFile.getBytes();

                    originalImage = ImageIO.read(new ByteArrayInputStream(bytes));
                    resizedImage = Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, PROFILE_PICTURE_WIDTH, PROFILE_PICTURE_WIDTH, Scalr.OP_ANTIALIAS);
                    
                    //fix iOS always sending landscape image with EXIF orientation metadata
                    Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(bytes.clone()));
                    if (metadata.containsDirectory(ExifIFD0Directory.class)){
                        ExifIFD0Directory directory = metadata.getDirectory(ExifIFD0Directory.class);
                        if (directory!=null && directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)){
                            Integer value = directory.getInteger(ExifIFD0Directory.TAG_ORIENTATION);
                            if (value!=null){
                                switch (value){
                                    case 6: //this is the value the iPhone sends for photos captures in portait mode
                                        resizedImage = Scalr.rotate(resizedImage, Scalr.Rotation.CW_90);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                    
                    Image image = fileUtil.save(resizedImage);
                    persistedPlayer.setProfileImage(image);
                }
            } catch (IOException | ImageProcessingException e) {
                log.warn("Error while resiging image: " + e.getMessage());
                bindingResult.addError(new ObjectError("pictureMultipartFile", msg.get("ErrorWhileResizingImage")));
                return profileView;
            } finally {
                if (originalImage!=null){
                    originalImage.flush();
                }
                if (resizedImage!=null){
                    resizedImage.flush();
                }
            }

            playerDAO.saveOrUpdate(persistedPlayer);
            sessionUtil.setUser(request, persistedPlayer);
        }
        return new ModelAndView("redirect:/");
    }

    private ModelAndView getIndexView(Player user) {
        ModelAndView mav = new ModelAndView("account/profile/index", "Model", user);
        mav.addObject("SkillLevels", SkillLevel.values());
        return mav;
    }
}
