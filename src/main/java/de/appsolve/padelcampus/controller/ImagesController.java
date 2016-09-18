/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller;

import com.drew.imaging.ImageProcessingException;
import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.db.dao.ImageBaseDAOI;
import de.appsolve.padelcampus.db.model.Image;
import de.appsolve.padelcampus.utils.FileUtil;
import de.appsolve.padelcampus.utils.imaging.ImageUtilI;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    
    private static final Logger LOG = Logger.getLogger(ImagesController.class);
    
    @Autowired
    FileUtil fileUtil;
    
    @Autowired
    @Qualifier("TinifyImageUtil")
    ImageUtilI imageUtil;

    @Autowired
    ImageBaseDAOI imageBaseDAO;
    
    @RequestMapping(value="image/{sha256}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> showImage(@PathVariable("sha256") String sha256, HttpServletResponse response) throws IOException{
        Image image = imageBaseDAO.findBySha256(sha256);
        if (image!=null){
            byte[] byteArray = fileUtil.getByteArray(image.getFilePath());
            ResponseEntity.BodyBuilder builder = ResponseEntity
                    .ok()
                    .cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
                    .contentLength(byteArray.length);
                    
            if (!StringUtils.isEmpty(image.getContentType())){
                builder = builder.header("Content-Type", image.getContentType());
            }
            return builder.body(byteArray);
        }
        LOG.warn(String.format("Unable to display image %s", sha256));
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    
    @ResponseBody
    @RequestMapping(value="upload", method = POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String postImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException, ImageProcessingException {
        Image image = imageUtil.saveImage(file.getContentType(), file.getBytes(), Constants.DATA_DIR_SUMMERNOTE_IMAGES);
        return "/images/image/"+image.getSha256();
    }
}
