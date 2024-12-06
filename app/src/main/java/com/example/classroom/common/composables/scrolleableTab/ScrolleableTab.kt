package com.example.classroom.common.composables.scrolleableTab

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomScrollableTabRow(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    scope: CoroutineScope,
    pagerState: PagerState?,
    modifier: Modifier = Modifier,
    selectedColor: Color = Color(0xFF007AFF), // Customizable color for the selected tab
    unselectedColor: Color = Color.Gray,
    backgroundColor: Color = Color.White // Light blue color for the entire tab row background
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(20.dp)) // Rounded corners for the whole tab row background
            .background(backgroundColor) // Background color for the entire tab row
    ) {
        // Use a horizontal scroll container to achieve the scrollable effect
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp), // Added horizontal padding to center items more effectively
            horizontalArrangement = Arrangement.spacedBy(16.dp), // Adds space between items uniformly
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTabIndex == index
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50)) // Rounded shape for selected tab
                        .background(
                            if (isSelected) selectedColor else Color.Transparent
                        )
                        .clickable {
                            onTabSelected(index)
                            if (pagerState != null) {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                        }
                ) {
                    Text(
                        text = title,
                        color = if (isSelected) Color.White else unselectedColor,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp) // Adjust padding to make text more balanced
                    )
                }
            }
        }
    }
}
