package fr.lewon.bot.manager.util.errors

class NoBotForThisGameException(gameId: Long) : BotManagerException("No bot found for the game [$gameId]")