package edu.wisc.cae.springbug;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomWebMvcConfigurer implements WebMvcConfigurer {

    // Need to make it so our demo directory
    // uses the index.html by default
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/demo").setViewName("redirect:/demo/");
        registry.addViewController("/demo/").setViewName("forward:/demo/index.html");
    }
}
