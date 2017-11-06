package de.fhro.inf.sa.jerichoDemo.api.verticles

import com.google.inject.Inject
import de.fhro.inf.sa.jerichoDemo.api.ICategoriesApi
import de.fhro.inf.sa.jerichoDemo.api.controllers.CategoriesController
import de.fhro.inf.sa.jerichoDemo.api.error.ApiExceptions
import de.fhro.inf.sa.jerichoDemo.api.error.MainApiException
import de.fhro.inf.sa.jerichoDemo.model.CategoryDto
import de.fhro.inf.sa.jerichoDemo.persistence.CategoriesJpaApi
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
class CategoriesApiVerticle @Inject constructor(private val categoriesController: CategoriesController): AbstractVerticle() {

	private val createCategoryServiceId = "createCategory"
	private val getCategoriesServiceId = "getCategories"

	override fun start() {
		vertx.eventBus().consumer<JsonObject>(createCategoryServiceId).handler(categoriesController::createCategory)
		vertx.eventBus().consumer<JsonObject>(getCategoriesServiceId).handler(categoriesController::getCategories)
	}
}
