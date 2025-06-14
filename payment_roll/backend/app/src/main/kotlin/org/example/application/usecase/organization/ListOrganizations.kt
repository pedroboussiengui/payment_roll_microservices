package org.example.application.usecase.organization

import kotlinx.serialization.Serializable
import org.example.infra.jwt.JwtService
import org.example.infra.ktor.LocalDateSerializer
import org.example.infra.ktor.UUIDSerializer
import org.example.infra.repository.organization.OrganizationRepository
import java.time.LocalDate
import java.util.UUID

class ListOrganizations(
    private val organizationRepository: OrganizationRepository,
    private val jwtService: JwtService
) {
    fun execute(accessToken: String): List<ListOrganizationsOutput> {
//        jwtService.isValid(accessToken)
        val output = mutableListOf<ListOrganizationsOutput>()
        organizationRepository.findAll().map { organization ->
            output.add(
                ListOrganizationsOutput(
                    id = organization.id,
                    name = organization.name,
                    cnpj = organization.cnpj,
                    sigla = organization.sigla,
                    parentId = organization.parentId,
                    createdAt = organization.createdAt
                )
            )
        }
        return output
    }
}

@Serializable
data class ListOrganizationsOutput(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val cnpj: String,
    val sigla: String,
    @Serializable(with = UUIDSerializer::class)
    val parentId: UUID?,
    @Serializable(with = LocalDateSerializer::class)
    val createdAt: LocalDate
)