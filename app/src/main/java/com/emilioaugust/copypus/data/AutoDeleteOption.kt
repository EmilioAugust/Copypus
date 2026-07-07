package com.emilioaugust.copypus.data

import androidx.annotation.StringRes
import com.emilioaugust.copypus.R

enum class AutoDeleteOption(@StringRes val titleRes: Int) {
    NEVER(R.string.never_ad_option),
    ONE_HOUR(R.string._1_hour_ad_option),
    FIVE_HOURS(R.string._5_hours_ad_option),
    ONE_DAY(R.string._1_day_ad_option),
    ONE_WEEK(R.string._1_week_ad_option),
    ONE_MONTH(R.string._1_month_ad_option);

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