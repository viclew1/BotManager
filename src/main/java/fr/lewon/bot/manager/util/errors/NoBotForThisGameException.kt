package fr.lewon.bot.manager.util.errors

class NoBotForThisGameException(gameId: String) : BotManagerException("No bot found for the game [$gameId]")