package com.techquantum.tqdkhata.model.constants

object AppConstants {
    const val DATABASE_VERSION = 2
    const val DEFAULT_CITY = "Bharuch"

    // Broadcast Intent Actions
    const val ACTION_REMINDER_ALERT = "com.techquantum.tqdkhata.ACTION_REMINDER_ALERT"
    const val ACTION_MARK_DONE = "com.techquantum.tqdkhata.ACTION_MARK_DONE"

    // Reminder Intent Extras
    const val EXTRA_REMINDER_ID = "reminderId"
    const val EXTRA_CLIENT_ID = "clientId"
    const val EXTRA_CLIENT_NAME = "clientName"
    const val EXTRA_CLIENT_PHONE = "clientPhone"
    const val EXTRA_TITLE = "title"
    const val EXTRA_NOTES = "notes"
}
