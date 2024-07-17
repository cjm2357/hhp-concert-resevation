//package com.example.concert_reservation.config;
//
//import com.example.concert_reservation.interceptor.TokenInterceptor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//
//@Configuration
//public class WebMvcConfig implements WebMvcConfigurer {
//
//    private final TokenInterceptor tokenInterceptor;
//
//    public WebMvcConfig (TokenInterceptor tokenInterceptor) {
//        this.tokenInterceptor = tokenInterceptor;
//    }
//
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
////        registry.addInterceptor(tokenInterceptor);
//
//        // 가로채는 경로 설정 가능
//        registry.addInterceptor(tokenInterceptor)
//                .addPathPatterns("/api/concerts/**")
//                .addPathPatterns("/api/schedules/**")
//                .addPathPatterns("/api/seats/**")
//                .addPathPatterns("/api/payments/**");
////                .excludePathPatterns("/swagger-ui/**"); // /sample 경로에 대해서는 Interceptor 가로채지 않을것이다.
//    }
//
//}
