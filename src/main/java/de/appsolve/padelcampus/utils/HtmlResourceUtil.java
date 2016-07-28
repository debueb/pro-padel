/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.data.CustomerI;
import de.appsolve.padelcampus.db.dao.CssAttributeBaseDAOI;
import de.appsolve.padelcampus.db.dao.CustomerDAOI;
import de.appsolve.padelcampus.db.model.CssAttribute;
import de.appsolve.padelcampus.db.model.Customer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import javax.servlet.ServletContext;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.lesscss.LessCompiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class HtmlResourceUtil {
    
    private static final Logger LOG = Logger.getLogger(HtmlResourceUtil.class);
    
    private LessCompiler lessCompiler;
    
    @Autowired
    CustomerDAOI customerDAO;
    
    @Autowired
    CssAttributeBaseDAOI cssAttributeBaseDAO;
    
    @Autowired
    CustomerUtil customerUtil;
    
    private static final String VARIABLES_LESS  = "/less/variables.less";
    private static final String PROJECT_LESS    = "/less/90_project.less";
    private static final String PROJECT_CSS     = "/css/90_project.css";
    private static final String BOOTSTRAP_LESS  = "/less/10_bootstrap.less"; 
    private static final String BOOTSTRAP_CSS   = "/css/10_bootstrap.css";

    private static final String FOLDER_CSS      = "/css";
    private static final String FOLDER_LESS     = "/less";
    
    private static final String ALL_MIN_CSS = "/css/all.min.css";

    public void updateCss(final ServletContext context) throws Exception {
        List<Customer> customers = customerDAO.findAll();
        if (customers.isEmpty()){
            applyCustomerCss(context, getDefaultCssAttributes(), "");
        } else {
            int availableProcessors = Runtime.getRuntime().availableProcessors();
            LOG.info(String.format("Compiling less with %s processors", availableProcessors));
            ExecutorService executor = Executors.newFixedThreadPool(availableProcessors);
            List<FutureTask<Void>> taskList = new ArrayList<>();

            for (final Customer customer: customers){
                FutureTask<Void> futureTask = new FutureTask<>(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        try {
                            updateCss(context, customer);
                        } catch (Exception ex) {
                            LOG.error(ex);
                        }
                        return null;
                    }
                });
                taskList.add(futureTask);
                executor.execute(futureTask);
            } 
            for (FutureTask task: taskList) {
                task.get();
            }
            executor.shutdown();
        }
    }
    
    public void updateCss(ServletContext context, CustomerI customer) throws Exception{
        Map<String, Object> attributeMap = new HashMap<>();
        attributeMap.put("customer", customer);
        List<CssAttribute> cssAttributes = cssAttributeBaseDAO.findByAttributes(attributeMap);
        if (cssAttributes.isEmpty()){
            cssAttributes = getDefaultCssAttributes();
        }
        applyCustomerCss(context, cssAttributes, customer.getName());
    }
    
    private void applyCustomerCss(ServletContext context, List<CssAttribute> cssAttributes, String customerName) throws Exception {
        if (!cssAttributes.isEmpty()) {
            //PROBLEM: on openshift, the war file is not extracted. Thus, sortedFiles cannot be overwritten and must be read with context.getResource...

            File destDir = customerUtil.getCustomerDir(customerName);

            //copy css sortedFiles to data directory
            copyResources(context, FOLDER_CSS, destDir);
            //FileUtils.copyDirectory(new File(rootPath + FOLDER_CSS), new File(DATA_DIR + FOLDER_CSS));

            //remove all.min.css from data directory as we do not want to concatenate it with itself
            new File(destDir, ALL_MIN_CSS).delete();

            //copy less sortedFiles
            copyResources(context, FOLDER_LESS, destDir);

            //replace variables in variables.less, 90_project.less
            replaceVariables(context, cssAttributes, VARIABLES_LESS, destDir);
            replaceVariables(context, cssAttributes, PROJECT_LESS, destDir);


            //compile less and overwrite css sortedFiles in data directory
            if (lessCompiler == null){
                lessCompiler = new LessCompiler();
            }
            lessCompiler.compile(new File(destDir, PROJECT_LESS), new File(destDir, PROJECT_CSS));
            lessCompiler.compile(new File(destDir, BOOTSTRAP_LESS), new File(destDir, BOOTSTRAP_CSS));

            Path allMinCssPath = new File(destDir, ALL_MIN_CSS).toPath();
            //concatenate all sortedFiles into all.min.css
            concatenateCss(new File(destDir, FOLDER_CSS).toPath(), allMinCssPath);

            //reload content for css controller
            context.setAttribute(customerName, null);
        }
    }

    private void concatenateCss(Path path, Path outFile) throws FileNotFoundException, IOException {
        DirectoryStream<Path> cssFiles = getFiles(path, ".css");
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

    public String getAllMinCss(ServletContext context, String customerName) throws IOException{
        String cssContent = (String) context.getAttribute(customerName);
        if (cssContent == null){
            cssContent = reloadAllMinCss(context, customerUtil.getCustomerDir(customerName));
            context.setAttribute(customerName, cssContent);
        }
        return cssContent;
    }

    private String reloadAllMinCss(ServletContext context, File customerDir) throws IOException {
        byte[] cssData;
        Path cssFile = new File(customerDir, ALL_MIN_CSS).toPath();
        if (Files.exists(cssFile)){
            cssData = Files.readAllBytes(cssFile);
        } else {
            //read from classpath
            InputStream is = context.getResourceAsStream(ALL_MIN_CSS);
            cssData = IOUtils.toByteArray(is);
        }
        String css = new String(cssData, Constants.UTF8);
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

    private void replaceVariables(ServletContext context, List<CssAttribute> cssAttributes, String FILE_NAME, File destDir) throws IOException {
        InputStream lessIs = context.getResourceAsStream(FILE_NAME);
        Path outputPath = new File(destDir, FILE_NAME).toPath();
        String content = IOUtils.toString(lessIs, Constants.UTF8);
        for (CssAttribute attribute : cssAttributes) {
            if (!StringUtils.isEmpty(attribute.getCssValue())){
                content = content.replaceAll(attribute.getCssDefaultValue(), attribute.getCssValue());
            }
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
    
    public String getCssFile(ServletContext context, String name, String customer) throws IOException{
        File customerDir = customerUtil.getCustomerDir(customer);
        File cssDir = new File(customerDir, "css");
        File file = new File(cssDir, name);
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            return new String(bytes, Constants.UTF8);
        } catch (IOException e){
            InputStream stream = context.getResourceAsStream("/css/"+name);
            if (stream == null){
                LOG.warn("Unable to load fallback /css/"+name);
                return null;
            }
            return IOUtils.toString(stream, StandardCharsets.UTF_8);
        }
        
    }

    public List<CssAttribute> getDefaultCssAttributes() {
        List<CssAttribute> atts = new ArrayList<>();
        atts.add(getCssAttribute("bgColor", "#94cfea", "#94cfea"));
        atts.add(getCssAttribute("primaryColor", "#613815", "#613815"));
        atts.add(getCssAttribute("primaryLinkColor", "#31708f", "#31708f"));
        atts.add(getCssAttribute("primaryLinkHoverColor", "#94cfeb", "#94cfeb"));
        atts.add(getCssAttribute("backgroundImage", "url\\('\\/images\\/bg\\.jpg'\\)", "url('/images/bg.jpg')"));
        return atts;
    }

    private CssAttribute getCssAttribute(String name, String cssDefaultValue, String cssValue) {
        CssAttribute att = new CssAttribute();
        att.setName(name);
        att.setCssDefaultValue(cssDefaultValue);
        att.setCssValue(cssValue);
        return att;
    }
}
