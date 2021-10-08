package fr.lewon.bot.manager

import com.leek.wars.bot.LWBotBuilder
import fr.lewon.bot.kinkoid.HHBotBuilder
import fr.lewon.bot.manager.modele.repo.GameRepository
import fr.lewon.bot.stocks.StocksBotBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["fr.lewon.bot.runner", "fr.lewon.bot.manager"])
class BotManagerApp : CommandLineRunner {

    @Autowired
    private lateinit var gameRepository: GameRepository

    override fun run(vararg args: String?) {
        gameRepository.addGame("Hentai Heroes", HHBotBuilder(), "image/hentaiheroes.jpg")
        gameRepository.addGame("Comix Harem", HHBotBuilder(), "image/comixharem.jpg")
        gameRepository.addGame("Leek Wars", LWBotBuilder(), "image/leekwars.png")
        gameRepository.addGame("Stonks", StocksBotBuilder(), "image/stocks.jpg")
    }

}

fun main(args: Array<String>) {
    runApplication<BotManagerApp>(*args)
}