package de.fhro.inf.sa.jerichoDemo.api

import de.fhro.inf.sa.jerichoDemo.model.CategoryDto
import io.vertx.core.AsyncResult
import io.vertx.core.Handler

/**
 * @author Peter Kurfer
 * Created on 10/29/17.
 */
interface ICategoriesApi {

	fun createCategory(categoryId: Int, category: CategoryDto, handler: Handler<AsyncResult<CategoryDto>>)

	fun getCategories(pageIndex: Int, pageSize: Int, handler: Handler<AsyncResult<CategoryDto>>)
}