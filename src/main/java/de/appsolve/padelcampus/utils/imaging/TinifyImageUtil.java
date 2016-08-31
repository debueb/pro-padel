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
import de.appsolve.padelcampus.constants.Constants;
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
    public Image saveImage(String contentType, byte[] bytes, String folderName) throws IOException, ImageProcessingException {
        return saveImage(contentType, bytes, null, null, folderName);
    }

    @Override
    public Image saveImage(String contentType, byte[] bytes, Integer maxHeight, String folderName) throws IOException, ImageProcessingException {
        return saveImage(contentType, bytes, null, maxHeight, folderName);
    }

    @Override
    public Image saveImage(String contentType, byte[] bytes, Integer maxWidth, Integer maxHeight, String folderName) throws IOException, ImageProcessingException {
        
        try {
            if (bytes == null || bytes.length == 0 || StringUtils.isEmpty(contentType)){
                throw new IOException(msg.get("FileIsNotAValidImage"));
            }
            switch (contentType){
                case "image/svg+xml":
                    return fileUtil.save(contentType, bytes, Constants.DATA_DIR_SUMMERNOTE_IMAGES, null, null);
                case "image/jpeg":
                case "image/jpg":
                case "image/png":
                case "image/pjpeg"://M$
                case "x-png"://M$
                    byte[] resizedBytes;
                    BufferedImage image;
                    //NO RESIZING
                    if (maxWidth == null && maxHeight == null){
                        image = readImage(bytes);
                        resizedBytes = Tinify.fromBuffer(bytes).toBuffer();
                    } else {
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
                        resizedBytes = resized.toBuffer();
                        image = readImage(resizedBytes);
                    }
                    return fileUtil.save(contentType, resizedBytes, folderName, image.getWidth(), image.getHeight());
                default:
                    throw new IOException(msg.get("FileIsNotAValidImage"));
            }
        } catch (AccountException e){
            if (recoverAccountException(e)){
                return saveImage(contentType, bytes, folderName);
            }
            throw e;
        }
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
