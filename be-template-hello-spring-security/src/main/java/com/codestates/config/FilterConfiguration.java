//package com.codestates.config;
//
//import com.codestates.filter.FirstFilter;
//import com.codestates.filter.SecondFilter;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class FilterConfiguration {
//
//    @Bean
//    public FilterRegistrationBean<FirstFilter> firstFilterRegister(){
//        FilterRegistrationBean<FirstFilter> registrationBean = new FilterRegistrationBean<>(new FirstFilter());
//        registrationBean.setOrder(1);
//        return registrationBean;
//    }
//
//    @Bean
//    public FilterRegistrationBean<SecondFilter> secondFilterRegister(){
//        FilterRegistrationBean<SecondFilter> registrationBean = new FilterRegistrationBean<>(new SecondFilter());
//        registrationBean.setOrder(2);
//        return registrationBean;
//    }
//
//}
