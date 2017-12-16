package de.fhro.inf.sa.jerichoDemo.utilities

import de.fhro.inf.sa.jerichoDemo.api.error.ApiExceptions
import de.fhro.inf.sa.jerichoDemo.api.error.MainApiException
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.Logger

/**
 * @author Peter Kurfer
 * Created on 12/5/17.
 */
fun Logger.logUnexpectedError(serviceName: String, cause: Throwable) = this.error("Unexpected error in $serviceName", cause)

fun Logger.manageError(message: Message<JsonObject>, cause: Throwable, serviceName: String) {
	var code = ApiExceptions.INTERNAL_SERVER_ERROR.statusCode
	var statusMessage = ApiExceptions.INTERNAL_SERVER_ERROR.statusMessage

	when (cause) {
		is MainApiException -> {
			code = cause.statusCode
			statusMessage = cause.statusMessage
		}
		else -> this.logUnexpectedError(serviceName, cause)
	}

	message.fail(code, statusMessage)
}