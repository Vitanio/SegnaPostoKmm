package com.example.segnaposto

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform