/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.appsolve.padelcampus.filter.ResponseCachingFilter;
import de.appsolve.padelcampus.listener.ContextInitializationListener;
import de.appsolve.padelcampus.resolver.PutAwareCommonsMultipartResolver;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.http.CacheControl;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

/**
 *
 * @author dominik
 */
@Configuration
@ComponentScan(useDefaultFilters = true, basePackages = "de.appsolve", excludeFilters={@Filter(org.springframework.stereotype.Controller.class)})
@PropertySource(value="classpath:settings.properties")
@EnableTransactionManagement
public class AppConfig {
    
    @Autowired
    Environment env;
    
    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setCacheSeconds(env.getProperty("messagesReloadInterval", Integer.class, 0));
        messageSource.setBasename("/WEB-INF/Messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
    
    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://"+env.getProperty("OPENSHIFT_MYSQL_DB_HOST")+":"+env.getProperty("OPENSHIFT_MYSQL_DB_PORT")+"/"+env.getProperty("OPENSHIFT_APP_NAME"));
        dataSource.setUsername(env.getProperty("OPENSHIFT_MYSQL_DB_USERNAME"));
        dataSource.setPassword(env.getProperty("OPENSHIFT_MYSQL_DB_PASSWORD"));
        return dataSource;
    }
    
    @Bean
    public JpaVendorAdapter jpaVendorAdapter(){
        return new HibernateJpaVendorAdapter();
    }
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPackagesToScan("de.appsolve.padelcampus.db.model");
        emf.setJpaVendorAdapter(jpaVendorAdapter());
        Map<String, Object> propertyMap = new HashMap<>();
        propertyMap.put("hibernate.hbm2ddl.auto", "update");
        propertyMap.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        emf.setJpaPropertyMap(propertyMap);
        return emf;
    }
    
    @Bean
    public JpaTransactionManager transactionManager(){
        JpaTransactionManager jtm = new JpaTransactionManager();
        jtm.setEntityManagerFactory(entityManagerFactory().getObject());
        return jtm;
    }
    
    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor(){
        return new PersistenceExceptionTranslationPostProcessor();
    }
   
    @Bean
    public ResponseCachingFilter responseCachingFilter(){
        ResponseCachingFilter responseCachingFilter = new ResponseCachingFilter();
        responseCachingFilter.setCacheSeconds(0);
        responseCachingFilter.setCacheControl(CacheControl.noStore());
        Integer duration = env.getProperty("httpResponseCacheDuration", Integer.class);
        responseCachingFilter.addCacheMapping(CacheControl.maxAge(duration, TimeUnit.SECONDS), "/**/*.js", "/**/*.css", "/**/*.png", "/**/*.jpg", "/**/*.gif", "/**/*.svg", "/**/*.ttf", "/**/*.woff");
        return responseCachingFilter;
    }
    
    @Bean
    public LocaleResolver localeResolver(){
        FixedLocaleResolver resolver = new FixedLocaleResolver();
        resolver.setDefaultLocale(Locale.GERMANY);
        return resolver;
    }
    
    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
    
    @Bean
    public MultipartResolver multipartResolver(){
        PutAwareCommonsMultipartResolver multipartResolver = new PutAwareCommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(20971520); //20MB
        return multipartResolver;
    }
    
    @Bean
    public ContextInitializationListener contextInitializationListener(){
        return new ContextInitializationListener();
    }
}
