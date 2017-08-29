/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author dominik
 */
@Controller
public class CacheManifestController {

    private static final Logger log = Logger.getLogger(CacheManifestController.class);
    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping(value = "/cache.manifest", produces = "text/cache-manifest")
    @ResponseBody
    public String generateManifest() throws IOException {
        StringBuilder hash = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        sb.append("CACHE MANIFEST\n");
        sb.append("CACHE:\n");
        appendFolder(sb, hash, "css");
        appendFolder(sb, hash, "js");
        appendFolder(sb, hash, "images");
        appendFolder(sb, hash, "fonts");

        sb.append("\nNETWORK:\n");
        sb.append("cache.manifest");

        sb.append("\nFALLBACK:\n");
        appendFolder(sb, hash, "jsp/offline", "/ /offline");
        sb.append("# MD5: ").append(DigestUtils.md5Hex(hash.toString()));

        return sb.toString();
    }

    private void appendFolder(StringBuilder sb, StringBuilder hash, String path) throws IOException {
        appendFolder(sb, hash, path, null);
    }

    private void appendFolder(StringBuilder sb, StringBuilder hash, String path, String pathMapping) throws IOException {
        Resource resourceFolder = applicationContext.getResource(path);
        File folder = resourceFolder.getFile();
        appendContentsOfFolder(sb, hash, folder, path, pathMapping);
    }

    private void appendContentsOfFolder(StringBuilder sb, StringBuilder hash, File folder, String path, String pathMapping) throws FileNotFoundException, IOException {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                appendContentsOfFolder(sb, hash, file, path + "/" + file.getName(), pathMapping);
            } else {
                hash.append(DigestUtils.md5Hex(new FileInputStream(file)));
                if (pathMapping == null) {
                    sb.append(path).append("/").append(file.getName()).append("\n");
                } else {
                    sb.append(pathMapping).append("\n");
                }
            }
        }
    }

}
