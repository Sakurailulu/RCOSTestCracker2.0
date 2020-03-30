package com.example.share.unit.pagehelper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class PageHelperConfig {
    @Bean
    public com.github.pagehelper.PageHelper pageHelper() {
        com.github.pagehelper.PageHelper pageHelper = new com.github.pagehelper.PageHelper();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("reasonable", "true");
        properties.setProperty("dialect", "mysql");    //set dialect of mysql dataset
        pageHelper.setProperties(properties);
        return pageHelper;
    }
}
