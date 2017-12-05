package de.fhro.inf.sa.jerichoDemo.api.verticles

import de.fhro.inf.sa.jerichoDemo.api.error.ApiExceptions
import de.fhro.inf.sa.jerichoDemo.persistence.CQRSEndpoints
import de.fhro.inf.sa.jerichoDemo.utilities.addContentTypeJson
import de.fhro.inf.sa.jerichoDemo.utilities.logUnexpectedError
import de.fhro.inf.sa.jerichoDemo.utilities.manageError
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.eventbus.Message
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory

/**
 * @author Peter Kurfer
 * Created on 10/29/17.
 */
class JokesApiVerticle : AbstractVerticle() {

	private val logger: Logger = LoggerFactory.getLogger(JokesApiVerticle::class.java)
	private val getJokeServiceId = "getJoke"
	private val getJokesServiceId = "getJokes"
	private val getRandomJokeServiceId = "getRandomJoke"

	override fun start() {
		vertx.eventBus().consumer<JsonObject>(getJokeServiceId).handler(this::getJoke)
		vertx.eventBus().consumer<JsonObject>(getJokesServiceId).handler(this::getJokes)
		vertx.eventBus().consumer<JsonObject>(getRandomJokeServiceId).handler(this::getRandomJoke)
	}

	private fun getJoke(message: Message<JsonObject>) {
		try {
			val jokeId = Json.mapper.readValue(message.body().getString("jokeId"), Int::class.java)

			vertx.eventBus().send<JsonObject>(CQRSEndpoints.GET_JOKE_JPA_ID, jokeId, { result ->
				if (result.succeeded()) {
					message.reply(result.result().body(), DeliveryOptions().addContentTypeJson())
				} else {
					logger.manageError(message, result.cause(), "getJoke")
				}
			})
		} catch (e: Exception) {
			logger.logUnexpectedError("getJoke", e)
			message.fail(ApiExceptions.INTERNAL_SERVER_ERROR.statusCode, ApiExceptions.INTERNAL_SERVER_ERROR.statusMessage)
		}
	}

	private fun getJokes(message: Message<JsonObject>) {
		try {
			vertx.eventBus().send<JsonObject>(CQRSEndpoints.GET_JOKES_JPA_ID, message.body(), { result ->
				if (result.succeeded())
					message.reply(result.result().body(), DeliveryOptions().addContentTypeJson())
				else
					logger.manageError(message, result.cause(), "getJokes")
			})
		} catch (e: Exception) {
			logger.logUnexpectedError("getJokes", e)
			message.fail(ApiExceptions.INTERNAL_SERVER_ERROR.statusCode, ApiExceptions.INTERNAL_SERVER_ERROR.statusMessage)
		}
	}

	private fun getRandomJoke(message: Message<JsonObject>) {
		try {
			vertx.eventBus().send<JsonObject>(CQRSEndpoints.GET_RANDOM_JOKE_JPA_ID, null, { result ->
				if (result.succeeded()) {
					message.reply(result.result().body(), DeliveryOptions().addContentTypeJson())
				} else {
					logger.manageError(message, result.cause(), "getRandomJoke")
				}
			})
		} catch (e: Exception) {
			logger.logUnexpectedError("getRandomJoke", e)
			message.fail(ApiExceptions.INTERNAL_SERVER_ERROR.statusCode, ApiExceptions.INTERNAL_SERVER_ERROR.statusMessage)
		}
	}
}