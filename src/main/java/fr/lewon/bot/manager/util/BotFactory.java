package fr.lewon.bot.manager.util;

import com.leek.wars.bot.LWBot;

import fr.lewon.bot.AbstractBot;
import fr.lewon.web.bot.HHBot;

public enum BotFactory {

	HHBOT("Hentai Heroes", () -> new HHBot()),
	LWBOT("Leek Wars", () -> new LWBot());

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

	public static BotFactory fromGameName(String gameName) {
		for (BotFactory bf : values()) {
			if (bf.getGameName().equals(gameName)) {
				return bf;
			}
		}
		return null;
	}

}
