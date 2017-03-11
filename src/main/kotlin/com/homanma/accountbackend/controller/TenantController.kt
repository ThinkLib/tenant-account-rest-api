package com.homanma.accountbackend.controller

import com.homanma.accountbackend.domain.RentReceiptP
import com.homanma.accountbackend.domain.TenantP
import com.homanma.accountbackend.service.RentReceiptService
import com.homanma.accountbackend.service.TenantService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.Optional

/**
 * Tenant REST API
 * GET /tenant - Returns all the tenants
 * GET /tenant?paidInTheLastNumberOfHour=? - Returns the list of tenants who have a rentReceipt in the last x hours
 * POST /tenant - Creates a new tenant
 * POST /tenant/{tenantId}/rent-receipt - Adds a new rent receipt for the given tenant
 * GET /tenant/{tenantId}/rent-receipt - Gets the list of rent-receipts for a given tenant
 * GET /tenant/{tenantId}/rent-receipt/{rentReceiptId} - Gets the rent-receipts represented by the id for the given tenant
 *
 * @property tenantService for manipulating tenants
 * @property rentReceiptService for manipulating rentReceipts
 * @constructor Creates a RentReceiptController
 */
@RestController
@RequestMapping("/tenant")
class TenantController(val tenantService: TenantService, val rentReceiptService: RentReceiptService) {

	@RequestMapping(value = "/{tenantId}", method = arrayOf(RequestMethod.GET))
	fun get(@PathVariable tenantId: Long): TenantP {
		return tenantService.get(tenantId)
	}

	@RequestMapping(method = arrayOf(RequestMethod.GET))
	fun query(@RequestParam("paidInTheLastNumberOfHour") paidInTheLastNumberOfHour: Optional<Int>): List<TenantP> {
		return tenantService.getAll(paidInTheLastNumberOfHour)
	}

	@RequestMapping(method = arrayOf(RequestMethod.POST))
	fun create(@RequestBody tenantP: TenantP): TenantP {
		return tenantService.save(tenantP)
	}

	@RequestMapping(value = "/{tenantId}/rent-receipt", method = arrayOf(RequestMethod.POST))
	fun addRentReceipt(@PathVariable tenantId: Long, @RequestBody rentReceipt: RentReceiptP): RentReceiptP {
		return tenantService.addRentReceipt(tenantId, rentReceipt)
	}

	@RequestMapping(value = "/{tenantId}/rent-receipt", method = arrayOf(RequestMethod.GET))
	fun queryRentReceiptsForTenant(@PathVariable tenantId: Long): List<RentReceiptP> {
		return rentReceiptService.getAllByTenantId(tenantId)
	}

	@RequestMapping(value = "/{tenantId}/rent-receipt/{rentReceiptId}", method = arrayOf(RequestMethod.GET))
	fun getRentReceiptByIdForTenant(@PathVariable rentReceiptId: Long): RentReceiptP {
		return rentReceiptService.get(rentReceiptId)
	}

}