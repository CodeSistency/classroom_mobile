package com.example.classroom.common.animations.gsap

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween

data class AnimationProperty(
    val startValue: Float,
    val endValue: Float,
    val duration: Int = 1000,
    val easing: Easing = FastOutSlowInEasing
)

class Animator(
    val property: AnimationProperty,
    val onUpdate: (Float) -> Unit,
    val onComplete: (() -> Unit)? = null
) {
    suspend fun animate() {
        val animatable = Animatable(property.startValue)

        // Calculate progress based on scroll position
        val progress = (property.endValue - property.startValue) / property.duration
        val targetValue = property.endValue

        animatable.animateTo(
            targetValue = targetValue,
            animationSpec = tween(
                durationMillis = property.duration,
                easing = property.easing
            )
        )

        onUpdate(animatable.value)
        onComplete?.invoke()
    }
}



class GSAPAnimator(
    val property: AnimationProperty,
    val onUpdate: (Float) -> Unit,
    val onComplete: (() -> Unit)? = null
) {
    suspend fun animate() {
        val animatable = Animatable(property.startValue)

        // Calculate progress based on scroll position
        val progress = (property.endValue - property.startValue) / property.duration
        val targetValue = property.endValue

        animatable.animateTo(
            targetValue = targetValue,
            animationSpec = tween(
                durationMillis = property.duration,
                easing = property.easing
            )
        )

        onUpdate(animatable.value)
        onComplete?.invoke()
    }
}