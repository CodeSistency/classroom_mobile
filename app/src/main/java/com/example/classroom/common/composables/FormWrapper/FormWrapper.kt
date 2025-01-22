package com.example.classroom.common.composables.FormWrapper

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

//@Composable
//fun FormWrapper(
//    modifier: Modifier = Modifier,
//    content: @Composable ColumnScope.() -> Unit
//) {
//    LazyColumn(
//        modifier = modifier
//            .fillMaxSize()
//            .imePadding() // Adjust for keyboard
////            .padding(16.dp), // Optional padding
////        verticalArrangement = Arrangement.spacedBy(16.dp) // Space between items
//    ) {
//        // Use a lambda for ColumnScope to directly allow multiple items
//        item {
//            Column(
//                modifier = Modifier.fillMaxWidth(),
////                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                content()
//            }
//        }
//    }
//}

@Composable
fun FormWrapper(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()

    // Automatically scroll to the bottom when the keyboard opens
    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(scrollState.maxValue, tween(300))
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState), // Make the content scrollable
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
        ) {
            content()
        }
    }
}


