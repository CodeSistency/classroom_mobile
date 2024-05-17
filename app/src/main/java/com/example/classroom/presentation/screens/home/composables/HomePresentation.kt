package com.example.classroom.presentation.screens.home.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.classroom.common.customTab.CustomTab
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.Gris
import com.example.classroom.presentation.theme.PaddingCustom

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomePresentation(){
    var input by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf(SelectedOption.COURSES) }
    Column {
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(
                Azul,
                shape = RoundedCornerShape(
                    bottomStart = PaddingCustom.MEDIUM.size
                )
            )
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row {

                }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = input,
                    onValueChange = {
                        input = it
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = Color.White,
                        focusedBorderColor = Color.Gray,
                        disabledBorderColor = Gris,
                        unfocusedBorderColor = Gris,
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
//                    keyboardActions = KeyboardActions(
//                        onNext = {
//                            onNextClick()
//                        }
//                    ),
                    maxLines = 1
                )

            }
        }

        Column(modifier = Modifier.padding(horizontal = 5.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                val tabTitles = listOf("CURSOS", "MIS CURSOS")
                val pagerState = rememberPagerState(pageCount = { tabTitles.size })
                val (selected, setSelected) = remember { mutableStateOf(0) }

                CustomTab(
                    items = listOf("CURSOS", "MIS CURSOS"),
                    selectedItemIndex = selected,
                    onClick = setSelected,
                )
                HorizontalPager(
                    state = pagerState,
                ) { page ->
                    when(page){
                        0 -> {}
                        1 -> {}
                    }
                }
            }
        }
    }
}

enum class SelectedOption {
    MY_COURSES,
    COURSES
}