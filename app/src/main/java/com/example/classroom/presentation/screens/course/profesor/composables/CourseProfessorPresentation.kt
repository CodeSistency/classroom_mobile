package com.example.classroom.presentation.screens.course.profesor.composables

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.classroom.App
import com.example.classroom.R
import com.example.classroom.common.composables.customDialogs.SetupCustomDialog
import com.example.classroom.common.composables.customDialogs.SetupCustomDialogState
import com.example.classroom.common.customTab.CustomTab
import com.example.classroom.common.scrolleableTab.CustomScrollableTabRow
import com.example.classroom.domain.model.entity.Gender
import com.example.classroom.presentation.navigation.Destination
import com.example.classroom.presentation.screens.activity.ActivityViewmodel
import com.example.classroom.presentation.screens.activity.addActivity.AddActivityViewModel
import com.example.classroom.presentation.screens.course.AddCourse.AddCourseViewModel
import com.example.classroom.presentation.screens.course.CourseViewmodel
import com.example.classroom.presentation.screens.course.posts.ListPosts
import com.example.classroom.presentation.screens.course.posts.PostsViewModel
import com.example.classroom.presentation.screens.home.HomeViewmodel
import com.example.classroom.presentation.screens.home.composables.ListCourses
import com.example.classroom.presentation.screens.home.composables.ListMyCourses
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.Azul2
import com.example.classroom.presentation.theme.Azul3
import com.example.classroom.presentation.theme.AzulGradient
import com.example.classroom.presentation.theme.Gris
import com.example.classroom.presentation.theme.PaddingCustom
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.common.shimmerEffects.ListShimmer

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CourseProfessorPresentation(
    viewModel: ActivityViewmodel,
    courseViewmodel: CourseViewmodel,
    addActivityViewModel: AddActivityViewModel,
    addCourseViewModel: AddCourseViewModel,
    postsViewModel: PostsViewModel,
    id: String,
    navController: NavController
){
    val tabTitles = listOf(SelectedOption.MY_STUDENTS.title, SelectedOption.POSTS.title, SelectedOption.ACTIVITIES.title)
    val pagerState = rememberPagerState(pageCount = { tabTitles.size })
    val (selected, setSelected) = remember { mutableStateOf(0) }
//    val userInfo = viewModel.userInfo.collectAsState(initial = emptyList())
    val courseInfo = courseViewmodel.courseFlow.collectAsState(initial = null)
    val context = LocalContext.current

    //ESTADOS
    var dialogState: SetupCustomDialogState by remember {
        mutableStateOf(SetupCustomDialogState.Default())
    }

    val deleteStudentState = courseViewmodel.stateDeleteStudents.collectAsState()
    val deleteActivityState = viewModel.stateAddActivity.value
    val deletePostState = postsViewModel.deletePostState.collectAsState()



        LaunchedEffect(key1 = courseInfo.value, block = {
        Log.e("courseInfo", courseInfo.value.toString())
    })
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = courseInfo.value, block = {
        if (courseInfo.value == null) courseViewmodel.getCourseByIdLocal(id)
    })

    LaunchedEffect(key1 = true, block = {
            pagerState.animateScrollToPage(0)
    })

    LaunchedEffect(key1 = true, block = {
         courseViewmodel.getUsersByCourseLocal(id)
        viewModel.getActivitiesByCourse(id)
        postsViewModel.getPostsByCourseRemote(id)
        courseViewmodel.getUsersByCourseRemote(id)


    })

    val studentInput = courseViewmodel.studentInput.collectAsStateWithLifecycle()
    val postInput = courseViewmodel.postInput.collectAsStateWithLifecycle()
    val activityInput = courseViewmodel.activityInput.collectAsStateWithLifecycle()


//    LaunchedEffect(key1 = true, block = {
//        courseViewmodel.getCourseByIdLocal(id)
//
//        //have to make a method that involves the two
////        courseViewmodel.getUsersByCourseRemote(id)
////        courseViewmodel.getUsersByCourseLocal(id)
//        viewModel.getActivitiesByCourse(id)
//    })
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
                        Text(text = courseInfo.value.let {
                                                         if (it != null){
                                                             it.title
                                                         }else{
                                                             "..."
                                                         }
                        },
                            style = TextStyle(
                                Color.White,
                                fontSize = 26.sp,
                                FontWeight.Bold
                            ))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = courseInfo.value.let {
                            if (it != null){
                                it.section
                            }else{
                                "..."
                            }
                        } ,
                            style = TextStyle(
                                Color.White,
                                fontSize = 16.sp,
                            )
                        )

                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            modifier = Modifier
