package fr.lewon.bot.manager.mappers

import fr.lewon.bot.manager.entities.BotPropertyDescriptorDTO
import fr.lewon.bot.runner.bot.props.BotPropertyDescriptor
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface BotPropertyMapper {

    fun botPropertiesToDto(botMethod: List<BotPropertyDescriptor>): List<BotPropertyDescriptorDTO>
    fun botPropertyToDto(botMethod: BotPropertyDescriptor): BotPropertyDescriptorDTO

}