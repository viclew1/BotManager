package fr.lewon.bot.manager.util.errors

open class BotManagerException : Exception {
    constructor(message: String) : super(message) {}
    constructor(cause: Exception) : super(cause) {}
}