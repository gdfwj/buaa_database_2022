package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

import java.util.TimeZone;

@SpringBootApplication
@MapperScan("com.example.demo.mapper")
//@ComponentScan(basePackages = {"com.example.demo.mapper"})
public class DemoApplication {

    public static void main(String[] args) {
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
            System.setProperty("spring.devtools.restart.enabled", "false");
//        SpringApplication.run(DemoApplication.class, args);
            new SpringApplicationBuilder(DemoApplication.class)
                    .web(WebApplicationType.NONE).run(args);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }


}
