package de.fhro.inf.sa.jerichoDemo.persistence

import de.fhro.inf.sa.jerichoDemo.model.JokeDto
import de.fhro.inf.sa.jerichoDemo.persisence.generated.tables.daos.CategoriesDao
import de.fhro.inf.sa.jerichoDemo.persisence.generated.tables.daos.JokesDao
import de.fhro.inf.sa.jerichoDemo.utilities.random
import io.github.jklingsporn.vertx.jooq.async.future.AsyncJooqSQLClient
import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import org.jooq.Configuration
import java.util.*

/**
 * @author Peter Kurfer
 * Created on 10/29/17.
 */
class JokesJpaApiVerticle(private val clientProducer: (Vertx) -> AsyncJooqSQLClient, configuration: Configuration) : AbstractVerticle() {

	private lateinit var client: AsyncJooqSQLClient
	private val jokeDao: JokesDao = JokesDao(configuration)
	private val categoryDao: CategoriesDao = CategoriesDao(configuration)

	override fun start() {
		client = clientProducer(vertx)
		jokeDao.setClient(client)
		categoryDao.setClient(client)

		/* GET_JOKE_JPA_ID */
		vertx.eventBus().consumer<Int>(CQRSEndpoints.GET_JOKE_JPA_ID).handler(this::handleGetJokeById)
		vertx.eventBus().consumer<Void>(CQRSEndpoints.GET_RANDOM_JOKE_JPA_ID).handler(this::handleGetRandomJoke)
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

	private fun getJoke(id: Int, successHandler: (JokeDto) -> Unit, errorHandler: () -> Unit) {
		jokeDao.existsByIdAsync(id).thenAcceptAsync { res ->
			if(res){
				jokeDao.fetchOneByIdAsync(id).thenAcceptAsync { jokes ->
					if(jokes.categoryid != null){
						categoryDao.fetchOneByIdAsync(jokes.categoryid).thenAcceptAsync { categories ->
							successHandler(JokeDto(jokes.id, jokes.joke, categories.name))
						}
					}else {
						successHandler(JokeDto(jokes.id, jokes.joke))
					}
				}
			}else {
				errorHandler()
			}
		}
	}
}