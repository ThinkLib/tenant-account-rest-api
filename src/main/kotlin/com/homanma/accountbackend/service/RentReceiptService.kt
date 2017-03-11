package com.homanma.accountbackend.service

import com.homanma.accountbackend.domain.RentReceiptP
import org.springframework.stereotype.Service

interface RentReceiptService {
	fun getAllByTenantId(tenantId: Long): List<RentReceiptP>
	fun get(id: Long): RentReceiptP
}

@Service
class RentReceiptServiceImpl(val rentReceiptRepository: RentReceiptRepository, val modelToEntityTransformer: ModelToEntityTransformer) : RentReceiptService {
	override fun getAllByTenantId(tenantId: Long): List<RentReceiptP> {
		return rentReceiptRepository.findByTenantId(tenantId).map { rentReceipt -> modelToEntityTransformer.toModel(rentReceipt) }
	}

	override fun get(id: Long): RentReceiptP {
		return modelToEntityTransformer.toModel(rentReceiptRepository.findOne(id))
	}
}