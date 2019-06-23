package fr.lewon.bot.manager.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import fr.lewon.bot.manager.entities.BotPropertyDescriptorDTO;
import fr.lewon.bot.props.BotPropertyDescriptor;

@Mapper(componentModel = "spring")
public interface BotPropertyMapper {

	List<BotPropertyDescriptorDTO> botPropertiesToDto(List<BotPropertyDescriptor> botMethod);

	BotPropertyDescriptorDTO botPropertyToDto(BotPropertyDescriptor botMethod);

}
