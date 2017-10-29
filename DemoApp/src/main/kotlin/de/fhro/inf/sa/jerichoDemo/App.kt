package de.fhro.inf.sa.jerichoDemo

import io.vertx.core.Vertx
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import java.sql.DriverManager

/**
 * @author Peter Kurfer
 * Created on 10/6/17.
 */
fun main(args: Array<String>) {
	try {
		val liquibase = Liquibase("database/db.changelog.yaml", ClassLoaderResourceAccessor(), JdbcConnection(DriverManager.getConnection("jdbc:postgresql://database:5432/jericho", "jericho", "W@c[3~DV>~:]4%+5")))
		liquibase.update("production")
	}catch (e: Exception) {
		e.printStackTrace()
	}

	val vertx = Vertx.vertx()
	vertx.deployVerticle(MainApiVerticle())
}