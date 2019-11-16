package fr.lewon.bot.manager.service.impl;

import fr.lewon.bot.errors.BotRunnerException;
import fr.lewon.bot.manager.entities.*;
import fr.lewon.bot.manager.mappers.BotMethodMapper;
import fr.lewon.bot.manager.mappers.BotPropertyMapper;
import fr.lewon.bot.manager.mappers.GameInfoMapper;
import fr.lewon.bot.manager.modele.BotRunnersManager;
import fr.lewon.bot.manager.modele.GameInfos;
import fr.lewon.bot.manager.modele.RunnerInfos;
import fr.lewon.bot.manager.service.BotService;
import fr.lewon.bot.manager.util.BotRunnerBuilderFactory;
import fr.lewon.bot.manager.util.errors.BotManagerException;
import fr.lewon.bot.manager.util.errors.NoBotForThisGameException;
import fr.lewon.bot.manager.util.errors.NoBotMethodForThisIdException;
import fr.lewon.bot.methods.AbstractBotMethodProcessor;
import fr.lewon.bot.props.BotPropertyDescriptor;
import fr.lewon.bot.runner.BotRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class BotServiceImpl implements BotService {

    @Autowired
    private BotPropertyMapper botPropertyMapper;
    @Autowired
    private BotMethodMapper botMethodMapper;
    @Autowired
    private GameInfoMapper gameInfoMapper;

    @Override
    public void createBot(String login, String password, String gameId, Map<String, Object> params) throws BotManagerException {
        BotRunnerBuilderFactory botFacto = BotRunnerBuilderFactory.valueOf(gameId);
        if (botFacto == null) {
            throw new NoBotForThisGameException(gameId);
        }
        try {
            BotRunnersManager.INSTANCE.createBot(login, password, params, botFacto);
        } catch (BotRunnerException e) {
            throw new BotManagerException(e);
        }
    }


    @Override
    public void stopBot(String gameId, Long id) throws BotManagerException {
        try {
            BotRunnersManager.INSTANCE.stopBot(gameId, id);
        } catch (BotRunnerException e) {
            throw new BotManagerException(e);
        }
    }


    @Override
    public void pauseBot(String gameId, Long id) throws BotManagerException {
        try {
            BotRunnersManager.INSTANCE.pauseBot(gameId, id);
        } catch (BotRunnerException e) {
            throw new BotManagerException(e);
        }
    }


    @Override
    public void startBot(String gameId, Long id) throws BotManagerException {
        try {
            BotRunnersManager.INSTANCE.startBot(gameId, id);
        } catch (BotRunnerException e) {
            throw new BotManagerException(e);
        }
    }


    @Override
    public BotInfosDTO getBotInfos(String gameId, Long id) throws BotManagerException {
        RunnerInfos ri = BotRunnersManager.INSTANCE.getRunnerInfos(gameId, id);
        return new BotInfosDTO(ri.getId(), ri.getLogin(), ri.getBotRunner().getState().toString());
    }


    @Override
    public GameInfosListDTO getAllBotInfos() throws BotManagerException {
        List<GameInfos> gameInfos = BotRunnersManager.INSTANCE.getGameInfos();
        List<GameInfosDTO> gameInfosList = this.gameInfoMapper.gameInfosToDto(gameInfos);
        return new GameInfosListDTO(gameInfosList);
    }


    @Override
    public void trimStoppedBots() throws BotManagerException {
        try {
            BotRunnersManager.INSTANCE.trimStoppedBots();
        } catch (BotRunnerException | BotManagerException e) {
            throw new BotManagerException(e);
        }
    }


    @Override
    public BotLogsDTO getBotLogs(String gameId, Long id) throws BotManagerException {
        return new BotLogsDTO(BotRunnersManager.INSTANCE.getRunnerInfos(gameId, id).getBotRunner().getBotLogger().getLogs());
    }


    @Override
    public BotMethodsDTO getMethods(String gameId, Long botId) throws BotManagerException {
        BotRunner runner = BotRunnersManager.INSTANCE.getRunnerInfos(gameId, botId).getBotRunner();
        List<? extends AbstractBotMethodProcessor> botMethods = BotRunnerBuilderFactory.valueOf(gameId).getBotRunnerBuilder().getBotMethods();
        List<BotMethodDTO> methods = this.botMethodMapper.botMethodToDto(botMethods, runner);
        return new BotMethodsDTO(methods);
    }


    @Override
    public Object callMethod(String gameId, String idMethod, Long botId, Map<String, Object> params) throws BotManagerException {
        BotRunner runner = BotRunnersManager.INSTANCE.getRunnerInfos(gameId, botId).getBotRunner();
        AbstractBotMethodProcessor method = BotRunnerBuilderFactory.valueOf(gameId).getBotRunnerBuilder().getBotMethodById(idMethod);
        if (method == null) {
            throw new NoBotMethodForThisIdException(idMethod);
        }
        try {
            return method.process(runner, params);
        } catch (Exception e) {
            throw new BotManagerException(e);
        }
    }


    @Override
    public void trimStoppedBot(String gameId, Long id) throws BotManagerException {
        try {
            BotRunnersManager.INSTANCE.trimStoppedBot(gameId, id);
        } catch (BotRunnerException e) {
            throw new BotManagerException(e);
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
    public BotPropertiesDescriptorsDTO getProperties(String gameId) throws BotManagerException {
        BotRunnerBuilderFactory botFacto = BotRunnerBuilderFactory.valueOf(gameId);
        if (botFacto == null) {
            throw new NoBotForThisGameException(gameId);
        }
        List<BotPropertyDescriptor> descriptors = botFacto.getBotRunnerBuilder().getPropertiesBuilders();
        return new BotPropertiesDescriptorsDTO(this.botPropertyMapper.botPropertiesToDto(descriptors));
    }
}
