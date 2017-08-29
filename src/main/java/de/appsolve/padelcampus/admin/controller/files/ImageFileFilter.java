/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.admin.controller.files;

import de.appsolve.padelcampus.db.model.Image;

import java.io.File;
import java.io.FileFilter;
import java.util.Collection;

/**
 * @author dominik
 */
public class ImageFileFilter implements FileFilter {

    private final Collection<Image> images;

    public ImageFileFilter(Collection<Image> images) {
        this.images = images;
    }

    @Override
    public boolean accept(File file) {
        if (file.isHidden()) {
            return false;
        }
        if (file.isDirectory()) {
            return true;
        }
        for (Image image : images) {
            if (image.getSha256().equals(file.getName())) {
                return true;
            }
        }
        return false;
    }
}
