package fr.lewon.bot.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import fr.lewon.client.exceptions.CliException;

@SpringBootApplication
public class BotManagerApp {

	public static void main(String[] args) throws CliException {
		SpringApplication.run(BotManagerApp.class, args);
	}

}
