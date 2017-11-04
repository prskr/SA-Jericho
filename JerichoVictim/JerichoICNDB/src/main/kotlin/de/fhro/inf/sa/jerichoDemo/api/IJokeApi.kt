package de.fhro.inf.sa.jerichoDemo.api

import de.fhro.inf.sa.jerichoDemo.model.JokeDto
import de.fhro.inf.sa.jerichoDemo.model.JokesArrayDto
import io.vertx.core.AsyncResult
import io.vertx.core.Handler

/**
 * @author Peter Kurfer
 * Created on 10/29/17.
 */
interface IJokeApi {
	fun createJoke(joke: JokeDto, handler: Handler<AsyncResult<JokeDto>>)

	fun getJoke(jokeId: Int, handler: Handler<AsyncResult<JokeDto>>)

	fun getJokes(pageIndex: Int, pageSize: Int, handler: Handler<AsyncResult<JokesArrayDto>>)

	fun getRandomJoke(handler: Handler<AsyncResult<JokeDto>>)

	fun updateJoke(jokeId: Int, joke: JokeDto, handler: Handler<AsyncResult<JokeDto>>)
}