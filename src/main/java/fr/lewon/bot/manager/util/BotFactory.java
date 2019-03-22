package fr.lewon.bot.manager.util;

import fr.lewon.bot.AbstractBot;
import fr.lewon.web.bot.HHBot;

public enum BotFactory {

	HHBOT("Hentai Heroes", () -> new HHBot());

	private final BotCreator botCreator;
	private final String gameName;

	private BotFactory(String gameName, BotCreator botCreator) {
		this.botCreator = botCreator;
		this.gameName = gameName;
	}

	public AbstractBot getNewBot() {
		return botCreator.init();
	}

	public String getGameName() {
		return gameName;
	}

}
