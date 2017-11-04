package de.fhro.inf.sa.jerichoDemo.persistence.repositories.impl

import de.fhro.inf.sa.jerichoDemo.persistence.repositories.IRepository
import io.github.jklingsporn.vertx.jooq.async.future.AsyncJooqSQLClient
import io.github.jklingsporn.vertx.jooq.async.future.VertxDAO
import io.github.jklingsporn.vertx.jooq.async.shared.VertxPojo
import org.jooq.UpdatableRecord
import java.util.concurrent.CompletableFuture

/**
 * @author Peter Kurfer
 * Created on 10/30/17.
 */
abstract class RepositoryBase<TEntity : VertxPojo?, TRecord : UpdatableRecord<TRecord>?, TId>(protected val dao: VertxDAO<TRecord, TEntity, TId>, protected val keySelector: (TEntity) -> TId) : IRepository<TEntity, TId> {

	override fun insertAsync(entity: TEntity): CompletableFuture<Int> = dao.insertExecAsync(entity)

	override fun updateAsync(entity: TEntity): CompletableFuture<Int> = dao.updateExecAsync(entity)

	override fun dropAsync(entity: TEntity): CompletableFuture<Int> = dao.deleteExecAsync(keySelector(entity))

	override fun findAsync(id: TId): CompletableFuture<TEntity> = dao.findByIdAsync(id)

	override fun countAsync(): CompletableFuture<Long> = dao.countAsync()

	override fun existsAsync(id: TId): CompletableFuture<Boolean> = dao.existsByIdAsync(id)

	override fun setClient(asyncJooqSQLClient: AsyncJooqSQLClient) = dao.setClient(asyncJooqSQLClient)
}