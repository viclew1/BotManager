package fr.lewon.bot.manager.util.errors

class NoBotMethodForThisIdException(gameName: String, id: Long) : BotManagerException("No bot method found in game [$gameName] for the id [$id]")
