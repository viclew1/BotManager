package fr.lewon.bot.manager.service;

import java.util.Map;

import fr.lewon.bot.manager.entities.AvailableBotsDTO;
import fr.lewon.bot.manager.entities.BotInfosListDTO;
import fr.lewon.bot.manager.entities.BotLogsDTO;
import fr.lewon.bot.manager.entities.BotMethodsDTO;
import fr.lewon.bot.manager.util.errors.BotManagerException;

public interface BotService {

	AvailableBotsDTO getAvailableBots() throws BotManagerException;

	void startBot(String login, String password, String gameName) throws BotManagerException;

	void stopBot(String login, String gameName) throws BotManagerException;

	void pauseBot(String login, String gameName) throws BotManagerException;

	BotInfosListDTO getAllBotInfos() throws BotManagerException;

	void trimStoppedBots() throws BotManagerException;

	BotLogsDTO getBotLogs(String login, String gameName) throws BotManagerException;

	BotMethodsDTO getMethods(String login, String gameName) throws BotManagerException;

	Object callMethod(String login, String gameName, Long id, Map<String, Object> params) throws BotManagerException;


}
