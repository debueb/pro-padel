/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.db.model.CssAttribute;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.lesscss.LessCompiler;

/**
 *
 * @author dominik
 */
public class HtmlResourceUtil {
    
    private static final String VARIABLES_LESS  = "/less/variables.less";
    private static final String PROJECT_LESS    = "/less/90_project.less";
    private static final String PROJECT_CSS     = "/css/90_project.css";
    
    private static final String FOLDER_CSS      = "/css";
    private static final String ALL_MIN_CSS     = "/css/all.min.css";
    
    private static final String FOLDER_JS       = "/js";
    private static final String ALL_MIN_JS      = "/css/all.min.js";
    
    public static void updateCss(String rootPath, Set<CssAttribute> cssAttributes) throws Exception{
        if (!cssAttributes.isEmpty()){
            
            //PROBLEM: on openshift, the war file is not extracted. Thus, files cannot be overwritten
            
            
            //replace variables in variables.less
            Path path = Paths.get(rootPath  + VARIABLES_LESS);
            String content = IOUtils.toString(Files.newInputStream(path), Constants.UTF8);
            
            for (CssAttribute attribute: cssAttributes){
                content = content.replaceAll(attribute.getCssDefaultValue(), attribute.getCssValue());
            }
            Files.write(path, content.getBytes(Constants.UTF8));
            
            //compile less
            LessCompiler lessCompiler = new LessCompiler();
            lessCompiler.compile(new File(rootPath + PROJECT_LESS), new File(rootPath + PROJECT_CSS));

            minifyAndConcatenateCss(rootPath);
        }
    }

    private static void minifyAndConcatenateCss(String rootPath) throws FileNotFoundException, IOException {
        File folder = new File(rootPath + FOLDER_CSS);
        Path outFile = Paths.get(rootPath + ALL_MIN_CSS);
        if (folder.isDirectory()){
            File[] files = folder.listFiles();
            for (File cssFile: files){
                if (cssFile.isFile()){
                    Path path = cssFile.toPath();
                    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                    Files.write(outFile, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                }
            }
        }
    }

    public static void minifyAndConcatenateJs(String rootPath) throws FileNotFoundException, IOException {
        File folder = new File(rootPath + FOLDER_JS);
        try (OutputStreamWriter writer = new FileWriter(rootPath + ALL_MIN_JS)) {
            if (folder.isDirectory()){
                File[] files = folder.listFiles();
                for (File file: files){
                    FileReader reader = new FileReader(file);
                    new JavaScriptCompressor(reader, null).compress(writer, 0, true, false, true, true);
                }
            }
            writer.flush();
        }
    }
}
