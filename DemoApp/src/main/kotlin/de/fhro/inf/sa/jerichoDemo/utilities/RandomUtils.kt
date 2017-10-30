package de.fhro.inf.sa.jerichoDemo.utilities

import java.util.*

/**
 * @author Peter Kurfer
 * Created on 10/30/17.
 */
private val random: Random = Random()

fun ClosedRange<Int>.random() = random.nextInt(endInclusive - start) + start