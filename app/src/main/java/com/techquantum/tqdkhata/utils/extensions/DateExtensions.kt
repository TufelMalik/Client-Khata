package com.techquantum.tqdkhata.utils.extensions

import com.techquantum.tqdkhata.utils.helpers.DateUtils

fun Long?.toFormattedDate(): String = DateUtils.formatDate(this)

fun Long?.toFormattedTime(): String = DateUtils.formatTime(this)

fun Long?.toFormattedDateTime(): String = DateUtils.formatDateTime(this)

fun Long?.isToday(): Boolean = DateUtils.isToday(this)

fun Long?.isPast(): Boolean = DateUtils.isPast(this)
