package org.example.infra.repository.employee

import org.example.domain.employee.ContractType
import org.example.domain.employee.EventType
import org.example.domain.employee.Gender
import org.example.domain.employee.MaritalStatus
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime

object EmployeeModel : Table("employee") {
    val id = uuid("employee_id")
    val name = varchar("name", 100)
    val document = varchar("document", 14)
    val birthDate = date("birth_date")
    val identity = varchar("identity", 11)
    val maritalStatus = enumeration<MaritalStatus>("marital_status")
    val gender = enumeration<Gender>("gender")
    val motherName = varchar("mother_name", 100)
    val fatherName = varchar("father_name", 100).nullable()
    override val primaryKey = PrimaryKey(id)
}

object ContractModel : Table("contract") {
    val id = uuid("contract_id")
    val matricula = varchar("matricula", 10)
    val entryDate = date("entry_date")
    val exerciseDate = date("exercise_date")
    val contractType = enumeration<ContractType>("contract_type")
    val contractState = varchar("contract_state", 50)
    val department = varchar("department", 100)
    val position = varchar("position", 100)
    val function = varchar("function", 100).nullable()
    val possibleEvents = varchar("possible_events", 255)
    val employeeId = uuid("employee_id").references(EmployeeModel.id)
    override val primaryKey = PrimaryKey(id)
}

object AdmissionEventModel : Table("admission_event") {
    val id = integer("id").autoIncrement()
    val eventType = enumeration<EventType>("event_type")
    val createdAt = datetime("created_at")
    val entryDate = date("entry_date")
    val exerciseDate = date("exercise_date")
    val contractId = uuid("contract_id").references(ContractModel.id)
    override val primaryKey = PrimaryKey(id)
}

object AfastamentoEventModel : Table("afastamento_event") {
    val id = integer("id").autoIncrement()
    val eventType = enumeration<EventType>("event_type")
    val createdAt = datetime("created_at")
    val reason = varchar("reason", 255)
    val contractId = uuid("contract_id").references(ContractModel.id)
    override val primaryKey = PrimaryKey(id)
}