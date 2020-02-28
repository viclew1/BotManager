package fr.lewon.bot.manager.util.errors

class NoBotFoundException(id: Long) : BotManagerException("No bot found for the id [$id]")