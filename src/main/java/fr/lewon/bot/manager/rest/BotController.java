package fr.lewon.bot.manager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.lewon.bot.manager.entities.AvailableBotsDTO;
import fr.lewon.bot.manager.entities.BotActionInfosDTO;
import fr.lewon.bot.manager.entities.BotInfosListDTO;
import fr.lewon.bot.manager.entities.BotLogsDTO;
import fr.lewon.bot.manager.entities.BotMethodDTO;
import fr.lewon.bot.manager.entities.BotMethodsDTO;
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
	public ResponseEntity<AvailableBotsDTO> getAvailableBots() throws BotManagerException {
		AvailableBotsDTO bots = botService.getAvailableBots();
		return new ResponseEntity<>(bots, HttpStatus.OK);
	}

	@RequestMapping(
			method = RequestMethod.GET, produces = "application/json", consumes = "application/json",
			value = "/infos/all")
	public ResponseEntity<BotInfosListDTO> getAllBotInfos() throws BotManagerException {
		BotInfosListDTO infos = botService.getAllBotInfos();
		return new ResponseEntity<>(infos, HttpStatus.OK);
	}

	@RequestMapping(
			method = RequestMethod.POST, produces = "application/json", consumes = "application/json",
			value = "/start")
	public ResponseEntity<Void> startBot(@RequestBody BotActionInfosDTO infos) throws BotManagerException {
		botService.startBot(infos.getLogin(), infos.getPassword(), infos.getGameName());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(
			method = RequestMethod.POST, produces = "application/json", consumes = "application/json",
			value = "/stop")
	public ResponseEntity<Void> stopBot(@RequestBody BotActionInfosDTO infos) throws BotManagerException {
		botService.stopBot(infos.getLogin(), infos.getGameName());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(
			method = RequestMethod.POST, produces = "application/json", consumes = "application/json",
			value = "/pause")
	public ResponseEntity<Void> pauseBot(@RequestBody BotActionInfosDTO infos) throws BotManagerException {
		botService.pauseBot(infos.getLogin(), infos.getGameName());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(
			method = RequestMethod.POST, produces = "application/json", consumes = "application/json",
			value = "/trim")
	public ResponseEntity<Void> trimStoppedBots() throws BotManagerException {
		botService.trimStoppedBots();
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(
			method = RequestMethod.POST, produces = "application/json", consumes = "application/json",
			value = "/logs")
	public ResponseEntity<BotLogsDTO> getLogs(@RequestBody BotActionInfosDTO infos) throws BotManagerException {
		BotLogsDTO logs = botService.getBotLogs(infos.getLogin(), infos.getGameName());
		return new ResponseEntity<>(logs, HttpStatus.OK);
	}

	@RequestMapping(
			method = RequestMethod.POST, produces = "application/json", consumes = "application/json",
			value = "/methods")
	public ResponseEntity<BotMethodsDTO> getMethods(@RequestBody BotActionInfosDTO infos) throws BotManagerException {
		BotMethodsDTO methods = botService.getMethods(infos.getLogin(), infos.getGameName());
		return new ResponseEntity<>(methods, HttpStatus.OK);
	}

	@RequestMapping(
			method = RequestMethod.POST, produces = "application/json", consumes = "application/json",
			value = "/methods/process")
	public ResponseEntity<Object> callMethod(@RequestParam String login, @RequestBody BotMethodDTO method) throws BotManagerException {
		Object result = botService.callMethod(login, method.getGameName(), method.getId(), method.getParams());
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
