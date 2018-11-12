package de.fhro.inf.sa.jerichoDemo.utilities

import io.vertx.core.eventbus.DeliveryOptions

/**
 * @author Peter Kurfer
 * Created on 12/4/17.
 */
fun DeliveryOptions.addContentTypeJson(): DeliveryOptions = this.apply {
	this.addHeader("Content-Type", "application/json")
}