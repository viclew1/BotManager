package fr.lewon.bot.manager.util;

import com.leek.wars.bot.LWBot;

import fr.lewon.bot.AbstractBot;
import fr.lewon.smutstone.bot.SmutStoneBot;
import fr.lewon.web.bot.HHBot;

public enum BotFactory {

	HHBOT("Hentai Heroes", "hentaiheroes.jpg", () -> new HHBot()),
	LWBOT("Leek Wars", "leekwars.png", () -> new LWBot()),
	SMUTSTONE_BOT("Smutstone", "smutstone.jpg", () -> new SmutStoneBot());

	private final BotCreator botCreator;
	private final String gameName;
	private final String iconFileName;

	private BotFactory(String gameName, String iconFileName, BotCreator botCreator) {
		this.botCreator = botCreator;
		this.gameName = gameName;
		this.iconFileName = iconFileName;
	}

	public AbstractBot<?, ?> getNewBot() {
		return botCreator.init();
	}

	public String getGameName() {
		return gameName;
	}

	public String getIconFileName() {
		return iconFileName;
	}

}
