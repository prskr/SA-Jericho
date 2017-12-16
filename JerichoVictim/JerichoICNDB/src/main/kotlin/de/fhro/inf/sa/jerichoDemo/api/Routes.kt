package de.fhro.inf.sa.jerichoDemo.api

import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router

/**
 * @author Peter Kurfer
 * Created on 12/5/17.
 */
fun Router.addRoutes(vertx: Vertx) {

	this.routeWithRegex(HttpMethod.GET, "^\\/api\\/v1\\/jokes\\/random$").handler( { ctx ->
		vertx.eventBus()?.send<String>("getRandomJoke", null, { msg ->
			val response = ctx.response()
			if(msg.succeeded()){
				msg.result().headers().forEach { response.putHeader(it.key, it.value) }
				response.end(msg.result().body())
			}
		})
	})

	this.routeWithRegex(HttpMethod.GET, "^\\/api\\/v1\\/jokes\\/([0-9]+)$").handler({ ctx ->
		val requestBody = JsonObject()
		requestBody.put("jokeId", ctx.pathParam("param0"))
		vertx.eventBus()?.send<String>("getJoke", requestBody, { msg ->
			val response = ctx.response()
			if(msg.succeeded()){
				msg.result().headers().forEach { response.putHeader(it.key, it.value) }
				response.end(msg.result().body())
			}
		})
	})

	this.routeWithRegex(HttpMethod.GET, "^\\/api\\/v1\\/jokes$").handler({ ctx ->
		val requestBody = JsonObject()
		ctx.queryParams().forEach { requestBody.put(it.key, it.value) }
		vertx.eventBus()?.send<String>("getJokes", requestBody, { msg ->
			val response = ctx.response()
			if(msg.succeeded()){
				msg.result().headers().forEach { response.putHeader(it.key, it.value) }
				response.end(msg.result().body())
			}
		})
	})
}