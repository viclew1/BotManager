package fr.lewon.bot.manager.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.lewon.bot.errors.BotRunnerException;
import fr.lewon.bot.manager.entities.AvailableBotsDTO;
import fr.lewon.bot.manager.entities.BotInfosDTO;
import fr.lewon.bot.manager.entities.BotInfosListDTO;
import fr.lewon.bot.manager.entities.BotLogsDTO;
import fr.lewon.bot.manager.entities.BotMethodDTO;
import fr.lewon.bot.manager.entities.BotMethodsDTO;
import fr.lewon.bot.manager.modele.BotRunnersManager;
import fr.lewon.bot.manager.modele.RunnerInfos;
import fr.lewon.bot.manager.service.BotService;
import fr.lewon.bot.manager.util.BotFactory;
import fr.lewon.bot.manager.util.errors.BotManagerException;
import fr.lewon.bot.manager.util.errors.NoBotForThisGameException;
import fr.lewon.bot.manager.util.errors.NoBotMethodForThisIdException;
import fr.lewon.bot.methods.AbstractBotMethod;

@Service
public class BotServiceImpl implements BotService {

	@Override
	public AvailableBotsDTO getAvailableBots() {
		List<String> gameNames = Arrays.asList(BotFactory.values()).stream()
				.map(BotFactory::getGameName)
				.collect(Collectors.toList());
		AvailableBotsDTO ab = new AvailableBotsDTO();
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
	public BotInfosListDTO getAllBotInfos() throws BotManagerException {
		BotInfosListDTO ret = new BotInfosListDTO();
		List<BotInfosDTO> botInfos = BotRunnersManager.INSTANCE.getBotInfosList().stream()
				.map(r -> new BotInfosDTO(r.getLogin(), r.getGameName(), r.getBotRunner().getState().toString()))
				.collect(Collectors.toList());
		ret.setBotInfosList(botInfos);
		return ret;
	}

	@Override
	public void trimStoppedBots() throws BotManagerException {
		BotRunnersManager.INSTANCE.trimStoppedBots();
	}

	@Override
	public BotLogsDTO getBotLogs(String login, String gameName) throws BotManagerException {
		BotLogsDTO bl = new BotLogsDTO();
		bl.setLogs(BotRunnersManager.INSTANCE.getRunnerInfos(login, gameName).getBotRunner().getLogs());
		return bl;
	}

	@Override
	public BotMethodsDTO getMethods(String login, String gameName) throws BotManagerException {

		RunnerInfos infos = BotRunnersManager.INSTANCE.getRunnerInfos(login, gameName);

		List<BotMethodDTO> methods = new ArrayList<>();
		for (AbstractBotMethod<?, ?> botMethod : infos.getBotRunner().getBot().getBotMethods()) {
			Long id = botMethod.getId();
			String label = botMethod.getLabel();
			Map<String, Object> params = botMethod.getneededParameters();
			methods.add(new BotMethodDTO(gameName, id, label, params));
		}
		return new BotMethodsDTO(methods);
	}

	@Override
	public Object callMethod(String login, String gameName, Long id, Map<String, Object> params) throws BotManagerException {
		RunnerInfos infos = BotRunnersManager.INSTANCE.getRunnerInfos(login, gameName);
		AbstractBotMethod<?, ?> method = infos.getBotRunner().getBot().getBotMethodById(id);
		if (method == null) {
			throw new NoBotMethodForThisIdException(gameName, id);
		}
		try {
			return method.process(infos.getBotRunner(), params);
		} catch (Exception e) {
			throw new BotManagerException(e.getMessage());
		}
	}

}
