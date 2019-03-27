package fr.lewon.bot.manager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.lewon.bot.manager.entities.AvailableBots;
import fr.lewon.bot.manager.entities.BotActionInfos;
import fr.lewon.bot.manager.entities.BotInfosList;
import fr.lewon.bot.manager.entities.BotLogs;
import fr.lewon.bot.manager.service.BotService;
import fr.lewon.bot.manager.util.errors.BotManagerException;

@RestController
@RequestMapping("/bots")
public class BotController {

	@Autowired
	private BotService botService;

	@RequestMapping(
			method = RequestMethod.GET, produces = "application/json", consumes = "application/json",
			value = "/all")
	public AvailableBots getAvailableBots() throws BotManagerException {
		return botService.getAvailableBots();
	}

	@RequestMapping(
			method = RequestMethod.GET, produces = "application/json", consumes = "application/json",
			value = "/infos/all")
	public BotInfosList getAllBotInfos() throws BotManagerException {
		return botService.getAllBotInfos();
	}

	@RequestMapping(
			method = RequestMethod.POST, produces = "application/json", consumes = "application/json",
			value = "/start")
	public void startBot(@RequestBody BotActionInfos infos) throws BotManagerException {
		botService.startBot(infos.getLogin(), infos.getPassword(), infos.getGameName());
	}

	@RequestMapping(
			method = RequestMethod.POST, produces = "application/json", consumes = "application/json",
			value = "/stop")
	public void stopBot(@RequestBody BotActionInfos infos) throws BotManagerException {
		botService.stopBot(infos.getLogin(), infos.getGameName());
	}

	@RequestMapping(
			method = RequestMethod.POST, produces = "application/json", consumes = "application/json",
			value = "/pause")
	public void pauseBot(@RequestBody BotActionInfos infos) throws BotManagerException {
		botService.pauseBot(infos.getLogin(), infos.getGameName());
	}

	@RequestMapping(
			method = RequestMethod.POST, produces = "application/json", consumes = "application/json",
			value = "/trim")
	public void trimStoppedBots() throws BotManagerException {
		botService.trimStoppedBots();
	}

	@RequestMapping(
			method = RequestMethod.POST, produces = "application/json", consumes = "application/json",
			value = "/logs")
	public BotLogs getLogs(@RequestBody BotActionInfos infos) throws BotManagerException {
		return botService.getBotLogs(infos.getLogin(), infos.getGameName());
	}

}
