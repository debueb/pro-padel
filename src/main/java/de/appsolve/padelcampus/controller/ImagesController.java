/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller;

import com.drew.imaging.ImageProcessingException;
import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.db.dao.ImageDAOI;
import de.appsolve.padelcampus.db.model.Image;
import de.appsolve.padelcampus.utils.FileUtil;
import de.appsolve.padelcampus.utils.imaging.ImageUtilI;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/images")
public class ImagesController extends BaseController{
    
    private static final Logger log = Logger.getLogger(ImagesController.class);
    
    @Autowired
    FileUtil fileUtil;
    
    @Autowired
    @Qualifier("TinifyImageUtil")
    ImageUtilI imageUtil;

    @Autowired
    ImageDAOI imageDAO;
    
    @ResponseBody
    @RequestMapping(value="image/{sha256}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] showImage(@PathVariable("sha256") String sha256, HttpServletResponse response) throws IOException{
        Image image = imageDAO.findBySha256(sha256);
        if (image!=null){
            return fileUtil.getByteArray(image.getFilePath());
        }
        return null;
    }
    
    @ResponseBody
    @RequestMapping(value="upload", method = POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String postImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException, ImageProcessingException {
        Image image = imageUtil.saveImage(file.getBytes(), Constants.DATA_DIR_SUMMERNOTE_IMAGES);
        return "/images/image/"+image.getSha256();
    }
}
