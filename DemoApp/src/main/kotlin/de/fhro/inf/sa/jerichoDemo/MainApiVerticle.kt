package de.fhro.inf.sa.jerichoDemo

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.github.phiz71.vertx.swagger.router.OperationIdServiceIdResolver
import com.github.phiz71.vertx.swagger.router.SwaggerRouter
import de.fhro.inf.sa.jerichoDemo.api.verticles.CategoriesApiVerticle
import de.fhro.inf.sa.jerichoDemo.api.verticles.JokesApiVerticle
import io.swagger.parser.SwaggerParser
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.json.Json
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import java.nio.charset.Charset

/**
 * @author Peter Kurfer
 * Created on 10/29/17.
 */
class MainApiVerticle : AbstractVerticle() {

	private val logger: Logger = LoggerFactory.getLogger(MainApiVerticle::class.java)
	private val router: Router = Router.router(vertx)

	override fun start(startFuture: Future<Void>?) {
		Json.mapper.registerModule(JavaTimeModule())
		val fileSystem = vertx.fileSystem()
		fileSystem.readFile("swagger.yml", { readFile ->
			if (readFile.succeeded()) {
				val swagger = SwaggerParser().parse(readFile.result().toString(Charset.forName("utf-8")))
				val swaggerRouter = SwaggerRouter.swaggerRouter(router, swagger, vertx.eventBus(), OperationIdServiceIdResolver())

				deployVerticles(startFuture)

				vertx.createHttpServer()
						.requestHandler(swaggerRouter::accept)
						.listen(8080)
				startFuture?.complete()

			} else {
				startFuture?.fail(readFile.cause())
			}
		})
	}

	private fun deployVerticles(startFuture: Future<Void>?) {
		vertx.deployVerticle(JokesApiVerticle(), { res ->
			if (res.succeeded()) {
				logger.info("JokeApiVerticle: Deployed")
			} else {
				startFuture?.fail(res.cause())
			}
		})

		vertx.deployVerticle(CategoriesApiVerticle(), { res ->
			if (res.succeeded())
				logger.info("CategoriesApiVerticle: Deployed")
			else {
				startFuture?.fail(res.cause())
				logger.error("CategoriesApiVerticle: Deployment failed")
			}
		})
	}
}