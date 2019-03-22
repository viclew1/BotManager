package fr.lewon.bot.manager.menus.actions;

import fr.lewon.bot.errors.BotRunnerException;
import fr.lewon.bot.runner.BotRunner;
import fr.lewon.client.exceptions.ActionException;
import fr.lewon.client.menus.AbstractMenu;
import fr.lewon.client.menus.Action;

public class StopBotAction extends Action {

	private BotRunner runner;

	public StopBotAction(AbstractMenu containingMenu, BotRunner runner) {
		super(containingMenu);
		this.runner = runner;
	}

	@Override
	protected AbstractMenu processAction(AbstractMenu caller) throws ActionException {
		try {
			runner.stop();
		} catch (BotRunnerException e) {
			System.out.println("ERROR : " + e.getMessage());
		}
		return caller;
	}

	@Override
	public String getLabel() {
		return "Stop this bot";
	}

}
