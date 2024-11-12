package com.example.classroom.common.CustomButton

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class NavigationButtonStyle {
    SolidGradient, OutlineWithIconGradient, OutlineOnly
}

@Composable
fun CustomButton(
    text: String,
    style: NavigationButtonStyle,
    color1: Color,
    color2: Color,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    onClick: () -> Unit = {},
    disabled: Boolean = false,
    disabledColor: Color = Color.Gray
) {
    val gradient = Brush.horizontalGradient(colors = listOf(color1, color2))
    val backgroundBrush = if (disabled) Brush.horizontalGradient(listOf(disabledColor, disabledColor)) else gradient
    val borderStroke = when {
        disabled -> BorderStroke(2.dp, disabledColor)
        style == NavigationButtonStyle.OutlineOnly || style == NavigationButtonStyle.OutlineWithIconGradient -> BorderStroke(2.dp, gradient)
        else -> null
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(
                brush = when (style) {
                    NavigationButtonStyle.SolidGradient -> backgroundBrush
                    else -> Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                }
            )
            .then(if (borderStroke != null) Modifier.border(borderStroke, shape = RoundedCornerShape(50)) else Modifier)
            .clickable(enabled = !disabled) { onClick() }
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (icon == null) Arrangement.Center else Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = text,
                color = if (disabled) Color.LightGray else if (style == NavigationButtonStyle.SolidGradient) Color.White else color1,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            if (icon != null) {
                val iconBorderStroke = if (style == NavigationButtonStyle.OutlineOnly && !disabled) {
                    BorderStroke(1.dp, gradient)
                } else {
                    BorderStroke(1.dp, Color.Transparent)
                }

                Box(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(24.dp)
                        .clip(CircleShape)
                        .then(
                            if (style == NavigationButtonStyle.OutlineWithIconGradient) {
                                Modifier.background(backgroundBrush)
                            } else {
                                Modifier.background(Color.Transparent)
                            }
                        )
                        .border(iconBorderStroke, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (disabled) Color.LightGray else if (style == NavigationButtonStyle.SolidGradient) Color.White else color1,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
