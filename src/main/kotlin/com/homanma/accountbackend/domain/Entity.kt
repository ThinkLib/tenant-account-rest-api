package com.homanma.accountbackend.domain

import java.io.Serializable
import java.time.LocalDate
import java.util.Date
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Temporal
import javax.persistence.TemporalType

/**
 * Represents a Tenant and his rent status.
 *
 *
 * @property id identifier for the tenant
 * @property name name of the tenant
 * @property weeklyRentAmount how much the tenant is being charged rent
 * @property currentRentPaidToDate the date when the tenant has paid till. This is based on the Rent Receipts
 * @property currentRentCreditAmount the amount in positive the tenant has paid for but not enough for a week's rent
 * @constructor Creates an empty group.
 */
@Entity(name = "TENANT")
data class Tenant(@Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long?,
				  @Column(name = "CREATION_DATE", insertable = true, updatable = false) @Temporal(TemporalType.TIMESTAMP) var creationDate: Date?,
				  @Column(name = "NAME", nullable = false) val name: String,
				  @Column(name = "WEEKLY_RENT_AMOUNT", nullable = false) var weeklyRentAmount: Double,
				  @Column(name = "CURR_RENT_PAID_TO_DATE", nullable = false) var currentRentPaidToDate: LocalDate = LocalDate.now(),
				  @Column(name = "CURR_RENT_CREDIT_AMT", nullable = false) var currentRentCreditAmount: Double = 0.0,
				  @OneToMany(mappedBy = "tenant", cascade = ALL, orphanRemoval = true) val rentReceipts: MutableList<RentReceipt>) : Serializable {
	constructor(id: Long?, name: String, weeklyRentAmount: Double) : this(id, null, name, weeklyRentAmount, LocalDate.now(), 0.0, mutableListOf()) {
	}
}

@Entity(name = "RENT_RECEIPT")
data class RentReceipt(@Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long?,
					   @Column(name = "CREATION_DATE", insertable = true, updatable = false) @Temporal(TemporalType.TIMESTAMP) var creationDate: Date?,
					   @Column(name = "AMOUNT", nullable = false) val amount: Double,
					   @ManyToOne(fetch = FetchType.LAZY, cascade = REFRESH)
					   @JoinColumn(name = "TENANT_ID") var tenant: Tenant?) : Serializable {
	constructor(id: Long?, amount: Double) : this(id, null, amount, null) {
	}
}

