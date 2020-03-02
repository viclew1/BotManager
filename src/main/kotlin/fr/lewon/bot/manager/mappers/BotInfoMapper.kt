package fr.lewon.bot.manager.mappers

import fr.lewon.bot.manager.entities.BotInfoDTO
import fr.lewon.bot.manager.modele.BotEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface BotInfoMapper {

    fun botsToDto(bots: List<BotEntity>): List<BotInfoDTO>

    @Mapping(source = "bot.state", target = "state")
    fun botToDto(bot: BotEntity): BotInfoDTO

}