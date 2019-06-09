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
import fr.lewon.bot.manager.entities.BotPropertiesDTO;
import fr.lewon.bot.manager.entities.BotPropertyDTO;
import fr.lewon.bot.manager.entities.GameInfosDTO;
import fr.lewon.bot.manager.entities.GameInfosListDTO;
import fr.lewon.bot.manager.modele.BotRunnersManager;
import fr.lewon.bot.manager.modele.GameInfos;
import fr.lewon.bot.manager.modele.RunnerInfos;
import fr.lewon.bot.manager.service.BotService;
import fr.lewon.bot.manager.util.BotRunnerBuilderFactory;
import fr.lewon.bot.manager.util.errors.BotManagerException;
import fr.lewon.bot.manager.util.errors.NoBotForThisGameException;
import fr.lewon.bot.manager.util.errors.NoBotMethodForThisIdException;
import fr.lewon.bot.methods.IBotMethod;
import fr.lewon.bot.runner.BotRunner;

@Service
public class BotServiceImpl implements BotService {

	@Override
	public void createBot(String login, String password, String gameId, Map<String, Object> params) throws BotManagerException {
		BotRunnerBuilderFactory botFacto = BotRunnerBuilderFactory.valueOf(gameId);
		if (botFacto == null) {
			throw new NoBotForThisGameException(gameId);
		}
		BotRunnersManager.INSTANCE.createBot(login, password, params, botFacto);
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
	public void startBot(Long id) throws BotManagerException {
		try {
			BotRunnersManager.INSTANCE.startBot(id);
		} catch (BotRunnerException e) {
			throw new BotManagerException(e.getMessage());
		}
	}

	@Override
	public BotInfosDTO getBotInfos(Long id) throws BotManagerException {
		RunnerInfos ri = BotRunnersManager.INSTANCE.getRunnerInfos(id);
		return new BotInfosDTO(ri.getId(), ri.getLogin(), ri.getBotRunner().getState().toString());
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
	public BotMethodsDTO getMethods(String gameId, Long botId) throws BotManagerException {
		BotRunner runner = BotRunnersManager.INSTANCE.getRunnerInfos(botId).getBotRunner();
		List<? extends IBotMethod<?, ?>> botMethods = BotRunnerBuilderFactory.valueOf(gameId).getBotRunnerBuilder().getBotMethods();
		List<BotMethodDTO> methods = new ArrayList<>();
		for (IBotMethod<?, ?> botMethod : botMethods) {
			String methodId = botMethod.getId();
			String label = botMethod.getLabel();
			Map<String, Object> params = botMethod.getProcessor().getNeededParameters(runner);
			methods.add(new BotMethodDTO(methodId, label, params));
		}
		return new BotMethodsDTO(methods);
	}

	@Override
	public Object callMethod(String gameId, String idMethod, Long botId, Map<String, Object> params) throws BotManagerException {
		BotRunner runner = BotRunnersManager.INSTANCE.getRunnerInfos(botId).getBotRunner();
		IBotMethod<?, ?> method = BotRunnerBuilderFactory.valueOf(gameId).getBotRunnerBuilder().getBotMethodById(idMethod);
		if (method == null) {
			throw new NoBotMethodForThisIdException(idMethod);
		}
		try {
			return runner.callBotMethod(method, params);
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
		BotRunnerBuilderFactory botFacto = BotRunnerBuilderFactory.valueOf(gameId);
		if (botFacto == null) {
			throw new NoBotForThisGameException(gameId);
		}
		return new ClassPathResource("image/" + botFacto.getIconFileName());
	}

	@Override
	public BotPropertiesDTO getProperties(String gameId) throws BotManagerException {
		BotRunnerBuilderFactory botFacto = BotRunnerBuilderFactory.valueOf(gameId);
		if (botFacto == null) {
			throw new NoBotForThisGameException(gameId);
		}
		return new BotPropertiesDTO(botFacto.getBotRunnerBuilder().getPropertiesBuilders().stream()
				.map(builder -> new BotPropertyDTO(builder.getKey(), 
						builder.getDescriptor().getDescription(), 
						builder.getDescriptor().isNeeded(),
						builder.getDescriptor().isNullable(),
						builder.getDefaultValue()))
				.collect(Collectors.toList()));
	}

}
