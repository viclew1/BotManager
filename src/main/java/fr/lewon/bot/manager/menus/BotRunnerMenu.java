package fr.lewon.bot.manager.menus;

import java.util.ArrayList;
import java.util.List;

import fr.lewon.bot.manager.menus.actions.StopBotAction;
import fr.lewon.bot.manager.menus.actions.TogglePauseAction;
import fr.lewon.bot.manager.modele.BotInfos;
import fr.lewon.bot.runner.State;
import fr.lewon.client.menus.AbstractMenu;
import fr.lewon.client.menus.Menu;

public class BotRunnerMenu extends Menu {

	private BotInfos botInfos;

	public BotRunnerMenu(AbstractMenu containingMenu, BotInfos botInfos) {
		super(containingMenu, "");
		this.botInfos = botInfos;
	}

	@Override
	protected List<AbstractMenu> getMenuOptions() {
		List<AbstractMenu> options = new ArrayList<>();
		switch (botInfos.getBotRunner().getState()) {
		case RUNNING:
		case PAUSED:
			options.add(new StopBotAction(this, botInfos.getBotRunner()));
			options.add(new TogglePauseAction(this, botInfos.getBotRunner()));
		default:
			break;
		}
		return options;
	}

	@Override
	public String getLabel() {
		State state = botInfos.getBotRunner().getState();
		String gameName = botInfos.getGameName();
		String login = botInfos.getLogin();
		return state + " - " + gameName + " ( " + login + " )";
	}

}
