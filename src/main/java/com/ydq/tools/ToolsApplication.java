package com.ydq.tools;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@SpringBootApplication
public class ToolsApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ToolsApplication.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run();
    }

    @Bean
    public String keepAlive(){
        return new Scanner(System.in).nextLine();
    }

}
