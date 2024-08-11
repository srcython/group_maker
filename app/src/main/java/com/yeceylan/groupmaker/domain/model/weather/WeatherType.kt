package com.yeceylan.groupmaker.domain.model.weather

import com.yeceylan.groupmaker.R

object WeatherType {

    val weatherIconMap = mapOf(

        "Güneşli" to R.drawable.ic_sunny,
        "Açık" to R.drawable.ic_moon,

        "Parçalı Bulutlu" to R.drawable.ic_cloudy,
        "Az Bulutlu" to R.drawable.ic_cloudy,
        "Bulutlu" to R.drawable.ic_cloudy,
        "Çok Bulutlu" to R.drawable.ic_very_cloudy,

        "Sisli" to R.drawable.ic_foggy,
        "Yoğun Sis" to R.drawable.ic_foggy,
        "Dumanlı" to R.drawable.ic_foggy,
        "Puslu" to R.drawable.ic_foggy,
        "Dondurucu sis" to R.drawable.ic_foggy,

        "Bölgesel düzensiz yağmur yağışlı" to R.drawable.ic_rainy,
        "Hafif Yağmur" to R.drawable.ic_rainy,
        "Orta Şiddetli Yağmur" to R.drawable.ic_rainy,
        "Yağmur" to R.drawable.ic_rainy,
        "Hafif Sağanak Yağmur" to R.drawable.ic_rainy,
        "Sağanak Yağmur" to R.drawable.ic_rainshower,
        "Şiddetli Sağanak Yağmur" to R.drawable.ic_rainshower,
        "Çiseleme" to R.drawable.ic_rainy,
        "Yer yer Sağanak Yağmur" to R.drawable.ic_rainy,
        "Orta kuvvetli veya yoğun sağnak yağışlı" to R.drawable.ic_rainy,
        "Hafif çisenti" to R.drawable.ic_rainy,
        "Düzensiz hafif çisenti" to R.drawable.ic_rainy,
        "Düzensiz hafif yağmurlu" to R.drawable.ic_rainy,

        "Gök Gürültülü Sağanak Yağış" to R.drawable.ic_rainythunder,
        "Hafif Gök Gürültülü Sağanak Yağış" to R.drawable.ic_rainythunder,
        "Bölgesel gök gürültülü düzensiz hafif yağmur" to R.drawable.ic_rainythunder,
        "Bölgesel gök gürültülü orta kuvvetli veya şiddetli yağış" to R.drawable.ic_rainythunder,

        "Hafif Kar Yağışı" to R.drawable.ic_snowy,
        "Kar Yağışı" to R.drawable.ic_snowy,
        "Yoğun Kar Yağışı" to R.drawable.ic_snowy,
        "Karla Karışık Yağmur" to R.drawable.ic_snowyrainy,
        "Hafif Karla Karışık Yağmur" to R.drawable.ic_snowyrainy,
        "Yer yer Kar Yağışı" to R.drawable.ic_snowy,
        "Orta Şiddetli Kar" to R.drawable.ic_snowy,
        "Düzensiz yoğun kar yağışlı" to R.drawable.ic_snowy,
        "Kar Fırtınası" to null,
        "Tipi" to R.drawable.ic_snowy,
        "Orta kuvvetli veya yoğun karla karışık sağnak yağış" to R.drawable.ic_snowyrainy,
        "Hafif sağnak şeklinde kar" to R.drawable.ic_snowyrainy,
        "Orta kuvvetli veya yoğun ve sağnak şeklinde kar" to R.drawable.ic_snowyrainy,
        "Düzensiz orta kuvvetli karlı" to R.drawable.ic_snowy,

        "Dolu" to R.drawable.ic_rainy,
        "Buzlu Yağmur" to R.drawable.ic_rainy,

        "Rüzgarlı" to R.drawable.ic_wind,
        "Fırtına" to R.drawable.ic_thunder,
        "Toz Fırtınası" to R.drawable.ic_thunder,

        "Tornado" to R.drawable.ic_thunder,
        "Kasırga" to R.drawable.ic_windy,

        "Basınç" to R.drawable.ic_pressure,
        "Karla Karışık Yağmur" to R.drawable.ic_snowyrainy
    )

}
