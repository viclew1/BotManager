package fr.lewon.bot.manager.util;

import com.leek.wars.bot.LWBotRunnerBuilder;

import fr.lewon.bot.AbstractBotRunnerBuilder;
import fr.lewon.smutstone.bot.SSBotRunnerBuilder;
import fr.lewon.web.bot.HHBotRunnerBuilder;

public enum BotRunnerBuilderFactory {

	HHBOT("Hentai Heroes", "hentaiheroes.jpg", new HHBotRunnerBuilder()),
	LWBOT("Leek Wars", "leekwars.png", new LWBotRunnerBuilder()),
	SMUTSTONE_BOT("Smutstone", "smutstone.jpg", new SSBotRunnerBuilder());

	private final AbstractBotRunnerBuilder<?> botRunnerBuilder;
	private final String gameName;
	private final String iconFileName;

	private BotRunnerBuilderFactory(String gameName, String iconFileName, AbstractBotRunnerBuilder<?> botRunnerBuilder) {
		this.botRunnerBuilder = botRunnerBuilder;
		this.gameName = gameName;
		this.iconFileName = iconFileName;
	}

	public AbstractBotRunnerBuilder<?> getBotRunnerBuilder() {
		return botRunnerBuilder;
	}
	
	public String getGameName() {
		return gameName;
	}

	public String getIconFileName() {
		return iconFileName;
	}

}
