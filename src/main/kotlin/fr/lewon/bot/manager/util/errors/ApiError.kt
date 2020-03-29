package fr.lewon.bot.manager.util.errors

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class ApiError(
        ex: Throwable? = null,
        val status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
        @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss") val timestamp: LocalDateTime = LocalDateTime.now(),
        val message: String = "Unexpected error",
        val debugMessage: String = ex?.localizedMessage ?: ""
)