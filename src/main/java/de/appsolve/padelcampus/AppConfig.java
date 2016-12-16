/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.external.cloudflare.CloudFlareApiRequestInterceptor;
import de.appsolve.padelcampus.external.openshift.OpenshiftApiRequestInterceptor;
import de.appsolve.padelcampus.listener.ContextInitializationListener;
import de.appsolve.padelcampus.resolver.PutAwareCommonsMultipartResolver;
import de.appsolve.padelcampus.spring.SubDomainLocaleResolver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 *
 * @author dominik
 */
@Configuration
@PropertySource(value="classpath:settings.properties")
@EnableTransactionManagement
@EnableScheduling
@ComponentScan(basePackages = "de.appsolve")
@EnableWebMvc
public class AppConfig extends WebMvcConfigurerAdapter{
    
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
    public MessageSource validationMessageSource(){
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setCacheSeconds(env.getProperty("messagesReloadInterval", Integer.class, 0));
        source.setBasename("/WEB-INF/ValidationMessages");
        source.setDefaultEncoding("UTF-8");
        return source;
    }
    
    @Bean
    public LocaleResolver localeResolver() {
        SubDomainLocaleResolver resolver = new SubDomainLocaleResolver();
        resolver.setDefaultLocale(Constants.DEFAULT_LOCALE);
        return resolver;
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
    
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(5000);
        return factory;
    }
    
    @Bean
    public ClientHttpRequestFactory openshiftClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(3*60000);
        factory.setConnectTimeout(3*60000);
        return factory;
    }
    
    @Bean
    public RestTemplate cloudFlareApiRestTemplate(){
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        ArrayList<ClientHttpRequestInterceptor> arrayList = new ArrayList<>();
        arrayList.add(new CloudFlareApiRequestInterceptor(env.getProperty("CLOUDFLARE_API_EMAIL"), env.getProperty("CLOUDFLARE_API_KEY")));
        restTemplate.setInterceptors(arrayList);
        return restTemplate;
    }
    
    @Bean
    public RestTemplate openshiftApiRestTemplate(){
        RestTemplate restTemplate = new RestTemplate(openshiftClientHttpRequestFactory());
        ArrayList<ClientHttpRequestInterceptor> arrayList = new ArrayList<>();
        arrayList.add(new OpenshiftApiRequestInterceptor(env.getProperty("OPENSHIFT_USERNAME"), env.getProperty("OPENSHIFT_PASSWORD")));
        restTemplate.setInterceptors(arrayList);
        return restTemplate;
    }
    
    @Bean
    public ViewResolver getViewResolver(){
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/jsp/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
    
    @Bean
    public Validator validator(){
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(validationMessageSource());
        return validator;
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
        registry.addResourceHandler("/images/**").addResourceLocations("/images/");
        registry.addResourceHandler("/templates/**").addResourceLocations("/templates/");
        registry.addResourceHandler("/filemanager/**").addResourceLocations("/filemanager/");
    }
    
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
    
    @Override
    public Validator getValidator(){
        return validator();
    }
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        argumentResolvers.add(resolver);
    }
}
