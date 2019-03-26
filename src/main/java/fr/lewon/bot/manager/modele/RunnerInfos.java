package fr.lewon.bot.manager.modele;

import fr.lewon.bot.runner.BotRunner;

public class RunnerInfos {

	private final BotRunner botRunner;
	private final String login;
	private final String gameName;

	public RunnerInfos(BotRunner botRunner, String login, String gameName) {
		this.botRunner = botRunner;
		this.login = login;
		this.gameName = gameName;
	}

	public String getGameName() {
		return gameName;
	}
	public BotRunner getBotRunner() {
		return botRunner;
	}
	public String getLogin() {
		return login;
	}



}
