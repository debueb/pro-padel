/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils.imaging;

import de.appsolve.padelcampus.utils.Msg;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author dominik
 */
public abstract class AbstractImageUtil implements ImageUtilI {

    @Autowired
    Msg msg;

    protected BufferedImage readImage(byte[] bytes) throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
        if (image == null) {
            throw new IOException(msg.get("FileIsNotAValidImage"));
        }
        return image;
    }

}
