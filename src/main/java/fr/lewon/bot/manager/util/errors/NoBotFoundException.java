package fr.lewon.bot.manager.util.errors;

public class NoBotFoundException extends BotManagerException {

	private static final long serialVersionUID = -476868329067240576L;

	public NoBotFoundException(Long id) {
		super("No bot found for the id \"" + id + "\"");
	}

}
