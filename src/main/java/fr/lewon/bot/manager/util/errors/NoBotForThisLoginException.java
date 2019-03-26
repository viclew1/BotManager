package fr.lewon.bot.manager.util.errors;

public class NoBotForThisLoginException extends BotManagerException {

	private static final long serialVersionUID = -476868329067240576L;

	public NoBotForThisLoginException(String login, String gameName) {
		super("No bot found for the game \"" + gameName + "\" with the user \"" + login + "\"");
	}

}
