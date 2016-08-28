/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils.imaging;

import com.drew.imaging.ImageProcessingException;
import com.tinify.AccountException;
import com.tinify.Options;
import com.tinify.Source;
import com.tinify.Tinify;
import de.appsolve.padelcampus.db.model.Image;
import de.appsolve.padelcampus.utils.FileUtil;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
@Qualifier("TinifyImageUtil")
public class TinifyImageUtil extends AbstractImageUtil{
    
    private static final List<String> TINIFY_KEYS = Arrays.asList(new String[]{"Iq3g1wY5w0-Lq9Fyp1nZNMYr4T-neSvv", "lT9aSwA6Jr5ywk58oeDALdL34b4k9hc_"});
    
    @Autowired
    FileUtil fileUtil;

    public TinifyImageUtil(){
        //dominik@appsolve.de --> https://tinypng.com/session/s/cunar7hQYCy7/YbSawnmy3tY6ew --> Iq3g1wY5w0-Lq9Fyp1nZNMYr4T-neSvv
        //walls@appsolve.de --> https://tinypng.com/session/s/QeGQIc5lEM_M/wDq4J5st7WOns --> lT9aSwA6Jr5ywk58oeDALdL34b4k9hc_
        //if we exceed the 500 images per month limit: create multiple accounts
        //and change key when server returns AccountException
        Tinify.setKey(TINIFY_KEYS.get(0));
    }
    
    @Override
    public Image saveImage(byte[] bytes, String folderName) throws IOException, ImageProcessingException {
        try {
            BufferedImage image = readImage(bytes);
            byte[] resizedBytes = Tinify.fromBuffer(bytes).toBuffer();
            return fileUtil.save(resizedBytes, folderName, image.getWidth(), image.getHeight());
        } catch (AccountException e){
            if (recoverAccountException(e)){
                return saveImage(bytes, folderName);
            }
            throw e;
        }
    }

    @Override
    public Image saveImage(byte[] bytes, Integer maxWidth, Integer maxHeight, String folderName) throws IOException, ImageProcessingException {
        try {
            Source source = Tinify.fromBuffer(bytes);
            Options options = new Options()
                .with("method", "fit");
            if (maxWidth != null){
                options.with("width", maxWidth);
            }
            if (maxHeight != null){
                options.with("height", maxHeight);
            }
            Source resized = source.resize(options);
            byte[] resizedBytes = resized.toBuffer();
            BufferedImage image = readImage(resizedBytes);
            return fileUtil.save(resizedBytes, folderName, image.getWidth(), image.getHeight());
        } catch (AccountException e){
            if (recoverAccountException(e)){
                return saveImage(bytes, maxWidth, maxHeight, folderName);
            }
            throw e;
        }
    }
    
    @Override
    public Image saveImage(byte[] bytes, Integer maxHeight, String folderName) throws IOException, ImageProcessingException {
        return saveImage(bytes, null, maxHeight, folderName);
    }

    private boolean recoverAccountException(AccountException e) {
        if (!StringUtils.isEmpty(e.getMessage()) && e.getMessage().equalsIgnoreCase("Your monthly limit has been exceeded (HTTP 429/TooManyRequests)")){
            Integer keyPosition = TINIFY_KEYS.indexOf(Tinify.key());
            if (keyPosition.equals(TINIFY_KEYS.size()-1)){
                return false;
            }
            Tinify.setKey(TINIFY_KEYS.get(keyPosition+1));
            return true;
        }
        return false;
    }
}
