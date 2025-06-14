package org.example.application.usecase.organization

import kotlinx.serialization.Serializable
import org.example.domain.organization.OrganizationExceptions
import org.example.infra.jwt.JwtService
import org.example.infra.ktor.LocalDateSerializer
import org.example.infra.ktor.UUIDSerializer
import org.example.infra.repository.organization.OrganizationRepository
import java.time.LocalDate
import java.util.UUID

class DetailOrganizationById(
    private val organizationRepository: OrganizationRepository,
    private val jwtService: JwtService
) {
    fun execute(organizationId: UUID, accessToken: String): DetailOrganizationByIdOutput {
//        jwtService.isValid(accessToken)
        val organization = organizationRepository.findById(organizationId)
            ?: throw OrganizationExceptions.NotFound()
        return DetailOrganizationByIdOutput(
            id = organization.id,
            name = organization.name,
            cnpj = organization.cnpj,
            sigla = organization.sigla,
            parentId = organization.parentId,
            createdAt = organization.createdAt
        )
    }
}

@Serializable
data class DetailOrganizationByIdOutput(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val cnpj: String,
    val sigla: String,
    @Serializable(with = UUIDSerializer::class)
    val parentId: UUID? = null,
    @Serializable(with = LocalDateSerializer::class)
    val createdAt: LocalDate
)