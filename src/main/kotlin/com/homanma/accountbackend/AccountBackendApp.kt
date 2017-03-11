package com.homanma.accountbackend

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("com.homanma.accountbackend")
open class AccountBackendApp

fun main(args: Array<String>) {
    SpringApplication.run(AccountBackendApp::class.java, *args)
}


