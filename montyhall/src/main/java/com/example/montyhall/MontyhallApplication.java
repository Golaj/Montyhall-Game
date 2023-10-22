package com.example.montyhall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MontyhallApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(MontyhallApplication.class, args);
		MontyhallGame montyhallGame = context.getBean(MontyhallGame.class);
		montyhallGame.startGame();
	}
}
