package com.example.job_test.utility

import android.os.Build
import java.util.*
import java.text.SimpleDateFormat



    fun timeSince(dateString: String): String {

        val utcFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        utcFormat.timeZone = TimeZone.getTimeZone("UTC")

        // Parse the input date string
        val parsedDate: Date? = utcFormat.parse(dateString)

        // Get the current time in IST (India Standard Time)
        val now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata")).time

        // Calculate the difference in milliseconds
        val difference = now.time - (parsedDate?.time ?: 0L)

        // Convert the difference into days, hours, minutes, and seconds
        val seconds = difference / 1000 % 60
        val minutes = difference / (1000 * 60) % 60
        val hours = difference / (1000 * 60 * 60) % 24
        val days = difference / (1000 * 60 * 60 * 24)

        // Return a human-readable format
        return when {
            days > 0 -> "$days days, $hours hours ago"
            hours > 0 -> "$hours hours, $minutes minutes ago"
            minutes > 0 -> "$minutes minutes ago"
            else -> "$seconds seconds ago"
        }
    }
