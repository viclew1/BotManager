package fr.lewon.bot.manager.util.errors

import fr.lewon.bot.runner.lifecycle.bot.BotState

class CantTrimBotException(id: Long) : BotManagerException("Can't trim bot [$id], it must be in state [${BotState.STOPPED}]")