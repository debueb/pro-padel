/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import static de.appsolve.padelcampus.constants.Constants.DATA_DIR_PROFILE_PICTURES;
import de.appsolve.padelcampus.db.dao.ImageDAOI;
import de.appsolve.padelcampus.db.model.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
  
    public Image save(BufferedImage bufferedImage) throws IOException, ImageProcessingException{
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            
            //convert buffered image to byte array
            ImageIO.write(bufferedImage, "jpg", baos);
            baos.flush();
            byte[] byteArray = baos.toByteArray();
            
            //generate file path based on checksum of byte array
            String checksum = DigestUtils.sha256Hex(byteArray);
            String filePath = DATA_DIR + File.separator + DATA_DIR_PROFILE_PICTURES + File.separator + checksum;

            //save file to generated file name
            File file = new File(filePath);
            FileUtils.writeByteArrayToFile(file, byteArray);

            //create Image
            Image image = new Image();
            image.setFilePath(filePath);
            image.setSha256(checksum);
            imageDAO.saveOrUpdate(image);
            return image;
        }
    }
    
    public byte[] getByteArray(String filePath) throws IOException{
        File file = new File(filePath);
        return IOUtils.toByteArray(new FileInputStream(file));
    }
    
}
