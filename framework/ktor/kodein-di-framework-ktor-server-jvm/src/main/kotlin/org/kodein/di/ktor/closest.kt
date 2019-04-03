package org.kodein.di.ktor

import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import org.kodein.di.*


// attribute key for storing injector in a call
val KodeinKey = AttributeKey<Kodein>("kodein")

/**
 * Getting the global [Kodein] container from the [Application]
 */
fun Application.kodein() = LazyKodein { attributes[KodeinKey] }

/**
 * Getting the global [Kodein] container from the [Application] parameter
 */
fun kodein(getApplication: () -> Application) = getApplication().kodein()

/**
 * Getting the global [Kodein] container from the [ApplicationCall]
 */
fun PipelineContext<*, ApplicationCall>.kodein() = kodein { context.application }

/**
 * Getting the global [Kodein] container from the [ApplicationCall]
 */
fun ApplicationCall.kodein() = kodein { application }

/**
 * Getting the global [Kodein] container from the [Routing] feature
 */
fun Routing.kodein() = kodein { application }

/**
 * Getting the global [Kodein] container from the current [Route]
 * by browsering all the routing tree until we get to the root level, the [Routing] feature
 *
 * @throws IllegalStateException if there is no [Kodein] container
 */
fun Route.kodein(): LazyKodein = when {
    this is Routing -> kodein()
    else -> parent?.kodein() ?: throw IllegalStateException("No kodein container found for [$this]")
}