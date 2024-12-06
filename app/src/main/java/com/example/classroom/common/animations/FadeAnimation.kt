package com.example.classroom.common.animations

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

@Composable
fun FadeAnimation(
    isVisible: Boolean,
    durationMillis: Int = 300,
    content: @Composable (Modifier) -> Unit
) {
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis), label = ""
    )

    content(Modifier.alpha(alpha))
}
