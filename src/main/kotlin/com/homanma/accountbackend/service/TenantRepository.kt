package com.homanma.accountbackend.service

import com.homanma.accountbackend.domain.Tenant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Date

@Repository
interface TenantRepository : JpaRepository<Tenant, Long> {
	fun findDistinctByRentReceiptsCreationDateBetween(startDate: Date, endDate: Date): List<Tenant>
}