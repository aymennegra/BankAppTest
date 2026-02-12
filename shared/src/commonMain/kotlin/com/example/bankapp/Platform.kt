package com.example.bankapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
