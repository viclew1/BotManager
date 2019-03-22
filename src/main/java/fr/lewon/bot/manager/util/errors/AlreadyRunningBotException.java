package fr.lewon.bot.manager.util.errors;

public class AlreadyRunningBotException extends BotManagerException {

	private static final long serialVersionUID = 3136764996295531470L;

	public AlreadyRunningBotException(String gameName, String login) {
		super("Bot for game \"" + gameName + "\" already running for user \"" + login + "\"");
	}

}
