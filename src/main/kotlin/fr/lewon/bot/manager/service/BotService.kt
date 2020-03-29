package fr.lewon.bot.manager.service

import fr.lewon.bot.manager.entities.*
import org.springframework.core.io.ClassPathResource

interface BotService {

    fun createBot(login: String, password: String, gameId: Long, params: Map<String, String?>): BotInfoDTO

    fun processBotTransition(transition: String, id: Long)

    fun getAllBotInfo(): GameInfoListDTO

    fun trimStoppedBots()

    fun trimStoppedBot(id: Long)

    fun getBotLogs(id: Long): List<BotLogDTO>

    fun getBotOperations(id: Long): BotOperationsDTO

    fun callBotOperation(operationId: Long, botId: Long, params: Map<String, String?>): OperationResultDTO

    fun getIcon(gameId: Long): ClassPathResource

    fun getGameProperties(gameId: Long): BotPropertiesDescriptorsDTO

    fun getBotProperties(botId: Long): Map<String, Any?>

    fun updateBotProperties(botId: Long, properties: Map<String, String?>): Map<String, Any?>

    fun getBotInfo(id: Long): BotInfoDTO

    fun getBotTasks(botId: Long): BotTasksDTO
}