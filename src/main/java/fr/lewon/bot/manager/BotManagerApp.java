package fr.lewon.bot.manager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import fr.lewon.bot.manager.menus.HomeMenu;
import fr.lewon.client.AbstractAppClient;
import fr.lewon.client.exceptions.CliException;
import fr.lewon.client.exceptions.InitializationException;
import fr.lewon.client.menus.Menu;
import fr.lewon.client.menus.MenuRunner;
import fr.lewon.client.util.parameters.Parameter;
import fr.lewon.client.util.parameters.impl.DirParameter;
import fr.lewon.client.util.parameters.impl.SimpleParameter;

public class BotManagerApp extends AbstractAppClient {

	private static Logger LOGGER;

	private static final Parameter PATH_LOGS = new DirParameter("logs.path", true, true);
	private static final Parameter LOGS_LEVEL_PARAM = new SimpleParameter("logs.level", false);


	public static void main(String[] args) throws CliException {
		new BotManagerApp().launch();
	}

	@Override
	protected List<Parameter> getParamsToInit() {
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(PATH_LOGS);
		parameters.add(LOGS_LEVEL_PARAM);
		return parameters;
	}

	@Override
	protected void initUtils() throws InitializationException {
		initLogger();
		LOGGER.info("Init ok");
	}

	private void initLogger() {
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");
		System.setProperty("log.name", PATH_LOGS.getValue() + "/" + ldt.format(dtf) + ".txt");
		ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.setLevel(Level.valueOf(LOGS_LEVEL_PARAM.getValue()));
		LOGGER = LoggerFactory.getLogger(this.getClass());
	}

	@Override
	protected Menu getHomeMenu() {
		return new HomeMenu();
	}

	@Override
	protected void run() throws CliException {
		MenuRunner.INSTANCE.runMenu(getHomeMenu());
		// In order to stop every running bot
		System.exit(0);
	}

}
