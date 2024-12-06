package com.orbitalsonic.offlineprayertime.models

/**
 * Represents the prayer times for a specific day.
 *
 * @property prayerName The name of the prayer (e.g., Fajr, Dhuhr, etc.).
 * @property prayerTime The time of the prayer, formatted as per user settings.
 * @property isCurrentPrayer Indicates whether this prayer is currently active.
 */
data class PrayerTimesItem(
    val prayerName: String,
    val prayerTime: String,
    val isCurrentPrayer: Boolean = false
)

