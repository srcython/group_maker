package com.yeceylan.groupmaker.domain.model.weather

data class Hour(
    val time: String,
    val temp_c: Double,
    val condition: Condition,
    val precip_mm: Double,
)