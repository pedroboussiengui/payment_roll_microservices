package org.example.infra.repository.employee

import org.example.domain.employee.AdmissionEvent
import org.example.domain.employee.AfastamentoEvent
import org.example.domain.employee.Contract
import org.example.domain.employee.ContractEvent
import org.example.domain.employee.ContractState
import org.example.domain.employee.Employee
import org.example.infra.repository.employee.ContractModel.contractState
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.sql.Connection
import java.util.UUID

interface EmployeeRepository {
    fun add(employee: Employee): UUID
    fun findAll(): List<Employee>
    fun findById(employeeId: UUID): Employee?
    fun findByDocument(document: String): Employee?
    fun update(employee: Employee)
}

class EmployeeRepositoryImpl : EmployeeRepository {

    init {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
        transaction {
//            drop(
//                EmployeeModel,
//                ContractModel,
//                AdmissionEventModel,
//                AfastamentoEventModel
//            )
            create(
                EmployeeModel,
                ContractModel,
                AdmissionEventModel,
                AfastamentoEventModel
            )
        }
    }

    override fun update(employee: Employee) {
        transaction {
            // Atualiza o employee
            EmployeeModel.update({ EmployeeModel.id eq employee.id!! }) {
                it[name] = employee.name
                it[document] = employee.document
                it[birthDate] = employee.birthDate
                it[identity] = employee.identity
                it[maritalStatus] = employee.maritalStatus
                it[gender] = employee.gender
                it[motherName] = employee.motherName
                it[fatherName] = employee.fatherName
            }

            // Busca contratos atuais do banco
            val currentContracts = ContractModel
                .selectAll().where { ContractModel.employeeId eq employee.id!! }
                .associateBy { it[ContractModel.id] }

            for (contract in employee.contracts) {
                val contractId = contract.id ?: UUID.randomUUID()

                if (contract.id == null || !currentContracts.containsKey(contract.id)) {
                    // Inserção
                    ContractModel.insert {
                        it[id] = contractId
                        it[matricula] = contract.matricula
                        it[entryDate] = contract.entryDate
                        it[exerciseDate] = contract.exerciseDate
                        it[contractType] = contract.contractType
                        it[contractState] = contract.contractState.toString()
                        it[department] = contract.department
                        it[position] = contract.position
                        it[function] = contract.function
                        it[possibleEvents] = contract.possibleEvents.joinToString(";")
                        it[employeeId] = employee.id!!
                    }
                } else if (currentContracts[contract.id] != null) {
                    // Atualização
                    ContractModel.update({ ContractModel.id eq contractId }) {
                        it[matricula] = contract.matricula
                        it[entryDate] = contract.entryDate
                        it[exerciseDate] = contract.exerciseDate
                        it[contractType] = contract.contractType
                        it[contractState] = contract.contractState.toString()
                        it[department] = contract.department
                        it[position] = contract.position
                        it[function] = contract.function
                        it[possibleEvents] = contract.possibleEvents.joinToString(";")
                    }
                }

                AdmissionEventModel.deleteWhere() { AdmissionEventModel.contractId eq contractId }
                AfastamentoEventModel.deleteWhere() { AfastamentoEventModel.contractId eq contractId }

                for (event in contract.events) {
                    when (event) {
                        is AdmissionEvent -> insertAdmission(event, contractId)
                        is AfastamentoEvent -> insertAfastamento(event, contractId)
                    }
                }
            }
        }
    }

    private fun insertAdmission(event: AdmissionEvent, contractIdValue: UUID) {
        AdmissionEventModel.insert {
            it[eventType] = event.type
            it[createdAt] = event.createdAt
            it[entryDate] = event.entryDate
            it[exerciseDate] = event.exerciseDate
            it[contractId] = contractIdValue
        }
    }

    private fun insertAfastamento(event: AfastamentoEvent, contractIdValue: UUID) {
        AfastamentoEventModel.insert {
            it[eventType] = event.type
            it[createdAt] = event.createdAt
            it[reason] = event.reason
            it[contractId] = contractIdValue
        }
    }

    private fun selectAllEvents(contract: Contract) {
        val admissionEvents = AdmissionEventModel.selectAll().where { AdmissionEventModel.contractId eq contract.id!! }.map {
            AdmissionEvent(
                it[AdmissionEventModel.entryDate],
                it[AdmissionEventModel.exerciseDate],
                it[AdmissionEventModel.createdAt]
            )
        }
        val afastamentoEvents = AfastamentoEventModel.selectAll().where { AfastamentoEventModel.contractId eq contract.id!! }.map {
            AfastamentoEvent(
                it[AfastamentoEventModel.reason],
                it[AfastamentoEventModel.createdAt]
            )
        }
        contract.events.addAll(admissionEvents)
        contract.events.addAll(afastamentoEvents)
    }

    override fun add(employee: Employee): UUID {
        return transaction {
            val employeeId = UUID.randomUUID()
            EmployeeModel.insert {
                it[id] = employee.id ?: employeeId
                it[name] = employee.name
                it[document] = employee.document
                it[birthDate] = employee.birthDate
                it[identity] = employee.identity
                it[maritalStatus] = employee.maritalStatus
                it[gender] = employee.gender
                it[motherName] = employee.motherName
                it[fatherName] = employee.fatherName
            }
            employeeId
        }
    }

    override fun findAll(): List<Employee> {
        return transaction {
            EmployeeModel.selectAll().map {
                Employee(
                    it[EmployeeModel.id],
                    it[EmployeeModel.name],
                    it[EmployeeModel.document],
                    it[EmployeeModel.birthDate],
                    it[EmployeeModel.identity],
                    it[EmployeeModel.maritalStatus],
                    it[EmployeeModel.gender],
                    it[EmployeeModel.motherName],
                    it[EmployeeModel.fatherName]
                )
            }
        }
    }

    override fun findById(employeeId: UUID): Employee? {
        return transaction {
            val employee = EmployeeModel.selectAll().where { EmployeeModel.id eq employeeId }.map {
                Employee(
                    it[EmployeeModel.id],
                    it[EmployeeModel.name],
                    it[EmployeeModel.document],
                    it[EmployeeModel.birthDate],
                    it[EmployeeModel.identity],
                    it[EmployeeModel.maritalStatus],
                    it[EmployeeModel.gender],
                    it[EmployeeModel.motherName],
                    it[EmployeeModel.fatherName]
                )
            }.singleOrNull()
            employee?.let {
                val contracts = ContractModel.selectAll().where { ContractModel.employeeId eq employeeId }.map {
                    val contract = Contract(
                        it[ContractModel.id],
                        it[ContractModel.matricula],
                        it[ContractModel.entryDate],
                        it[ContractModel.exerciseDate],
                        it[ContractModel.contractType],
                        ContractState.fromString(it[contractState]),
                        it[ContractModel.department],
                        it[ContractModel.position],
                        it[ContractModel.function]
                    )
                    contract.possibleEvents = it[ContractModel.possibleEvents].split(";").map {
                        ContractEvent.fromString(it)
                    }
                    selectAllEvents(contract)
                    contract
                }.toMutableList()
                it.copy(contracts = contracts)
            }
        }
    }

    override fun findByDocument(document: String): Employee? {
        return transaction {
            val employee = EmployeeModel.selectAll().where { EmployeeModel.document eq document }.map {
                Employee(
                    it[EmployeeModel.id],
                    it[EmployeeModel.name],
                    it[EmployeeModel.document],
                    it[EmployeeModel.birthDate],
                    it[EmployeeModel.identity],
                    it[EmployeeModel.maritalStatus],
                    it[EmployeeModel.gender],
                    it[EmployeeModel.motherName],
                    it[EmployeeModel.fatherName]
                )
            }.singleOrNull()
            employee?.let {
                val contracts = ContractModel.selectAll().where { ContractModel.employeeId eq employee.id!! }.map {
                    val contract = Contract(
                        it[ContractModel.id],
                        it[ContractModel.matricula],
                        it[ContractModel.entryDate],
                        it[ContractModel.exerciseDate],
                        it[ContractModel.contractType],
                        ContractState.fromString(it[contractState]),
                        it[ContractModel.department],
                        it[ContractModel.position],
                        it[ContractModel.function]
                    )
                    contract.possibleEvents = it[ContractModel.possibleEvents].split(";").map {
                        ContractEvent.fromString(it)
                    }
                    contract
                }.toMutableList()
                it.copy(contracts = contracts)
            }
        }
    }
}