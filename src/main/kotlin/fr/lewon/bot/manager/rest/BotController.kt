package fr.lewon.bot.manager.rest

import fr.lewon.bot.manager.entities.*
import fr.lewon.bot.manager.service.BotService
import fr.lewon.bot.manager.util.errors.BotManagerException
import fr.lewon.bot.runner.bot.operation.OperationResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.StreamUtils
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@CrossOrigin
@RestController
@RequestMapping("/bots")
open class BotController {

    @Autowired
    private lateinit var botService: BotService

    @GetMapping(produces = ["application/json"], value = ["/all"])
    @Throws(BotManagerException::class)
    fun getAllBotInfo(): ResponseEntity<GameInfoListDTO> {
        val info = botService.getAllBotInfo()
        return ResponseEntity(info, HttpStatus.OK)
    }

    @GetMapping(produces = ["application/json"], value = ["/bot/{id}"])
    @Throws(BotManagerException::class)
    fun getBotInfo(@PathVariable id: Long): ResponseEntity<BotInfoDTO> {
        val info = botService.getBotInfo(id)
        return ResponseEntity(info, HttpStatus.OK)
    }

    @PostMapping(produces = ["application/json"], consumes = ["application/json"], value = ["/{gameId}/create"])
    @Throws(BotManagerException::class)
    fun createBot(@PathVariable gameId: Long, @RequestBody info: UserInfoDTO): ResponseEntity<BotInfoDTO> {
        var botInfo = botService.createBot(info.login, info.password, gameId, info.params)
        return ResponseEntity(botInfo, HttpStatus.OK)
    }

    @PostMapping(produces = ["application/json"], value = ["/bot/{id}/transition/{transition}"])
    @Throws(BotManagerException::class)
    fun processBotTransition(@PathVariable transition: String, @PathVariable id: Long): ResponseEntity<Nothing> {
        botService.processBotTransition(transition, id)
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping(produces = ["application/json"], value = ["/bot/{id}/trim"])
    @Throws(BotManagerException::class)
    fun trimStoppedBot(@PathVariable id: Long): ResponseEntity<Nothing> {
        botService.trimStoppedBot(id)
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping(produces = ["application/json"], value = ["/trim"])
    @Throws(BotManagerException::class)
    fun trimStoppedBots(): ResponseEntity<Nothing> {
        botService.trimStoppedBots()
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping(produces = ["application/json"], value = ["/bot/{id}/logs"])
    @Throws(BotManagerException::class)
    fun getLogs(@PathVariable id: Long): ResponseEntity<BotLogsDTO> {
        val logs = botService.getBotLogs(id)
        return ResponseEntity(logs, HttpStatus.OK)
    }

    @GetMapping(produces = ["application/json"], value = ["/bot/{botId}/operations"])
    @Throws(BotManagerException::class)
    fun getBotOperations(@PathVariable botId: Long): ResponseEntity<BotOperationsDTO> {
        val methods = botService.getBotOperations(botId)
        return ResponseEntity(methods, HttpStatus.OK)
    }

    @PostMapping(produces = ["application/json"], consumes = ["application/json"], value = ["/bot/{botId}/operation/call"])
    @Throws(BotManagerException::class)
    fun callBotOperation(@PathVariable botId: Long, @RequestBody callBotOperationDTO: CallBotOperationDTO): ResponseEntity<OperationResult> {
        val result = botService.callBotOperation(callBotOperationDTO.operationId, botId, callBotOperationDTO.params)
        return ResponseEntity(result, HttpStatus.OK)
    }

    @GetMapping(produces = ["application/json"], value = ["/{gameId}/properties"])
    @Throws(BotManagerException::class)
    fun getGameProperties(@PathVariable gameId: Long): ResponseEntity<BotPropertiesDescriptorsDTO> {
        val props = botService.getGameProperties(gameId)
        return ResponseEntity(props, HttpStatus.OK)
    }

    @GetMapping(produces = ["application/json"], value = ["/bot/{botId}/properties"])
    @Throws(BotManagerException::class)
    fun getBotProperties(@PathVariable botId: Long): ResponseEntity<Map<String, Any?>> {
        val properties = botService.getBotProperties(botId)
        return ResponseEntity(properties, HttpStatus.OK)
    }

    @PostMapping(produces = ["application/json"], value = ["/bot/{botId}/properties"])
    @Throws(BotManagerException::class)
    fun updateBotProperties(@PathVariable botId: Long, @RequestBody properties: Map<String, String?>): ResponseEntity<Map<String, Any?>> {
        val newProperties = botService.updateBotProperties(botId, properties)
        return ResponseEntity(newProperties, HttpStatus.OK)
    }

    @GetMapping(produces = [MediaType.IMAGE_JPEG_VALUE], value = ["/{gameId}/icons"])
    @Throws(BotManagerException::class)
    fun getImage(@PathVariable gameId: Long, response: HttpServletResponse) {
        val imgFile = botService.getIcon(gameId)
        response.contentType = MediaType.IMAGE_JPEG_VALUE
        StreamUtils.copy(imgFile.inputStream, response.outputStream)
    }
}