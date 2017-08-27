/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils.imaging;

import com.drew.imaging.ImageProcessingException;
import de.appsolve.padelcampus.constants.ImageCategory;
import de.appsolve.padelcampus.db.model.Image;

import java.io.IOException;

/**
 * @author dominik
 */
public interface ImageUtilI {

    Image saveImage(String contentType, byte[] bytes, ImageCategory category) throws IOException, ImageProcessingException;

    Image saveImage(String contentType, byte[] bytes, Integer width, Integer height, ImageCategory category) throws IOException, ImageProcessingException;
}
