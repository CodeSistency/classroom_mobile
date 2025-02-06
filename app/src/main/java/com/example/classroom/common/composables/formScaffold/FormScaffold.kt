package com.example.classroom.common.composables.formScaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classroom.presentation.theme.Azul2
import com.example.classroom.presentation.theme.Poppins

@Composable
fun FormScaffold(
    title: String,
    subtitle: String? = null,
    onBackClick: () -> Unit,
    primaryButton: @Composable () -> Unit,
    secondaryButton: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        topBar = {
            Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Box(modifier = Modifier.background(Color.LightGray, RoundedCornerShape(12.dp))){
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.padding(10.dp).clickable {
                            onBackClick()                            })

                }
            }

        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = if (secondaryButton != null) Arrangement.spacedBy(8.dp) else Arrangement.Center
            ) {
                if (secondaryButton != null) {
                    Box(modifier = Modifier.weight(1f)) { secondaryButton() }
                    Box(modifier = Modifier.weight(1f)) { primaryButton() }
                } else {
                    Box(modifier = Modifier.fillMaxWidth()) { primaryButton() }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = title, fontSize = 28.sp, modifier = Modifier.fillMaxWidth(0.8f), fontWeight = FontWeight.ExtraBold, color = Azul2, fontFamily = Poppins)
            subtitle?.let {
                Text(text = it, fontSize = 16.sp, fontFamily = Poppins,  modifier = Modifier.padding(bottom = 4.dp))
            }
            content()
        }
    }
}
