/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus;

import de.appsolve.padelcampus.filter.AdminFilter;
import de.appsolve.padelcampus.filter.LoginFilter;
import de.appsolve.padelcampus.filter.WhitespaceFilter;
import de.appsolve.padelcampus.filter.XSSFilter;
import de.appsolve.padelcampus.listener.SessionEventListener;
import de.appsolve.padelcampus.listener.TomcatUndeployListener;
import java.util.regex.Pattern;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

/**
 *
 * @author dominik
 */
public class WebAppInitializer implements WebApplicationInitializer{

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(AppConfig.class);
        
        // Manage the lifecycle of the root application context
        servletContext.addListener(new ContextLoaderListener(rootContext));
        
        // Register and map the dispatcher servlet
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
        
        servletContext.addListener(SessionEventListener.class);
        
        servletContext.addListener(TomcatUndeployListener.class);
    
        //expose request attributes via RequestContextHolder. Required for multi customer support 
        servletContext.addListener(RequestContextListener.class);
        
        XSSFilter xssFilter = new XSSFilter();
        xssFilter.setExcludePattern(Pattern.compile("/admin/general/modules/page.*"));
        servletContext.addFilter("XSSFilter", xssFilter).addMappingForUrlPatterns(null, false, "/*");
        
        servletContext.addFilter("loginFilter", new DelegatingFilterProxy("loginFilter")).addMappingForUrlPatterns(null, false, "/*");
        
        servletContext.addFilter("adminFilter", new DelegatingFilterProxy("adminFilter")).addMappingForUrlPatterns(null, false, "/admin/*");
        
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        servletContext.addFilter("characterEncoding", characterEncodingFilter).addMappingForUrlPatterns(null, true, "/*");
        
        servletContext.addFilter("whitespaceFilter", WhitespaceFilter.class).addMappingForUrlPatterns(null, true, "/*");        
    }  
}
