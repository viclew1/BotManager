package fr.lewon.bot.manager.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import fr.lewon.bot.errors.BotRunnerException;
import fr.lewon.bot.manager.entities.BotInfosDTO;
import fr.lewon.bot.manager.entities.BotLogsDTO;
import fr.lewon.bot.manager.entities.BotMethodDTO;
import fr.lewon.bot.manager.entities.BotMethodsDTO;
import fr.lewon.bot.manager.entities.GameInfosDTO;
import fr.lewon.bot.manager.entities.GameInfosListDTO;
import fr.lewon.bot.manager.modele.BotRunnersManager;
import fr.lewon.bot.manager.modele.GameInfos;
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
	public void startBot(String login, String password, String gameId) throws BotManagerException {
		BotFactory botFacto = BotFactory.valueOf(gameId);
		if (botFacto == null) {
			throw new NoBotForThisGameException(gameId);
		}
		try {
			BotRunnersManager.INSTANCE.startBot(login, password, botFacto);
		} catch (BotRunnerException e) {
			throw new BotManagerException(e.getMessage());
		}
	}

	@Override
	public void stopBot(Long id) throws BotManagerException {
		try {
			BotRunnersManager.INSTANCE.stopBot(id);
		} catch (BotRunnerException e) {
			throw new BotManagerException(e.getMessage());
		}
	}

	@Override
	public void pauseBot(Long id) throws BotManagerException {
		try {
			BotRunnersManager.INSTANCE.pauseBot(id);
		} catch (BotRunnerException e) {
			throw new BotManagerException(e.getMessage());
		}
	}

	@Override
	public GameInfosListDTO getAllBotInfos() throws BotManagerException {
		List<GameInfosDTO> gameInfosList = new ArrayList<>();
		for (Entry<GameInfos, List<RunnerInfos>> entry : BotRunnersManager.INSTANCE.getBotInfosMap().entrySet()) {
			GameInfos gameInfos = entry.getKey();
			List<BotInfosDTO> botInfosList = entry.getValue().stream()
					.map(r -> new BotInfosDTO(r.getId(), r.getLogin(), r.getBotRunner().getState().toString()))
					.collect(Collectors.toList());
			GameInfosDTO gameInfosDTO = new GameInfosDTO(gameInfos.getId(), gameInfos.getName(), botInfosList);
			gameInfosList.add(gameInfosDTO);
		}
		return new GameInfosListDTO(gameInfosList);
	}

	@Override
	public void trimStoppedBots() throws BotManagerException {
		BotRunnersManager.INSTANCE.trimStoppedBots();
	}

	@Override
	public BotLogsDTO getBotLogs(Long id) throws BotManagerException {
		BotLogsDTO bl = new BotLogsDTO();
		bl.setLogs(BotRunnersManager.INSTANCE.getRunnerInfos(id).getBotRunner().getLogs());
		return bl;
	}

	@Override
	public BotMethodsDTO getMethods(Long id) throws BotManagerException {

		RunnerInfos infos = BotRunnersManager.INSTANCE.getRunnerInfos(id);

		List<BotMethodDTO> methods = new ArrayList<>();
		for (AbstractBotMethod<?, ?> botMethod : infos.getBotRunner().getBot().getBotMethods()) {
			Long methodId = botMethod.getId();
			String label = botMethod.getLabel();
			Map<String, Object> params = botMethod.getneededParameters();
			methods.add(new BotMethodDTO(methodId, label, params));
		}
		return new BotMethodsDTO(methods);
	}

	@Override
	public Object callMethod(Long idBot, Long idMethod, Map<String, Object> params) throws BotManagerException {
		RunnerInfos infos = BotRunnersManager.INSTANCE.getRunnerInfos(idBot);
		AbstractBotMethod<?, ?> method = infos.getBotRunner().getBot().getBotMethodById(idMethod);
		if (method == null) {
			throw new NoBotMethodForThisIdException(idMethod);
		}
		try {
			return method.process(infos.getBotRunner(), params);
		} catch (Exception e) {
			throw new BotManagerException(e.getMessage());
		}
	}

	@Override
	public void trimStoppedBot(Long id) throws BotManagerException {
		try {
			BotRunnersManager.INSTANCE.trimStoppedBot(id);
		} catch (BotRunnerException e) {
			throw new BotManagerException(e.getMessage());
		}
	}

	@Override
	public ClassPathResource getIcon(String gameId) throws BotManagerException {
		BotFactory bf = BotFactory.valueOf(gameId);
		return new ClassPathResource("image/" + bf.getIconFileName());
	}

}
