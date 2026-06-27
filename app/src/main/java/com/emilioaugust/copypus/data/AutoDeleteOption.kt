package com.emilioaugust.copypus.data

enum class AutoDeleteOption(val title: String) {
    NEVER("Never"),
    ONE_HOUR("1 hour"),
    FIVE_HOURS("5 hours"),
    ONE_DAY("1 day"),
    ONE_WEEK("1 week"),
    ONE_MONTH("1 month");

    fun toMillis(): Long {
        return when (this) {
            NEVER -> Long.MAX_VALUE
            ONE_HOUR -> 60 * 60 * 1000L
            FIVE_HOURS -> 5 * 60 * 60 * 1000L
            ONE_DAY -> 24 * 60 * 60 * 1000L
            ONE_WEEK -> 7 * 24 * 60 * 60 * 1000L
            ONE_MONTH -> 30L * 24 * 60 * 60 * 1000L
        }
    }
}