/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import com.yahoo.platform.yui.compressor.CssCompressor;
import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.db.model.CssAttribute;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import javax.servlet.ServletContext;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.lesscss.LessCompiler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class HtmlResourceUtil {
    
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

    public void updateCss(ServletContext context, List<CssAttribute> cssAttributes) throws Exception {
        if (!cssAttributes.isEmpty()) {
            
            String rootPath = context.getRealPath("/");
        
            //PROBLEM: on openshift, the war file is not extracted. Thus, files cannot be overwritten
            
            //copy css files to data directory
            FileUtils.copyDirectory(new File(rootPath + FOLDER_CSS), new File(DATA_DIR + FOLDER_CSS));
            
            //remove all.min.css from data directory as we do not want to concatenate it with itself
            new File(DATA_DIR + ALL_MIN_CSS).delete();
            
            //copy less files
            FileUtils.copyDirectory(new File(rootPath + FOLDER_LESS), new File(DATA_DIR + FOLDER_LESS));
            
            //replace variables in variables.less
            Path variablesLessInput = Paths.get(rootPath + VARIABLES_LESS);
            Path variablesLessOutput = Paths.get(DATA_DIR + VARIABLES_LESS);
            String content = IOUtils.toString(Files.newInputStream(variablesLessInput), Constants.UTF8);
            for (CssAttribute attribute : cssAttributes) {
                content = content.replaceAll(attribute.getCssDefaultValue(), attribute.getCssValue());
            }
            
            //overwrite variables.less in data directory
            if (!Files.exists(variablesLessOutput)){
                if (!Files.exists(variablesLessOutput.getParent())){
                    Files.createDirectory(variablesLessOutput.getParent());
                }
                Files.createFile(variablesLessOutput);
            }
            Files.write(variablesLessOutput, content.getBytes(Constants.UTF8), StandardOpenOption.CREATE, StandardOpenOption.WRITE);

            //compile less and overwrite css files in data directory
            LessCompiler lessCompiler = new LessCompiler();
            lessCompiler.compile(new File(DATA_DIR + File.separator + PROJECT_LESS), new File(DATA_DIR + File.separator + PROJECT_CSS));
            lessCompiler.compile(new File(DATA_DIR + File.separator + BOOTSTRAP_LESS), new File(DATA_DIR + File.separator + BOOTSTRAP_CSS));

            //concatenate all files into all.min.css
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
        for (Path cssFile : cssFiles) {
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
        if (cssFile == null){
            getAllMinCssFile(context.getRealPath("/"));
        }
        byte[] encoded = Files.readAllBytes(cssFile);
        String css = new String(encoded, Constants.UTF8);
        context.setAttribute(ALL_MIN_CSS_APPLICATION_CONTEXT, css);
        return css;
    }
}
