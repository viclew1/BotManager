package fr.lewon.bot.manager.service.impl

import fr.lewon.bot.manager.entities.*
import fr.lewon.bot.manager.mappers.*
import fr.lewon.bot.manager.modele.repo.BotRepository
import fr.lewon.bot.manager.modele.repo.GameRepository
import fr.lewon.bot.manager.service.BotService
import fr.lewon.bot.manager.util.errors.*
import fr.lewon.bot.runner.bot.operation.def.ReadBotPropertiesOperation
import fr.lewon.bot.runner.bot.operation.def.UpdateBotPropertiesOperation
import fr.lewon.bot.runner.errors.BotOperationExecutionException
import fr.lewon.bot.runner.lifecycle.bot.BotLifeCycleOperation
import fr.lewon.bot.runner.lifecycle.bot.BotState
import fr.lewon.bot.runner.util.BotOperationRunner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
class BotServiceImpl : BotService {

    @Autowired
    private lateinit var gameRepository: GameRepository

    @Autowired
    private lateinit var botRepository: BotRepository

    @Autowired
    private lateinit var botOperationMapper: BotOperationMapper

    @Autowired
    private lateinit var botPropertyMapper: BotPropertyMapper

    @Autowired
    private lateinit var gameInfoMapper: GameInfoMapper

    @Autowired
    private lateinit var botInfoMapper: BotInfoMapper

    @Autowired
    private lateinit var operationResultMapper: OperationResultMapper

    @Autowired
    private lateinit var botLogsMapper: BotLogsMapper

    @Autowired
    private lateinit var botTaskMapper: BotTaskMapper

    override fun createBot(
        login: String,
        loginProperties: Map<String, String>,
        gameId: Long,
        params: Map<String, String?>
    ): BotInfoDTO {
        val game = gameRepository[gameId] ?: throw NoBotForThisGameException(gameId)
        game.botsByLogin[login]?.let {
            throw AlreadyRunningBotException(it.game.name, login)
        }
        val bot = game.abstractBotBuilder.buildBot(login, loginProperties, params)
        val createdEntity = botRepository.addBot(bot, login, game)
        return botInfoMapper.botToDto(createdEntity)
    }

    override fun processBotTransition(transition: String, id: Long) {
        val bot = botRepository[id] ?: throw NoBotFoundException(id)
        BotLifeCycleOperation.values()
            .firstOrNull { it.name.equals(transition, true) }
            ?.run(bot.bot)
            ?: throw InvalidTransitionNameException(transition)
    }

    override fun getAllBotInfo(): GameInfoListDTO {
        return GameInfoListDTO(gameInfoMapper.gamesToDto(gameRepository.values.toList()))
    }

    override fun trimStoppedBots() {
        val toTrim = botRepository.values
            .filter { b -> b.bot.state == BotState.STOPPED }
            .toList()
        botRepository.values.removeAll(toTrim)
        gameRepository.values.forEach { g ->
            g.botsByLogin.values.removeAll(toTrim)
        }
    }

    override fun trimStoppedBot(id: Long) {
        val bot = botRepository[id] ?: throw NoBotFoundException(id)
        bot.bot.state.takeIf { it == BotState.STOPPED } ?: throw CantTrimBotException(id)
        botRepository.remove(id)
    }

    override fun getBotLogs(id: Long): List<BotLogDTO> {
        val bot = botRepository[id] ?: throw NoBotFoundException(id)
        return botLogsMapper.logsToDTO(bot.bot.logger.getLogs())
    }

    override fun getBotOperations(id: Long): BotOperationsDTO {
        val bot = botRepository[id] ?: throw NoBotFoundException(id)
        val botOperations = bot.game.botOperationsById.values.toList()
        return BotOperationsDTO(botOperationMapper.botOperationsToDto(botOperations, bot.bot))
    }

    override fun callBotOperation(operationId: Long, botId: Long, params: Map<String, String?>): OperationResultDTO {
        val bot = botRepository[botId] ?: throw NoBotFoundException(botId)
        val botOperation = bot.game.botOperationsById[operationId]
            ?: throw NoBotMethodForThisIdException(bot.game.name, operationId)
        return operationResultMapper.operationToDTO(
            BotOperationRunner.runOperation(botOperation.botOperation, bot.bot, params)
        )
    }

    override fun getIcon(gameId: Long): ClassPathResource {
        return gameRepository[gameId]?.iconClassPathResource ?: throw NoBotForThisGameException(gameId)
    }

    override fun getGameProperties(gameId: Long): BotPropertiesDescriptorsDTO {
        val game = gameRepository[gameId] ?: throw NoBotForThisGameException(gameId)
        return BotPropertiesDescriptorsDTO(botPropertyMapper.botPropertiesToDto(game.abstractBotBuilder.botPropertyDescriptors))
    }

    override fun getLoginProperties(gameId: Long): BotPropertiesDescriptorsDTO {
        val game = gameRepository[gameId] ?: throw NoBotForThisGameException(gameId)
        return BotPropertiesDescriptorsDTO(botPropertyMapper.botPropertiesToDto(game.abstractBotBuilder.expectedLoginProperties))
    }

    @Suppress("UNCHECKED_CAST")
    override fun getBotProperties(botId: Long): Map<String, Any?> {
        val bot = botRepository[botId] ?: throw NoBotFoundException(botId)
        val readOperation = ReadBotPropertiesOperation()
        val operationResult = BotOperationRunner.runOperation(readOperation, bot.bot, HashMap())
        if (!operationResult.isSuccess) {
            throw BotOperationExecutionException(readOperation, operationResult.message)
        }
        return operationResult.content as Map<String, Any?>
    }

    @Suppress("UNCHECKED_CAST")
    override fun updateBotProperties(botId: Long, properties: Map<String, String?>): Map<String, Any?> {
        val bot = botRepository[botId] ?: throw NoBotFoundException(botId)
        val updateOperation = UpdateBotPropertiesOperation()
        val operationResult = BotOperationRunner.runOperation(updateOperation, bot.bot, properties)
        if (!operationResult.isSuccess) {
            throw BotOperationExecutionException(updateOperation, operationResult.message)
        }
        return operationResult.content as Map<String, Any?>
    }

    override fun getBotInfo(id: Long): BotInfoDTO {
        return botInfoMapper.botToDto(botRepository[id] ?: throw NoBotFoundException(id))
    }

    override fun getBotTasks(botId: Long): BotTasksDTO {
        val bot = botRepository[botId] ?: throw NoBotFoundException(botId)
        return BotTasksDTO(System.currentTimeMillis(), botTaskMapper.tasksToDto(bot.bot.tasks))
    }

}