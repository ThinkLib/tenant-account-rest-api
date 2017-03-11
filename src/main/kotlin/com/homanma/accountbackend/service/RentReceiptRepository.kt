package com.homanma.accountbackend.service

import com.homanma.accountbackend.domain.RentReceipt
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RentReceiptRepository : JpaRepository<RentReceipt, Long> {
	fun findByTenantId(tenantId: Long): List<RentReceipt>
}