package fr.lewon.bot.manager.util;

import fr.lewon.bot.AbstractBot;

public interface BotCreator {

	AbstractBot<?, ?> init();

}
