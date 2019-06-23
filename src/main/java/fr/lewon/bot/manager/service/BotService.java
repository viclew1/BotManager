package fr.lewon.bot.manager.service;

import java.util.Map;

import org.springframework.core.io.ClassPathResource;

import fr.lewon.bot.manager.entities.BotInfosDTO;
import fr.lewon.bot.manager.entities.BotLogsDTO;
import fr.lewon.bot.manager.entities.BotMethodsDTO;
import fr.lewon.bot.manager.entities.BotPropertiesDescriptorsDTO;
import fr.lewon.bot.manager.entities.GameInfosListDTO;
import fr.lewon.bot.manager.util.errors.BotManagerException;

public interface BotService {

	void createBot(String login, String password, String gameId, Map<String, Object> params) throws BotManagerException;

	void stopBot(String gameId, Long id) throws BotManagerException;

	void pauseBot(String gameId, Long id) throws BotManagerException;
	
	void startBot(String gameId, Long id) throws BotManagerException;

	GameInfosListDTO getAllBotInfos() throws BotManagerException;

	void trimStoppedBots() throws BotManagerException;

	void trimStoppedBot(String gameId, Long id) throws BotManagerException;

	BotLogsDTO getBotLogs(String gameId, Long id) throws BotManagerException;

	BotMethodsDTO getMethods(String gameId, Long botId) throws BotManagerException;

	Object callMethod(String gameId, String idMethod, Long botId, Map<String, Object> params) throws BotManagerException;

	ClassPathResource getIcon(String gameId) throws BotManagerException;

	BotPropertiesDescriptorsDTO getProperties(String gameId) throws BotManagerException;

	BotInfosDTO getBotInfos(String gameId, Long id) throws BotManagerException;

}
