package de.fhro.inf.sa.jerichoDemo.api.error

/**
 * @author Peter Kurfer
 * Created on 10/29/17.
 */

open class MainApiException(val statusCode: Int, val statusMessage: String) : Exception()

object ApiExceptions {
	val INTERNAL_SERVER_ERROR = MainApiException(500, "Internal Server Error")
}

class CategoriesApiException(statusCode: Int, statusMessage: String) : MainApiException(statusCode, statusMessage)

class JokesApiException(statusCode: Int, statusMessage: String) : MainApiException(statusCode, statusMessage)