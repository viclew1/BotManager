package fr.lewon.bot.manager.modele.repo

import fr.lewon.bot.manager.modele.BotOperationEntity
import fr.lewon.bot.manager.modele.GameEntity
import fr.lewon.bot.runner.AbstractBotBuilder
import org.springframework.stereotype.Component

@Component
class GameRepository : HashMap<Long, GameEntity>() {

    fun addGame(name: String, abstractBotBuilder: AbstractBotBuilder): GameEntity {
        var cpt: Long = 1
        val ge = GameEntity(name, abstractBotBuilder, HashMap(),
                abstractBotBuilder.botOperations
                        .map { b -> BotOperationEntity(b, b.label) }
                        .map { cpt++ to it }.toMap())
        this[ge.id] = ge
        return ge
    }

}