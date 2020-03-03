package fr.lewon.bot.manager.modele.repo

import fr.lewon.bot.manager.modele.BotOperationEntity
import fr.lewon.bot.manager.modele.GameEntity
import fr.lewon.bot.runner.AbstractBotBuilder
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class GameRepository : HashMap<Long, GameEntity>() {

    fun addGame(name: String, abstractBotBuilder: AbstractBotBuilder, iconPath: String): GameEntity {
        val ge = GameEntity(
                name,
                abstractBotBuilder,
                HashMap(),
                abstractBotBuilder.botOperations
                        .map { b -> BotOperationEntity(b, b.label) }
                        .map { it.id to it }
                        .toMap(),
                ClassPathResource(iconPath))
        this[ge.id] = ge
        return ge
    }

}