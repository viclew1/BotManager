package fr.lewon.bot.manager.mappers

import fr.lewon.bot.manager.entities.OperationResultDTO
import fr.lewon.bot.runner.bot.operation.OperationResult
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface OperationResultMapper {

    fun operationToDTO(operation: OperationResult): OperationResultDTO

}