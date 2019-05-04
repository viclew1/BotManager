package fr.lewon.bot.manager.service;

import java.util.Map;

import org.springframework.core.io.ClassPathResource;

import fr.lewon.bot.manager.entities.GameInfosListDTO;
import fr.lewon.bot.manager.entities.BotLogsDTO;
import fr.lewon.bot.manager.entities.BotMethodsDTO;
import fr.lewon.bot.manager.util.errors.BotManagerException;

public interface BotService {

	void startBot(String login, String password, String gameId) throws BotManagerException;

	void stopBot(Long id) throws BotManagerException;

	void pauseBot(Long id) throws BotManagerException;

	GameInfosListDTO getAllBotInfos() throws BotManagerException;

	void trimStoppedBots() throws BotManagerException;

	void trimStoppedBot(Long id) throws BotManagerException;

	BotLogsDTO getBotLogs(Long id) throws BotManagerException;

	BotMethodsDTO getMethods(Long id) throws BotManagerException;

	Object callMethod(Long id, Long idMethod, Map<String, Object> params) throws BotManagerException;

	ClassPathResource getIcon(String gameId) throws BotManagerException;


}
