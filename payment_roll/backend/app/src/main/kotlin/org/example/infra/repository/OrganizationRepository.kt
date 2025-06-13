package org.example.infra.repository

import org.example.domain.organization.Organization
import java.util.UUID

interface OrganizationRepository {
    fun add(organization: Organization)
    fun bulkAdd(organizations: List<Organization>)
    fun findAll(): List<Organization>
    fun findById(organizationId: UUID): Organization?
}

class InMemoryOrganizationRepository : OrganizationRepository {
    private val db: MutableMap<UUID, Organization> = mutableMapOf()

    override fun add(organization: Organization) {
        db[organization.id] = organization
    }

    override fun bulkAdd(organizations: List<Organization>) {
        organizations.forEach { organization ->
            db[organization.id] = organization
        }
    }

    override fun findAll(): List<Organization> {
        return db.values.toList()
    }

    override fun findById(organizationId: UUID): Organization? {
        return db[organizationId]
    }
}