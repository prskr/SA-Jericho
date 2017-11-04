package de.fhro.inf.sa.jerichoDemo.persistence

import de.fhro.inf.sa.jerichoDemo.api.ICategoriesApi
import de.fhro.inf.sa.jerichoDemo.model.CategoryDto
import io.vertx.core.AsyncResult
import io.vertx.core.Handler

/**
 * @author Peter Kurfer
 * Created on 10/29/17.
 */
class CategoriesJpaApi : ICategoriesApi {
	override fun createCategory(categoryId: Int, category: CategoryDto, handler: Handler<AsyncResult<CategoryDto>>) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getCategories(pageIndex: Int, pageSize: Int, handler: Handler<AsyncResult<CategoryDto>>) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}