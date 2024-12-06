package com.example.classroom.common.composables.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun <T> GenericCard(
    item: T,
    modifier: Modifier = Modifier,
    isSwipeable: Boolean = false,
    actions: (@Composable RowScope.() -> Unit)? = null,
    onExpanded: (() -> Unit)? = null,
    onCollapsed: (() -> Unit)? = null,
    content: @Composable (T) -> Unit
) {
    if (isSwipeable && actions != null) {
        // Swipeable card with actions
        SwipeableItemWithActions(
            isRevealed = false,
            actions = actions,
            modifier = modifier,
            onExpanded = onExpanded ?: {},
            onCollapsed = onCollapsed ?: {}
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                content(item)
            }
        }
    } else {
        // Regular card
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            content(item)
        }
    }
}
