package com.luckydrive.zdbinfo.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import static org.hibernate.cfg.Environment.*;

//@Configuration
//@PropertySource("classpath:application.properties")
//@EnableTransactionManagement
//@ComponentScans(value = {
//        @ComponentScan("com.luckydrive.zdbinfo.model"),
//        @ComponentScan("com.luckydrive.zdbinfo.repository")
//})
public class IbaDbConfig {
//    
//    @Autowired
//    private Environment env;
//
//    @Bean
//    public LocalSessionFactoryBean getSessionFactory() {
//        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
//        Properties prop = new Properties();
//        
//        // Setting JDBC properties
//        prop.put(DRIVER, env.getProperty("bar.datasource.driver-class-name"));
//        prop.put(URL, env.getProperty("bar.datasource.url"));
//        prop.put(USER, env.getProperty("bar.datasource.username"));
//        prop.put(PASS, env.getProperty("bar.datasource.password"));
//        
//        // Setting hibernate properties
//        prop.put(DIALECT, env.getProperty("bar.datasource.dialect"));
//        //prop.put(SHOW_SQL, env.getProperty("hibernate.show_sql"));
//        //prop.put(HBM2DDL_AUTO, env.getProperty("hibernate.hbm2ddl.auto"));
//        
//        factoryBean.setHibernateProperties(prop);
//        factoryBean.setPackagesToScan("com.luckydrive.zdbinfo.model");
//        return factoryBean;
//    }
//    
//    @Bean
//    public HibernateTransactionManager getTransactionManager() {
//        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
//        transactionManager.setSessionFactory(getSessionFactory().getObject());
//        return transactionManager;
//    }
}
