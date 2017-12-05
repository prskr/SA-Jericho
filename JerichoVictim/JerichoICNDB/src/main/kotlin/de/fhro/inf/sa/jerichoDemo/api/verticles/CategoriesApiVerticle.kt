package de.fhro.inf.sa.jerichoDemo.api.verticles

import de.fhro.inf.sa.jerichoDemo.api.ICategoriesApi
import de.fhro.inf.sa.jerichoDemo.api.error.ApiExceptions
import de.fhro.inf.sa.jerichoDemo.model.CategoryDto
import de.fhro.inf.sa.jerichoDemo.persistence.CategoriesJpaApi
import de.fhro.inf.sa.jerichoDemo.utilities.logUnexpectedError
import de.fhro.inf.sa.jerichoDemo.utilities.manageError
import io.vertx.core.AbstractVerticle
import io.vertx.core.Handler
import io.vertx.core.eventbus.Message
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory

/**
 * @author Peter Kurfer
 * Created on 10/29/17.
 */
class CategoriesApiVerticle : AbstractVerticle() {

	private val logger: Logger = LoggerFactory.getLogger(CategoriesApiVerticle::class.java)
	private val service: ICategoriesApi = CategoriesJpaApi()
	private val createCategoryServiceId = "createCategory"
	private val getCategoriesServiceId = "getCategories"

	override fun start() {
		vertx.eventBus().consumer<JsonObject>(createCategoryServiceId).handler(this::createCategory)
		vertx.eventBus().consumer<JsonObject>(getCategoriesServiceId).handler(this::getCategories)
	}

	private fun createCategory(message: Message<JsonObject>) {
		try {
			val categoryId = Json.mapper.readValue(message.body().getString("categoryId"), Int::class.java)
			val category = Json.mapper.readValue(message.body().getString("category"), CategoryDto::class.java)
			service.createCategory(categoryId, category, Handler { result ->
				if (result.succeeded()) {
					message.reply(JsonObject(Json.encode(result.result())).encode())
				} else {
					logger.manageError(message, result.cause(), "createCategory")
				}
			})
		} catch (e: Exception) {
			logger.logUnexpectedError("createCategory", e)
			message.fail(ApiExceptions.INTERNAL_SERVER_ERROR.statusCode, ApiExceptions.INTERNAL_SERVER_ERROR.statusMessage)
		}
	}

	private fun getCategories(message: Message<JsonObject>) {
		try {
			val pageIndex = Json.mapper.readValue(message.body().getString("page_index"), Int::class.java)
			val pageSize = Json.mapper.readValue(message.body().getString("page_size"), Int::class.java)

			service.getCategories(pageIndex, pageSize, Handler { result ->
				if (result.succeeded()) {
					message.reply(JsonObject(Json.encode(result.result())).encode())
				} else {
					logger.manageError(message, result.cause(), "getCategories")
				}
			})
		} catch (e: Exception) {
			logger.logUnexpectedError("getCategories", e)
			message.fail(ApiExceptions.INTERNAL_SERVER_ERROR.statusCode, ApiExceptions.INTERNAL_SERVER_ERROR.statusMessage)
		}
	}
}
