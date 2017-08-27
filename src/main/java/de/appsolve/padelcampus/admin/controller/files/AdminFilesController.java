/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.admin.controller.files;

import com.drew.imaging.ImageProcessingException;
import de.appsolve.padelcampus.constants.ImageCategory;
import de.appsolve.padelcampus.db.dao.ImageDAOI;
import de.appsolve.padelcampus.db.model.Image;
import de.appsolve.padelcampus.utils.imaging.ImageUtilI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author dominik
 */
@Controller
@RequestMapping("/admin/files")
public class AdminFilesController {

    @Autowired
    ImageDAOI imageDAO;

    @Autowired
    @Qualifier("TinifyImageUtil")
    ImageUtilI imageUtil;

    @RequestMapping
    public ModelAndView demoPage() {
        return new ModelAndView("admin/include/file-manager");
    }

    @RequestMapping(method = GET, value = "/list", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Collection<ApiFile> listFilesAndFolders(@RequestParam(required = false) String path) throws IOException {
        List<Image> images = imageDAO.findAllWithContent(ImageCategory.cmsImage);

        List<ApiFile> apiFiles = new ArrayList<>();
        for (Image image : images) {
            ApiFile apiFile = new ApiFile();
            apiFile.setName(image.getSha256());
            apiFile.setType("file");
            apiFile.setFileSize(image.getContentLength());
            apiFile.setUrl("/images/image/" + image.getSha256());
            apiFiles.add(apiFile);
        }
        Collections.sort(apiFiles);
        return apiFiles;
    }

    @ResponseBody
    @RequestMapping(value = "upload", method = POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String postImage(@RequestParam("file") MultipartFile file) throws IOException, ImageProcessingException {
        Image image = imageUtil.saveImage(file.getContentType(), file.getBytes(), ImageCategory.cmsImage);
        return "/images/image/" + image.getSha256();
    }
}