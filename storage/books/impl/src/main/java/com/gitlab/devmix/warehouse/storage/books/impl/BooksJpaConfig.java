//package com.gitlab.devmix.warehouse.storage.books.impl;
//
//import org.hibernate.jpa.HibernatePersistenceProvider;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//
///**
// * @author Sergey Grachev
// */
//@Configuration
//@EnableTransactionManagement
//public class BooksJpaConfig {
//
//    @Bean("entityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean getEntityManagerFactoryBean(DataSource dataSource) {
//        final LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
//        bean.setPersistenceUnitName("books-unit");
//        bean.setDataSource(dataSource);
//        bean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
//        return bean;
//    }
//
////    @Bean("jpaVendorAdapter")
////    public HibernateJpaVendorAdapter asd() {
////        HibernateJpaVendorAdapter bean = new HibernateJpaVendorAdapter();
////        bean.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
////    }
//}
