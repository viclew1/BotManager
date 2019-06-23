package fr.lewon.bot.manager.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.lewon.bot.manager.entities.GameInfosDTO;
import fr.lewon.bot.manager.modele.GameInfos;

@Mapper(
		componentModel = "spring",
		uses = { BotInfosMapper.class })
public interface GameInfoMapper {

	public List<GameInfosDTO> gameInfosToDto(List<GameInfos> gameInfos);

	@Mapping(source = "runnerInfos", target = "botInfos")
	public GameInfosDTO gameInfosToDto(GameInfos gameInfos);

}
