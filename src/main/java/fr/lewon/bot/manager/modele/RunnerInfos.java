package fr.lewon.bot.manager.modele;

import java.util.concurrent.atomic.AtomicLong;

import fr.lewon.bot.runner.BotRunner;

public class RunnerInfos {

	private static final AtomicLong ID_GENERATOR = new AtomicLong();

	private final Long id;
	private final BotRunner botRunner;
	private final String login;

	public RunnerInfos(BotRunner botRunner, String login) {
		this.botRunner = botRunner;
		this.login = login;
		this.id = ID_GENERATOR.incrementAndGet();
	}


	public Long getId() {
		return id;
	}
	public BotRunner getBotRunner() {
		return botRunner;
	}
	public String getLogin() {
		return login;
	}



}
