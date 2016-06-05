/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import com.drew.imaging.ImageProcessingException;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import de.appsolve.padelcampus.db.dao.ImageDAOI;
import de.appsolve.padelcampus.db.model.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class FileUtil {
    
    @Value("${OPENSHIFT_DATA_DIR}")
    private String DATA_DIR;
    
    @Autowired
    ImageDAOI imageDAO;
  
    public Image save(byte[] byteArray, String folderName) throws IOException, ImageProcessingException{
        //generate file path based on checksum of byte array
        String checksum = DigestUtils.sha256Hex(byteArray);

        Image existingImage = imageDAO.findBySha256(checksum);
        String filePath = DATA_DIR + File.separator + folderName + File.separator + checksum;

        File file = new File(filePath);
        if (existingImage != null && file.exists()){
            return existingImage;
        }

        //save file to generated file name
        FileUtils.writeByteArrayToFile(file, byteArray);

        //create Image
        Image image = new Image();
        image.setFilePath(filePath);
        image.setSha256(checksum);
        imageDAO.saveOrUpdate(image);
        return image;
    }
    
    public Image save(BufferedImage bufferedImage, String folderName) throws IOException, ImageProcessingException{
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            
            //convert buffered image to byte array
            ImageIO.write(bufferedImage, "png", baos);
            baos.flush();
            byte[] byteArray = baos.toByteArray();
            
            return save(byteArray, folderName);
        }
    }
    
    public byte[] getByteArray(String filePath) throws IOException{
        File file = new File(filePath);
        return IOUtils.toByteArray(new FileInputStream(file));
    }
    
    public static String getFileContents(String fileName) throws IOException {
        URL url = Resources.getResource(fileName);
        return Resources.toString(url, Charsets.UTF_8);
    }
    
}
