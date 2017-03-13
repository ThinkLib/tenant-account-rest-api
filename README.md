# tenant-account-rest-api
@homan.ma 2017

An API to record and manage tenants and their rent payments. 

This is build using the following technologies:
* Kotlin
* Spring Boot
* Spring Data JPA framework
* H2 in-memory database
* Built using Maven

## Build and Execute

This is built through Maven. In the root of the project execute: 

```
mvn clean install
```

To Execute the application

```
java -jar ./target/accountbackend-0.0.1-SNAPSHOT.jar
```

## REST api

This is a list of all the REST endpoints created. 
**Note: There is a Postman collection that can be used to MANUALLY test the endpoints**

Fetches all Tenants
 ```
 GET /tenant
 ```
Fetches all Tenants with a Rent Receipt that was created within the last X hours (paidInTheLastNumberOfHour)
 ```
 GET /tenant?paidInTheLastNumberOfHour=?
 ```
 Fetches a Tenant by ID
 ```
 GET /tenant/{tenantId}
 ```
Creates a new tenant
 ```
 POST /tenant
 ```
Add a single Rent Receipt for a single Tenant
 ```
 POST /tenant/{tenantId}/rent-receipt
 ```
Fetches all Rent Receipts for a single Tenant
 ```
 GET /tenant/{tenantId}/rent-receipt
 ```
Fetches a Rent Receipt for a Tenant by ID
 ```
 GET /tenant/{tenantId}/rent-receipt/{rentReceiptId}
 ```
Fetches a Rent Receipt by ID
 ```
 GET /rent-receipt/{rentReceiptId}
 ```
## Testing 

Contains examples of the following:
* Integration Tests
* Mockito Test - verify the execution path and check arguments of the overloaded query
* Unit Test

## Sample Payloads
Tenant
 ```json
{
  "name": "name",
  "weeklyRentAmount": 100,
  "currentRentPaidToDate":"2016-01-01",
  "currentRentCreditAmount": 0
}
 ```
Rent Receipt
 ```json
{
	"amount": 600
} 
 ```
