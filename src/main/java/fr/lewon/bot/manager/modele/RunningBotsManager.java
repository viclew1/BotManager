package fr.lewon.bot.manager.modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.lewon.bot.AbstractBot;
import fr.lewon.bot.errors.BotRunnerException;
import fr.lewon.bot.manager.util.errors.AlreadyRunningBotException;
import fr.lewon.bot.manager.util.errors.BotManagerException;
import fr.lewon.bot.runner.BotRunner;

public enum RunningBotsManager {

	INSTANCE;

	/**
	 * key : name of the game
	 * value : BotInfos linked to this game
	 */
	private Map<String, List<BotInfos>> runners = new HashMap<>();

	public void startBot(String login, String password, String gameName, AbstractBot bot) throws BotRunnerException, BotManagerException {
		verifyNotRunning(gameName, login);
		BotRunner runner = new BotRunner(bot);
		runner.start(login, password);
		runners.putIfAbsent(gameName, new ArrayList<>());
		runners.get(gameName).add(new BotInfos(runner, login, gameName));
	}

	private void verifyNotRunning(String gameName, String login) throws AlreadyRunningBotException {

		List<BotInfos> botInfos = runners.get(gameName);
		if (botInfos == null) {
			return;
		}
		for (BotInfos bi : botInfos) {
			if (bi.getLogin().equals(login)) {
				throw new AlreadyRunningBotException(gameName, login);
			}
		}
	}

	public List<BotInfos> getBotInfosList() {
		return runners.values().stream()
				.flatMap(b -> b.stream())
				.collect(Collectors.toList());
	}

}
