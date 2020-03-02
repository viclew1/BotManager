package fr.lewon.bot.manager.modele

import fr.lewon.bot.runner.Bot
import fr.lewon.bot.runner.bot.task.BotTask
import java.util.concurrent.atomic.AtomicLong

class BotTaskEntity(
        val botTask: BotTask,
        val bot: Bot
) {

    val id: Long = ID_GEN.incrementAndGet()

    companion object {
        var ID_GEN = AtomicLong()
    }
}