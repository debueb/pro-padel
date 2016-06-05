/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils.imaging;

import com.drew.imaging.ImageProcessingException;
import com.tinify.Options;
import com.tinify.Source;
import com.tinify.Tinify;
import de.appsolve.padelcampus.db.model.Image;
import de.appsolve.padelcampus.utils.FileUtil;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
@Qualifier("TinifyImageUtil")
public class TinifyImageUtil implements ImageUtilI{
    
    @Autowired
    FileUtil fileUtil;

    public TinifyImageUtil(){
        //dominik@appsolve.de --> https://tinypng.com/session/s/cunar7hQYCy7/YbSawnmy3tY6ew
        //if we exceed the 500 images per month limit: create multiple accounts
        //and change key when server returns AccountException
        Tinify.setKey("Iq3g1wY5w0-Lq9Fyp1nZNMYr4T-neSvv");
    }
    
    @Override
    public Image saveImage(byte[] bytes, String folderName) throws IOException, ImageProcessingException {
        byte[] resizedBytes = Tinify.fromBuffer(bytes).toBuffer();
        return fileUtil.save(resizedBytes, folderName);
    }

    @Override
    public Image saveImage(byte[] bytes, int maxWidth, int maxHeight, String folderName) throws IOException, ImageProcessingException {
        Source source = Tinify.fromBuffer(bytes);
        Options options = new Options()
            .with("method", "fit")
            .with("width", maxWidth)
            .with("height", maxHeight)
                ;
        Source resized = source.resize(options);
        byte[] resizedBytes = resized.toBuffer();
        return fileUtil.save(resizedBytes, folderName);
    }
    
    @Override
    public Image saveImage(byte[] bytes, int maxHeight, String folderName) throws IOException, ImageProcessingException {
        Source source = Tinify.fromBuffer(bytes);
        Options options = new Options()
            .with("method", "scale")
            .with("height", maxHeight)
                ;
        Source resized = source.resize(options);
        byte[] resizedBytes = resized.toBuffer();
        return fileUtil.save(resizedBytes, folderName);
    }
}
