package de.fhro.inf.sa.jerichoDemo.api.verticles

import de.fhro.inf.sa.jerichoDemo.api.error.ApiExceptions
import de.fhro.inf.sa.jerichoDemo.api.error.MainApiException
import de.fhro.inf.sa.jerichoDemo.persistence.CQRSEndpoints
import io.vertx.core.AbstractVerticle
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

	private val logger: Logger

	private val createJokeServiceId = "createJoke"
	private val getJokeServiceId = "getJoke"
	private val getJokesServiceId = "getJokes"
	private val getRandomJokeServiceId = "getRandomJoke"
	private val updateJokeServiceId = "updateJoke"

	init {
		logger = LoggerFactory.getLogger(JokesApiVerticle::class.java)
	}

	override fun start() {
		/*vertx.eventBus().consumer<JsonObject>(createJokeServiceId).handler({ message ->
			try {
				val joke = Json.mapper.readValue<JokeDto>(message.body().getJsonObject("joke").encode(), JokeDto::class.java)
				service.createJoke(joke, Handler { result: AsyncResult<JokeDto> ->
					if (result.succeeded()) {
						message.reply(JsonObject(Json.encode(result.result())).encode())
					} else {
						val cause = result.cause()
						manageError(message, cause, createJokeServiceId)
					}
				})
			} catch (e: Exception) {
				logUnexpectedError(createJokeServiceId, e)
				message.fail(ApiExceptions.INTERNAL_SERVER_ERROR.statusCode, ApiExceptions.INTERNAL_SERVER_ERROR.statusMessage)
			}
		})*/

		vertx.eventBus().consumer<JsonObject>(getJokeServiceId).handler(this::handleGetJoke)

		vertx.eventBus().consumer<JsonObject>(getJokesServiceId).handler(this::handleGetJokes)

		vertx.eventBus().consumer<JsonObject>(getRandomJokeServiceId).handler(this::handleGetRandomJoke)

		/*vertx.eventBus().consumer<JsonObject>(updateJokeServiceId).handler { message ->
			try {
				val jokeId = Json.mapper.readValue(message.body().getString("jokeId"), Int::class.java)
				val joke = Json.mapper.readValue(message.body().getString("joke"), JokeDto::class.java)
				service.updateJoke(jokeId, joke, Handler { result ->
					if(result.succeeded()) {
						message.reply(JsonObject(Json.encode(result.result())).encode())
					}else {
						val cause = result.cause()
						manageError(message, cause, updateJokeServiceId)
					}
				})
			}catch (e: Exception) {
				logUnexpectedError(updateJokeServiceId, e)
				message.fail(ApiExceptions.INTERNAL_SERVER_ERROR.statusCode, ApiExceptions.INTERNAL_SERVER_ERROR.statusMessage)
			}
		}*/
	}

	private fun handleGetJoke(message: Message<JsonObject>) {
		try {
			val jokeId = Json.mapper.readValue(message.body().getString("jokeId"), Int::class.java)

			vertx.eventBus().send<JsonObject>(CQRSEndpoints.GET_JOKE_JPA_ID, jokeId, { result ->
				if (result.succeeded()) {
					message.reply(result.result().body())
				} else {
					manageError(message, result.cause(), getJokesServiceId)
				}
			})
		} catch (e: Exception) {
			logUnexpectedError(getJokeServiceId, e)
			message.fail(ApiExceptions.INTERNAL_SERVER_ERROR.statusCode, ApiExceptions.INTERNAL_SERVER_ERROR.statusMessage)
		}
	}

	private fun handleGetJokes(message: Message<JsonObject>) {
		try {
			vertx.eventBus().send<JsonObject>(CQRSEndpoints.GET_JOKES_JPA_ID, message.body(), { result ->
				if(result.succeeded())
					message.reply(result.result().body())
				else
					manageError(message, result.cause(), getJokesServiceId)
			})
		}catch (e: Exception) {
			logUnexpectedError(getJokesServiceId, e)
			message.fail(ApiExceptions.INTERNAL_SERVER_ERROR.statusCode, ApiExceptions.INTERNAL_SERVER_ERROR.statusMessage)
		}
	}

	private fun handleGetRandomJoke(message: Message<JsonObject>) {
		try {
			vertx.eventBus().send<JsonObject>(CQRSEndpoints.GET_RANDOM_JOKE_JPA_ID, null, { result ->
				if (result.succeeded()) {
					message.reply(result.result().body())
				} else {
					manageError(message, result.cause(), getRandomJokeServiceId)
				}
			})
		} catch (e: Exception) {
			logUnexpectedError(getRandomJokeServiceId, e)
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