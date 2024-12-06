package com.example.classroom.common.animations.gsap.examples

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.classroom.common.animations.gsap.yoyo
import kotlinx.coroutines.launch

@Composable
fun ScrollDrivenAnimation() {
    val scrollState = rememberScrollState()
    val scrollPosition = scrollState.value.toFloat() // Get current scroll position
    val scrollRange = 1000f // Define total scroll range

    val scope = rememberCoroutineScope()

    // Define the animation with the correct signature
    val animation: suspend (Float, Float) -> Unit = { scrollPos, range ->
        // Animation logic, replace this with your actual animation
        println("Animating at scrollPosition: $scrollPos, scrollRange: $range")
    }

    // Create a timeline using yoyo animation (repeats 3 times)
    val timeline = yoyo(3, animation)

    // Trigger the animation if scrollPosition exceeds a threshold
    LaunchedEffect(scrollPosition) {
        if (scrollPosition > 100f) {
            timeline(scrollPosition, scrollRange) // Run the animation with scrollPosition and scrollRange
        }
    }

    // Display the scrollable content
    Box(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
        Box(
            modifier = Modifier
                .offset(x = scrollPosition.dp, y = scrollPosition.dp)
                .size(100.dp)
                .background(Color.Red)
        )
    }
}

@Preview
@Composable
fun PreviewScrollDrivenAnimation() {
    ScrollDrivenAnimation()
}