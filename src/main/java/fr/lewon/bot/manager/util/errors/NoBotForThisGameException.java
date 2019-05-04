package fr.lewon.bot.manager.util.errors;

public class NoBotForThisGameException extends BotManagerException {

	private static final long serialVersionUID = 5451862492015332539L;

	public NoBotForThisGameException(String gameId) {
		super("No bot found for the game \"" + gameId + "\"");
	}

}
