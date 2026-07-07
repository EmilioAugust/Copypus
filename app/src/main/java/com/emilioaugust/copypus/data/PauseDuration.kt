package com.emilioaugust.copypus.data

import androidx.annotation.StringRes
import com.emilioaugust.copypus.R

enum class PauseDuration(val minutes: Long, @StringRes val titleRes: Int) {
    MIN_15(15, R.string._15_min_pd),
    MIN_30(30, R.string._30_min_pd),
    HOUR_1(60, R.string._1_hour_pd)
}