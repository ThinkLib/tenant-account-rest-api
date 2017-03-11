package com.homanma.accountbackend.service

import com.homanma.accountbackend.domain.RentReceipt
import com.homanma.accountbackend.domain.Tenant
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate

class TenantRentReceiptCalcTest {

	@Test
	fun testAddRentReceipt() {
		// Given a tenant and a rent Receipt
		var homan = Tenant(null, null, "Homan", 300.0, LocalDate.of(2016, 10, 14), 50.0, mutableListOf())
		var rentReceipt = RentReceipt(null, 275.0)
		// When a rent receipt is added
		homan.addRentReceipt(rentReceipt)
		// Then currentRentPaidToDate to date is 2016-10-21
		// and currentRentCreditAmount is 25
		Assert.assertEquals(25.0, homan.currentRentCreditAmount, 0.0)
		Assert.assertEquals(LocalDate.of(2016, 10, 21), homan.currentRentPaidToDate)
	}

	@Test
	fun testAddRentReceipt2() {
		// Given a tenant and a rent Receipt
		var homan = Tenant(null, null, "Homan", 300.0, LocalDate.of(2016, 10, 14), 50.0, mutableListOf())
		var rentReceipt = RentReceipt(null, 249.0)
		// When a rent receipt is added
		homan.addRentReceipt(rentReceipt)
		// Then currentRentPaidToDate to date is 2016-10-14
		// and currentRentCreditAmount is 299
		Assert.assertEquals(299.0, homan.currentRentCreditAmount, 0.0)
		Assert.assertEquals(LocalDate.of(2016, 10, 14), homan.currentRentPaidToDate)
	}

	@Test
	fun testAddRentReceipt3() {
		// Given a tenant and a rent Receipt
		var homan = Tenant(null, null, "Homan", 300.0, LocalDate.of(2016, 10, 14), 50.0, mutableListOf())
		var rentReceipt = RentReceipt(null, 10000.0)
		// When a rent receipt is added
		homan.addRentReceipt(rentReceipt)
		// Then currentRentPaidToDate to date is 2016-10-14
		// and currentRentCreditAmount is 299
		Assert.assertEquals(150.0, homan.currentRentCreditAmount, 0.0)
		Assert.assertEquals(LocalDate.of(2017, 6, 2), homan.currentRentPaidToDate)
	}
}