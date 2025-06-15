package org.example.application.usecase

import org.example.domain.Contract
import org.example.domain.UserNotFoundByIdException
import org.example.domain.UserStatus
import org.example.infra.repository.UserRepository
import java.time.Instant
import java.util.*

class AddUserContract(
    val userRepository: UserRepository
) {
    fun execute(userId: String, input: ContractInput): AddUserContractOutput {
        val user = userRepository.findById(UUID.fromString(userId))
            ?: throw UserNotFoundByIdException(userId)

        val contract = Contract(
            UUID.fromString(input.id),
            input.matricula,
            input.organization,
            input.department
        )

        user.addContract(contract)
        userRepository.save(user)
        return AddUserContractOutput(
            userId = user.userId.toString(),
            username = user.username,
            email = user.email,
            status = user.status,
            contracts = user.contracts,
            lastLoginAt = user.lastLoginAt,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
    }
}

data class ContractInput(
    val id: String,
    val matricula: String,
    val organization: String,
    val department: String
)

data class AddUserContractOutput(
    val userId: String,
    val username: String,
    val email: String,
    var status: UserStatus,
    val contracts: MutableList<Contract>,
    val lastLoginAt: Instant? = null,
    val createdAt: Instant,
    val updatedAt: Instant? = null
)