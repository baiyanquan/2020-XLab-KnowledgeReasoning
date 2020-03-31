package com.example.sockshopmonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class SockshopMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SockshopMonitorApplication.class, args);
    }

}
