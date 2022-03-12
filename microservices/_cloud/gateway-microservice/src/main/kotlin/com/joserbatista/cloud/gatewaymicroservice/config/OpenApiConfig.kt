package com.joserbatista.cloud.gatewaymicroservice.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springdoc.core.GroupedOpenApi
import org.springdoc.core.SwaggerUiConfigParameters
import org.springframework.cloud.gateway.route.RouteDefinition
import org.springframework.cloud.gateway.route.RouteDefinitionLocator
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class OpenApiConfig {

    companion object {

        private val log: Logger = LoggerFactory.getLogger(OpenApiConfig::class.java)
        private const val ROUTE_SUFFIX = "-microservice"
        private const val ROUTE_SUFFIX_REGEX_EXP = "(.*)$ROUTE_SUFFIX"
        private const val DOCS_PATH = "/v3/api-docs"
    }

    @Bean
    @Lazy(false)
    fun groupedOpenApiList(swaggerUiConfigParameters: SwaggerUiConfigParameters, locator: RouteDefinitionLocator): List<GroupedOpenApi> {
        val groups: MutableList<GroupedOpenApi> = ArrayList()

        locator.routeDefinitions.filter { it.id.matches(ROUTE_SUFFIX_REGEX_EXP.toRegex()) }
            .subscribe {
                val name: String = it.id.replace(ROUTE_SUFFIX, "")
                swaggerUiConfigParameters.addGroup(name)
                GroupedOpenApi.builder().pathsToMatch("/$name/**").group(name).build()
            }

        return groups
    }

    /**
     * Create routes for OpenAPI (/v3/api-docs/service) to support groupedOpenApiList
     */
    @Bean
    fun openApiRouteLocator(builder: RouteLocatorBuilder, locator: RouteDefinitionLocator): RouteLocator {
        val routes = builder.routes()

        locator.routeDefinitions.filter { it.id.matches(ROUTE_SUFFIX_REGEX_EXP.toRegex()) }
            .subscribe { routeDefinition -> mapRoute(routeDefinition, routes) }

        log.trace("Created OpenAPI routes")
        return routes.build()
    }

    private fun mapRoute(routeDefinition: RouteDefinition, routes: RouteLocatorBuilder.Builder) {
        val id = routeDefinition.id
        val serverPath = "$DOCS_PATH/${id.replace(ROUTE_SUFFIX, "")}"
        routes.route("openapi-$id") { newRoute ->
            newRoute.path(serverPath).filters { filter -> filter.rewritePath(serverPath, DOCS_PATH) }.uri("${routeDefinition.uri}$DOCS_PATH")
        }
        log.trace("Created route for <$id> with path <$serverPath>")
    }

}