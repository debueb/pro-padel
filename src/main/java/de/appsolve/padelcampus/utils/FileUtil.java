/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import com.drew.imaging.ImageProcessingException;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import de.appsolve.padelcampus.constants.ImageCategory;
import de.appsolve.padelcampus.db.dao.ImageDAOI;
import de.appsolve.padelcampus.db.model.Image;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * @author dominik
 */
@Component
public class FileUtil {

    @Autowired
    ImageDAOI imageDAO;

    public static String getFileContents(String fileName) throws IOException {
        URL url = Resources.getResource(fileName);
        return Resources.toString(url, Charsets.UTF_8);
    }

    public Image save(String contentType, byte[] byteArray, ImageCategory category, Integer width, Integer height) throws IOException, ImageProcessingException {
        //generate file path based on checksum of byte array
        String checksum = DigestUtils.sha256Hex(byteArray);

        Image existingImage = imageDAO.findBySha256(checksum);
        if (existingImage != null) {
            return existingImage;
        }

        //create Image
        Image image = new Image();
        image.setSha256(checksum);
        image.setWidth(width);
        image.setHeight(height);
        image.setContentType(contentType);
        image.setContentLength(0L + byteArray.length);
        image.setContent(byteArray);
        image.setCategory(category);
        imageDAO.saveOrUpdate(image);
        return image;
    }

    public Image save(String contentType, BufferedImage bufferedImage, ImageCategory category) throws IOException, ImageProcessingException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            //convert buffered image to byte array
            ImageIO.write(bufferedImage, "png", baos);
            baos.flush();
            byte[] byteArray = baos.toByteArray();

            return save(contentType, byteArray, category, bufferedImage.getWidth(), bufferedImage.getHeight());
        }
    }

}
