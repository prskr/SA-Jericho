package de.fhro.inf.sa.jerichoDemo.persistence.repositories

import io.github.jklingsporn.vertx.jooq.async.future.VertxDAO
import io.github.jklingsporn.vertx.jooq.async.shared.VertxPojo
import org.jooq.UpdatableRecord
import org.jooq.impl.DAOImpl
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

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
}