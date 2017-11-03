package de.fhro.inf.sa.jerichoDemo.utilities

import java.util.Random

/**
 * @author Peter Kurfer
 * Created on 10/30/17.
 */
private val randomInstance: Random = Random()

fun ClosedRange<Int>.random() = randomInstance.nextInt(endInclusive - start) + start