package de.fhro.inf.sa.jerichoDemo.model

import io.vertx.core.json.JsonObject

/**
 * @author Peter Kurfer
 * Created on 11/4/17.
 */
data class JdbcConfig(var hostname: String = "localhost", var port: Int = 5432, var dbName: String = "jericho", var userName: String = "postgres", var password: String = "")

data class RuntimeConfig(var httpPort: Int = 8080, var verticlesCount: Int = 1)

fun JdbcConfig.fromJson(obj: JsonObject) = this.apply {
	hostname = obj.getString("JDBC_HOSTNAME", hostname)
	port = obj.getInteger("JDBC_PORT", port)
	userName = obj.getString("JDBC_USER", userName)
	password = obj.getString("JDBC_PASSWORD", password)
	dbName = obj.getString("JDBC_DATABASE", dbName)
}

fun RuntimeConfig.fromJson(obj: JsonObject) = this.apply {
	httpPort = obj.getInteger("HTTP_PORT", httpPort)
	verticlesCount = obj.getInteger("VERTICLES_COUNT", verticlesCount)
}