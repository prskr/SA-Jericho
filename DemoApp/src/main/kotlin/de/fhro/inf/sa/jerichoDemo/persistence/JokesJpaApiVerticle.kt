package de.fhro.inf.sa.jerichoDemo.persistence

import com.google.inject.Inject
import de.fhro.inf.sa.jerichoDemo.api.error.ApiExceptions
import de.fhro.inf.sa.jerichoDemo.model.JokeDto
import de.fhro.inf.sa.jerichoDemo.model.JokesArrayDto
import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.daos.CategoriesDao
import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.daos.JokesDao
import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos.Categories
import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos.Jokes
import de.fhro.inf.sa.jerichoDemo.persistence.repositories.ICategoriesRepository
import de.fhro.inf.sa.jerichoDemo.persistence.repositories.IJokesRepository
import de.fhro.inf.sa.jerichoDemo.persistence.repositories.impl.CategoriesRepository
import de.fhro.inf.sa.jerichoDemo.persistence.repositories.impl.JokesRepository
import de.fhro.inf.sa.jerichoDemo.utilities.anyToInt
import de.fhro.inf.sa.jerichoDemo.utilities.random
import io.github.jklingsporn.vertx.jooq.async.future.AsyncJooqSQLClient
import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.asyncsql.PostgreSQLClient
import org.jooq.Configuration

/**
 * @author Peter Kurfer
 * Created on 10/29/17.
 */
class JokesJpaApiVerticle @Inject constructor(private val jokesRepo : IJokesRepository, private val categoriesRepo: ICategoriesRepository) : AbstractVerticle() {

	private lateinit var client: AsyncJooqSQLClient


	override fun start() {
		client = AsyncJooqSQLClient.create(vertx, PostgreSQLClient.createNonShared(vertx, config()))
		jokesRepo.setClient(client)
		categoriesRepo.setClient(client)

		/* GET_JOKE_JPA_ID */
		vertx.eventBus().consumer<Int>(CQRSEndpoints.GET_JOKE_JPA_ID).handler(this::handleGetJokeById)
		vertx.eventBus().consumer<Void>(CQRSEndpoints.GET_RANDOM_JOKE_JPA_ID).handler(this::handleGetRandomJoke)
		vertx.eventBus().consumer<JsonObject>(CQRSEndpoints.GET_JOKES_JPA_ID).handler(this::handleGetJokes)
	}

	private fun handleGetJokes(message: Message<JsonObject>) {
		val pageSize = message.body().getValue("page_size", 20).anyToInt()
		val pageIndex = message.body().getValue("page_index", 0).anyToInt()
		jokesRepo.findAllIncludingCategory(pageIndex, pageSize).thenApplyAsync { list ->
			if (list.isEmpty()) message.fail(ApiExceptions.INTERNAL_SERVER_ERROR.statusCode, ApiExceptions.INTERNAL_SERVER_ERROR.statusMessage)
			message.reply(JsonObject(Json.encode(JokesArrayDto(list.size, list))).encode())
		}.whenComplete({ _, u ->
			if (u != null) {
				message.fail(ApiExceptions.INTERNAL_SERVER_ERROR.statusCode, u.message)
			}
		})
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
		jokesRepo.countAsync().thenAcceptAsync { count ->
			val randomId = (1..count.toInt()).random()
			getJoke(randomId, { joke ->
				message.reply(JsonObject(Json.encode(joke)).encode())
			}, {
				message.fail(404, "Joke does not exist.")
			})

		}
	}

	private fun handleCreateJoke(message: Message<JsonObject>) {
		val joke = Json.mapper.readValue<JokeDto>(message.body().getJsonObject("joke").encode(), JokeDto::class.java)
		if (joke.category == null) {
			jokesRepo.insertAsync(Jokes(joke.id, joke.joke, null)).thenAcceptAsync {
				/*jokeDao.fetchByJokeAsync(listOf(joke.joke)).thenAcceptAsync { matchingJokes ->
					if (matchingJokes.isEmpty()) {
						message.fail(ApiExceptions.INTERNAL_SERVER_ERROR.statusCode, ApiExceptions.INTERNAL_SERVER_ERROR.statusMessage)
					} else {
						message.reply(JsonObject(Json.encode(matchingJokes[0])).encode())
					}
				}*/
			}
		} else {
			categoriesRepo.exists(joke.category!!)
					.thenAcceptAsync({ exists ->
						if (!exists) {
							categoriesRepo.insertAsync(Categories(0, joke.category)).thenAcceptAsync {

							}
						} else {
							//TODO insert joke
						}
					})
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

