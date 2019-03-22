package fr.lewon.bot.manager.menus;

import java.util.ArrayList;
import java.util.List;

import fr.lewon.bot.manager.menus.actions.StartBotAction;
import fr.lewon.bot.manager.modele.BotInfos;
import fr.lewon.bot.manager.modele.RunningBotsManager;
import fr.lewon.client.menus.AbstractMenu;
import fr.lewon.client.menus.Menu;

public class HomeMenu extends Menu {

	public HomeMenu() {
		super(null, "");
	}

	@Override
	protected List<AbstractMenu> getMenuOptions() {
		List<AbstractMenu> options = new ArrayList<>();
		for (BotInfos bi : RunningBotsManager.INSTANCE.getBotInfosList()) {
			options.add(new BotRunnerMenu(this, bi));
		}
		options.add(new StartBotAction(this));
		return options;
	}

	@Override
	public String getLabel() {
		return "Bot manager";
	}

}
