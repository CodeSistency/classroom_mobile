package com.example.classroom.common.scrolleableTab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomScrollableTabRow(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    selectedColor: Color = Color(0xFF007AFF), // Customizable color for the selected tab
    unselectedColor: Color = Color.Gray,
    indicator: @Composable @UiComposable (tabPositions: List<TabPosition>) -> Unit = { tabPositions ->
        TabRowDefaults.Indicator(
            Modifier
                .tabIndicatorOffset(tabPositions[selectedTabIndex])
                .padding(horizontal = 8.dp) // Optional padding to match design
                .background(selectedColor, shape = CircleShape)
                .fillMaxWidth()
                .height(4.dp) // Customize thickness as needed
        )
    },
    edgePadding: Dp = 0.dp // Remove padding for a tighter look
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        edgePadding = edgePadding,
        indicator = indicator,
        modifier = modifier
            .background(backgroundColor) // Set background color using Modifier
            .padding(vertical = 8.dp)
    ) {
        tabs.forEachIndexed { index, title ->
            val isSelected = selectedTabIndex == index
            Tab(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .background(
                        if (isSelected) selectedColor else Color.Transparent,
                        shape = RoundedCornerShape(50) // Rounded shape for selected tab
                    )
            ) {
                Text(
                    text = title,
                    color = if (isSelected) Color.White else unselectedColor,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

            }
        }
    }
}