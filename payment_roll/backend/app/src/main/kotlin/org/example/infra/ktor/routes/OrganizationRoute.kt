package org.example.infra.ktor.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import org.example.ACCESS_TOKEN_KEY
import org.example.application.usecase.organization.CreateOrganization
import org.example.application.usecase.organization.CreateOrganizationInput
import org.example.application.usecase.organization.ListOrganizations
import org.example.domain.organization.Organization
import org.example.infra.jwt.Auth0JwtService
import org.example.infra.repository.InMemoryOrganizationRepository
import org.example.infra.repository.OrganizationRepository
import java.time.LocalDate
import java.util.UUID

fun Route.organizationRoute() {
    val organizationRepository = InMemoryOrganizationRepository()
    val jwtService = Auth0JwtService()
    val createOrganization = CreateOrganization(organizationRepository, jwtService)
    val listOrganizations = ListOrganizations(organizationRepository, jwtService)

    LoadOrganizations(organizationRepository)

    post("/organizations") {
        val accessToken = call.attributes[ACCESS_TOKEN_KEY]
        val input = call.receive<CreateOrganizationInput>()
        val output = createOrganization.execute(input, accessToken)
        call.respond(HttpStatusCode.Created, output)
    }
    get("/organizations") {
        val accessToken = call.attributes[ACCESS_TOKEN_KEY]
        val output = listOrganizations.execute(accessToken)
        call.respond(HttpStatusCode.OK, output)
    }
}

class LoadOrganizations(organizationRepository: OrganizationRepository) {

    init {
        organizationRepository.bulkAdd(
            listOf(
                Organization(
                    UUID.fromString("5f76e22c-ca03-4e08-b322-5486e61ba8f4"),
                    "Organization 1",
                    "11.111.111/1111-11",
                    "ORG1",
                    null,
                    LocalDate.now()
                ),
                Organization(
                    UUID.fromString("739f3fb5-dafc-413b-9f18-2b38c4cf8d98"),
                    "Organization 2",
                    "22.222.222/2222-22",
                    "ORG2",
                    UUID.fromString("5f76e22c-ca03-4e08-b322-5486e61ba8f4"),
                    LocalDate.now()
                ),
                Organization(
                    UUID.fromString("915c717b-f80e-432e-ba6c-a170e208a902"),
                    "Organization 3",
                    "33.333.333/3333-33",
                    "ORG3",
                    UUID.fromString("5f76e22c-ca03-4e08-b322-5486e61ba8f4"),
                    LocalDate.now()
                )
            )
        )
    }
}