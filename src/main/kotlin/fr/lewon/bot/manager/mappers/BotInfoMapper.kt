package fr.lewon.bot.manager.mappers

import fr.lewon.bot.manager.entities.BotInfoDTO
import fr.lewon.bot.manager.modele.BotEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface BotInfoMapper {

    fun botsToDto(bots: List<BotEntity>): List<BotInfoDTO>

    @Mappings(
            Mapping(source = "bot.state", target = "state"),
            Mapping(source = "bot.game.id", target = "gameId"),
            Mapping(source = "bot.state.operations ", target = "availableTransitions")
    )
    fun botToDto(bot: BotEntity): BotInfoDTO

}