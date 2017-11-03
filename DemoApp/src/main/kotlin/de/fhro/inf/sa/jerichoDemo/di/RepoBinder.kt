package de.fhro.inf.sa.jerichoDemo.di

import com.google.inject.AbstractModule
import de.fhro.inf.sa.jerichoDemo.api.controllers.JokesController
import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.daos.CategoriesDao
import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.daos.JokesDao
import de.fhro.inf.sa.jerichoDemo.persistence.repositories.ICategoriesRepository
import de.fhro.inf.sa.jerichoDemo.persistence.repositories.IJokesRepository
import de.fhro.inf.sa.jerichoDemo.persistence.repositories.impl.CategoriesRepository
import de.fhro.inf.sa.jerichoDemo.persistence.repositories.impl.JokesRepository
import io.vertx.core.Vertx
import org.jooq.SQLDialect
import org.jooq.impl.DefaultConfiguration
import javax.inject.Provider

/**
 * @author Peter Kurfer
 * Created on 11/3/17.
 */
class RepoBinder : AbstractModule() {

	override fun configure() {
		bind(ICategoriesRepository::class.java).toProvider(CategoriesProvider::class.java)
		bind(IJokesRepository::class.java).toProvider(JokesProvider::class.java)

	}

	private object CategoriesProvider : Provider<ICategoriesRepository> {
		override fun get(): ICategoriesRepository = CategoriesRepository(CategoriesDao(DefaultConfiguration().set(SQLDialect.POSTGRES)))
	}

	private object JokesProvider : Provider<IJokesRepository> {
		override fun get(): IJokesRepository = JokesRepository(JokesDao(DefaultConfiguration().set(SQLDialect.POSTGRES)))
	}

	private object JokesControllerProvider : Provider<JokesController> {
		override fun get(): JokesController = JokesController()
	}

}

