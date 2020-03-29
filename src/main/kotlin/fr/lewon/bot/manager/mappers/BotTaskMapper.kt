package fr.lewon.bot.manager.mappers

import fr.lewon.bot.manager.entities.BotTaskDTO
import fr.lewon.bot.runner.bot.task.BotTask
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface BotTaskMapper {

    fun tasksToDto(tasks: Collection<BotTask>): List<BotTaskDTO>

    @Mappings(
            Mapping(target = "state", source = "state"),
            Mapping(target = "executionTime", source = "executionDate.time"))
    fun taskToDto(task: BotTask): BotTaskDTO

}