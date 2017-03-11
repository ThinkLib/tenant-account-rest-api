package com.homanma.accountbackend.service

import com.homanma.accountbackend.domain.RentReceipt
import com.homanma.accountbackend.domain.RentReceiptP
import com.homanma.accountbackend.domain.Tenant
import com.homanma.accountbackend.domain.TenantP
import org.springframework.stereotype.Component

/**
 * Maps from the backend JPA entity to the REST API model object.
 * That way if the JPA model changes, the model entity can remain the same. The other
 * advantage is that the model entity has the json annotations and the JPA entity has the JPA annotations.
 *
 * @property tenantRepo for fetching tenants
 * @property rentReceiptRepo for fetching rentReceipts
 * @constructor Creates a RentReceiptController
 */
@Component
class ModelToEntityTransformer(val tenantRepo: TenantRepository, val rentReceiptRepo: RentReceiptRepository) {
	/**
     * Maps from the JPA [tenant] to the REST API object
     * @return the REST API model object
     */
	fun toModel(tenant: Tenant): TenantP {
		var rentReceiptIds = tenant.rentReceipts.map { rentReceipt -> rentReceipt.id!! }

		val targetP = TenantP(id = tenant.id,
				name = tenant.name,
				weeklyRentAmount = tenant.weeklyRentAmount,
				currentRentPaidToDate = tenant.currentRentPaidToDate,
				currentRentCreditAmount = tenant.currentRentCreditAmount,
				rentReceiptIds = rentReceiptIds)
		return targetP
	}

	/**
     * Maps from the REST API [tenantP] to the JPA entity
     * @return the JPA tenant entity
     */
	fun toEntity(tenantP: TenantP): Tenant {
		var target: Tenant = Tenant(tenantP.id, tenantP.name, tenantP.weeklyRentAmount)
		// This means it exists in the database
		if (tenantP.id != null) {
			// Retrieve it and update values
			target = tenantRepo.getOne(tenantP.id)
		}
		// Update properties. Would be more convenient to use an object mapping tool like dozer, rather than to map individual fields one at a time
		target.weeklyRentAmount = tenantP.weeklyRentAmount
		if (tenantP.currentRentPaidToDate != null) {
			target.currentRentPaidToDate = tenantP.currentRentPaidToDate
		}
		if (tenantP.currentRentCreditAmount != null) {
			target.currentRentCreditAmount = tenantP.currentRentCreditAmount
		}
		return target
	}

	/**
     * Maps from the JPA [rentReceipt] to the REST API object
     * @return the REST API model object
     */
	fun toModel(rentReceipt: RentReceipt): RentReceiptP {
		val targetP = RentReceiptP(id = rentReceipt.id,
				amount = rentReceipt.amount,
				creationDate = rentReceipt.creationDate,
				tenantId = rentReceipt.tenant?.id)
		return targetP
	}

	/**
     * Maps from the REST API [rentReceiptP] to the JPA entity
     * @return the JPA entity
     */
	fun toEntity(rentReceiptP: RentReceiptP): RentReceipt {
		var target: RentReceipt = RentReceipt(rentReceiptP.id, rentReceiptP.amount)
		// Check if the RentReceipt exists in the database
		if (rentReceiptP.id != null) {
			//Retrieve it
			target = rentReceiptRepo.getOne(rentReceiptP.id)
		}
		// Not going to update values because this will mean that the Tenant will need to be updated? Extension?
		return target
	}
}