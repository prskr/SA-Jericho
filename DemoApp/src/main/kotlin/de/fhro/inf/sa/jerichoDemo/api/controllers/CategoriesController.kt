package de.fhro.inf.sa.jerichoDemo.api.controllers

import de.fhro.inf.sa.jerichoDemo.api.ICategoriesApi
import de.fhro.inf.sa.jerichoDemo.api.error.ApiExceptions
import de.fhro.inf.sa.jerichoDemo.api.error.MainApiException
import de.fhro.inf.sa.jerichoDemo.model.CategoryDto
import de.fhro.inf.sa.jerichoDemo.persistence.CategoriesJpaApi
import io.vertx.core.Handler
import io.vertx.core.eventbus.EventBus
import io.vertx.core.eventbus.Message
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory

/**
 * @author Peter Kurfer
 * Created on 11/3/17.
 */
class CategoriesController {

	private val logger: Logger = LoggerFactory.getLogger(CategoriesController::class.java)
	private val service: ICategoriesApi = CategoriesJpaApi()

	var eventBus: EventBus? = null

	fun createCategory(message: Message<JsonObject>) {
		try {
			val categoryId = Json.mapper.readValue(message.body().getString("categoryId"), Int::class.java)
			val category = Json.mapper.readValue(message.body().getString("category"), CategoryDto::class.java)
			service.createCategory(categoryId, category, Handler { result ->
				if (result.succeeded()) {
					message.reply(JsonObject(Json.encode(result.result())).encode())
				} else {
					manageError(message, result.cause(), "createCategory")
				}
			})
		} catch (e: Exception) {
			logUnexpectedError("createCategory", e)
			message.fail(ApiExceptions.INTERNAL_SERVER_ERROR.statusCode, ApiExceptions.INTERNAL_SERVER_ERROR.statusMessage)
		}
	}

	fun getCategories(message: Message<JsonObject>) {
		try {
			val pageIndex = Json.mapper.readValue(message.body().getString("page_index"), Int::class.java)
			val pageSize = Json.mapper.readValue(message.body().getString("page_size"), Int::class.java)

			service.getCategories(pageIndex, pageSize, Handler { result ->
				if (result.succeeded()) {
					message.reply(JsonObject(Json.encode(result.result())).encode())
				} else {
					manageError(message, result.cause(), "getCategories")
				}
			})
		} catch (e: Exception) {
			logUnexpectedError("getCategories", e)
			message.fail(ApiExceptions.INTERNAL_SERVER_ERROR.statusCode, ApiExceptions.INTERNAL_SERVER_ERROR.statusMessage)
		}
	}

	private fun manageError(message: Message<JsonObject>, cause: Throwable, serviceName: String) {
		var code = ApiExceptions.INTERNAL_SERVER_ERROR.statusCode
		var statusMessage = ApiExceptions.INTERNAL_SERVER_ERROR.statusMessage

		when (cause) {
			is MainApiException -> {
				code = cause.statusCode
				statusMessage = cause.statusMessage
			}
			else -> logUnexpectedError(serviceName, cause)
		}

		message.fail(code, statusMessage)
	}

	private fun logUnexpectedError(serviceName: String, cause: Throwable) {
		logger.error("Unexpected error in $serviceName", cause)
	}
}