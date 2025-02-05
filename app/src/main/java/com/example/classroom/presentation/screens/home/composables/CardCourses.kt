package com.example.classroom.presentation.screens.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Verified
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classroom.R
import com.example.classroom.common.CustomDialog
import com.example.classroom.common.composables.cardWrapper.CardWrapper
import com.example.classroom.domain.model.entity.LocalCourses
import com.example.classroom.presentation.navigation.Destination
import com.example.classroom.presentation.screens.course.AddCourse.AddCourseViewModel
import com.example.classroom.presentation.theme.Azul2
import com.example.classroom.presentation.theme.PaddingCustom


@Composable
fun CardCourses(
    course: LocalCourses,
    email: String,
    isOwner: Boolean,
    isMyCourse: Boolean = false,
    msgDelete: String,
    msgDeleteBtn: String,
    action: () -> Unit,
    viewModel: AddCourseViewModel,
    navController: NavController
) {
    var isDeleteOpen by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {  // Wrap everything inside a Box
        // Colored Indicator on the Left
        Box(
            modifier = Modifier
                .height(80.dp)
                .width(5.dp)
                .background(Azul2, RoundedCornerShape(PaddingCustom.MEDIUM.size))
                .align(Alignment.CenterStart)  // Now it works inside this Box
        )

        // The CardWrapper containing course information
        CardWrapper(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp) // Adds a small gap from the colored indicator
                .clickable {
                    navController.navigate("${Destination.COURSES.screenRoute}?id=${course.idApi}&email=${email}&isOwner=${isOwner}")
                }
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Course Title + Verified Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = course.title,
                        style = TextStyle(
                            color = Color.DarkGray,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.weight(1f) // Ensures text wraps properly
                    )

                    if (course.verified) {
                        Icon(
                            imageVector = Icons.Default.Verified,
//                            painter = painterResource(id = R.drawable.ic_verified),
                            contentDescription = "Verified Course",
                            tint = Azul2,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Subject and Section
                Text(
                    text = "${course.subject} - ${course.section}",
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Owner Name (if not the owner)
                if (!isOwner) {
                    Text(
                        text = "Created by: ${course.ownerName}",
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Italic
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action Row (Delete if it's the user’s course)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (isMyCourse) {
                        IconButton(onClick = { isDeleteOpen = true }) {
                            Icon(
                                painterResource(id = R.drawable.ic_cancel),
                                contentDescription = "Delete Course",
                                tint = Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (isDeleteOpen) {
        CustomDialog(
            message = msgDelete,
            messageBtn = msgDeleteBtn,
            loading = false,
            action = { action() },
            dismissDialog = { isDeleteOpen = false },
            icon = painterResource(id = R.drawable.ic_cancel)
        )
    }
}

//@Composable
//fun CardCourses(
//    course: LocalCourses,
//    email: String,
//    isOwner: Boolean,
//    isMyCourse:Boolean = false,
//    msgDelete: String,
//    msgDeleteBtn: String,
//    action: () -> Unit,
//    viewModel: AddCourseViewModel,
//    navController: NavController
//){
//    val shape = RoundedCornerShape(PaddingCustom.MEDIUM.size)
//    var isDeleteOpen by remember { mutableStateOf(false) }
//    Box(modifier = Modifier){
//        Box(
//            modifier = Modifier
//                .shadow(8.dp, shape)
//                .background(Color.White, shape)
//                .padding(16.dp)
//                .clickable {
//                    navController.navigate("${Destination.COURSES.screenRoute}?id=${course.idApi}&email=${email}&isOwner=${isOwner.toString()}")
//
//                }
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                Arrangement.SpaceBetween
//            ){
//                Column {
//                    Text(
//                        text = course.title,
//                        style = TextStyle(
//                            color = Color.DarkGray,
//                            fontSize = 20.sp,
//                            fontWeight = FontWeight.Bold,
////                            fontFamily = InterTight
//                        )
//                    )
//                    Spacer(modifier = Modifier.height(5.dp))
//                    Text(
//                        text = course.section,
//                        style = TextStyle(
//                            color = Color.DarkGray,
//                            fontSize = 10.sp,
//                            fontWeight = FontWeight.Bold,
////                            fontFamily = InterTight
//                        )
//                    )
//                }
//
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
////                    horizontalArrangement = Arrangement.Center
//                ) {
////                    IconButton(onClick = {
////                        viewModel.fillForm(course)
////                        navController.navigate("${Destination.REGISTRO_COURSE.screenRoute}?id=${course.idApi}")
////
////                    }) {
////                        Icon(
////                            Icons.Default.Edit,
////                            contentDescription = null,
////                            tint = Color.Gray,
////                            modifier = Modifier.size(35.dp)
////                        )
////                    }
////                    Spacer(modifier = Modifier.width(3.dp))
//                    if (isMyCourse){
//                        IconButton(onClick = {
//                            isDeleteOpen = true
//                        }) {
//                            Icon(painterResource(id = R.drawable.ic_cancel),
//                                contentDescription = null,
//                                tint = Color.Gray,
//                                modifier = Modifier.size(35.dp)
//                            )
//                        }
//                    }
//
//                }
//
//            }
//        }
//        Box(modifier = Modifier
//            .height(80.dp)
//            .width(5.dp)
//            .background(Azul2, RoundedCornerShape(PaddingCustom.MEDIUM.size))
//            .align(Alignment.CenterStart),)
//    }
//
//    if (isDeleteOpen){
//        CustomDialog(
//            message = msgDelete,
//            messageBtn = msgDeleteBtn,
//            loading = false,
//            action = { action() },
//            dismissDialog = { isDeleteOpen = false },
//            icon = painterResource(id = R.drawable.ic_cancel)
//        )
//    }
//}

