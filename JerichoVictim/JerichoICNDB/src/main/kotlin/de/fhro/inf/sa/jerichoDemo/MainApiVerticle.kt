package de.fhro.inf.sa.jerichoDemo

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import de.fhro.inf.sa.jerichoDemo.api.addRoutes
import de.fhro.inf.sa.jerichoDemo.api.verticles.CategoriesApiVerticle
import de.fhro.inf.sa.jerichoDemo.api.verticles.JokesApiVerticle
import de.fhro.inf.sa.jerichoDemo.di.RepoBinder
import de.fhro.inf.sa.jerichoDemo.model.JdbcConfig
import de.fhro.inf.sa.jerichoDemo.model.RuntimeConfig
import de.fhro.inf.sa.jerichoDemo.model.fromJson
import de.fhro.inf.sa.jerichoDemo.persistence.JokesJpaApiVerticle
import io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE
import io.vertx.config.ConfigRetriever
import io.vertx.core.*
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.CorsHandler
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.exception.LiquibaseException
import liquibase.resource.ClassLoaderResourceAccessor
import org.jooq.SQLDialect
import org.jooq.impl.DefaultConfiguration
import java.sql.DriverManager

/**
 * @author Peter Kurfer
 * Created on 10/29/17.
 */
class MainApiVerticle : AbstractVerticle() {

	private val logger: Logger = LoggerFactory.getLogger(MainApiVerticle::class.java)
	private val router: Router = Router.router(vertx)

	override fun start(startFuture: Future<Void>?) {

		router.route().handler(
				CorsHandler.create("*")
				.allowedMethods(setOf(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS, HttpMethod.PUT))
				.allowedHeaders(setOf(CONTENT_TYPE.toString()))
		)

		ConfigRetriever.create(vertx).getConfig { configResult ->
			if (configResult.failed()) {
				startFuture?.fail(configResult.cause())
			} else {
				val jsonConfig = configResult.result()
				val dbConfig = JdbcConfig().fromJson(jsonConfig)
				val runtimeConfig = RuntimeConfig().fromJson(jsonConfig)

				try {
					val liquibase = Liquibase(
							"database/db.changelog.yaml",
							ClassLoaderResourceAccessor(),
							JdbcConnection(DriverManager.getConnection("jdbc:postgresql://${dbConfig.hostname}:${dbConfig.port}/${dbConfig.dbName}", dbConfig.userName, dbConfig.password))
					)
					liquibase.update("production")
				} catch (e: LiquibaseException) {
					e.printStackTrace()
				}

				Json.mapper.registerModule(JavaTimeModule())

				router.addRoutes(vertx)

				deployVerticles(startFuture, dbConfig, runtimeConfig)

				vertx.createHttpServer()
						.requestHandler(router::accept)
						.listen(runtimeConfig.httpPort)

				startFuture?.complete()
			}
		}
	}

	private fun deployVerticles(startFuture: Future<Void>?, dbConfig: JdbcConfig, runtimeConfig: RuntimeConfig) {

		val jooqConfig = DefaultConfiguration()
		jooqConfig.set(SQLDialect.POSTGRES)

		val configJson = JsonObject(mapOf(
				"host" to dbConfig.hostname,
				"username" to dbConfig.userName,
				"password" to dbConfig.password,
				"database" to dbConfig.dbName,
				"guice_binder" to RepoBinder::class.java.name
		))

		println("Deploying ${runtimeConfig.verticlesCount} instance/-s of each verticle")
		val deploymentOptions = DeploymentOptions().setConfig(configJson)
		deploymentOptions.instances = runtimeConfig.verticlesCount
		listOf(JokesApiVerticle::class.java.name,
				CategoriesApiVerticle::class.java.name,
				JokesJpaApiVerticle::class.java.name)
				.forEach { className ->
					vertx.deployVerticle("java-guice:$className", deploymentOptions) { res ->
						if (res.succeeded()) {
							logger.info("$className: Deployed")
						} else {
							startFuture?.fail(res.cause())
						}
					}
				}
	}
}
