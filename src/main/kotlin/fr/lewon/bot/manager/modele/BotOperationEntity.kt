package fr.lewon.bot.manager.modele

import fr.lewon.bot.runner.bot.operation.BotOperation
import java.util.concurrent.atomic.AtomicLong

class BotOperationEntity(
        val botOperation: BotOperation,
        val label: String
) {
    val id: Long = ID_GEN.incrementAndGet()

    companion object {
        var ID_GEN = AtomicLong()
    }
}