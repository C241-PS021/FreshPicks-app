package com.mobile.freshpicks.data.pref

import android.media.Image

data class ScanResult (
    val result: ResultItem
)

data class ResultItem(
    val image: Image,
    val fruitName: String,
    val scanResult: String
)