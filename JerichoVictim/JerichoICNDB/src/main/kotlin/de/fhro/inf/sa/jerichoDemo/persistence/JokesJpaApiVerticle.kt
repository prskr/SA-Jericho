package de.fhro.inf.sa.jerichoDemo.persistence

import com.google.inject.Inject
import de.fhro.inf.sa.jerichoDemo.api.error.ApiExceptions
import de.fhro.inf.sa.jerichoDemo.model.JokeDto
import de.fhro.inf.sa.jerichoDemo.model.JokesArrayDto
import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos.Categories
import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos.Jokes
import de.fhro.inf.sa.jerichoDemo.persistence.repositories.ICategoriesRepository
import de.fhro.inf.sa.jerichoDemo.persistence.repositories.IJokesRepository
import de.fhro.inf.sa.jerichoDemo.utilities.anyToInt
import io.github.jklingsporn.vertx.jooq.async.future.AsyncJooqSQLClient
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.Message
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.asyncsql.AsyncSQLClient
import io.vertx.ext.asyncsql.PostgreSQLClient

/**
 * @author Peter Kurfer
 * Created on 10/29/17.
 */
class JokesJpaApiVerticle @Inject constructor(private val jokesRepo: IJokesRepository, private val categoriesRepo: ICategoriesRepository) : AbstractVerticle() {

	private lateinit var client: AsyncJooqSQLClient
	private lateinit var postgresClient: AsyncSQLClient

	override fun start() {
		postgresClient = PostgreSQLClient.createShared(vertx, config())
		client = AsyncJooqSQLClient.create(vertx, postgresClient)
		jokesRepo.setClient(client)
		categoriesRepo.setClient(client)

		/* GET_JOKE_JPA_ID */
		vertx.eventBus().consumer<Int>(CQRSEndpoints.GET_JOKE_JPA_ID).handler(this::handleGetJokeById)
		vertx.eventBus().consumer<Void>(CQRSEndpoints.GET_RANDOM_JOKE_JPA_ID).handler(this::handleGetRandomJoke)
		vertx.eventBus().consumer<JsonObject>(CQRSEndpoints.GET_JOKES_JPA_ID).handler(this::handleGetJokes)
	}

	override fun stop() {
		postgresClient.close()
	}

	private fun handleGetJokes(message: Message<JsonObject>) {
		val pageSize = message.body().getValue("page_size", 20).anyToInt()
		val pageIndex = message.body().getValue("page_index", 0).anyToInt()
		jokesRepo.findAllIncludingCategory(pageIndex, pageSize).thenApplyAsync { list ->
			if (list.isEmpty()) message.fail(ApiExceptions.INTERNAL_SERVER_ERROR.statusCode, ApiExceptions.INTERNAL_SERVER_ERROR.statusMessage)
			message.reply(JsonObject(Json.encode(JokesArrayDto(list.size, list))).encode())
		}.whenComplete { _, u ->
			if (u != null) {
				message.fail(ApiExceptions.INTERNAL_SERVER_ERROR.statusCode, u.message)
			}
		}
	}

	private fun handleGetJokeById(message: Message<Int>) {
		val jokeId = message.body()
		getJoke(jokeId, { joke ->
			message.reply(JsonObject(Json.encode(joke)).encode())
		}, {
			message.fail(404, "Joke does not exist.")
		})
	}

	private fun handleGetRandomJoke(message: Message<Void>) {
		jokesRepo.getRandomJoke().thenAcceptAsync { joke ->
			message.reply(JsonObject(Json.encode(joke)).encode())
		}
	}

	private fun getJoke(id: Int, successHandler: (JokeDto) -> Unit, errorHandler: () -> Unit) {
		jokesRepo.existsAsync(id).thenAcceptAsync { res ->
			if (res) {
				jokesRepo.findAsync(id).thenAcceptAsync { jokes ->
					if (jokes.categoryid != null) {
						categoriesRepo.findAsync(jokes.categoryid).thenAcceptAsync { categories ->
							successHandler(JokeDto(jokes.id, jokes.joke, categories.name))
						}
					} else {
						successHandler(JokeDto(jokes.id, jokes.joke))
					}
				}
			} else {
				errorHandler()
			}
		}
	}


}

