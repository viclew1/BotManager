package fr.lewon.bot.manager.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import fr.lewon.bot.manager.entities.BotInfosDTO;
import fr.lewon.bot.manager.modele.RunnerInfos;

@Mapper(componentModel = "spring")
public interface BotInfosMapper {
	
	public List<BotInfosDTO> runnerInfosToDto(List<RunnerInfos> runnerInfos);

	public BotInfosDTO runnerInfosToDto(RunnerInfos runnerInfos);
	
}
