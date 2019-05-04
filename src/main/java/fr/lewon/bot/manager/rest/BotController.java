package fr.lewon.bot.manager.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.lewon.bot.manager.entities.BotLogsDTO;
import fr.lewon.bot.manager.entities.BotMethodDTO;
import fr.lewon.bot.manager.entities.BotMethodsDTO;
import fr.lewon.bot.manager.entities.GameInfosListDTO;
import fr.lewon.bot.manager.entities.UserInfosDTO;
import fr.lewon.bot.manager.service.BotService;
import fr.lewon.bot.manager.util.errors.BotManagerException;

@RestController
@RequestMapping("/bots")
public class BotController {

	@Autowired
	private BotService botService;

	@RequestMapping(
			method = RequestMethod.GET, produces = "application/json", value = "/all")
	public ResponseEntity<GameInfosListDTO> getAllBotInfos() throws BotManagerException {
		GameInfosListDTO infos = botService.getAllBotInfos();
		return new ResponseEntity<>(infos, HttpStatus.OK);
	}

	@RequestMapping(
			method = RequestMethod.POST, produces = "application/json", consumes = "application/json", value = "/{gameId}/start")
	public ResponseEntity<Void> startBot(@PathVariable String gameId, @RequestBody UserInfosDTO infos) throws BotManagerException {
		botService.startBot(infos.getLogin(), infos.getPassword(), gameId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(
			method = RequestMethod.POST, produces = "application/json", value = "/{id}/stop")
	public ResponseEntity<Void> stopBot(@PathVariable Long id) throws BotManagerException {
		botService.stopBot(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(
			method = RequestMethod.POST, produces = "application/json", value = "/{id}/pause")
	public ResponseEntity<Void> pauseBot(@PathVariable Long id) throws BotManagerException {
		botService.pauseBot(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(
			method = RequestMethod.POST, produces = "application/json", value = "/{id}/trim")
	public ResponseEntity<Void> trimStoppedBot(@PathVariable Long id) throws BotManagerException {
		botService.trimStoppedBot(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(
			method = RequestMethod.POST, produces = "application/json", value = "/trim")
	public ResponseEntity<Void> trimStoppedBots() throws BotManagerException {
		botService.trimStoppedBots();
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(
			method = RequestMethod.GET, produces = "application/json", value = "/{id}/logs")
	public ResponseEntity<BotLogsDTO> getLogs(@PathVariable Long id) throws BotManagerException {
		BotLogsDTO logs = botService.getBotLogs(id);
		return new ResponseEntity<>(logs, HttpStatus.OK);
	}

	@RequestMapping(
			method = RequestMethod.GET, produces = "application/json", value = "/{id}/methods")
	public ResponseEntity<BotMethodsDTO> getMethods(@PathVariable Long id) throws BotManagerException {
		BotMethodsDTO methods = botService.getMethods(id);
		return new ResponseEntity<>(methods, HttpStatus.OK);
	}

	@RequestMapping(
			method = RequestMethod.POST, produces = "application/json", consumes = "application/json", value = "/{id}/methods/process")
	public ResponseEntity<Object> callMethod(@PathVariable Long id, @RequestBody BotMethodDTO method) throws BotManagerException {
		Object result = botService.callMethod(id, method.getId(), method.getParams());
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@RequestMapping(
			method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE, value = "/icons/{gameId}")
	public void getImage(@PathVariable String gameId, HttpServletResponse response) throws BotManagerException {
		ClassPathResource imgFile = botService.getIcon(gameId);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		try {
			StreamUtils.copy(imgFile.getInputStream(), response.getOutputStream());
		} catch (IOException e) {
			throw new BotManagerException(e.getMessage());
		}
	}
}
