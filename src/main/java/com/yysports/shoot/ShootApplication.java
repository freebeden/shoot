package com.yysports.shoot;

import com.yysports.shoot.util.SystemRequestFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@MapperScan("com.yysports.shoot.mapper")// mybatis扫描路径，针对的是接口Mapper类
public class ShootApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ShootApplication.class, args);
    }

    /**
     * 配置
     *
     * @param application
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ShootApplication.class);
    }

    /**
     * 文件上传配置
     *
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 文件最大KB,MB
        factory.setMaxFileSize("200MB");
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("200MB");
        return factory.createMultipartConfig();
    }

    /**
     * 配置过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(systemRequestFilter());
        registration.addUrlPatterns("*");
        // 排除一些不必要的url
        registration.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico");
        registration.setName("systemRquestFilter");
        return registration;
    }

    /**
     * 创建一个bean
     *
     * @return
     */
    @Bean(name = "systemRquestFilter")
    public Filter systemRequestFilter() {
        return new SystemRequestFilter();
    }


}
