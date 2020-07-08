package com.avacado.stupidapps.joana;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@SpringBootApplication
public class JoanaApplication {

	public static void main(String[] args) {
		SpringApplication.run(JoanaApplication.class, args);
	}

}
