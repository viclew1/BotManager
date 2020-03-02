package fr.lewon.bot.manager.modele

import fr.lewon.bot.runner.AbstractBotBuilder
import org.springframework.core.io.ClassPathResource
import java.util.concurrent.atomic.AtomicLong

class GameEntity(
        val name: String,
        val abstractBotBuilder: AbstractBotBuilder,
        val botsByLogin: MutableMap<String, BotEntity> = HashMap(),
        val botOperationsById: Map<Long, BotOperationEntity> = emptyMap(),
        val iconClassPathResource: ClassPathResource
) {

    val id: Long = ID_GEN.incrementAndGet()

    companion object {
        var ID_GEN = AtomicLong()
    }
}