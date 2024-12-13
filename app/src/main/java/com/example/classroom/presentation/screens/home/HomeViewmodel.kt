package com.example.classroom.presentation.screens.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classroom.common.composables.lists.PaginationState
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

    // Pagination state for courses
    private val _coursesPaginationState = MutableStateFlow(PaginationState<LocalCourses>())
    val coursesPaginationState: StateFlow<PaginationState<LocalCourses>> = _coursesPaginationState

    // Pagination state for my courses
    private val _myCoursesPaginationState = MutableStateFlow(PaginationState<LocalCourses>())
    val myCoursesPaginationState: StateFlow<PaginationState<LocalCourses>> = _myCoursesPaginationState



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

    fun loadItemsCourses(page: Int, pageSize: Int): List<LocalCourses> {
        val allCourses = _coursesPaginationState.value.items
        return allCourses.drop((page - 1) * pageSize).take(pageSize)
    }

    fun loadItemsMyCourses(page: Int, pageSize: Int): List<LocalCourses> {
        val allCourses = _coursesPaginationState.value.items
        return allCourses.drop((page - 1) * pageSize).take(pageSize)
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
                        // After insertion, trigger pagination and filtering
                        _coursesPaginationState.value = _coursesPaginationState.value.copy(
                            items = it,
                            isLoading = false
                        )
                        filterCourses()

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

                        // After deletion, update the pagination state
                        _coursesPaginationState.value = _coursesPaginationState.value.copy(
                            items = it.filter { course -> course.idApi != id }
                        )
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
    // Apply the filter based on user input for both course lists
    private fun filterCourses() {
        viewModelScope.launch {
            val allCourses = _coursesPaginationState.value.items

            // Filter for all courses list
            _filteredListCoursesFlow.value = if (coursesInput.value.isNotEmpty()) {
                allCourses.filter { it.title.contains(coursesInput.value, ignoreCase = true) }
            } else {
                allCourses
            }

            // Filter for my courses list
            _filteredListMyCoursesFlow.value = if (myCoursesInput.value.isNotEmpty()) {
                allCourses.filter { it.title.contains(myCoursesInput.value, ignoreCase = true) && it.owner == _userInfo.value?.idApi }
            } else {
                allCourses.filter { it.owner == _userInfo.value?.idApi }
            }
        }
    }

    // Observe changes in listCoursesFlow and coursesInput separately to update _filteredListCoursesFlow
    private fun observeListAndFilter() {
        viewModelScope.launch {
            coursesInput.collectLatest {
                filterCourses()  // Reapply filter when coursesInput changes
            }
        }

        viewModelScope.launch {
            myCoursesInput.collectLatest {
                filterCourses()  // Reapply filter when myCoursesInput changes
            }
        }
    }

//    private fun observeListAndFilter() {
//        // Observe changes in listCoursesFlow and coursesInput separately to update _filteredListCoursesFlow
//        viewModelScope.launch {
//            listCoursesFlow.collectLatest { courses ->
//                val input = coursesInput.value
//                _filteredListCoursesFlow.value = if (input.isEmpty()) {
//                    courses
//                } else {
//                    courses.filter { it.title.startsWith(input, ignoreCase = true) }
//                }
//            }
//        }
//
//        viewModelScope.launch {
//            coursesInput.collectLatest { input ->
//                val courses = listCoursesFlow.value
//                _filteredListCoursesFlow.value = if (input.isEmpty()) {
//                    courses
//                } else {
//                    courses.filter { it.title.startsWith(input, ignoreCase = true) }
//                }
//            }
//        }
//
//        // Observe changes in listMyCoursesFlow and myCoursesInput separately to update _filteredListMyCoursesFlow
//        viewModelScope.launch {
//            listMyCoursesFlow.collectLatest { courses ->
//                val input = myCoursesInput.value
//                _filteredListMyCoursesFlow.value = if (input.isEmpty()) {
//                    courses
//                } else {
//                    courses.filter { it.title.startsWith(input, ignoreCase = true) }
//                }
//            }
//        }
//
//        viewModelScope.launch {
//            myCoursesInput.collectLatest { input ->
//                val courses = listMyCoursesFlow.value
//                _filteredListMyCoursesFlow.value = if (input.isEmpty()) {
//                    courses
//                } else {
//                    courses.filter { it.title.startsWith(input, ignoreCase = true) }
//                }
//            }
//        }
//    }




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
