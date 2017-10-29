package de.fhro.inf.sa.jerichoDemo.persisence

import de.fhro.inf.sa.jerichoDemo.api.IJokeApi
import de.fhro.inf.sa.jerichoDemo.model.JokeDto
import de.fhro.inf.sa.jerichoDemo.model.JokesArrayDto
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler

/**
 * @author Peter Kurfer
 * Created on 10/29/17.
 */
class JokesJpaApi : IJokeApi {
	override fun getJoke(jokeId: Int, handler: Handler<AsyncResult<JokeDto>>) {
		handler.handle(Future.succeededFuture(JokeDto(1, "deine mama")))
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getJokes(pageIndex: Int, pageSize: Int, handler: Handler<AsyncResult<JokesArrayDto>>) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getRandomJoke(handler: Handler<AsyncResult<JokeDto>>) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun updateJoke(jokeId: Int, joke: JokeDto, handler: Handler<AsyncResult<JokeDto>>) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun createJoke(joke: JokeDto, handler: Handler<AsyncResult<JokeDto>>) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}