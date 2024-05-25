package com.example.classroom.presentation.screens.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalCourses
import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.domain.model.entity.toCoursesLocal
import com.example.classroom.domain.use_case.courses.GetCoursesUseCase
import com.example.classroom.presentation.screens.home.composables.SelectedOption
import com.example.classroom.presentation.screens.home.states.CourseState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.common.Resource
import timber.log.Timber

class HomeViewmodel(
    private val getCoursesUseCase: GetCoursesUseCase,
    private val repositoryBundle: RepositoryBundle
): ViewModel() {

    var filteredListCoursesFLow: Flow<List<LocalCourses>> = emptyFlow()
    var filteredListMyCoursesFLow: Flow<List<LocalCourses>> = emptyFlow()

    var listCoursesFlow: Flow<List<LocalCourses>> = emptyFlow()
    var listMyCoursesFlow: Flow<List<LocalCourses>> = emptyFlow()
    var userInfo: Flow<LocalUser?> = emptyFlow()

    val myCoursesInput = mutableStateOf("")
    val coursesInput = mutableStateOf("")

    private val _stateCourse = mutableStateOf(CourseState())
    val stateCourse: State<CourseState> = _stateCourse

    init {
        viewModelScope.launch {
            userInfo = repositoryBundle.loginRepository.getUserLogged()
            filteredListCoursesFLow = repositoryBundle.coursesRepository.getCoursesWithFlow()
            filteredListMyCoursesFLow = userInfo.first()?.let { user ->
                repositoryBundle.coursesRepository.getCoursesWithFlow().map { courses ->
                    courses.filter {
                        it.owner != user.idApi
                    }
                }
            } ?: emptyFlow()

//            filteredListMyCoursesFLow = userInfo?.let { user ->
//                if (user.first().isNotEmpty()) {
//                    user.first()[0].idApi?.let { idApi ->
//                        repositoryBundle.coursesRepository.getCoursesWithFlow().map { courses ->
//                            courses.filter {
//                                it.owner != idApi
//                            }
//                        }
//                    }
//                } else {
//                    emptyFlow()
//                }
//            } ?: emptyFlow()

        }
    }

    suspend fun getCourses(id: String){
                getCoursesUseCase(id).onEach { result ->
                    when(result){
                        is Resource.Error -> {
                            //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
                            Log.e("HOME_VM:", "Error ${result.message?.uiMessage}")
                            _stateCourse.value = CourseState(error = result.message)
                        }
                        is Resource.Loading -> {
                            Timber.tag("HOME_VM").e("is loading")
                            _stateCourse.value = CourseState(isLoading = true)
                        }
                        is Resource.Success -> {
                            Timber.tag("HOME_VM").e("success")
                            Log.e("HOME_VM:", "success")
                            _stateCourse.value = CourseState(info = result.data?.toCoursesLocal())
                            Log.e("HOME_VM:", "${stateCourse.value.info}")
                            _stateCourse.value.info?.let {
                                repositoryBundle.coursesRepository.insertAllCourses(it)
                                delay(1000)
                            }
                        }
                    }

                }.launchIn(viewModelScope)
    }

    suspend fun getCoursesLocal(){
        Log.e("lista cursos viewmodel", repositoryBundle.coursesRepository.getCoursesWithFlow().first().toString())
        listCoursesFlow = repositoryBundle.coursesRepository.getCoursesWithFlow()
    }

    suspend fun getMyCoursesLocal(){
        Log.e("lista cursos viewmodel", repositoryBundle.coursesRepository.getCoursesWithFlow().first().toString())
        listMyCoursesFlow = userInfo.first()?.let { user ->
            repositoryBundle.coursesRepository.getCoursesWithFlow().map { courses ->
                courses.filter {
                    it.owner != user.idApi
                }
            }
        } ?: emptyFlow()
//            userInfo?.let { user ->
//            if (user.first().isNotEmpty()) {
//                user.first()[0].idApi?.let { idApi ->
//                    repositoryBundle.coursesRepository.getCoursesWithFlow().map { courses ->
//                        courses.filter {
//                            it.owner != idApi
//                        }
//                    }
//                }
//            } else {
//                emptyFlow()
//            }
//        } ?: emptyFlow()
    }

    fun filterListByInput(typeCourse: SelectedOption) {
        viewModelScope.launch {
            when(typeCourse){
                SelectedOption.MY_COURSES -> {
                    filteredListMyCoursesFLow = if (myCoursesInput.value.isNotEmpty()){
                        listMyCoursesFlow.map { list ->
                            list.filter { it.title.startsWith(myCoursesInput.value) }
                                .sortedByDescending { course -> course.id }
                        }
                    }else{
                        listMyCoursesFlow
                    }
                }

                SelectedOption.COURSES -> {
                    filteredListCoursesFLow = if (coursesInput.value.isNotEmpty()){
                        listCoursesFlow.map { list ->
                            list.filter { it.title.startsWith(coursesInput.value) }
                                .sortedByDescending { course -> course.id }
                        }
                    } else {
                        listCoursesFlow
                    }

                }
            }
        }
    }

}