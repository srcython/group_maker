package com.yeceylan.groupmaker.domain.model.weather

data class ForecastDay(
    val date: String,
    val hour: List<Hour>
)