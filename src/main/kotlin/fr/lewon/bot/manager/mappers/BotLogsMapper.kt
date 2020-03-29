package fr.lewon.bot.manager.mappers

import fr.lewon.bot.manager.entities.BotLogDTO
import fr.lewon.bot.runner.bot.logs.Log
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface BotLogsMapper {

    fun logsToDTO(logs: List<Log>): List<BotLogDTO>

    fun logToDTO(log: Log): BotLogDTO

}