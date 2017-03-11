package com.homanma.accountbackend.controller

import com.homanma.accountbackend.domain.RentReceiptP
import com.homanma.accountbackend.service.RentReceiptService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * RentReceipt REST API
 * GET /rent/rent-receipt/{id}
 *
 *
 * @property rentReceiptService for manipulating rentReceipts
 * @constructor Creates a RentReceiptController
 */
@RestController
@RequestMapping("/rent-receipt")
class RentReceiptController(val rentReceiptService: RentReceiptService) {

	@RequestMapping(value = "/{id}", method = arrayOf(RequestMethod.GET))
	fun get(@PathVariable id: Long): RentReceiptP {
		return rentReceiptService.get(id)
	}
}