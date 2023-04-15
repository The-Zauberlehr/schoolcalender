package com.example.schoolcalender

import okhttp3.Headers
import javax.xml.transform.Result

data class QuoteList(
    val count: Int,
    val lastItemIndex: Int,
    val page: Int,
    val headers: Headers,
    val results: List<Results>,
    val totalCount: Int,
    val totalPages: Int
)
