package com.example.classroom.common.composables.FormWrapper

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

//@Composable
//fun FormWrapper2(
//    modifier: Modifier = Modifier,
//    content: @Composable ColumnScope.() -> Unit
//) {
//    Box(
//        modifier = modifier
//            .fillMaxSize()
//            .imePadding() // Ajusta para evitar el teclado
////            .padding(16.dp) // Espaciado opcional para el contenido
//    ) {
//        Column(
////            verticalArrangement = Arrangement.spacedBy(16.dp),
////            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier.fillMaxSize() // Asegura que use todo el espacio disponible
//        ) {
//            content()
//        }
//    }
//}

@Composable
fun FormWrapper2(
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit
) {
    val imeState = rememberImeState()
    val scrollState = rememberLazyListState()

    // Automatically scroll to the bottom when the keyboard opens
    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollToItem(scrollState.layoutInfo.totalItemsCount - 1)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .imePadding(), // Adjust padding for the keyboard
        state = scrollState,
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//        contentPadding = PaddingValues(16.dp)
    ) {
        content()
    }
}
