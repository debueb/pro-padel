/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author dominik
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "de.appsolve")
public class TestConfig {
    
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
//        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
//        emf.setDataSource(dataSource());
//        emf.setPackagesToScan("de.appsolve.padelcampus.db.model");
//        emf.setJpaVendorAdapter(jpaVendorAdapter());
//        Map<String, Object> propertyMap = new HashMap<>();
//        propertyMap.put("hibernate.hbm2ddl.auto", "create");
//        propertyMap.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
//        propertyMap.put("jadira.usertype.autoRegisterUserTypes", "true");
//        emf.setJpaPropertyMap(propertyMap);
//        return emf;
//    }
    
//    @Bean
//    public DataSource dataSource(){
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql://localhost:3306/padelcampustest?useSSL=false");
//        dataSource.setUsername("padelcampustest");
//        dataSource.setPassword("padelcampustest");
//        return dataSource;
//    }

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
        propertyMap.put("hibernate.hbm2ddl.auto", "create");
        propertyMap.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        propertyMap.put("jadira.usertype.autoRegisterUserTypes", "true");
        emf.setJpaPropertyMap(propertyMap);
        return emf;
    }
    
    @Bean
    public DataSource dataSource(){
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }
    
    @Bean
    public JpaTransactionManager transactionManager(){
        JpaTransactionManager jtm = new JpaTransactionManager();
        jtm.setEntityManagerFactory(entityManagerFactory().getObject());
        return jtm;
    }
    
    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
