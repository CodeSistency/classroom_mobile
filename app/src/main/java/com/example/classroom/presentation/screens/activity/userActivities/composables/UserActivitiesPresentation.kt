package com.example.classroom.presentation.screens.activity.userActivities.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classroom.common.customTab.CustomTab
import com.example.classroom.presentation.screens.activity.ActivityViewmodel
import com.example.classroom.presentation.screens.course.CourseViewmodel
import com.example.classroom.presentation.screens.course.profesor.composables.ListUsers
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.Azul3
import com.example.classroom.presentation.theme.AzulGradient
import com.example.classroom.presentation.theme.Gris
import com.example.classroom.presentation.theme.PaddingCustom
import proyecto.person.appconsultapopular.common.shimmerEffects.ListShimmer

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserActivitiesPresentation(
    viewModel: ActivityViewmodel,
    courseViewmodel: CourseViewmodel,
    id: String,
    name: String,
    course: String,
    navController: NavController
){
//    val tabTitles = listOf(SelectedOption.MY_STUDENTS.title, SelectedOption.ACTIVITIES.title)
//    val pagerState = rememberPagerState(pageCount = { tabTitles.size })
//    val (selected, setSelected) = remember { mutableStateOf(0) }
//    val userInfo = viewModel.userInfo.collectAsState(initial = emptyList())
    val courseInfo = courseViewmodel.courseFlow.collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = true, block = {
        courseViewmodel.getCourseByIdLocal(id)
        courseViewmodel.getUsersByCourseRemote(id)
        courseViewmodel.getUsersByCourseLocal(id)
        viewModel.getActivitiesByCourse(id)
    })
    Column(
        modifier = Modifier.background(Azul3)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(

                brush = Brush.verticalGradient(
                    colors = listOf(
                        Azul,
                        AzulGradient
                    ),
                ),
                shape = RoundedCornerShape(
                    bottomStart = PaddingCustom.EXTRA_LARGE.size
                )
            )
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    Arrangement.SpaceBetween,
//                    Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = name,
                            style = TextStyle(
                                Color.White,
                                fontSize = 26.sp,
                                FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = course,
                            style = TextStyle(
                                Color.White,
                                fontSize = 16.sp,
                            )
                        )
                    }
//                    val icon = if (userInfo.value.let { it.isNotEmpty() }){
//                        userInfo.value.let {
//                            if (it[0].gender == Gender.Man){
//                                painterResource(R.drawable.ic_male_avatar)
//                            }else if (it[0].gender == Gender.Woman){
//                                painterResource(R.drawable.ic_female_avatar)
//                            }else{
//                                painterResource(R.drawable.ic_male_avatar)
//                            }
//                        }
//                    }else{
//                        painterResource(R.drawable.ic_male_avatar)
//                    }
//                    Icon(
//                        icon, contentDescription = null,
//                        modifier = Modifier
//                            .size(70.dp)
//                            .background(Color.White, CircleShape)
//                            .padding(horizontal = 5.dp))
                }
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.activityInput.value,
                    onValueChange = {
//                        viewModel.activityInput.value = it

                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = Color.White,
                        focusedBorderColor = Color.Gray,
                        disabledBorderColor = Gris,
                        unfocusedBorderColor = Gris,
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(PaddingCustom.EXTRA_LARGE.size),
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

        Column(modifier = Modifier
            .padding(horizontal = 5.dp)
        ) {

            if (viewModel.stateGetActivities.value.isLoading){
                ListShimmer(quantity = 10)
            }else{
                ListActivities(
                    viewModel = viewModel,
                    courseViewmodel = courseViewmodel,
                    scope = scope,
                    id = id,
                    navController = navController,
                )

            }
        }
    }
}


enum class SelectedOption(val title: String) {
    MY_STUDENTS("Alumnos"),
    ACTIVITIES("Actividades")
}