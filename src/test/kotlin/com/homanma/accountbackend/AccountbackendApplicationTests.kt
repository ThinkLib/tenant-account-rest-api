package com.homanma.accountbackend

import com.homanma.accountbackend.domain.RentReceipt
import com.homanma.accountbackend.domain.TenantP
import com.homanma.accountbackend.service.TenantService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.test.context.junit4.SpringRunner
import java.net.URI
import java.time.LocalDate
import com.homanma.accountbackend.domain.Tenant

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AccountbackendApplicationTest() {

	@Autowired
	lateinit var restTemplate: TestRestTemplate

	@Autowired
	lateinit var tenantService: TenantService

	lateinit var homan: TenantP
	lateinit var isaac: TenantP
	lateinit var scrub: TenantP

	@Before
	fun setUp() {
		homan = TenantP(null, "Homan", 300.0, LocalDate.of(2016, 10, 14), 50.0, null)
		isaac = TenantP(null, "Isaac", 300.0, null, null, null)
		scrub = TenantP(null, "Scrub", 1000.0, null, null, null)
		tenantService.save(homan)
		tenantService.save(isaac)
	}

	inline fun <reified T : Any> typeRef(): ParameterizedTypeReference<T> = object : ParameterizedTypeReference<T>() {}
	
	// Integration Test to test the GET /tenant
	// Should return 2
	@Test
	fun testTenantGetAll() {
		val request = RequestEntity<Any>(HttpMethod.GET, URI("/tenant"))
		var body = restTemplate.exchange(request, typeRef<List<TenantP>>()).body
		assertEquals(2, body.size)

		var retrievedHoman = body.get(0)
		assertNotNull(retrievedHoman.id)
		assertEquals(homan.name, retrievedHoman.name)
		assertEquals(homan.weeklyRentAmount, retrievedHoman.weeklyRentAmount, 0.0)
		assertEquals(homan.currentRentPaidToDate, retrievedHoman.currentRentPaidToDate)
		assertEquals(homan.currentRentCreditAmount, retrievedHoman.currentRentCreditAmount)

		var retrievedIsaac = body.get(1)
		assertNotNull(retrievedIsaac.id)
		assertEquals(isaac.name, retrievedIsaac.name)
		assertEquals(isaac.weeklyRentAmount, retrievedIsaac.weeklyRentAmount, 0.0)
		assertEquals(LocalDate.now(), retrievedIsaac.currentRentPaidToDate)
		assertEquals(0.0, retrievedIsaac.currentRentCreditAmount)
	}

	// Integration Test to test GET /tenant{id}
	@Test
	fun testTenantGetOne() {
		var homanid = 1L
		val request = RequestEntity<Any>(HttpMethod.GET, URI("/tenant/$homanid"))
		var retrievedHoman = restTemplate.exchange(request, typeRef<TenantP>()).body
		assertEquals(homanid, retrievedHoman.id)
		assertEquals(homan.name, retrievedHoman.name)
		assertEquals(homan.weeklyRentAmount, retrievedHoman.weeklyRentAmount, 0.0)
		assertEquals(homan.currentRentPaidToDate, retrievedHoman.currentRentPaidToDate)
		assertEquals(homan.currentRentCreditAmount, retrievedHoman.currentRentCreditAmount)

		var isaacid = 2L
		val request2 = RequestEntity<Any>(HttpMethod.GET, URI("/tenant/$isaacid"))
		var retrievedIsaac = restTemplate.exchange(request2, typeRef<TenantP>()).body
		assertNotNull(retrievedIsaac.id)
		assertEquals(isaac.name, retrievedIsaac.name)
		assertEquals(isaac.weeklyRentAmount, retrievedIsaac.weeklyRentAmount, 0.0)
		assertEquals(LocalDate.now(), retrievedIsaac.currentRentPaidToDate)
		assertEquals(0.0, retrievedIsaac.currentRentCreditAmount)
	}

	// Integration Test to test POST /tenant
	@Test
	fun testCreateOne() {
		val request = RequestEntity.post(URI("/tenant")).body(scrub)
		var retrievedScrub = restTemplate.exchange(request, typeRef<TenantP>()).body
		assertNotNull(retrievedScrub.id)
		assertEquals(scrub.name, retrievedScrub.name)
		assertEquals(scrub.weeklyRentAmount, retrievedScrub.weeklyRentAmount, 0.0)
		assertEquals(LocalDate.now(), retrievedScrub.currentRentPaidToDate)
		assertEquals(0.0, retrievedScrub.currentRentCreditAmount)
	}

	// Integration Test to test adding a receipt to Homan to see if it has changed the Tenant corectly
	@Test
	fun testCreateSingleRentReceipt() {
		var homanid = 1L
		var rentReceipt = RentReceipt(null, 275.0)
		val postRequest = RequestEntity.post(URI("/tenant/$homanid/rent-receipt")).body(rentReceipt)
		restTemplate.exchange(postRequest, typeRef<TenantP>()).body

		val requestHoman = RequestEntity<Any>(HttpMethod.GET, URI("/tenant/$homanid"))
		var retrievedHoman = restTemplate.exchange(requestHoman, typeRef<TenantP>()).body
		assertEquals(homanid, retrievedHoman.id)
		assertEquals(homan.name, retrievedHoman.name)
		assertEquals(homan.weeklyRentAmount, retrievedHoman.weeklyRentAmount, 0.0)
		// These values would have adjusted
		assertEquals(25.0, retrievedHoman.currentRentCreditAmount)
		assertEquals(LocalDate.of(2016, 10, 21), retrievedHoman.currentRentPaidToDate)
		// assertThatAReceiptHasBeenAadded
		assertEquals(1, retrievedHoman.rentReceiptIds?.size)

		// Retrieve all receipts for Homan
		val requestRentReceiptsForHoman = RequestEntity<Any>(HttpMethod.GET, URI("/tenant/$homanid/rent-receipt"))
		var retrievedRentReceiptsForHoman = restTemplate.exchange(requestRentReceiptsForHoman, typeRef<List<RentReceipt>>()).body
		assertEquals(1, retrievedRentReceiptsForHoman.size)
		assertEquals(275.0, retrievedRentReceiptsForHoman.get(0).amount, 0.0)
	}


}
