package com.example.classroom.common.animations

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SlideAnimation(
    isVisible: Boolean,
    offsetX: Dp = 100.dp,
    content: @Composable (Modifier) -> Unit
) {
    val offset by animateDpAsState(
        targetValue = if (isVisible) 0.dp else offsetX,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    content(Modifier.offset(x = offset))
}
