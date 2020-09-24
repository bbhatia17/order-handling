package com.orderhandling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * The application class for the com.orderhandling spring boot service.
 */

@SpringBootApplication
public class OrderHandlingApplication {
    /**
     * Standalone spring boot starter.
     *
     * @param args arguments for the spring boot app run.
     */
    public static void main(String... args) {

        SpringApplication.run(OrderHandlingApplication.class, args);
    }
}
