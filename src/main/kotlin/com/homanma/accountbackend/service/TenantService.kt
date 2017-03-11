package com.homanma.accountbackend.service

import com.homanma.accountbackend.domain.RentReceipt
import com.homanma.accountbackend.domain.RentReceiptP
import com.homanma.accountbackend.domain.Tenant
import com.homanma.accountbackend.domain.TenantP
import org.springframework.stereotype.Service
import java.util.Date
import java.util.Optional
import javax.transaction.Transactional

interface TenantService {
	fun save(tenantP: TenantP): TenantP
	fun get(id: Long): TenantP
	fun getAll(paidInTheLastNumberOfHour: Optional<Int>?): List<TenantP>
	fun addRentReceipt(id: Long, rentReceiptP: RentReceiptP): RentReceiptP
}

@Service
class TenantServiceImpl(val tenantRepository: TenantRepository, val rentReceiptRepository: RentReceiptRepository, val modelToEntityTransformer: ModelToEntityTransformer) : TenantService {

	/**
     * Saves the [tenant] to the DB
     * @return the REST API model object
     */
	@Transactional
	override fun save(tenantP: TenantP): TenantP {
		var tenant = modelToEntityTransformer.toEntity(tenantP)
		if (tenant.creationDate == null) {
			tenant.creationDate = Date()
		}
		tenantRepository.save(tenant)
		return modelToEntityTransformer.toModel(tenant)
	}

	/**
     * Returns the tenant refenced by the id [id]
     * @return the REST API model object
     */
	override fun get(id: Long): TenantP {
		return modelToEntityTransformer.toModel(tenantRepository.findOne(id))
	}

	/**
     * Query to get [tenant]s 
     * @return the resultset of tenants
     */
	override fun getAll(paidInTheLastNumberOfHour: Optional<Int>?): List<TenantP> {
		var matches: List<Tenant>
		if (paidInTheLastNumberOfHour!!.isPresent()) {
			var currentTimeInMillis = System.currentTimeMillis()
			var endDate = Date(currentTimeInMillis)
			var startDate = Date(currentTimeInMillis - (paidInTheLastNumberOfHour.get() * 60 * 60 * 1000))
			matches = tenantRepository.findDistinctByRentReceiptsCreationDateBetween(startDate, endDate)
		} else {
			matches = tenantRepository.findAll()
		}
		return matches.map { tenant -> modelToEntityTransformer.toModel(tenant) }
	}

	/**
     * Adds the [rentReceiptP] to the tenant referenced by [id]
     * @return the recorded RentReceipt
     */
	@Transactional
	override fun addRentReceipt(id: Long, rentReceiptP: RentReceiptP): RentReceiptP {
		var rentReceipt = modelToEntityTransformer.toEntity(rentReceiptP)
		if (rentReceipt.creationDate == null) {
			rentReceipt.creationDate = Date()
		}

		var tenant = tenantRepository.findOne(id)
		tenant.addRentReceipt(rentReceipt)
		rentReceiptRepository.save(rentReceipt)
		tenantRepository.save(tenant)
		return modelToEntityTransformer.toModel(rentReceipt)
	}
}

/**
  * Records a [rentReceipt] against the client[this]
  */
fun Tenant.addRentReceipt(rentReceipt: RentReceipt) {
	// Make sure we dont divide by 0 : )
	if(this.weeklyRentAmount == 0.0) {
		this.currentRentCreditAmount = rentReceipt.amount + this.currentRentCreditAmount
	} else {
		// Update Tenant
		// 1) Calculate how much credit in total = rentReceipt.amount + tenant.currentRentCredit
		var totalCredit = rentReceipt.amount + this.currentRentCreditAmount
		// 2) Calculate the number of weeks paid fully = (totalCredit)/tenant.weeklyRentAmount round down
		var numWeeksPaidFully = Math.floor(totalCredit / this.weeklyRentAmount).toLong()
		// 3) Calculate the credit left = totalCredit - weeksPaidFully*weeklyRentAm
		this.currentRentCreditAmount = totalCredit - numWeeksPaidFully * this.weeklyRentAmount
		// 4) Update rentPaidToDate by weeksPaidFully weeks
		this.currentRentPaidToDate = this.currentRentPaidToDate.plusWeeks(numWeeksPaidFully)
	}

	// Add the rentReceipt to the tenant
	this.rentReceipts.add(rentReceipt)
	rentReceipt.tenant = this
}