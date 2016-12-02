/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.admin.controller.files;

import com.drew.imaging.ImageProcessingException;
import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.db.dao.ImageDAOI;
import de.appsolve.padelcampus.db.model.Image;
import de.appsolve.padelcampus.utils.imaging.ImageUtilI;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jadira.usertype.spi.utils.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller
@RequestMapping("/admin/files")
public class AdminFilesController {
    
    private static final Logger LOG = Logger.getLogger(AdminFilesController.class);
    
    @Autowired
    Environment environment;
    
    @Autowired
    ImageDAOI imageDAO;
    
    @Autowired
    @Qualifier("TinifyImageUtil")
    ImageUtilI imageUtil;
    
    @RequestMapping
    public ModelAndView demoPage(){
        return new ModelAndView("admin/include/file-manager");
    }
    
    @RequestMapping(method = GET, value = "/list", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Collection<ApiFile> listFilesAndFolders(@RequestParam(required = false) String path) throws IOException{
        String folderPath = environment.getProperty(Constants.OPENSHIFT_DATA_DIR) + File.separator + Constants.DATA_DIR_SUMMERNOTE_IMAGES;
        File rootFolder = new File(folderPath);
        
        if (!StringUtils.isEmpty(path)){
            if (!folderPath.endsWith(File.separator)){
                folderPath += File.separator;
            }
            folderPath += path;
        }
        File folder = FileUtils.getFile(folderPath);
        
        if (!rootFolder.exists()){
            throw new IllegalArgumentException(String.format("Folder does not exist: %s", rootFolder.getCanonicalPath()));
        }
        
        if (!folder.getCanonicalPath().startsWith(rootFolder.getCanonicalPath())){
            LOG.warn(String.format("Attempt to access directory %s blocked", folder.getCanonicalPath()));
            throw new IllegalArgumentException("invalid directory");
        }
        
        if (!folder.exists()){
            throw new IllegalArgumentException(String.format("Folder does not exist: %s", folderPath));
        }
        
        List<Image> images = imageDAO.findAll();
        
        File[] filesAndDirs = folder.listFiles(new ImageFileFilter(images));
        
        List<ApiFile> apiFiles = new ArrayList<>();
        for (File file: filesAndDirs){
            ApiFile apiFile = new ApiFile();
            apiFile.setName(file.getName());
            if (file.isDirectory()){
                apiFile.setType("folder");
            } else {
                apiFile.setType("file");
                apiFile.setFileSize(FileUtils.sizeOf(file));
                apiFile.setUrl("/images/image/"+file.getName());
            }
            apiFiles.add(apiFile);
        }
        Collections.sort(apiFiles);
        return apiFiles;
    }
    
    @ResponseBody
    @RequestMapping(value="upload", method = POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String postImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException, ImageProcessingException {
        Image image = imageUtil.saveImage(file.getContentType(), file.getBytes(), Constants.DATA_DIR_SUMMERNOTE_IMAGES);
        return "/images/image/"+image.getSha256();
    }
}