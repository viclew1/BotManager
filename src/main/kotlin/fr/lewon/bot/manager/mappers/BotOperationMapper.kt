package fr.lewon.bot.manager.mappers

import fr.lewon.bot.manager.entities.BotOperationDTO
import fr.lewon.bot.manager.modele.BotOperationEntity
import fr.lewon.bot.runner.Bot
import fr.lewon.bot.runner.bot.props.BotPropertyDescriptor
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named

@Mapper(componentModel = "spring", uses = [BotPropertyMapper::class])
abstract class BotOperationMapper {

    abstract fun botOperationsToDto(botOperations: List<BotOperationEntity>, @Context bot: Bot): List<BotOperationDTO>

    @Mapping(target = "params", qualifiedByName = ["fetchNeededParams"])
    abstract fun botOperationsToDto(botOperation: BotOperationEntity, @Context bot: Bot): BotOperationDTO

    @Named("fetchNeededParams")
    fun fetchNeededParams(botOperation: BotOperationEntity, @Context bot: Bot): List<BotPropertyDescriptor> {
        return botOperation.botOperation.getNeededProperties(bot)
    }

}