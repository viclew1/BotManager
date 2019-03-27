package fr.lewon.bot.manager.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.lewon.bot.errors.BotRunnerException;
import fr.lewon.bot.manager.entities.AvailableBots;
import fr.lewon.bot.manager.entities.BotInfos;
import fr.lewon.bot.manager.entities.BotInfosList;
import fr.lewon.bot.manager.entities.BotLogs;
import fr.lewon.bot.manager.modele.BotRunnersManager;
import fr.lewon.bot.manager.service.BotService;
import fr.lewon.bot.manager.util.BotFactory;
import fr.lewon.bot.manager.util.errors.BotManagerException;
import fr.lewon.bot.manager.util.errors.NoBotForThisGameException;

@Service
public class BotServiceImpl implements BotService {

	@Override
	public AvailableBots getAvailableBots() {
		List<String> gameNames = Arrays.asList(BotFactory.values()).stream()
				.map(BotFactory::getGameName)
				.collect(Collectors.toList());
		AvailableBots ab = new AvailableBots();
		ab.setBotNames(gameNames);
		return ab;
	}

	@Override
	public void startBot(String login, String password, String gameName) throws BotManagerException {
		BotFactory botFacto = BotFactory.fromGameName(gameName);
		if (botFacto == null) {
			throw new NoBotForThisGameException(gameName);
		}
		try {
			BotRunnersManager.INSTANCE.startBot(login, password, gameName, botFacto.getNewBot());
		} catch (BotRunnerException e) {
			throw new BotManagerException(e.getMessage());
		}
	}

	@Override
	public void stopBot(String login, String gameName) throws BotManagerException {
		try {
			BotRunnersManager.INSTANCE.stopBot(login, gameName);
		} catch (BotRunnerException e) {
			throw new BotManagerException(e.getMessage());
		}
	}

	@Override
	public void pauseBot(String login, String gameName) throws BotManagerException {
		try {
			BotRunnersManager.INSTANCE.pauseBot(login, gameName);
		} catch (BotRunnerException e) {
			throw new BotManagerException(e.getMessage());
		}
	}

	@Override
	public BotInfosList getAllBotInfos() throws BotManagerException {
		BotInfosList ret = new BotInfosList();
		List<BotInfos> botInfos = BotRunnersManager.INSTANCE.getBotInfosList().stream()
				.map(r -> new BotInfos(r.getLogin(), r.getGameName(), r.getBotRunner().getState().toString()))
				.collect(Collectors.toList());
		ret.setBotInfosList(botInfos);
		return ret;
	}

	@Override
	public void trimStoppedBots() throws BotManagerException {
		BotRunnersManager.INSTANCE.trimStoppedBots();
	}

	@Override
	public BotLogs getBotLogs(String login, String gameName) throws BotManagerException {
		BotLogs bl = new BotLogs();
		bl.setLogs(BotRunnersManager.INSTANCE.getRunnerInfos(login, gameName).getBotRunner().getLogs());
		return bl;
	}

}
