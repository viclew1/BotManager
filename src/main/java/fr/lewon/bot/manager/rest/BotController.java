package fr.lewon.bot.manager.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.lewon.bot.manager.entities.BotInfosDTO;
import fr.lewon.bot.manager.entities.BotLogsDTO;
import fr.lewon.bot.manager.entities.BotMethodsDTO;
import fr.lewon.bot.manager.entities.BotPropertiesDescriptorsDTO;
import fr.lewon.bot.manager.entities.CallMethodDTO;
import fr.lewon.bot.manager.entities.GameInfosListDTO;
import fr.lewon.bot.manager.entities.UserInfosDTO;
import fr.lewon.bot.manager.service.BotService;
import fr.lewon.bot.manager.util.errors.BotManagerException;

@CrossOrigin
@RestController
@RequestMapping("/bots")
public class BotController {

	@Autowired
	private BotService botService;

	@GetMapping(produces = "application/json", value = "/all")
	public ResponseEntity<GameInfosListDTO> getAllBotInfos() throws BotManagerException {
		GameInfosListDTO infos = botService.getAllBotInfos();
		return new ResponseEntity<>(infos, HttpStatus.OK);
	}

	@GetMapping(produces = "application/json", value = "/{gameId}/bot/{id}")
	public ResponseEntity<BotInfosDTO> getBotInfos(@PathVariable String gameId, @PathVariable Long id) throws BotManagerException {
		BotInfosDTO infos = botService.getBotInfos(gameId, id);
		return new ResponseEntity<>(infos, HttpStatus.OK);
	}
	
	@PostMapping(produces = "application/json", consumes = "application/json", value = "/{gameId}/create")
	public ResponseEntity<Void> createBot(@PathVariable String gameId, @RequestBody UserInfosDTO infos) throws BotManagerException {
		botService.createBot(infos.getLogin(), infos.getPassword(), gameId, infos.getParams());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping(produces = "application/json", value = "/{gameId}/bot/{id}/stop")
	public ResponseEntity<Void> stopBot(@PathVariable String gameId, @PathVariable Long id) throws BotManagerException {
		botService.stopBot(gameId, id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping(produces = "application/json", value = "/{gameId}/bot/{id}/start")
	public ResponseEntity<Void> startBot(@PathVariable String gameId, @PathVariable Long id) throws BotManagerException {
		botService.startBot(gameId, id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping(produces = "application/json", value = "/{gameId}/bot/{id}/pause")
	public ResponseEntity<Void> pauseBot(@PathVariable String gameId, @PathVariable Long id) throws BotManagerException {
		botService.pauseBot(gameId, id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping(produces = "application/json", value = "/{gameId}/bot/{id}/trim")
	public ResponseEntity<Void> trimStoppedBot(@PathVariable String gameId, @PathVariable Long id) throws BotManagerException {
		botService.trimStoppedBot(gameId, id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping(produces = "application/json", value = "/trim")
	public ResponseEntity<Void> trimStoppedBots() throws BotManagerException {
		botService.trimStoppedBots();
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping(produces = "application/json", value = "/{gameId}/bot/{id}/logs")
	public ResponseEntity<BotLogsDTO> getLogs(@PathVariable String gameId, @PathVariable Long id) throws BotManagerException {
		BotLogsDTO logs = botService.getBotLogs(gameId, id);
		return new ResponseEntity<>(logs, HttpStatus.OK);
	}

	@GetMapping(produces = "application/json", value = "/{gameId}/bot/{botId}/methods")
	public ResponseEntity<BotMethodsDTO> getMethods(@PathVariable String gameId, @PathVariable Long botId) throws BotManagerException {
		BotMethodsDTO methods = botService.getMethods(gameId, botId);
		return new ResponseEntity<>(methods, HttpStatus.OK);
	}

	@PostMapping(produces = "application/json", consumes = "application/json", value = "/{gameId}/bot/{botId}/methods/call")
	public ResponseEntity<Object> callMethod(@PathVariable String gameId, @PathVariable Long botId, @RequestBody CallMethodDTO callMethodDTO) throws BotManagerException {
		Object result = botService.callMethod(gameId, callMethodDTO.getMethodId(), botId, callMethodDTO.getParams());
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping(produces = "application/json", value = "/{gameId}/properties")
	public ResponseEntity<BotPropertiesDescriptorsDTO> getParams(@PathVariable String gameId) throws BotManagerException {
		BotPropertiesDescriptorsDTO props = botService.getProperties(gameId);
		return new ResponseEntity<>(props, HttpStatus.OK);
	}

	@GetMapping(produces = MediaType.IMAGE_JPEG_VALUE, value = "/{gameId}/icons")
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
