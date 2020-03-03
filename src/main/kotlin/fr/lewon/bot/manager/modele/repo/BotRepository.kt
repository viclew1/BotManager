package fr.lewon.bot.manager.modele.repo

import fr.lewon.bot.manager.modele.BotEntity
import fr.lewon.bot.manager.modele.BotTaskEntity
import fr.lewon.bot.manager.modele.GameEntity
import fr.lewon.bot.runner.Bot
import org.springframework.stereotype.Component

@Component
class BotRepository : HashMap<Long, BotEntity>() {

    fun addBot(bot: Bot, login: String, game: GameEntity): BotEntity {
        val botEntity = BotEntity(
                login,
                bot,
                game,
                bot.getTasks()
                        .map { t -> BotTaskEntity(t, bot, t.name) }
                        .map { it.id to it }
                        .toMap())
        this[botEntity.id] = botEntity
        game.botsByLogin[login] = botEntity
        return botEntity
    }

}