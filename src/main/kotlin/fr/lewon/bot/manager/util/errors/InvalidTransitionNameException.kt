package fr.lewon.bot.manager.util.errors

class InvalidTransitionNameException(transition: String) : BotManagerException("Transition [$transition] does not exist")