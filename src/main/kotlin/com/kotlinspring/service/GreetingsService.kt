package com.kotlinspring.service

import org.springframework.stereotype.Service

@Service
class GreetingsService {

    fun retrieveGreetings(name: String) = "Hello $name"

}