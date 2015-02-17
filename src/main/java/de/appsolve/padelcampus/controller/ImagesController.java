/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller;

import de.appsolve.padelcampus.db.dao.ImageDAOI;
import de.appsolve.padelcampus.db.model.Image;
import de.appsolve.padelcampus.utils.FileUtil;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    ImageDAOI imageDAO;
    
    @ResponseBody
    @RequestMapping(value="image/{sha256}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] showImage(@PathVariable("sha256") String sha256, HttpServletResponse response) throws IOException{
        Image image = imageDAO.findBySha256(sha256);
        if (image!=null){
            return fileUtil.getByteArray(image.getFilePath());
        }
        //TODO: return default image
        return null;
    }
}
