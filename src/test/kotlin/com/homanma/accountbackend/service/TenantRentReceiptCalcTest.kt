package com.homanma.accountbackend.service

import com.homanma.accountbackend.domain.RentReceipt
import com.homanma.accountbackend.domain.Tenant
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.time.LocalDate
import kotlin.test.assertEquals

@RunWith(JUnitPlatform::class)
object TenantRentReceiptCalcSpek : Spek({

	describe("a tenant called Homan") {
		var homan = Tenant(null, null, "Homan", 300.0, LocalDate.of(2016, 10, 14), 50.0, mutableListOf())

		on("adding a new rent receipt of 275") {
			var rentReceipt = RentReceipt(null, 275.0)
			homan.addRentReceipt(rentReceipt)

			it("should increment the paid to date to 2016-10-21 and have a currentRentCreditAmount of 25") {
				assertEquals(25.0, homan.currentRentCreditAmount)
				assertEquals(LocalDate.of(2016, 10, 21), homan.currentRentPaidToDate)
			}
		}

		on("adding a new receipt of 274.0") {
			var rentReceipt = RentReceipt(null, 274.0)
			homan.addRentReceipt(rentReceipt)

			it("should keep the same paid to date but have a currentRentCreditAmount of 299") {
				assertEquals(299.0, homan.currentRentCreditAmount)
				assertEquals(LocalDate.of(2016, 10, 21), homan.currentRentPaidToDate)
			}
		}

		on("adding a receipt of 10,000") {
			var rentReceipt = RentReceipt(null, 10000.0)
			homan.addRentReceipt(rentReceipt)

			it("should have a paid till date of 2017-06-16 but have a currentRentCreditAmount of 99") {
				assertEquals(99.0, homan.currentRentCreditAmount)
				assertEquals(LocalDate.of(2017, 6, 16), homan.currentRentPaidToDate)
			}
		}
	}
})