package com.example.classroom.common.animations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale

@Composable
fun ScaleAnimation(
    targetScale: Float = 1.2f,
    animationDuration: Int = 500,
    content: @Composable (Modifier) -> Unit
) {
    val scale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetScale,
            animationSpec = tween(animationDuration, easing = LinearOutSlowInEasing)
        )
    }

    content(Modifier.scale(scale.value))
}
