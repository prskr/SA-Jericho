package de.fhro.inf.sa.jerichoDemo.api.controllers

import de.fhro.inf.sa.jerichoDemo.api.error.ApiExceptions
import de.fhro.inf.sa.jerichoDemo.api.error.MainApiException
import de.fhro.inf.sa.jerichoDemo.model.JokeDto
import de.fhro.inf.sa.jerichoDemo.persistence.CQRSEndpoints
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.eventbus.EventBus
import io.vertx.core.eventbus.Message
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory

/**
 * @author Peter Kurfer
 * Created on 11/3/17.
 */
class JokesController {

	private val logger: Logger = LoggerFactory.getLogger(JokesController::class.java)

	var eventBus: EventBus? = null

	fun getJoke(message: Message<JsonObject>) {
		try {
			val jokeId = Json.mapper.readValue(message.body().getString("jokeId"), Int::class.java)

			eventBus?.send<JsonObject>(CQRSEndpoints.GET_JOKE_JPA_ID, jokeId, { result ->
				if (result.succeeded()) {
					message.reply(result.result().body(), DeliveryOptions().addHeader("Content-Type", "application/json"))
				} else {
					manageError(message, result.cause(), "getJoke")
				}
			})
		} catch (e: Exception) {
			logUnexpectedError("getJoke", e)
			message.fail(ApiExceptions.INTERNAL_SERVER_ERROR.statusCode, ApiExceptions.INTERNAL_SERVER_ERROR.statusMessage)
		}
	}

	fun getJokes(message: Message<JsonObject>) {
		try {
			eventBus?.send<JsonObject>(CQRSEndpoints.GET_JOKES_JPA_ID, message.body(), { result ->
				if (result.succeeded())
					message.reply(result.result().body(), DeliveryOptions().addHeader("Content-Type", "application/json"))
				else
					manageError(message, result.cause(), "getJokes")
			})
		} catch (e: Exception) {
			logUnexpectedError("getJokes", e)
			message.fail(ApiExceptions.INTERNAL_SERVER_ERROR.statusCode, ApiExceptions.INTERNAL_SERVER_ERROR.statusMessage)
		}
	}

	fun getRandomJoke(message: Message<JsonObject>) {
		try {
			eventBus?.send<JsonObject>(CQRSEndpoints.GET_RANDOM_JOKE_JPA_ID, null, { result ->
				if (result.succeeded()) {
					message.reply(result.result().body(), DeliveryOptions().addHeader("Content-Type", "application/json"))
				} else {
					manageError(message, result.cause(), "getRandomJoke")
				}
			})
		} catch (e: Exception) {
			logUnexpectedError("getRandomJoke", e)
			message.fail(ApiExceptions.INTERNAL_SERVER_ERROR.statusCode, ApiExceptions.INTERNAL_SERVER_ERROR.statusMessage)
		}
	}

	fun createJoke(message: Message<JsonObject>) {
		try {
			eventBus?.send<JsonObject>(CQRSEndpoints.CREATE_JOKE_JPA_ID, message, { result ->
				message.reply(null)
			})
		} catch (e: Exception) {
			logUnexpectedError("createJoke", e)
			message.fail(ApiExceptions.INTERNAL_SERVER_ERROR.statusCode, ApiExceptions.INTERNAL_SERVER_ERROR.statusMessage)
		}
	}

	fun updateJoke(message: Message<JsonObject>) {
		try {
			val jokeId = Json.mapper.readValue(message.body().getString("jokeId"), Int::class.java)
			val joke = Json.mapper.readValue(message.body().getString("joke"), JokeDto::class.java)
			eventBus?.send<JsonObject>(CQRSEndpoints.UPDATE_JOKE_JPA_ID, message, { result ->
				if(result.succeeded()) {
					message.reply(result.result().body())
				}else {
					val cause = result.cause()
					manageError(message, cause, "updateJoke")
				}
			})
		}catch (e: Exception) {
			logUnexpectedError("updateJoke", e)
			message.fail(ApiExceptions.INTERNAL_SERVER_ERROR.statusCode, ApiExceptions.INTERNAL_SERVER_ERROR.statusMessage)
		}
	}

	private fun manageError(message: Message<JsonObject>, cause: Throwable, serviceName: String) {
		var code = ApiExceptions.INTERNAL_SERVER_ERROR.statusCode
		var statusMessage = ApiExceptions.INTERNAL_SERVER_ERROR.statusMessage

		when (cause) {
			is MainApiException -> {
				code = cause.statusCode
				statusMessage = cause.statusMessage
			}
			else -> logUnexpectedError(serviceName, cause)
		}

		message.fail(code, statusMessage)
	}

	private fun logUnexpectedError(serviceName: String, cause: Throwable) {
		logger.error("Unexpected error in $serviceName", cause)
	}
}