package com.homanma.accountbackend.domain

import javax.persistence.AttributeConverter
import java.time.LocalDate
import java.util.Date
import java.time.Instant
import javax.persistence.Converter

/**
 * JPA Converter so that LocalDate can be stored into a normal Date
 *
 **/
@Converter(autoApply = true)
class LocalDateConverter : AttributeConverter<LocalDate, Date>{
	
	/**
     * Takes a Date and transforms it into a LocalDate.
     * @return the date transformed into LocalDate
     */
	override fun convertToEntityAttribute(dbData: Date): LocalDate {
		return java.sql.Date(dbData.getTime()).toLocalDate()
	}

	/**
     * Takes a LocalDate and transforms it into a Date.
     * @return the localDate transformed into a Date
     */
	override fun convertToDatabaseColumn(date: LocalDate): Date {
		return java.sql.Date.valueOf(date)
	}
}