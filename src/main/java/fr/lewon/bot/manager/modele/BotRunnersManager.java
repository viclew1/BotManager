package fr.lewon.bot.manager.modele;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.lewon.bot.errors.BotRunnerException;
import fr.lewon.bot.errors.WrongStateRunnerException;
import fr.lewon.bot.manager.util.BotRunnerBuilderFactory;
import fr.lewon.bot.manager.util.errors.AlreadyRunningBotException;
import fr.lewon.bot.manager.util.errors.BotManagerException;
import fr.lewon.bot.manager.util.errors.NoBotForThisGameException;
import fr.lewon.bot.manager.util.errors.NoBotFoundException;
import fr.lewon.bot.runner.BotRunner;
import fr.lewon.bot.runner.BotState;

public enum BotRunnersManager {

	INSTANCE;

	private final List<GameInfos> gameInfos;

	private BotRunnersManager() {
		gameInfos = Arrays.asList(BotRunnerBuilderFactory.values()).stream()
				.map(b -> new GameInfos(b.name(), b.getGameName()))
				.collect(Collectors.toList());
	}

	public void createBot(String login, String password, Map<String, Object> params, BotRunnerBuilderFactory bf) throws BotManagerException, BotRunnerException {
		GameInfos gi = findGameInfosById(bf.name());
		verifyNotRunning(gi, login);
		BotRunner runner = bf.getBotRunnerBuilder().buildRunner(login, password);
		runner.init(params);
		gi.getRunnerInfos().add(new RunnerInfos(runner, login));
	}

	public RunnerInfos getRunnerInfos(String gameId, Long id) throws BotManagerException {
		return getRunnerInfos(findGameInfosById(gameId), id);
	}
	
	public RunnerInfos getRunnerInfos(GameInfos gameInfos, Long id) throws BotManagerException {
		return gameInfos.getRunnerInfos().stream()
				.filter(r -> r.getId().equals(id))
				.findFirst()
				.orElseThrow(() -> new NoBotFoundException(id));
	}

	public void stopBot(String gameId, Long id) throws BotManagerException, BotRunnerException {
		RunnerInfos runnerInfos = getRunnerInfos(gameId, id);
		runnerInfos.getBotRunner().stop();
	}

	public void pauseBot(String gameId, Long id) throws BotManagerException, BotRunnerException {
		RunnerInfos runnerInfos = getRunnerInfos(gameId, id);
		runnerInfos.getBotRunner().togglePause();
	}

	public void startBot(String gameId, Long id) throws BotManagerException, BotRunnerException {
		RunnerInfos runnerInfos = getRunnerInfos(gameId, id);
		runnerInfos.getBotRunner().start();
	}

	private void verifyNotRunning(GameInfos gameInfos, String login) throws AlreadyRunningBotException {
		for (RunnerInfos bi : gameInfos.getRunnerInfos()) {
			if (bi.getLogin().equals(login)) {
				throw new AlreadyRunningBotException(gameInfos.getName(), login);
			}
		}
	}

	public GameInfos findGameInfosById(String gameId) throws NoBotForThisGameException {
		return gameInfos.stream()
				.filter(g -> g.getId().equals(gameId))
				.findFirst()
				.orElseThrow(() -> new NoBotForThisGameException(gameId));
	}

	public void trimStoppedBots() {
		gameInfos.stream()
		.map(GameInfos::getRunnerInfos)
		.forEach(ri -> ri.removeAll(ri.stream()
				.filter(r -> r.getBotRunner().getState() == BotState.STOPPED)
				.collect(Collectors.toList())));
	}

	public void trimStoppedBot(String gameId, Long id) throws BotManagerException, BotRunnerException {
		GameInfos gameInfos = findGameInfosById(gameId);
		RunnerInfos runnerInfos = getRunnerInfos(gameId, id);
		if (runnerInfos.getBotRunner().getState() != BotState.STOPPED) {
			throw new WrongStateRunnerException("Trim", runnerInfos.getBotRunner().getState(), BotState.STOPPED);
		}
		gameInfos.getRunnerInfos().remove(runnerInfos);
	}

	public List<GameInfos> getGameInfos() {
		return gameInfos;
	}

}
