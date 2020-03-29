package fr.lewon.bot.manager.modele

import fr.lewon.bot.runner.Bot
import java.util.concurrent.atomic.AtomicLong

class BotEntity(
        val login: String,
        val bot: Bot,
        val game: GameEntity
) {
    val id: Long = ID_GEN.incrementAndGet()

    companion object {
        var ID_GEN = AtomicLong()
    }
}