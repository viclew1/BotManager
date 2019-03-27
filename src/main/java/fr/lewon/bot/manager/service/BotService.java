package fr.lewon.bot.manager.service;

import fr.lewon.bot.manager.entities.AvailableBots;
import fr.lewon.bot.manager.entities.BotInfosList;
import fr.lewon.bot.manager.entities.BotLogs;
import fr.lewon.bot.manager.util.errors.BotManagerException;

public interface BotService {

	AvailableBots getAvailableBots() throws BotManagerException;

	void startBot(String login, String password, String gameName) throws BotManagerException;

	void stopBot(String login, String gameName) throws BotManagerException;

	void pauseBot(String login, String gameName) throws BotManagerException;

	BotInfosList getAllBotInfos() throws BotManagerException;

	void trimStoppedBots() throws BotManagerException;

	BotLogs getBotLogs(String login, String gameName) throws BotManagerException;

}
