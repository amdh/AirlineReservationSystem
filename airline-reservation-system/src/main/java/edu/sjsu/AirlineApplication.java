package edu.sjsu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//@ComponentScan(basePackages ={ "edu.sjsu.dao","edu.sjsu.controller", "edu.sjsu.model" , "edu.sjsu.compe275.lab2"})
@SpringBootApplication
public class AirlineApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirlineApplication.class, args);
	}
}
