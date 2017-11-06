package de.fhro.inf.sa.jerichoDemo.persistence.repositories.impl

import de.fhro.inf.sa.jerichoDemo.persistence.generated.Tables
import de.fhro.inf.sa.jerichoDemo.persistence.generated.Tables.CATEGORIES
import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.daos.CategoriesDao
import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos.Categories
import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.records.CategoriesRecord
import de.fhro.inf.sa.jerichoDemo.persistence.repositories.ICategoriesRepository
import org.jooq.impl.DSL
import java.util.concurrent.CompletableFuture

/**
 * @author Peter Kurfer
 * Created on 10/31/17.
 */
class CategoriesRepository(dao: CategoriesDao) : RepositoryBase<Categories, CategoriesRecord, Int>(dao, { c -> c.id }), ICategoriesRepository {

	override fun findAllAsync(pageIndex: Int, pageSize: Int): CompletableFuture<List<Categories>> {
		return dao.client().fetch(
				DSL.using(dao.configuration())
						.select()
						.from(Tables.CATEGORIES)
						.limit(pageSize)
						.offset(pageSize * pageIndex)
						.query, { t -> Categories(t) }
		)
	}

	override fun exists(category: String): CompletableFuture<Boolean> {
		return dao.client().fetch(
				DSL.using(dao.configuration())
						.select(CATEGORIES.ID)
						.from(CATEGORIES)
						.where(CATEGORIES.NAME.eq(category))
						.limit(1)
						.query, { t -> t.getInteger("id") }
		).thenApplyAsync { ids -> !ids.isEmpty() }
	}

}