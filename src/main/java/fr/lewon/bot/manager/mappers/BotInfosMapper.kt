package fr.lewon.bot.manager.mappers

import fr.lewon.bot.manager.entities.BotInfoDTO
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface BotInfosMapper {
    fun runnerInfosToDto(runnerInfos: MutableList<RunnerInfos?>?): MutableList<BotInfoDTO?>?
    fun runnerInfosToDto(runnerInfos: RunnerInfos?): BotInfoDTO?
}