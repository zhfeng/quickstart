package org.jboss.narayana.quickstarts.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootJtaApplication {
	private static final Logger log = LoggerFactory.getLogger(SpringbootJtaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringbootJtaApplication.class, args);
	}

}
