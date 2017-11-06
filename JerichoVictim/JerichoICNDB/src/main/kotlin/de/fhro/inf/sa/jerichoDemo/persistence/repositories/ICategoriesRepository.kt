package de.fhro.inf.sa.jerichoDemo.persistence.repositories

import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos.Categories
import java.util.concurrent.CompletableFuture

/**
 * @author Peter Kurfer
 * Created on 10/31/17.
 */
interface ICategoriesRepository : IRepository<Categories, Int> {

	fun exists(category: String): CompletableFuture<Boolean>
}