package fr.lewon.bot.manager.menus.actions;

import fr.lewon.bot.manager.modele.RunningBotsManager;
import fr.lewon.client.exceptions.ActionException;
import fr.lewon.client.menus.AbstractMenu;
import fr.lewon.client.menus.Action;

public class TrimStoppedBotsAction extends Action {

	public TrimStoppedBotsAction(AbstractMenu containingMenu) {
		super(containingMenu);
	}

	@Override
	protected AbstractMenu processAction(AbstractMenu caller) throws ActionException {
		RunningBotsManager.INSTANCE.trimStoppedBots();
		return caller;
	}

	@Override
	public String getLabel() {
		return "Trim stopped bots";
	}

}
