package fr.lewon.bot.manager.modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import fr.lewon.bot.errors.BotRunnerException;
import fr.lewon.bot.errors.WrongStateRunnerException;
import fr.lewon.bot.manager.util.BotRunnerBuilderFactory;
import fr.lewon.bot.manager.util.errors.AlreadyRunningBotException;
import fr.lewon.bot.manager.util.errors.BotManagerException;
import fr.lewon.bot.manager.util.errors.NoBotFoundException;
import fr.lewon.bot.runner.BotRunner;
import fr.lewon.bot.runner.State;

public enum BotRunnersManager {

	INSTANCE;

	/**
	 * key : name of the game
	 * value : BotInfos linked to this game
	 */
	private Map<GameInfos, List<RunnerInfos>> runners = initRunnersMap();

	private Map<GameInfos, List<RunnerInfos>> initRunnersMap() {
		Map<GameInfos, List<RunnerInfos>> map = new HashMap<>();
		for (BotRunnerBuilderFactory bf : BotRunnerBuilderFactory.values()) {
			map.put(new GameInfos(bf.name(), bf.getGameName()), new ArrayList<>());
		}
		return map;
	}

	public void createBot(String login, String password, Map<String, Object> params, BotRunnerBuilderFactory bf) throws BotManagerException {
		String gameId = bf.name();
		GameInfos gi = new GameInfos(gameId, bf.getGameName());
		verifyNotRunning(gi, login);
		BotRunner runner = bf.getBotRunnerBuilder().buildRunner(login, password);
		runner.init(params);
		runners.putIfAbsent(gi, new ArrayList<>());
		runners.get(gi).add(new RunnerInfos(runner, login));
	}

	public RunnerInfos getRunnerInfos(Long id) throws BotManagerException {
		for (RunnerInfos runner : runners.values().stream().flatMap(List::stream).collect(Collectors.toList())) {
			if (runner.getId().equals(id)) {
				return runner;
			}
		}
		throw new NoBotFoundException(id);
	}

	public void stopBot(Long id) throws BotManagerException, BotRunnerException {
		RunnerInfos runnerInfos = getRunnerInfos(id);
		runnerInfos.getBotRunner().stop();
	}

	public void pauseBot(Long id) throws BotManagerException, BotRunnerException {
		RunnerInfos runnerInfos = getRunnerInfos(id);
		runnerInfos.getBotRunner().togglePause();
	}
	
	public void startBot(Long id) throws BotManagerException, BotRunnerException {
		RunnerInfos runnerInfos = getRunnerInfos(id);
		runnerInfos.getBotRunner().start();
	}

	private void verifyNotRunning(GameInfos gi, String login) throws AlreadyRunningBotException {
		List<RunnerInfos> botInfos = runners.get(gi);
		if (botInfos == null) {
			return;
		}
		for (RunnerInfos bi : botInfos) {
			if (bi.getLogin().equals(login)) {
				throw new AlreadyRunningBotException(gi.getName(), login);
			}
		}
	}

	public Map<GameInfos, List<RunnerInfos>> getBotInfosMap() {
		return runners;
	}

	public void trimStoppedBots() {
		for (Entry<GameInfos, List<RunnerInfos>> entry : runners.entrySet()) {
			entry.setValue(entry.getValue().stream()
					.filter(ri -> ri.getBotRunner().getState() != State.STOPPED)
					.collect(Collectors.toList()));
		}
	}

	public void trimStoppedBot(Long id) throws BotManagerException, BotRunnerException {
		RunnerInfos runnerInfos = getRunnerInfos(id);
		if (runnerInfos.getBotRunner().getState() != State.STOPPED) {
			throw new WrongStateRunnerException("clean", runnerInfos.getBotRunner().getState(), State.STOPPED);
		}
		for (List<RunnerInfos> runnersInfos : runners.values()) {
			if (runnersInfos.contains(runnerInfos)) {
				runnersInfos.remove(runnerInfos);
				break;
			}
		}
	}

}
