package com.example.classroom.common.animations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BounceAnimation(
    content: @Composable (Modifier) -> Unit
) {
    val bounceHeight = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        bounceHeight.animateTo(
            targetValue = 20f,
            animationSpec = keyframes {
                durationMillis = 1000
                10f at 250
                20f at 500
                10f at 750
                0f at 1000
            }
        )
    }

    content(Modifier.offset(y = bounceHeight.value.dp))
}
