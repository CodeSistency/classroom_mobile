package com.example.classroom.presentation.screens.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classroom.data.local.db.daos.NotificationDao
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalCourses
import com.example.classroom.domain.model.entity.LocalNotification
import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.domain.model.entity.toCoursesLocal
import com.example.classroom.domain.use_case.courses.DeleteCourseUseCase
import com.example.classroom.domain.use_case.courses.GetCoursesUseCase
import com.example.classroom.domain.use_case.courses.JoinCourseUseCase
import com.example.classroom.presentation.screens.course.states.JoinUserState
import com.example.classroom.presentation.screens.home.composables.SelectedOption
import com.example.classroom.presentation.screens.home.states.CourseState
import com.example.classroom.presentation.screens.home.states.JoinCourseState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.common.Resource
import timber.log.Timber

class HomeViewmodel(
    private val getCoursesUseCase: GetCoursesUseCase,
    private val joinCourseUseCase: JoinCourseUseCase,
    private val deleteCourseUseCase: DeleteCourseUseCase,
    private val repositoryBundle: RepositoryBundle,
    private val notificationDao: NotificationDao // Inject DAO
) : ViewModel() {

    // StateFlow for user information
    private val _userInfo = MutableStateFlow<LocalUser?>(null)
    val userInfo: StateFlow<LocalUser?> = _userInfo

    private val _notifications = MutableStateFlow<List<LocalNotification>>(emptyList())
    val notifications: StateFlow<List<LocalNotification>> = _notifications

    private val _unseenCount = MutableStateFlow(0)
    val unseenCount: StateFlow<Int> = _unseenCount

    // Courses not owned by the user (joined courses)
//    val listCoursesFlow: StateFlow<List<LocalCourses>> = _userInfo.filterNotNull()
//        .flatMapLatest { user ->
//            repositoryBundle.coursesRepository.getCoursesWithFlow().map { courses ->
//                courses.filter { it.owner != user.idApi }
//            }
//        }
//        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
//
//    // Courses owned by the user
//    val listMyCoursesFlow: StateFlow<List<LocalCourses>> = _userInfo.filterNotNull()
//        .flatMapLatest { user ->
//            repositoryBundle.coursesRepository.getCoursesWithFlow().map { courses ->
//                courses.filter { it.owner == user.idApi }
//            }
//        }
//        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val listCoursesFlow: StateFlow<List<LocalCourses>> = channelFlow {
        _userInfo.filterNotNull().collectLatest { user ->
            repositoryBundle.coursesRepository.getCoursesWithFlow().collect { courses ->
                send(courses.filter { it.owner != user.idApi })
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val listMyCoursesFlow: StateFlow<List<LocalCourses>> = channelFlow {
        _userInfo.filterNotNull().collectLatest { user ->
            repositoryBundle.coursesRepository.getCoursesWithFlow().collect { courses ->
                send(courses.filter { it.owner == user.idApi })
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())



    // Mutable states for filtering course lists
    private val _filteredListCoursesFlow = MutableStateFlow<List<LocalCourses>>(emptyList())
    val filteredListCoursesFlow: StateFlow<List<LocalCourses>> = _filteredListCoursesFlow

    private val _filteredListMyCoursesFlow = MutableStateFlow<List<LocalCourses>>(emptyList())
    val filteredListMyCoursesFlow: StateFlow<List<LocalCourses>> = _filteredListMyCoursesFlow

    val myCoursesInput = MutableStateFlow("")
    val coursesInput = MutableStateFlow("")

    // UI states for different use cases
    private val _stateCourse = mutableStateOf(CourseState())
    val stateCourse: State<CourseState> = _stateCourse

    private val _stateJoinCourse = mutableStateOf(JoinCourseState())
    val stateJoinCourse: State<JoinCourseState> = _stateJoinCourse

    init {
        // Load user info and courses in the background
        viewModelScope.launch {
            repositoryBundle.loginRepository.getUserInfoWithFlow()
                .firstOrNull()
                ?.firstOrNull()
                ?.let { _userInfo.value = it }

            // Fetch and insert courses from server
            _userInfo.value?.idApi?.let { getCourses(it) }


            notificationDao.getAllNotifications().collect { notifications ->
                _notifications.value = notifications
            }

            notificationDao.getUnseenCount().collect { count ->
                _unseenCount.value = count
            }
        }

        observeListAndFilter()
    }

    fun markAllAsSeen() {
        viewModelScope.launch {
            val unseenIds = _notifications.value.filter { !it.isSeen }.map { it.id }
            notificationDao.markAsSeen(unseenIds)
        }
    }

    fun addNotification(notification: LocalNotification) {
        viewModelScope.launch {
            notificationDao.insert(notification)
        }
    }

    // Fetch courses from the server and update local database
    suspend fun getCourses(id: String) {
        getCoursesUseCase(id).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    Log.e("HOME_VM:", "Error ${result.message?.uiMessage}")
                    _stateCourse.value = CourseState(error = result.message)
                }
                is Resource.Loading -> {
                    _stateCourse.value = CourseState(isLoading = true)
                }
                is Resource.Success -> {
                    _stateCourse.value = CourseState(info = result.data?.toCoursesLocal())
                    _stateCourse.value.info?.let {
                        repositoryBundle.coursesRepository.insertAllCourses(it)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    suspend fun deleteCourse(id: String) {
        deleteCourseUseCase(id).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    Log.e("HOME_VM:", "Error ${result.message?.uiMessage}")
                    _stateCourse.value = _stateCourse.value.copy(error = result.message, isLoading = false)
                }
                is Resource.Loading -> {
                    _stateCourse.value = _stateCourse.value.copy(error = null, isLoading = true)
                }
                is Resource.Success -> {
                    _stateCourse.value = _stateCourse.value.copy(error = null, isLoading = false)
                    _stateCourse.value.info?.let {
                        repositoryBundle.coursesRepository.deleteCourse(id)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
    // Filter courses based on user input
    fun filterListByInput(typeCourse: SelectedOption) {
        viewModelScope.launch {
            when (typeCourse) {
                SelectedOption.MY_COURSES -> {
                    _filteredListMyCoursesFlow.value = if (myCoursesInput.value.isNotEmpty()) {
                        listMyCoursesFlow.value.filter { it.title.startsWith(myCoursesInput.value, ignoreCase = true) }
                    } else {
                        listMyCoursesFlow.value
                    }
                }
                SelectedOption.COURSES -> {
                    _filteredListCoursesFlow.value = if (coursesInput.value.isNotEmpty()) {
                        listCoursesFlow.value.filter { it.title.startsWith(coursesInput.value, ignoreCase = true) }
                    } else {
                        listCoursesFlow.value
                    }
                }
            }
        }
    }
    private fun observeListAndFilter() {
        // Observe changes in listCoursesFlow and coursesInput separately to update _filteredListCoursesFlow
        viewModelScope.launch {
            listCoursesFlow.collectLatest { courses ->
                val input = coursesInput.value
                _filteredListCoursesFlow.value = if (input.isEmpty()) {
                    courses
                } else {
                    courses.filter { it.title.startsWith(input, ignoreCase = true) }
                }
            }
        }

        viewModelScope.launch {
            coursesInput.collectLatest { input ->
                val courses = listCoursesFlow.value
                _filteredListCoursesFlow.value = if (input.isEmpty()) {
                    courses
                } else {
                    courses.filter { it.title.startsWith(input, ignoreCase = true) }
                }
            }
        }

        // Observe changes in listMyCoursesFlow and myCoursesInput separately to update _filteredListMyCoursesFlow
        viewModelScope.launch {
            listMyCoursesFlow.collectLatest { courses ->
                val input = myCoursesInput.value
                _filteredListMyCoursesFlow.value = if (input.isEmpty()) {
                    courses
                } else {
                    courses.filter { it.title.startsWith(input, ignoreCase = true) }
                }
            }
        }

        viewModelScope.launch {
            myCoursesInput.collectLatest { input ->
                val courses = listMyCoursesFlow.value
                _filteredListMyCoursesFlow.value = if (input.isEmpty()) {
                    courses
                } else {
                    courses.filter { it.title.startsWith(input, ignoreCase = true) }
                }
            }
        }
    }

    // Join a course by course ID and token
    suspend fun joinCourse(id: String, token: String) {
        joinCourseUseCase(id, token).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    Log.e("ACTIVITIES:", "Error ${result.message?.uiMessage}")
                    _stateJoinCourse.value = JoinCourseState(error = result.message)
                }
                is Resource.Loading -> {
                    _stateJoinCourse.value = JoinCourseState(isLoading = true)
                }
                is Resource.Success -> {
                    _stateJoinCourse.value = JoinCourseState(info = result.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    // Logout function to clear user session
    suspend fun logout() {
        repositoryBundle.loginRepository.logout()
    }
}


//
//class HomeViewmodel(
//    private val getCoursesUseCase: GetCoursesUseCase,
//    private val joinCourseUseCase: JoinCourseUseCase,
//    private val repositoryBundle: RepositoryBundle
//): ViewModel() {
//
//    var filteredListCoursesFLow: Flow<List<LocalCourses>> = emptyFlow()
//    var filteredListMyCoursesFLow: Flow<List<LocalCourses>> = emptyFlow()
//
//    var listCoursesFlow: Flow<List<LocalCourses>> = emptyFlow()
//    var listMyCoursesFlow: Flow<List<LocalCourses>> = emptyFlow()
//    var userInfo: Flow<LocalUser?> = emptyFlow()
//
//    val myCoursesInput = mutableStateOf("")
//    val coursesInput = mutableStateOf("")
//
//    private val _stateCourse = mutableStateOf(CourseState())
//    val stateCourse: State<CourseState> = _stateCourse
//
//    private val _stateJoinCourse = mutableStateOf(JoinCourseState())
//    val stateJoinCourse: State<JoinCourseState> = _stateJoinCourse
//    init {
//        viewModelScope.launch {
//            userInfo = repositoryBundle.loginRepository.getUserInfoWithFlow().let { userFlow ->
//                userFlow.firstOrNull()?.firstOrNull()?.let { user ->
//                    flow { emit(user) }
//                } ?: emptyFlow()
//            }
//
//            // Courses not owned by the user (joined courses)
//            listCoursesFlow = userInfo.firstOrNull()?.let { user ->
//                repositoryBundle.coursesRepository.getCoursesWithFlow().map { courses ->
//                    courses.filter {
//                        it.owner != user.idApi
//                    }
//                }
//            } ?: emptyFlow()
//
//            // Filtered list of joined courses
//            filteredListCoursesFLow = userInfo.firstOrNull()?.let { user ->
//                repositoryBundle.coursesRepository.getCoursesWithFlow().map { courses ->
//                    courses.filter {
//                        it.owner != user.idApi
//                    }
//                }
//            } ?: emptyFlow()
//
//            // Filtered list of owned courses
//            filteredListMyCoursesFLow = userInfo.firstOrNull()?.let { user ->
//                repositoryBundle.coursesRepository.getCoursesWithFlow().map { courses ->
//                    courses.filter {
//                        it.owner == user.idApi
//                    }
//                }
//            } ?: emptyFlow()
//
//            // List of owned courses
//            listMyCoursesFlow = userInfo.firstOrNull()?.let { user ->
//                repositoryBundle.coursesRepository.getCoursesWithFlow().map { courses ->
//                    courses.filter {
//                        it.owner == user.idApi
//                    }
//                }
//            } ?: emptyFlow()
//
//        }
//    }
//
//    suspend fun getCourses(id: String){
//                getCoursesUseCase(id).onEach { result ->
//                    when(result){
//                        is Resource.Error -> {
//                            //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
//                            Log.e("HOME_VM:", "Error ${result.message?.uiMessage}")
//                            _stateCourse.value = CourseState(error = result.message)
//                        }
//                        is Resource.Loading -> {
//                            Timber.tag("HOME_VM").e("is loading")
//                            _stateCourse.value = CourseState(isLoading = true)
//                        }
//                        is Resource.Success -> {
//                            Timber.tag("HOME_VM").e("success")
//                            Log.e("HOME_VM:", "success")
//                            _stateCourse.value = CourseState(info = result.data?.toCoursesLocal())
//                            Log.e("HOME_VM:", "${stateCourse.value.info}")
//                            _stateCourse.value.info?.let {
//                                repositoryBundle.coursesRepository.insertAllCourses(it)
//                                delay(1000)
//                            }
//                        }
//                    }
//
//                }.launchIn(viewModelScope)
//    }
//
//    suspend fun getCoursesLocal(){
//        Log.e("lista cursos viewmodel", repositoryBundle.coursesRepository.getCoursesWithFlow().first().toString())
//        listCoursesFlow = repositoryBundle.coursesRepository.getCoursesWithFlow()
//    }
//
//    suspend fun getMyCoursesLocal(){
//        Log.e("lista cursos viewmodel", repositoryBundle.coursesRepository.getCoursesWithFlow().first().toString())
//        listMyCoursesFlow = userInfo.first()?.let { user ->
//            repositoryBundle.coursesRepository.getCoursesWithFlow().map { courses ->
//                courses.filter {
//                    it.owner != user.idApi
//                }
//            }
//        } ?: emptyFlow()
////            userInfo?.let { user ->
////            if (user.first().isNotEmpty()) {
////                user.first()[0].idApi?.let { idApi ->
////                    repositoryBundle.coursesRepository.getCoursesWithFlow().map { courses ->
////                        courses.filter {
////                            it.owner != idApi
////                        }
////                    }
////                }
////            } else {
////                emptyFlow()
////            }
////        } ?: emptyFlow()
//    }
//
//    fun filterListByInput(typeCourse: SelectedOption) {
//        viewModelScope.launch {
//            when(typeCourse){
//                SelectedOption.MY_COURSES -> {
//                    filteredListMyCoursesFLow = if (myCoursesInput.value.isNotEmpty()){
//                        listMyCoursesFlow.map { list ->
//                            list.filter { it.title.startsWith(myCoursesInput.value) }
//                                .sortedByDescending { course -> course.id }
//                        }
//                    }else{
//                        listMyCoursesFlow
//                    }
//                }
//
//                SelectedOption.COURSES -> {
//                    filteredListCoursesFLow = if (coursesInput.value.isNotEmpty()){
//                        listCoursesFlow.map { list ->
//                            list.filter { it.title.startsWith(coursesInput.value) }
//                                .sortedByDescending { course -> course.id }
//                        }
//                    } else {
//                        listCoursesFlow
//                    }
//
//                }
//            }
//        }
//    }
//
//    suspend fun joinCourse(id: String, token: String){
//        joinCourseUseCase(id, token).onEach { result ->
//            when(result){
//                is Resource.Error -> {
//                    //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
//                    Log.e("ACTIVITIES:", "Error ${result.message?.uiMessage}")
//                    _stateJoinCourse.value = JoinCourseState(error = result.message)
//                }
//                is Resource.Loading -> {
//                    Timber.tag("ACTIVITIES").e("is loading")
//                    _stateJoinCourse.value = JoinCourseState(isLoading = true)
//                }
//                is Resource.Success -> {
//                    Timber.tag("ACTIVITIES_VM").e("success")
//                    Log.e("ACTIVITIES:", "success")
//                    _stateJoinCourse.value = JoinCourseState(info = result.data)
//                    Log.e("ACTIVITIES:", "${_stateCourse.value.info}")
//                    _stateJoinCourse.value.info?.let {
////                            insertUserDb(it)
//                        delay(300)
//                    }
//                }
//            }
//        }.launchIn(viewModelScope)
//    }
//
//    suspend fun logout(){
//        repositoryBundle.loginRepository.logout()
//    }
//
//}