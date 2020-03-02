package fr.lewon.bot.manager

import fr.lewon.bot.hh.HHBotBuilder
import fr.lewon.bot.manager.modele.repo.GameRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = ["fr.lewon.bot.runner", "fr.lewon.bot.manager"])
open class BotManagerApp : CommandLineRunner {

    @Autowired
    private lateinit var gameRepository: GameRepository

    override fun run(vararg args: String?) {
        gameRepository.addGame("Hentai Heroes", HHBotBuilder(), "image/hentaiheroes.jpg")
        gameRepository.addGame("Leek Wars", HHBotBuilder(), "image/leekwars.png")
        gameRepository.addGame("SmutStone", HHBotBuilder(), "image/smutstone.jpg")
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(BotManagerApp::class.java, *args)
}