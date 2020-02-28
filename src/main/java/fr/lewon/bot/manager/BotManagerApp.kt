package fr.lewon.bot.manager

import org.springframework.boot.SpringApplication

object BotManagerApp {
    @JvmStatic
    fun main(args: Array<String>) {
        SpringApplication.run(BotManagerApp::class.java, *args)
    }
}