package fr.lewon.bot.manager.service

import fr.lewon.bot.manager.entities.*
import fr.lewon.bot.manager.util.errors.BotManagerException
import fr.lewon.bot.runner.bot.operation.OperationResult
import org.springframework.core.io.ClassPathResource

interface BotService {
    @Throws(BotManagerException::class)
    fun createBot(login: String, password: String, gameId: Long, params: Map<String, String?>): BotInfoDTO

    @Throws(BotManagerException::class)
    fun processBotTransition(transition: String, id: Long)

    @Throws(BotManagerException::class)
    fun getAllBotInfo(): GameInfoListDTO

    @Throws(BotManagerException::class)
    fun trimStoppedBots()

    @Throws(BotManagerException::class)
    fun trimStoppedBot(id: Long)

    @Throws(BotManagerException::class)
    fun getBotLogs(id: Long): BotLogsDTO

    @Throws(BotManagerException::class)
    fun getBotOperations(id: Long): BotOperationsDTO

    @Throws(BotManagerException::class)
    fun callBotOperation(operationId: Long, botId: Long, params: Map<String, String?>): OperationResult

    @Throws(BotManagerException::class)
    fun getIcon(gameId: Long): ClassPathResource

    @Throws(BotManagerException::class)
    fun getGameProperties(gameId: Long): BotPropertiesDescriptorsDTO

    @Throws(BotManagerException::class)
    fun getBotInfo(id: Long): BotInfoDTO
}