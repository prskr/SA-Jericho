package de.fhro.inf.sa.jerichoDemo.persistence.repositories.impl

import de.fhro.inf.sa.jerichoDemo.model.JokeDto
import de.fhro.inf.sa.jerichoDemo.persistence.generated.Tables
import de.fhro.inf.sa.jerichoDemo.persistence.generated.Tables.CATEGORIES
import de.fhro.inf.sa.jerichoDemo.persistence.generated.Tables.JOKES
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

	override fun getRandomJoke(): CompletableFuture<JokeDto> {
		return dao.client().fetchOne(
				DSL.using(dao.configuration())
						.query("SELECT * FROM random_joke()")
		) { t -> JokeDto(t.getInteger("joke_id"), t.getString("joke_text"), t.getString("category_text")) }
	}

	override fun findByJokeAsync(joke: String): CompletableFuture<List<JokeDto>> {
		return dao.client().fetch(
				DSL.using(dao.configuration())
						.select(JOKES.ID, JOKES.JOKE, CATEGORIES.NAME)
						.from(JOKES)
						.leftJoin(CATEGORIES)
						.on(JOKES.CATEGORYID.eq(CATEGORIES.ID))
						.where(JOKES.JOKE.eq(joke))
						.query
		) { t -> JokeDto(t.getInteger("id", 0), t.getString("joke"), t.getString("name")) }
	}

	override fun findAllIncludingCategory(pageIndex: Int, pageSize: Int): CompletableFuture<List<JokeDto>> {
		return dao.client().fetch(
				DSL.using(dao.configuration())
						.select(JOKES.ID, JOKES.JOKE, CATEGORIES.NAME)
						.from(JOKES)
						.leftJoin(CATEGORIES)
						.on(JOKES.CATEGORYID.eq(CATEGORIES.ID))
						.limit(pageSize)
						.offset(pageSize * pageIndex)
						.query
		) { t -> JokeDto(t.getInteger("id", 0), t.getString("joke")) }
	}

	override fun findAllAsync(pageIndex: Int, pageSize: Int): CompletableFuture<List<Jokes>> {
		return dao.client().fetch(
				DSL.using(dao.configuration())
						.select()
						.from(Tables.JOKES)
						.limit(pageSize)
						.offset(pageSize * pageIndex)
						.query) { t -> Jokes(t) }
	}

}
