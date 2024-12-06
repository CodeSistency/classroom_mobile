package com.example.classroom.common.animations

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

object AnimationUtils {
    val easeOut = tween<Float>(durationMillis = 300, easing = FastOutSlowInEasing)
    val bounce = spring<Float>(dampingRatio = Spring.DampingRatioMediumBouncy)
}