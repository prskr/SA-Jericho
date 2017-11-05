package de.fhro.inf.sa.jerichoDemo.model

import io.vertx.core.json.JsonObject

/**
 * @author Peter Kurfer
 * Created on 11/4/17.
 */
data class JdbcConfig(var hostname: String = "localhost", var port: Int = 5432, var dbName: String = "jericho", var userName: String = "postgres", var password: String = "")

fun JdbcConfig.fromJson(obj: JsonObject) = this.apply {
	hostname = obj.getString("jdbc.hostname", hostname)
	port = obj.getInteger("jdbc.port", port)
	userName = obj.getString("jdbc.user", userName)
	password = obj.getString("jdbc.password", password)
	dbName = obj.getString("jdbc.database", dbName)
}