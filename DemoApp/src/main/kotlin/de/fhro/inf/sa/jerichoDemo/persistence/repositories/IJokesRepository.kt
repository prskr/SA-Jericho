package de.fhro.inf.sa.jerichoDemo.persistence.repositories

import de.fhro.inf.sa.jerichoDemo.model.JokeDto
import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos.Jokes
import java.util.concurrent.CompletableFuture

/**
 * @author Peter Kurfer
 * Created on 10/30/17.
 */
interface IJokesRepository : IRepository<Jokes, Int> {
	fun findAllIncludingCategory(pageIndex: Int, pageSize: Int): CompletableFuture<List<JokeDto>>
}