package fr.lewon.bot.manager.service.impl

import fr.lewon.bot.manager.entities.*
import fr.lewon.bot.manager.mappers.BotOperationMapper
import fr.lewon.bot.manager.modele.repo.BotRepository
import fr.lewon.bot.manager.modele.repo.GameRepository
import fr.lewon.bot.manager.service.BotService
import fr.lewon.bot.manager.util.errors.*
import fr.lewon.bot.runner.bot.operation.OperationResult
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
    private lateinit var botOperationRunner: BotOperationRunner

    override fun createBot(login: String, password: String, gameId: Long, params: Map<String, String?>) {
        val game = gameRepository[gameId] ?: throw NoBotForThisGameException(gameId = gameId.toString())
        game.botsByLogin[login]?.let {
            throw AlreadyRunningBotException(it.game.name, login)
        }
        val bot = game.abstractBotBuilder.buildBot(login, password, params)
        botRepository.addBot(bot, login, game)
    }

    override fun stopBot(id: Long) {
        val bot = botRepository[id] ?: throw NoBotFoundException(id)
        bot.bot.stop()
    }

    override fun startBot(id: Long) {
        val bot = botRepository[id] ?: throw NoBotFoundException(id)
        bot.bot.start()
    }

    override fun getAllBotInfo(): GameInfoListDTO {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun trimStoppedBots() {
        botRepository.entries.removeIf { e -> e.value.bot.state == BotState.STOPPED }
    }

    override fun trimStoppedBot(id: Long) {
        val bot = botRepository[id] ?: throw NoBotFoundException(id)
        bot.bot.state.takeIf { it == BotState.STOPPED } ?: throw CantTrimBotException(id)
        botRepository.remove(id)
    }

    override fun getBotLogs(id: Long): BotLogsDTO {
        val bot = botRepository[id] ?: throw NoBotFoundException(id)
        return BotLogsDTO(bot.bot.logger.getLogs())
    }

    override fun getBotOperations(id: Long): BotOperationsDTO {
        val bot = botRepository[id] ?: throw NoBotFoundException(id)
        val botOperations = bot.game.botOperationsById.values.toList()
        return BotOperationsDTO(botOperationMapper.botOperationToDto(botOperations, bot.bot))
    }

    override fun callBotOperation(operationId: Long, botId: Long, params: Map<String, String?>): OperationResult {
        val bot = botRepository[botId] ?: throw NoBotFoundException(botId)
        val botOperation = bot.game.botOperationsById[operationId]
                ?: throw NoBotMethodForThisIdException(bot.game.name, operationId)
        return botOperationRunner.runOperation(botOperation.botOperation, bot.bot, params)
    }

    override fun getIcon(gameId: Long): ClassPathResource {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getGameProperties(gameId: Long): BotPropertiesDescriptorsDTO {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBotProperties(id: Long): BotPropertiesDescriptorsDTO {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBotInfo(id: Long): BotInfoDTO {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}