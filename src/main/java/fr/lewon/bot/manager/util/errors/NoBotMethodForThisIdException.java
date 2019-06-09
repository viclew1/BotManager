package fr.lewon.bot.manager.util.errors;

public class NoBotMethodForThisIdException extends BotManagerException {

	private static final long serialVersionUID = -3223795949655740058L;

	public NoBotMethodForThisIdException(Long id) {
		this(Long.toString(id));
	}
	
	public NoBotMethodForThisIdException(String id) {
		super("No bot method found for the id \"" + id + "\"");
	}

}
