package com.applicate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class NifiApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(NifiApplication.class, args);
		context.getBean(ApplicationContextProvider.class).setApplicationContext(context);
	}

}
