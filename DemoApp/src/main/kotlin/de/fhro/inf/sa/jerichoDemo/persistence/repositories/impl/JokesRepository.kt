package de.fhro.inf.sa.jerichoDemo.persistence.repositories.impl

import de.fhro.inf.sa.jerichoDemo.model.JokeDto
import de.fhro.inf.sa.jerichoDemo.persistence.generated.Tables
import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.daos.JokesDao
import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos.Jokes
import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.records.JokesRecord
import de.fhro.inf.sa.jerichoDemo.persistence.repositories.IJokesRepository
import org.jooq.impl.DSL
import java.util.concurrent.CompletableFuture

/**
 * @author Peter Kurfer
 * Created on 10/30/17.
 */
class JokesRepository(jokesDao: JokesDao) : RepositoryBase<Jokes, JokesRecord, Int>(jokesDao, { j -> j.id }), IJokesRepository {

	override fun findAllIncludingCategory(pageIndex: Int, pageSize: Int): CompletableFuture<List<JokeDto>> {
		return dao.client().fetch(
				DSL.using(dao.configuration())
						.select(Tables.JOKES.ID, Tables.JOKES.JOKE, Tables.CATEGORIES.NAME)
						.from(Tables.JOKES)
						.leftJoin(Tables.CATEGORIES)
						.on(Tables.JOKES.CATEGORYID.eq(Tables.CATEGORIES.ID))
						.limit(pageSize)
						.offset(pageSize * pageIndex)
						.query, { t -> JokeDto(t.getInteger("id", 0), t.getString("joke")) }
		)
	}

	override fun findAllAsync(pageIndex: Int, pageSize: Int): CompletableFuture<List<Jokes>> {
		return dao.client().fetch(
				DSL.using(dao.configuration())
						.select()
						.from(Tables.JOKES)
						.limit(pageSize)
						.offset(pageSize * pageIndex)
						.query, { t -> Jokes(t) })
	}

}