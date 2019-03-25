package fr.lewon.bot.manager.menus.actions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fr.lewon.bot.errors.BotRunnerException;
import fr.lewon.bot.manager.modele.BotRunnersManager;
import fr.lewon.bot.manager.util.BotFactory;
import fr.lewon.bot.manager.util.errors.BotManagerException;
import fr.lewon.client.exceptions.ActionException;
import fr.lewon.client.menus.AbstractMenu;
import fr.lewon.client.menus.Action;
import fr.lewon.client.util.input.Choice;
import fr.lewon.client.util.input.UserInputUtil;

public class StartBotAction extends Action {


	public StartBotAction(AbstractMenu containingMenu) {
		super(containingMenu);
	}

	@Override
	protected AbstractMenu processAction(AbstractMenu caller) throws ActionException {
		List<Choice<BotFactory>> choices = Arrays.asList(BotFactory.values()).stream()
				.map(b -> new Choice<>(b.getGameName(), b))
				.collect(Collectors.toList());
		BotFactory bot = UserInputUtil.INSTANCE.askChoice("Game", false, choices);
		String login = UserInputUtil.INSTANCE.askString("Enter your login", false, true);
		String password = UserInputUtil.INSTANCE.askString("Enter your password", true, true);
		try {
			BotRunnersManager.INSTANCE.startBot(login, password, bot.getGameName(), bot.getNewBot());
		} catch (BotManagerException | BotRunnerException e) {
			System.out.println(e.getMessage());
		}
		return caller;
	}

	@Override
	public String getLabel() {
		return "Start a new bot";
	}

}
