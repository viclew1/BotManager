package fr.lewon.bot.manager.mappers;

import fr.lewon.bot.manager.entities.BotMethodDTO;
import fr.lewon.bot.manager.entities.BotPropertyDescriptorDTO;
import fr.lewon.bot.methods.AbstractBotMethodProcessor;
import fr.lewon.bot.runner.BotRunner;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {BotPropertyMapper.class})
public interface BotMethodMapper {

    List<BotMethodDTO> botMethodToDto(List<? extends AbstractBotMethodProcessor> botMethod, @Context BotRunner runner);

    @Mapping(target = "params", source = "botMethod", qualifiedByName = "fetchNeededParams")
    BotMethodDTO botMethodToDto(AbstractBotMethodProcessor botMethod, @Context BotRunner runner);

    @Named("fetchNeededParams")
    default List<BotPropertyDescriptorDTO> fetchNeededParams(AbstractBotMethodProcessor botMethod, @Context BotRunner runner) {
        return Mappers.getMapper(BotPropertyMapper.class).botPropertiesToDto(botMethod.getNeededParameters(runner));
    }

}
