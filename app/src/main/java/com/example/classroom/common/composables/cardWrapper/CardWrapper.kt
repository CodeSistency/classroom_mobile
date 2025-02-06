package com.example.classroom.common.composables.cardWrapper

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.classroom.presentation.theme.Azul2
import com.example.classroom.presentation.theme.PaddingCustom

@Composable
fun CardWrapper(
    modifier: Modifier = Modifier.fillMaxWidth(),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.padding(horizontal = 5.dp, vertical = 0.5.dp)
    ) {
        // Wrap the whole Row in a Box to control the height
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {

            // Blue Indicator (Background Decoration)
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(77.dp)
                    .align(Alignment.CenterStart)// Ensures it takes the full height of the parent
                    .background(Azul2, RoundedCornerShape(PaddingCustom.MEDIUM.size)

                        )
            )

            // Card Content Row
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .weight(1f) // Takes full width after the indicator
                        .background(Color.White, shape = RoundedCornerShape(16.dp))
                        .border(1.dp, Color(0xFFE0E0E0), shape = RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    content()
                }
            }
        }
    }
}
