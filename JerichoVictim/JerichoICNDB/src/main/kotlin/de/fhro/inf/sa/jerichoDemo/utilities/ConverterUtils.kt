package de.fhro.inf.sa.jerichoDemo.utilities

/**
 * @author Peter Kurfer
 * Created on 10/31/17.
 */
fun Any.anyToInt(): Int = when (this) {
	is String -> this.toInt()
	is Int -> this
	else -> 0
}