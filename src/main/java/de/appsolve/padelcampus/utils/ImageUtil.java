/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import de.appsolve.padelcampus.db.model.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class ImageUtil {
    
    @Autowired
    FileUtil fileUtil;
    
    public Image saveImage(byte[] bytes, int width, int height, String folderName) throws IOException, ImageProcessingException{
        BufferedImage originalImage = null;
        BufferedImage resizedImage = null;
                    
        try {
            originalImage = ImageIO.read(new ByteArrayInputStream(bytes));
            //https://github.com/thebuzzmedia/imgscalr/issues/82
            //when resizing with alpha the resulting image gets a tint
            //also, on OpenJDK 7 on openshift does not support the alpha channel
            if (originalImage.getColorModel().hasAlpha()){
                originalImage = dropAlphaChannel(originalImage);
            }
            resizedImage = Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, width, height, Scalr.OP_ANTIALIAS);
            //fix iOS always sending landscape image with EXIF orientation metadata
            Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(bytes.clone()));
            if (metadata.containsDirectoryOfType(ExifIFD0Directory.class)){
                ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
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

            Image image = fileUtil.save(resizedImage, folderName);
            return image;
        } finally {
            if (originalImage!=null){
                originalImage.flush();
            }
            if (resizedImage!=null){
                resizedImage.flush();
            }
        }
    }

    private BufferedImage dropAlphaChannel(BufferedImage originalImage) {
        BufferedImage convertedImg = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        convertedImg.getGraphics().drawImage(originalImage, 0, 0, null);
        return convertedImg;
    }
}
