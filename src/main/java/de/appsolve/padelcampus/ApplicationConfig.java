/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus;

import com.bugsnag.Bugsnag;
import com.bugsnag.Report;
import com.bugsnag.callbacks.Callback;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.data.CustomerI;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.external.cloudflare.CloudFlareApiRequestInterceptor;
import de.appsolve.padelcampus.filter.*;
import de.appsolve.padelcampus.listener.SessionEventListener;
import de.appsolve.padelcampus.spring.RedirectInterceptor;
import de.appsolve.padelcampus.spring.SubDomainLocaleResolver;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.Filter;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionListener;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author dominik
 */
@Configuration
@EnableTransactionManagement
@EnableScheduling
public class ApplicationConfig extends WebMvcConfigurerAdapter {

    @Autowired
    Environment env;

    @Autowired
    LoginFilter loginFilter;

    @Autowired
    AdminFilter adminFilter;

    @Autowired
    DataSource dataSource;


    @Bean
    public LocaleResolver localeResolver() {
        return new SubDomainLocaleResolver();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        return mapper;
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(5000);
        return factory;
    }

    @Bean
    public RestTemplate cloudFlareApiRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        ArrayList<ClientHttpRequestInterceptor> arrayList = new ArrayList<>();
        arrayList.add(new CloudFlareApiRequestInterceptor(env.getProperty("CLOUDFLARE_API_EMAIL"), env.getProperty("CLOUDFLARE_API_KEY")));
        restTemplate.setInterceptors(arrayList);
        return restTemplate;
    }

    @Bean
    public Bugsnag bugsnag() {
        Bugsnag bugsnag = new Bugsnag(env.getProperty("BUGSNAG_API_KEY"));
        bugsnag.addCallback(new Callback() {
            @Override
            public void beforeNotify(Report report) {
                ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
                if (attr != null && attr.getRequest() != null) {
                    HttpSession session = attr.getRequest().getSession();
                    Player user = (Player) session.getAttribute(Constants.SESSION_USER);
                    if (user != null) {
                        report.setUserName(user.toString());
                        report.setUserEmail(user.getEmail());
                        report.setUserId(user.getUUID());
                    }
                    CustomerI customer = (CustomerI) session.getAttribute(Constants.SESSION_CUSTOMER);
                    if (customer != null) {
                        report.setAppInfo("customer", customer.getName());
                    }
                }
            }
        });

        return bugsnag;
    }

    @Bean
    public FilterRegistrationBean xssFilterBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(xssFilter());
        registration.addUrlPatterns("/*");
        registration.setName("XSSFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean loginFilterBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(loginFilter);
        registration.addUrlPatterns("/*");
        registration.setName("loginFilter");
        registration.setOrder(2);
        return registration;
    }

    @Bean
    public FilterRegistrationBean adminFilterBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(adminFilter);
        registration.addUrlPatterns("/admin/*");
        registration.setName("adminFilter");
        registration.setOrder(3);
        return registration;
    }

    @Bean
    public FilterRegistrationBean whitespaceFilterBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new WhitespaceFilter());
        registration.addUrlPatterns("/*");
        registration.setName("whitespaceFilter");
        registration.setOrder(4);
        return registration;
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionListener> sessionEventListenerBean() {
        return new ServletListenerRegistrationBean<HttpSessionListener>(new SessionEventListener());
    }

    @Bean
    public ServletListenerRegistrationBean<ServletRequestListener> requestContextListenerBean() {
        return new ServletListenerRegistrationBean<>(new RequestContextListener());
    }

    @Bean
    public Flyway flyway() {
        Flyway flyway = new Flyway();
        flyway.setBaselineOnMigrate(true);
        flyway.setDataSource(dataSource);
        return flyway;
    }

    /*
        make sure css files are served with utf-8 encoding for Spring Boot deployment
        see web.xml for traditional war file deployment
     */
    @Bean
    public EmbeddedServletContainerCustomizer customizeServletContainer() {
        return container -> {
            MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
            mappings.add("css", "text/css;charset=UTF-8");
            container.setMimeMappings(mappings);
        };
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        Utf8CharacterEncodingFilter characterEncodingFilter = new Utf8CharacterEncodingFilter();
        registrationBean.setFilter(characterEncodingFilter);
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

    private Filter xssFilter() {
        XSSFilter xssFilter = new XSSFilter();
        xssFilter.setExcludePattern(Pattern.compile("/admin/general/modules/page.*"));
        return xssFilter;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        argumentResolvers.add(resolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RedirectInterceptor());
    }
}
