package com.example.classroom.common.animations.motion

import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
// Define AnimationVariants (e.g., scale, fade, move)
enum class AnimationVariant {
    SCALE_UP, SCALE_DOWN, FADE_IN, FADE_OUT, MOVE_RIGHT, MOVE_LEFT
}

// Motion Modifier extension function to animate multiple properties
@Composable
fun Modifier.motion(
    variant: AnimationVariant = AnimationVariant.SCALE_UP,
    initialState: Float = 0f,
    targetState: Float = 1f,
    durationMillis: Int = 1000,
    easing: Easing = LinearEasing,
    onExit: (() -> Unit)? = null,
    onTap: (() -> Unit)? = null
): Modifier {
    val transition = rememberInfiniteTransition()

    val scale by transition.animateFloat(
        initialValue = initialState,
        targetValue = targetState,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = easing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Apply fade in/out transition (alpha)
    val alpha by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = easing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Apply translation movement for horizontal position
    val translationX by transition.animateFloat(
        initialValue = 0f,
        targetValue = if (variant == AnimationVariant.MOVE_RIGHT) 200f else -200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = easing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Exit animation trigger (after the transition completes)
    LaunchedEffect(transition) {
        if (onExit != null) {
            kotlinx.coroutines.delay(durationMillis.toLong())
            onExit()
        }
    }
    // Apply variant-based transformations
    return this
        .pointerInput(Unit) {
            detectTapGestures { onTap?.invoke() }
        }
        .graphicsLayer(
            scaleX = if (variant == AnimationVariant.SCALE_UP || variant == AnimationVariant.SCALE_DOWN) scale else 1f,
            scaleY = if (variant == AnimationVariant.SCALE_UP || variant == AnimationVariant.SCALE_DOWN) scale else 1f,
            alpha = if (variant == AnimationVariant.FADE_IN || variant == AnimationVariant.FADE_OUT) alpha else 1f,
            translationX = translationX
        )
}

// Main composable that applies motion modifier
@Composable
fun MotionComponent(
    variant: AnimationVariant = AnimationVariant.SCALE_UP,
    initialState: Float = 0f,
    targetState: Float = 1f,
    durationMillis: Int = 1000,
    onExit: (() -> Unit)? = null,
    onTap: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .motion(variant = variant, initialState = initialState, targetState = targetState, durationMillis = durationMillis, onExit = onExit, onTap = onTap)
            .fillMaxSize()
    ) {
        Text(
            text = "Hello Motion",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

// Usage of the MotionComponent with variants
@Composable
fun MotionExample() {
    var tapped by remember { mutableStateOf(false) }

    MotionComponent(
        variant = if (tapped) AnimationVariant.FADE_OUT else AnimationVariant.FADE_IN,
        initialState = if (tapped) 1f else 0f,
        targetState = if (tapped) 0f else 1f,
        durationMillis = 1500,
        onExit = { println("Animation Exited!") },
        onTap = { tapped = !tapped }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MotionExample()
}