package de.fhro.inf.sa.jerichoDemo.api.verticles

import com.google.inject.Inject
import de.fhro.inf.sa.jerichoDemo.api.controllers.JokesController
import io.vertx.core.AbstractVerticle
import io.vertx.core.json.JsonObject

/**
 * @author Peter Kurfer
 * Created on 10/29/17.
 */
class JokesApiVerticle @Inject constructor(private val jokesController: JokesController) : AbstractVerticle() {

	private val createJokeServiceId = "createJoke"
	private val getJokeServiceId = "getJoke"
	private val getJokesServiceId = "getJokes"
	private val getRandomJokeServiceId = "getRandomJoke"
	private val updateJokeServiceId = "updateJoke"

	override fun start() {
		jokesController.eventBus = vertx.eventBus()

		vertx.eventBus().consumer<JsonObject>(createJokeServiceId).handler(jokesController::createJoke)

		vertx.eventBus().consumer<JsonObject>(getJokeServiceId).handler(jokesController::getJoke)

		vertx.eventBus().consumer<JsonObject>(getJokesServiceId).handler(jokesController::getJokes)

		vertx.eventBus().consumer<JsonObject>(getRandomJokeServiceId).handler(jokesController::getRandomJoke)

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
}
