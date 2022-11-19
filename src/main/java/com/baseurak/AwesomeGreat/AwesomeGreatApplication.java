package com.baseurak.AwesomeGreat;

import com.baseurak.AwesomeGreat.user.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AwesomeGreatApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(AwesomeGreatApplication.class, args);
		UserRepository memberRepository = context.getBean(UserRepository.class);
	}

}
