package fr.lewon.bot.manager.mappers

import fr.lewon.bot.manager.entities.GameInfoDTO
import fr.lewon.bot.manager.modele.BotEntity
import fr.lewon.bot.manager.modele.GameEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named

@Mapper(componentModel = "spring", uses = [BotInfoMapper::class])
abstract class GameInfoMapper {

    abstract fun gamesToDto(games: List<GameEntity>): MutableList<GameInfoDTO>

    @Mapping(source = "game", target = "botInfoList", qualifiedByName = ["fetchGameBots"])
    abstract fun gameToDto(game: GameEntity): GameInfoDTO

    @Named("fetchGameBots")
    fun fetchGameBots(game: GameEntity): List<BotEntity> {
        return game.botsByLogin.values.toList()
    }
}