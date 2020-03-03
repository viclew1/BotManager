package fr.lewon.bot.manager.mappers

import fr.lewon.bot.manager.entities.BotTaskDTO
import fr.lewon.bot.manager.modele.BotTaskEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface BotTaskMapper {

    fun tasksToDto(tasks: List<BotTaskEntity>): List<BotTaskDTO>

    @Mapping(target = "state", source = "botTask.state")
    fun taskToDto(task: BotTaskEntity): BotTaskDTO

}