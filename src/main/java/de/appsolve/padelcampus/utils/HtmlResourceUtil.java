/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.db.dao.CssAttributeDAOI;
import de.appsolve.padelcampus.db.model.CssAttribute;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletContext;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.lesscss.LessCompiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class HtmlResourceUtil {
    
    @Autowired
    CssAttributeDAOI cssAttributeDAO;
    
    private static final Logger log = Logger.getLogger(HtmlResourceUtil.class);
    
    private static final String ALL_MIN_CSS_APPLICATION_CONTEXT = "all.min.css";

    @Value("${OPENSHIFT_DATA_DIR}")
    private String DATA_DIR;

    private static final String VARIABLES_LESS  = "/less/variables.less";
    private static final String PROJECT_LESS    = "/less/90_project.less";
    private static final String PROJECT_CSS     = "/css/90_project.css";
    private static final String BOOTSTRAP_LESS  = "/less/10_bootstrap.less"; 
    private static final String BOOTSTRAP_CSS   = "/css/10_bootstrap.css";

    private static final String FOLDER_CSS      = "/css";
    private static final String FOLDER_LESS     = "/less";
    
    private static final String ALL_MIN_CSS = "/css/all.min.css";

    public void updateCss(ServletContext context) throws Exception {
        List<CssAttribute> cssAttributes = cssAttributeDAO.findAll();
        if (!cssAttributes.isEmpty()) {
            //PROBLEM: on openshift, the war file is not extracted. Thus, sortedFiles cannot be overwritten and must be read with context.getResource...
            
            //copy css sortedFiles to data directory
            copyResources(context, FOLDER_CSS, new File(DATA_DIR));
            //FileUtils.copyDirectory(new File(rootPath + FOLDER_CSS), new File(DATA_DIR + FOLDER_CSS));
            
            //remove all.min.css from data directory as we do not want to concatenate it with itself
            new File(DATA_DIR + ALL_MIN_CSS).delete();
            
            //copy less sortedFiles
            copyResources(context, FOLDER_LESS, new File(DATA_DIR));
            //FileUtils.copyDirectory(new File(rootPath + FOLDER_LESS), new File(DATA_DIR + FOLDER_LESS));
            
            //replace variables in variables.less, 90_project.less
            replaceVariables(context, cssAttributes, VARIABLES_LESS);
            replaceVariables(context, cssAttributes, PROJECT_LESS);
            

            //compile less and overwrite css sortedFiles in data directory
            LessCompiler lessCompiler = new LessCompiler();
            lessCompiler.compile(new File(DATA_DIR + File.separator + PROJECT_LESS), new File(DATA_DIR + File.separator + PROJECT_CSS));
            lessCompiler.compile(new File(DATA_DIR + File.separator + BOOTSTRAP_LESS), new File(DATA_DIR + File.separator + BOOTSTRAP_CSS));

            //concatenate all sortedFiles into all.min.css
            concatenateCss(Paths.get(DATA_DIR + FOLDER_CSS));
            
            //reload content for css controller
            reloadAllMinCss(context);
        }
    }

    private void concatenateCss(Path path) throws FileNotFoundException, IOException {
        DirectoryStream<Path> cssFiles = getFiles(path, ".css");
        Path outFile = getAllMinCssFile(DATA_DIR);
        if (Files.exists(outFile)){
            Files.delete(outFile);
        }
        List<Path> sortedFiles = new ArrayList<>();
        for (Path cssFile : cssFiles) {
            sortedFiles.add(cssFile);
        }
        
        Collections.sort(sortedFiles, new Comparator<Path>() {
            @Override
            public int compare(Path o1, Path o2) {
                return o1.getFileName().compareTo(o2.getFileName());
            }
        });
        
        for (Path cssFile : sortedFiles) {
            Files.write(outFile, Files.readAllBytes(cssFile), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
    }
    
    private DirectoryStream<Path> getFiles(Path path, final String fileExtension) throws IOException {

        DirectoryStream<Path> stream = Files.newDirectoryStream(path, new DirectoryStream.Filter<Path>() {
            @Override
            public boolean accept(Path entry) throws IOException {
                String name = entry.getFileName().toString();
                return !name.equals("all.min"+fileExtension) && name.endsWith(fileExtension);
            }
        });
        return stream;
    }

    private Path getAllMinCssFile(String dir) {
        return Paths.get(dir + ALL_MIN_CSS);
    }
    
    public String getAllMinCss(ServletContext context) throws IOException{
        String cssContent = (String) context.getAttribute(ALL_MIN_CSS_APPLICATION_CONTEXT);
        if (cssContent == null){
            cssContent = reloadAllMinCss(context);
        }
        return cssContent;
    }

    private String reloadAllMinCss(ServletContext context) throws IOException {
        Path cssFile = getAllMinCssFile(DATA_DIR);
        byte[] cssData;
        if (Files.exists(cssFile)){
            cssData = Files.readAllBytes(cssFile);
        } else {
            //read from classpath
            InputStream is = context.getResourceAsStream(ALL_MIN_CSS);
            cssData = IOUtils.toByteArray(is);
        }
        String css = new String(cssData, Constants.UTF8);
        context.setAttribute(ALL_MIN_CSS_APPLICATION_CONTEXT, css);
        return css;
    }

    private void copyResources(ServletContext context, String sourceFolder, File destinationFolder) throws MalformedURLException, IOException {
        Set<String> resourcePaths = context.getResourcePaths(sourceFolder);
        for (String resourcePath: resourcePaths){
            if (resourcePath.endsWith("/")){ //must be a directory
                copyResources(context, resourcePath, destinationFolder);
            } else {
                URL resource = context.getResource(resourcePath);
                FileUtils.copyURLToFile(resource, new File(destinationFolder, resourcePath));
            }
        }
    }

    private void replaceVariables(ServletContext context, List<CssAttribute> cssAttributes, String FILE_NAME) throws IOException {
        InputStream lessIs = context.getResourceAsStream(FILE_NAME);
        Path outputPath = Paths.get(DATA_DIR + FILE_NAME);
        String content = IOUtils.toString(lessIs, Constants.UTF8);
        for (CssAttribute attribute : cssAttributes) {
            content = content.replaceAll(attribute.getCssDefaultValue(), attribute.getCssValue());
        }

        //overwrite variables.less in data directory
        if (!Files.exists(outputPath)){
            if (!Files.exists(outputPath.getParent())){
                Files.createDirectory(outputPath.getParent());
            }
            Files.createFile(outputPath);
        }
        Files.write(outputPath, content.getBytes(Constants.UTF8), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }
}
