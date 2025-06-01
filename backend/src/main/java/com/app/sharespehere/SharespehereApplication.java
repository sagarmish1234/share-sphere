package com.app.sharespehere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SharespehereApplication {

	public static void main(String[] args) {
		SpringApplication.run(SharespehereApplication.class, args);
	}

}
