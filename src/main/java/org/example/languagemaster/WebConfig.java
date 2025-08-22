package org.example.languagemaster;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        "https://desired-fit-parakeet.ngrok-free.app",
                        "https://learn-english-with-us.netlify.app",
                        "https://learn-english.prime-core.uz",
                        "http://16.170.158.74:8081",
                        "http://localhost:3000",
                        "http://localhost:9000",
                        "http://localhost:50792")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true);
    }


//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**", "/webjars/**")
//                .addResourceLocations("classpath:/static/", "classpath:/META-INF/resources/webjars/");
//    }
}
