//package com.xiaobaitiao.springbootinit.config;
//
//import com.anji.captcha.properties.AjCaptchaProperties;
//import com.anji.captcha.service.CaptchaCacheService;
//import com.anji.captcha.service.impl.CaptchaServiceFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
////@Configuration
//public class CaptchaConfig {
//
//
//
////    @Bean(name = "CaptchaCacheService")
////    @Primary
//    public CaptchaCacheService captchaCacheService(AjCaptchaProperties config){
//        //缓存类型redis/local/....
//        CaptchaCacheService ret = CaptchaServiceFactory.getCache(config.getCacheType().name());
//        return ret;
//    }
//}