package de.fhro.inf.sa.jerichoDemo.persistence

import de.fhro.inf.sa.jerichoDemo.api.error.ApiExceptions
import de.fhro.inf.sa.jerichoDemo.model.JokeDto
import de.fhro.inf.sa.jerichoDemo.model.JokesArrayDto
import de.fhro.inf.sa.jerichoDemo.persistence.generated.Tables.CATEGORIES
import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.daos.CategoriesDao
import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.daos.JokesDao
import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos.Categories
import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos.Jokes
import de.fhro.inf.sa.jerichoDemo.persistence.repositories.IJokesRepository
import de.fhro.inf.sa.jerichoDemo.persistence.repositories.impl.JokesRepository
import de.fhro.inf.sa.jerichoDemo.utilities.anyToInt
import de.fhro.inf.sa.jerichoDemo.utilities.random
import io.github.jklingsporn.vertx.jooq.async.future.AsyncJooqSQLClient
import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import org.jooq.Configuration
import org.jooq.impl.DSL

/**
 * @author Peter Kurfer
 * Created on 10/29/17.
 */
class JokesJpaApiVerticle(private val clientProducer: (Vertx) -> AsyncJooqSQLClient, private val configuration: Configuration) : AbstractVerticle() {

	private lateinit var client: AsyncJooqSQLClient
	private lateinit var jokesRepo: IJokesRepository
	private val jokeDao: JokesDao = JokesDao(configuration)
	private val categoryDao: CategoriesDao = CategoriesDao(configuration)


	override fun start() {
		client = clientProducer(vertx)
		jokeDao.setClient(client)
		categoryDao.setClient(client)

		jokesRepo = JokesRepository(jokeDao)

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
		jokeDao.countAsync().thenAcceptAsync { count ->
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
			jokeDao.insertExecAsync(Jokes(joke.id, joke.joke, null)).thenAcceptAsync {
				jokeDao.fetchByJokeAsync(listOf(joke.joke)).thenAcceptAsync { matchingJokes ->
					if (matchingJokes.isEmpty()) {
						message.fail(ApiExceptions.INTERNAL_SERVER_ERROR.statusCode, ApiExceptions.INTERNAL_SERVER_ERROR.statusMessage)
					} else {
						message.reply(JsonObject(Json.encode(matchingJokes[0])).encode())
					}
				}
			}
		} else {
			categoryDao.client().fetch(DSL.using(categoryDao.configuration())
					.select(CATEGORIES.ID)
					.from(CATEGORIES)
					.where(CATEGORIES.NAME.eq(joke.category))
					.limit(1)
					.query, { t -> t.getInteger("id") })
					.thenAcceptAsync({ ids ->
						if (ids.isEmpty()) {
							categoryDao.insertExecAsync(Categories(0, joke.category)).thenAcceptAsync {

							}
						} else {
							//TODO insert joke
						}
					})
		}
	}

	private fun getJoke(id: Int, successHandler: (JokeDto) -> Unit, errorHandler: () -> Unit) {
		jokeDao.existsByIdAsync(id).thenAcceptAsync { res ->
			if (res) {
				jokeDao.fetchOneByIdAsync(id).thenAcceptAsync { jokes ->
					if (jokes.categoryid != null) {
						categoryDao.fetchOneByIdAsync(jokes.categoryid).thenAcceptAsync { categories ->
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

