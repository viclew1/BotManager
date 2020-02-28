package fr.lewon.bot.manager.mappers

import fr.lewon.bot.manager.entities.GameInfoDTO
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [BotInfosMapper::class])
interface GameInfoMapper {
    fun gameInfosToDto(gameInfos: MutableList<GameInfos?>?): MutableList<GameInfoDTO?>?
    @Mapping(source = "runnerInfos", target = "botInfos")
    fun gameInfosToDto(gameInfos: GameInfos?): GameInfoDTO?
}