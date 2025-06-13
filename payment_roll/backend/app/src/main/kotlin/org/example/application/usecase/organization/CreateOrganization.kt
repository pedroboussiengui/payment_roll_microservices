package org.example.application.usecase.organization

import kotlinx.serialization.Serializable
import org.example.domain.organization.Organization
import org.example.domain.organization.OrganizationExceptions
import org.example.infra.jwt.JwtService
import org.example.infra.ktor.UUIDSerializer
import org.example.infra.repository.OrganizationRepository
import java.util.UUID

class CreateOrganization(
    private val organizationRepository: OrganizationRepository,
    private val jwtService: JwtService
) {
    fun execute(input: CreateOrganizationInput, accessToken: String): CreateOrganizationOutput {
        jwtService.isValid(accessToken)
        input.parentId?.let {
            organizationRepository.findById(input.parentId)
                ?: throw OrganizationExceptions.NotFound()
        }
        val organization = Organization.create(
            name = input.nome,
            cnpj = input.cnpj,
            sigla = input.sigla,
            parentId = input.parentId
        )
        val validationResult = organization.validate()
        if (validationResult.isNotEmpty()) {
            throw OrganizationExceptions.BusinessRuleViolation(
                "Violations fo business rules was found in input data",
                validationResult
            )
        }
        organizationRepository.add(organization)
        return CreateOrganizationOutput(organization.id)
    }
}

@Serializable
data class CreateOrganizationInput(
    val nome: String,
    val cnpj: String,
    val sigla: String,
    @Serializable(with = UUIDSerializer::class)
    val parentId: UUID?
)

@Serializable
data class CreateOrganizationOutput(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID
)