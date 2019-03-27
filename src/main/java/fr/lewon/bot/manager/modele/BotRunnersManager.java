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
import fr.lewon.bot.manager.util.errors.NoBotForThisGameException;
import fr.lewon.bot.manager.util.errors.NoBotForThisLoginException;
import fr.lewon.bot.runner.BotRunner;
import fr.lewon.bot.runner.State;

public enum BotRunnersManager {

	INSTANCE;

	/**
	 * key : name of the game
	 * value : BotInfos linked to this game
	 */
	private Map<String, List<RunnerInfos>> runners = new HashMap<>();

	public void startBot(String login, String password, String gameName, AbstractBot bot) throws BotRunnerException, BotManagerException {
		verifyNotRunning(gameName, login);
		BotRunner runner = new BotRunner(bot);
		runner.start(login, password);
		runners.putIfAbsent(gameName, new ArrayList<>());
		runners.get(gameName).add(new RunnerInfos(runner, login, gameName));
	}

	public RunnerInfos getRunnerInfos(String login, String gameName) throws BotManagerException {
		List<RunnerInfos> runnerInfosList = runners.get(gameName);
		if (runnerInfosList == null) {
			throw new NoBotForThisGameException(gameName);
		}
		for (RunnerInfos ri : runnerInfosList) {
			if (ri.getLogin().equals(login)) {
				return ri;
			}
		}
		throw new NoBotForThisLoginException(login, gameName);
	}

	public void stopBot(String login, String gameName) throws BotManagerException, BotRunnerException {
		RunnerInfos runnerInfos = getRunnerInfos(login, gameName);
		runnerInfos.getBotRunner().stop();
	}

	public void pauseBot(String login, String gameName) throws BotManagerException, BotRunnerException {
		RunnerInfos runnerInfos = getRunnerInfos(login, gameName);
		runnerInfos.getBotRunner().togglePause();
	}

	private void verifyNotRunning(String gameName, String login) throws AlreadyRunningBotException {

		List<RunnerInfos> botInfos = runners.get(gameName);
		if (botInfos == null) {
			return;
		}
		for (RunnerInfos bi : botInfos) {
			if (bi.getLogin().equals(login)) {
				throw new AlreadyRunningBotException(gameName, login);
			}
		}
	}

	public List<RunnerInfos> getBotInfosList() {
		return runners.values().stream()
				.flatMap(b -> b.stream())
				.collect(Collectors.toList());
	}

	public void trimStoppedBots() {
		Map<String, List<RunnerInfos>> trimmedMap = new HashMap<>();
		for (RunnerInfos bi : getBotInfosList()) {
			if (bi.getBotRunner().getState() != State.STOPPED) {
				trimmedMap.putIfAbsent(bi.getGameName(), new ArrayList<>());
				trimmedMap.get(bi.getGameName()).add(bi);
			}
		}
		this.runners = trimmedMap;
	}

}
