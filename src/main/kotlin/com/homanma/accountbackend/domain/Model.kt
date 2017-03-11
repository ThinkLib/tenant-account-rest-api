package com.homanma.accountbackend.domain

import java.time.LocalDate
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Entity
import java.io.Serializable
import java.util.Date
import javax.persistence.OneToMany
import javax.persistence.Column
import javax.persistence.Temporal
import javax.persistence.TemporalType

annotation class NoArgConstructor

/**
 * Represents a Tenant and his rent status. 
 * Used for the REST API interface. Mapped from the JPA Entity to a similar structured object to present.

 * the rentReceipts attribute is abstracted to the just the ids, rather than the full object as to now pollute the Tenant object payload.
 *
 * @property id identifier for the tenant
 * @property name name of the tenant
 * @property weeklyRentAmount how much the tenant is being charged rent
 * @property currentRentPaidToDate the date when the tenant has paid till. This is based on the Rent Receipts
 * @property currentRentCreditAmount the amount in positive the tenant has paid for but not enough for a week's rent
 * @property rentReceiptIds the ids of the rentReceipts associated with a Tenant
 * @constructor Creates a Tenant representation.
 */
@NoArgConstructor
data class TenantP(val id: Long?, val name: String, val weeklyRentAmount: Double, val currentRentPaidToDate: LocalDate?, val currentRentCreditAmount: Double?, val rentReceiptIds: List<Long>?) : Serializable

/**
 * Represents a Tenant and his rent status. Mapped from the JPA Entity to a similar structured object to present.
 * Used for the REST API interface to represent a receipt
 *
 * @property id identifier for the tenant
 * @property creationDate timeInMillis(java.util.Date) that represents when the rent receipt was created/lodged.
 * @property amount the amount on the rentReceipt
 * @property tenantId the id of the tenant that the rentReceipt is for.
 * @constructor Creates a RentReceipt.
 */
@NoArgConstructor
data class RentReceiptP(val id: Long?, val creationDate: Date?, val amount: Double, val tenantId: Long?) : Serializable