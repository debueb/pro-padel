/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller;

import com.drew.imaging.ImageProcessingException;
import de.appsolve.padelcampus.constants.ImageCategory;
import de.appsolve.padelcampus.db.dao.ImageBaseDAOI;
import de.appsolve.padelcampus.db.model.Image;
import de.appsolve.padelcampus.utils.FileUtil;
import de.appsolve.padelcampus.utils.imaging.ImageUtilI;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/images")
public class ImagesController extends BaseController {

    private static final Logger LOG = Logger.getLogger(ImagesController.class);

    private static final int ONE_YEAR = 365 * 24 * 60 * 60;

    @Autowired
    FileUtil fileUtil;

    @Autowired
    @Qualifier("TinifyImageUtil")
    ImageUtilI imageUtil;

    @Autowired
    ImageBaseDAOI imageBaseDAO;

    /*
    Firefox on iOS sends "Accept: image/*;q=0.8" for <link rel="apple-touch-icon" />
    We need to list all possible Content-Types response headers explicitly
    Otherwise Spring throws HttpMediaTypeNotAcceptableException
    Unfortunately, Spring does not look at ResponseEntity Content-Type
     */
    @RequestMapping(
            value = "image/{sha256}",
            consumes = MediaType.ALL_VALUE,
            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, "image/svg+xml"})
    public ResponseEntity<byte[]> showImage(@PathVariable("sha256") String sha256) {
        Image image = imageBaseDAO.findBySha256(sha256);
        if (image != null && image.getContent() != null) {
            byte[] byteArray = image.getContent();
            ResponseEntity.BodyBuilder builder = ResponseEntity
                    .ok()
                    .header(HttpHeaders.CACHE_CONTROL, String.format("public,max-age=%s,immutable", ONE_YEAR))
                    .contentLength(byteArray.length)
                    .contentType(MediaType.IMAGE_PNG);

            if (!StringUtils.isEmpty(image.getContentType())) {
                try {
                    MediaType mediaType = MediaType.parseMediaType(image.getContentType());
                    builder.contentType(mediaType);
                } catch (InvalidMediaTypeException e) {
                    LOG.warn(e.getMessage(), e);
                }
            }
            return builder.body(byteArray);
        }
        LOG.warn(String.format("Unable to display image %s", sha256));
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @RequestMapping(value = "upload", method = POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String postImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException, ImageProcessingException {
        Image image = imageUtil.saveImage(file.getContentType(), file.getBytes(), ImageCategory.cmsImage);
        return "/images/image/" + image.getSha256();
    }
}
