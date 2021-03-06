package fr.lewon.bot.manager.mappers

import fr.lewon.bot.manager.entities.BotOperationDTO
import fr.lewon.bot.manager.entities.BotPropertyDescriptorDTO
import fr.lewon.bot.manager.modele.BotOperationEntity
import fr.lewon.bot.runner.Bot
import fr.lewon.bot.runner.bot.operation.BotOperation
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.factory.Mappers

@Mapper(componentModel = "spring", uses = [BotPropertyMapper::class])
abstract class BotOperationMapper {

    abstract fun botOperationsToDto(botOperations: List<BotOperationEntity>, @Context bot: Bot): List<BotOperationDTO>

    @Mapping(source = "botOperation", target = "params", qualifiedByName = ["fetchNeededParams"])
    abstract fun botOperationsToDto(botOperation: BotOperationEntity, @Context bot: Bot): BotOperationDTO

    @Named("fetchNeededParams")
    fun fetchNeededParams(botOperation: BotOperation, @Context bot: Bot): List<BotPropertyDescriptorDTO> {
        return Mappers.getMapper(BotPropertyMapper::class.java)
                .botPropertiesToDto(botOperation.getNeededProperties(bot))
    }

}