//                                .padding(8.dp)
                                .clickable {
                                    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Codigo", courseInfo.value?.token ?: "...")
                                    clipboardManager.setPrimaryClip(clip)
                                }
                        ) {
                            Text(
                                text = "codigo: ",
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = courseInfo.value?.token ?: "...",
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Normal
                                ),
                                // Ensures proper spacing
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Icon(
                                imageVector = Icons.Default.CopyAll, // Replace with your clipboard icon resource
                                contentDescription = "Copy to Clipboard",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                    }

                }
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = when (selected) {
                        0 -> studentInput.value
                        1 -> postInput.value
                        2 -> activityInput.value
                        else -> ""
                    },
                    onValueChange = { newValue ->
                        when (selected) {
                            0 -> {
                                courseViewmodel.studentInput.value = newValue
                                courseViewmodel.filterStudents(newValue)
                            }
                            1 -> {
                                courseViewmodel.postInput.value = newValue
                                postsViewModel.filterPosts(newValue) // Add a filterPosts method in PostsViewModel
                            }
                            2 -> {
                                viewModel.activityInput.value = newValue
                                viewModel.filterActivities(newValue) // Add a filterActivities method in ActivityViewModel
                            }
                        }
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
//            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                Arrangement.Center,
                Alignment.CenterVertically
            ) {
                CustomScrollableTabRow(
                    tabs = tabTitles,
                    selectedTabIndex = selected,
                    onTabSelected = {
                        setSelected(it)
                        scope.launch {
                            when (it) {
                                0 -> {
                                    courseViewmodel.postInput.value = "" // Clear Post input
                                    viewModel.activityInput.value = "" // Clear Activity input
                                }
                                1 -> {
                                    courseViewmodel.studentInput.value = "" // Clear Student input
                                    courseViewmodel.activityInput.value = "" // Clear Activity input
                                }
                                2 -> {
                                    courseViewmodel.studentInput.value = "" // Clear Student input
                                    courseViewmodel.postInput.value = "" // Clear Post input
                                }
                            }
                        }
                    },
                    scope = scope,
                    pagerState = pagerState,
                )
//                CustomTab(
//                    items = tabTitles,
//                    selectedItemIndex = selected,
//                    onClick = setSelected,
//                    pagerState = pagerState,
//                    tabWidth = 150.dp,
//                    color = AzulGradient,
//                    scope = scope
//                )

            }
            if (viewModel.stateGetActivities.value.isLoading){
                ListShimmer(quantity = 10)
            }else{
                HorizontalPager(
                    state = pagerState,
                ) { page ->
                    when(page){
                        0 -> {
                            ListUsers(viewModel, courseViewmodel, scope, id, navController)
                        }
                        1 -> {
                            ListPosts(viewModel = App.appModule.postViewModel, courseId = id, scope)
                        }
                        2 -> {
                            ListActivities(viewModel, courseViewmodel,addActivityViewModel, scope, id, navController)
                        }
                    }
                }
            }
        }
    }

    courseInfo.value.let {
        if (it != null){
            if (!it.verified){
                Box(modifier = Modifier.fillMaxSize().background(Color.White)){
                    Text(
                        text = "Este curso no esta verificado, contacte un administrador para verificar este curso.",
                        modifier = Modifier.align(Alignment.Center)
                        )
                }
            }
        }
    }



    LaunchedEffect(key1 = deleteActivityState, block = {
        Log.e("DELETE ACTIVITY STATE", deleteActivityState.toString())

        when{
            deleteActivityState.isLoading -> {
                dialogState = SetupCustomDialogState.Loading()
            }
            deleteActivityState.error != null -> {
                dialogState = SetupCustomDialogState.Error(deleteActivityState.error.uiMessage)
            }

            else -> {
                if (deleteActivityState.info != null){
                    dialogState = SetupCustomDialogState.Success(message = "La actividad ha sido eliminada exitosamente")
                    delay(1000)
                }
            }
        }
    })


    LaunchedEffect(key1 = deletePostState, block = {
        Log.e("DELETE POST STATE", deletePostState.value.toString())
        when{
            deletePostState.value.isLoading -> {
                dialogState = SetupCustomDialogState.Loading()
            }
            deletePostState.value.error != null -> {
                dialogState = SetupCustomDialogState.Error(deletePostState.value.error)
            }

            else -> {
                if (deletePostState.value.info != null){
                    dialogState = SetupCustomDialogState.Success(message = "La publicacion ha sido eliminada exitosamente")
                    delay(1000)
                }
            }
        }
    })

    LaunchedEffect(key1 = deleteStudentState, block = {
        Log.e("DELETE STUDENT STATE", deleteStudentState.value.toString())

        when{
            deleteStudentState.value.isLoading -> {
                dialogState = SetupCustomDialogState.Loading()
            }
            deleteStudentState.value.error != null -> {
                dialogState = SetupCustomDialogState.Error(deleteStudentState.value.error?.uiMessage)
            }

            else -> {
                if (deleteStudentState.value.info != null){
                    dialogState = SetupCustomDialogState.Success(message = "El alumno ha sido eliminada exitosamente")
                    delay(1000)
                }
            }
        }
    })

    SetupCustomDialog(setupCustomDialogState = dialogState, showDialog = dialogState != SetupCustomDialogState.Default()) {
        dialogState = SetupCustomDialogState.Default()
    }
}


enum class SelectedOption(val title: String) {
    MY_STUDENTS("Alumnos"),
    POSTS("Publicaciones"),
    ACTIVITIES("Actividades")
}