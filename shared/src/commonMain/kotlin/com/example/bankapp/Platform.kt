package com.example.bankapp

import io.ktor.client.HttpClient

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect object HttpClientFactory {
    fun create(): HttpClient
}
