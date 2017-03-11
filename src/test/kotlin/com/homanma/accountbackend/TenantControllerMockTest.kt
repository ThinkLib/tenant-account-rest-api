package com.homanma.accountbackend

import com.homanma.accountbackend.domain.Tenant
import com.homanma.accountbackend.domain.TenantP
import com.homanma.accountbackend.service.ModelToEntityTransformer
import com.homanma.accountbackend.service.RentReceiptRepository
import com.homanma.accountbackend.service.TenantRepository
import com.homanma.accountbackend.service.TenantServiceImpl
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.Date
import java.util.Optional

class TenantServiceMockTest {

	@InjectMocks
	lateinit var service: TenantServiceImpl

	@Mock
	lateinit var repository: TenantRepository

	@Mock
	lateinit var rentReceiptRepository: RentReceiptRepository

	@Mock
	lateinit var modelToEntityTransformer: ModelToEntityTransformer

	lateinit var homan: Tenant
	lateinit var isaac: Tenant

	@Before
	fun setup() {
		homan = Tenant(1L, "Homan", 500.0)
		isaac = Tenant(1L, "Isaac", 500.0)

		MockitoAnnotations.initMocks(this)
	}

	// Test to make sure the queryParameter to /Tenants uses the correct repository methods
	// If getAll with no arguments, it returns a set with Homan
	// If getAll with an argument of 3 hours, make sure that the time difference past to the reposity is exactly 3 hours in seconds
	@Test
	fun testCorrectRepositoryMethodCalled() {
		Mockito.`when`(repository.findAll()).thenReturn(mutableListOf(homan))

		val result: List<TenantP> = service.getAll(Optional.empty())
		Assert.assertEquals(1, result.size)


		var startDate = com.nhaarman.mockito_kotlin.argumentCaptor<Date>()
		var endDate = com.nhaarman.mockito_kotlin.argumentCaptor<Date>()
		Mockito.`when`(repository.findDistinctByRentReceiptsCreationDateBetween(startDate.capture(), endDate.capture())).thenReturn(mutableListOf(homan, isaac))

		val numberOfHours = 3
		val result2: List<TenantP> = service.getAll(Optional.of(numberOfHours))
		Assert.assertEquals(2, result2.size)

		// Assert that the time difference is correct and is exactly 2 hours
		Assert.assertEquals(numberOfHours * 60 * 60 * 1000L, endDate.firstValue.time - startDate.firstValue.time)

		Mockito.verify(repository)
	}
}