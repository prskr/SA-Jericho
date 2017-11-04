package de.fhro.inf.sa.jerichoDemo

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.github.phiz71.vertx.swagger.router.OperationIdServiceIdResolver
import com.github.phiz71.vertx.swagger.router.SwaggerRouter
import de.fhro.inf.sa.jerichoDemo.api.verticles.CategoriesApiVerticle
import de.fhro.inf.sa.jerichoDemo.api.verticles.JokesApiVerticle
import de.fhro.inf.sa.jerichoDemo.di.RepoBinder
import de.fhro.inf.sa.jerichoDemo.persistence.JokesJpaApiVerticle
import io.swagger.parser.SwaggerParser
import io.vertx.config.ConfigRetriever
import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Future
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.kotlin.config.ConfigRetrieverOptions
import io.vertx.kotlin.config.ConfigStoreOptions
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.exception.LiquibaseException
import liquibase.resource.ClassLoaderResourceAccessor
import org.jooq.SQLDialect
import org.jooq.impl.DefaultConfiguration
import java.nio.charset.Charset
import java.sql.DriverManager

/**
 * @author Peter Kurfer
 * Created on 10/29/17.
 */
class MainApiVerticle : AbstractVerticle() {

	private val logger: Logger = LoggerFactory.getLogger(MainApiVerticle::class.java)
	private val router: Router = Router.router(vertx)

	override fun start(startFuture: Future<Void>?) {

		try {
			val liquibase = Liquibase(
					"database/db.changelog.yaml",
					ClassLoaderResourceAccessor(),
					JdbcConnection(DriverManager.getConnection("jdbc:postgresql://database:5432/jericho", "jericho", "W@c[3~DV>~:]4%+5"))
			)
			liquibase.update("production")
		} catch (e: LiquibaseException) {
			e.printStackTrace()
		}

		val retrieverOptions = ConfigRetrieverOptions()
				.addStore(ConfigStoreOptions(JsonObject(mapOf(Pair("path", "conf/application-conf.json"))), type = "file"))
		val retriever = ConfigRetriever.create(vertx, retrieverOptions)

		retriever.getConfig({ configResult ->
			val port = configResult.result().getInteger("http.port", 8080)
			Json.mapper.registerModule(JavaTimeModule())
			val fileSystem = vertx.fileSystem()
			fileSystem.readFile("swagger.yml", { readFile ->
				if (readFile.succeeded()) {
					val swagger = SwaggerParser().parse(readFile.result().toString(Charset.forName("utf-8")))
					val swaggerRouter = SwaggerRouter.swaggerRouter(router, swagger, vertx.eventBus(), OperationIdServiceIdResolver())

					deployVerticles(startFuture, configResult.result())

					vertx.createHttpServer()
							.requestHandler(swaggerRouter::accept)
							.listen(port)
					startFuture?.complete()

				} else {
					startFuture?.fail(readFile.cause())
				}
			})
		})
	}

	private fun deployVerticles(startFuture: Future<Void>?, config: JsonObject) {

		val jooqConfig = DefaultConfiguration()
		jooqConfig.set(SQLDialect.POSTGRES)

		val configJson = JsonObject(mapOf(
				Pair("host", config.getString("jdbc.hostname", "localhost")),
				Pair("username", config.getString("jdbc.user", "jericho")),
				Pair("password", config.getString("jdbc.password", null)),
				Pair("database", "jericho"),
				Pair("guice_binder", RepoBinder::class.java.name)
		))

		val deploymentOptions = DeploymentOptions().setConfig(configJson)
		listOf(JokesApiVerticle::class.java.name,
				CategoriesApiVerticle::class.java.name,
				JokesJpaApiVerticle::class.java.name)
				.forEach { className ->
					vertx.deployVerticle("java-guice:$className", deploymentOptions, { res ->
						if (res.succeeded()) {
							logger.info("$className: Deployed")
						} else {
							startFuture?.fail(res.cause())
						}
					})
				}
	}
}
