package fr.lewon.bot.manager.util.errors

class AlreadyRunningBotException(gameName: String, login: String) : BotManagerException("Bot for game [$gameName] already running for user [$login]")