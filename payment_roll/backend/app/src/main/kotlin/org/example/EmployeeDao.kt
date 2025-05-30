package org.example

class EmployeeDao {
    private val db: MutableMap<String, Employee> = mutableMapOf()

    fun add(employee: Employee) {
        db[employee.id.toString()] = employee
    }

    fun findById(employeeId: String): Employee? {
        return db[employeeId]
    }

    fun findAll(): List<Employee> {
        return db.values.toList()
    }
}