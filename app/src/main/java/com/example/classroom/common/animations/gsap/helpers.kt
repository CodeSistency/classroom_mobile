package com.example.classroom.common.animations.gsap

import android.animation.Animator
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

// Helper function to define an animation based on scroll position
//fun animateTo(
//    targetValue: Float,
//    scrollRange: Float = 1000f, // Define how far the user has to scroll to complete the animation
//    duration: Int = 1000,
//    easing: Easing = FastOutSlowInEasing,
//    delay: Int = 0,
//    onStart: (() -> Unit)? = null,
//    onUpdate: (Float) -> Unit,
//    onComplete: (() -> Unit)? = null
//): Animator<Float> {
//    val animationProperty = AnimationProperty(
//        startValue = 0f,
//        endValue = targetValue,
//        duration = duration,
//        easing = easing
//    )
//
//    val animator = Animator(animationProperty, onUpdate, onComplete)
//
//    return animator.apply {
//        launch {
//            delay(delay.toLong())
//            onStart?.invoke()
//            animate()
//        }
//    }
//}

@Composable
fun animateTo(
    targetValue: Float,
    scrollRange: Float = 1000f, // Define how far the user has to scroll to complete the animation
    duration: Int = 1000,
    easing: Easing = FastOutSlowInEasing,
    delay: Int = 0,
    onStart: (() -> Unit)? = null,
    onUpdate: (Float) -> Unit,
    onComplete: (() -> Unit)? = null
): GSAPAnimator { // Use GSAPAnimator instead of Animator
    val animationProperty = AnimationProperty(
        startValue = 0f,
        endValue = targetValue,
        duration = duration,
        easing = easing
    )

    val animator = GSAPAnimator(animationProperty, onUpdate, onComplete)

    // Use LaunchedEffect to launch the animation with delay and start
    onStart?.invoke()

    return animator.apply {
        // Using `LaunchedEffect` in a Composable scope
        LaunchedEffect(Unit) {
            delay(delay.toLong()) // Ensure we pass a Long
            animate()
        }
    }
}

// Animate based on scroll position
@Composable
fun animateFrom(
    startValue: Float,
    targetValue: Float,
    scrollPosition: Float, // Scroll position influences the animation
    scrollRange: Float = 1000f, // Define how far the scroll affects the animation
    duration: Int = 1000,
    easing: Easing = FastOutSlowInEasing,
    delay: Int = 0,
    onStart: (() -> Unit)? = null,
    onUpdate: (Float) -> Unit,
    onComplete: (() -> Unit)? = null
): GSAPAnimator {
    val animationProperty = AnimationProperty(
        startValue = startValue + (targetValue - startValue) * (scrollPosition / scrollRange), // Dynamic based on scroll position
        endValue = targetValue,
        duration = duration,
        easing = easing
    )

//    val animator = Animator(animationProperty, onUpdate, onComplete)

    val animator = GSAPAnimator(animationProperty, onUpdate, onComplete)

    // Use LaunchedEffect to launch the animation with delay and start
    onStart?.invoke()

    return animator.apply {
//        launch {
//            delay(delay.toLong())
//            onStart?.invoke()
//            animate()
//        }
        LaunchedEffect(Unit) {
            delay(delay.toLong()) // Ensure we pass a Long
            animate()
        }
    }
}

fun delay(duration: Long): suspend (Float, Float) -> Unit {
    return { _, _ -> kotlinx.coroutines.delay(duration) }
}

// yoyo repeats the animation forward and reverse a certain number of times
fun repeat(count: Int, animation: suspend (Float, Float) -> Unit): suspend (Float, Float) -> Unit {
    return { scrollPosition, scrollRange ->
        repeat(count) { _ ->  // Ignoring the parameter with an underscore
            // Call the animation function with scrollPosition and scrollRange
            animation(scrollPosition, scrollRange)
        }
    }
}

// Function to create a yoyo effect (forward and reverse)
fun yoyo(count: Int, animation: suspend (Float, Float) -> Unit): suspend (Float, Float) -> Unit {
    return { scrollPosition, scrollRange ->
        repeat(count) { _ ->  // Ignoring the parameter with an underscore
            // Forward animation
            animation(scrollPosition, scrollRange)  // Pass the parameters to the animation function
            kotlinx.coroutines.delay(500)  // Optional pause between forward and reverse animation

            // Reverse animation (can be applied here)
            animation(scrollPosition, scrollRange)  // Reverse animation can be the same or modified
        }
    }
}

