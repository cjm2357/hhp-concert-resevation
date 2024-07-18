package com.example.concert_reservation.config;

import com.example.concert_reservation.presentation.interceptor.TokenInterceptor;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final TokenInterceptor tokenInterceptor;

    public WebMvcConfig (TokenInterceptor tokenInterceptor) {
        this.tokenInterceptor = tokenInterceptor;
    }


    @Bean
    public FilterRegistrationBean restApiFilter() {
        // SpringBoot 에서는 FilterRegistrationBean을 이용해서 필터 설정(was 올릴 때 서블릿 컨테이너 올릴 때 알아서 등록을 해준다.)
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new RestApiFilter()); // 등록할 필터
        filterRegistrationBean.addUrlPatterns("/api/*"); // 필터 적용할 url 패턴

        return filterRegistrationBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 가로채는 경로 설정 가능
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/api/concerts/**")
                .addPathPatterns("/api/schedules/**")
                .addPathPatterns("/api/seats/**")
                .addPathPatterns("/api/payments/**");
//                .excludePathPatterns("/swagger-ui/**"); // /sample 경로에 대해서는 Interceptor 가로채지 않을것이다.
    }

}
