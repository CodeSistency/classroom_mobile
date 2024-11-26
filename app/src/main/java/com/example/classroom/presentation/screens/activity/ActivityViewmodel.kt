package com.example.classroom.presentation.screens.activity

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classroom.common.validator.ActivityDataValidator
import com.example.classroom.data.remote.dto.activities.ActivityRequestDto
import com.example.classroom.data.remote.dto.activities.GetActivitiesWithQuizzResponseDto
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalActivities
import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.domain.model.entity.OptionEntity
import com.example.classroom.domain.model.entity.QuestionEntity
import com.example.classroom.domain.model.entity.QuizEntity
import com.example.classroom.domain.model.entity.Status
import com.example.classroom.domain.model.entity.toCoursesLocal
import com.example.classroom.domain.model.entity.toLocal
import com.example.classroom.domain.model.entity.toLocalActivity
import com.example.classroom.domain.use_case.activities.DeleteActivityUseCase
import com.example.classroom.domain.use_case.activities.GetActivitiesByUserUseCase
import com.example.classroom.domain.use_case.activities.GetActivitiesUseCase
import com.example.classroom.domain.use_case.activities.InsertActivityUseCase
import com.example.classroom.domain.use_case.activities.UpdateActivityUseCase
import com.example.classroom.domain.use_case.validators.activities.ActivitiesValidator
import com.example.classroom.presentation.screens.activity.addActivity.states.ActivityFormEvent
import com.example.classroom.presentation.screens.activity.addActivity.states.ActivityFormState
import com.example.classroom.presentation.screens.activity.addActivity.states.AddActivityState
import com.example.classroom.presentation.screens.activity.addActivity.states.GetActivitiesState
import com.example.classroom.presentation.screens.home.states.CourseState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.common.Resource
import timber.log.Timber

class ActivityViewmodel(
    private val activitiesValidator: ActivitiesValidator,
    private val activityDataValidator: ActivityDataValidator,
    private val updateActivityUseCase: UpdateActivityUseCase,
    private val insertActivityUseCase: InsertActivityUseCase,
    private val getActivitiesUseCase: GetActivitiesUseCase,
    private val deleteActivityUseCase: DeleteActivityUseCase,
    private val getActivitiesByUserUseCase: GetActivitiesByUserUseCase,
    private val repositoryBundle: RepositoryBundle
) : ViewModel() {

    // Reactive state flows for different activity lists
    private val _filteredListActivitiesFlow = MutableStateFlow<List<LocalActivities>>(emptyList())
    val filteredListActivitiesFlow: StateFlow<List<LocalActivities>> = _filteredListActivitiesFlow

    private val _listActivitiesFlow = MutableStateFlow<List<LocalActivities>>(emptyList())
    val listActivitiesFlow: StateFlow<List<LocalActivities>> = _listActivitiesFlow

    private val _filteredListActivitiesByCourseFlow = MutableStateFlow<List<LocalActivities>>(emptyList())
    val filteredListActivitiesByCourseFlow: StateFlow<List<LocalActivities>> = _filteredListActivitiesByCourseFlow

    private val _listActivitiesByCourseFlow = MutableStateFlow<List<LocalActivities>>(emptyList())
    val listActivitiesByCourseFlow: StateFlow<List<LocalActivities>> = _listActivitiesByCourseFlow

    private val _filteredListActivitiesByCourseOwnerFlow = MutableStateFlow<List<LocalActivities>>(emptyList())
    val filteredListActivitiesByCourseOwnerFlow: StateFlow<List<LocalActivities>> = _filteredListActivitiesByCourseOwnerFlow

    private val _listActivitiesByCourseOwnerFlow = MutableStateFlow<List<LocalActivities>>(emptyList())
    val listActivitiesByCourseOwnerFlow: StateFlow<List<LocalActivities>> = _listActivitiesByCourseOwnerFlow

    // UI State for activity form
    var stateActivityForm by mutableStateOf(ActivityFormState())
    private val _stateAddActivity = mutableStateOf(AddActivityState())
    val stateAddActivity: State<AddActivityState> = _stateAddActivity

    private val _stateGetActivities = mutableStateOf(GetActivitiesState())
    val stateGetActivities: State<GetActivitiesState> = _stateGetActivities

    // Input fields and state management
    val titleField = mutableStateOf("")
    val descripcionField = mutableStateOf("")
    val dateField = mutableStateOf("")
    val gradeField = mutableStateOf("")
    val statusField = mutableStateOf(Status.OPEN)

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    private val _userInfo = MutableStateFlow<List<LocalUser>>(emptyList())
    val userInfo: StateFlow<List<LocalUser>> = _userInfo

    val activityInput = mutableStateOf("")

    private val _activityDetailInfo = MutableStateFlow<LocalActivities?>(null)
    val activityDetailInfo: StateFlow<LocalActivities?> = _activityDetailInfo

    init {
        viewModelScope.launch {
            repositoryBundle.loginRepository.getUserInfoWithFlow()
                .collect { _userInfo.value = it }

            filterListActivitiesByInput()
        }
    }

    fun filterActivities(query: String) {
        _filteredListActivitiesByCourseFlow.value = if (query.isBlank()) {
            _listActivitiesFlow.value
        } else {
            _listActivitiesFlow.value.filter { activity ->
                activity.title.contains(query, ignoreCase = true)
//                        || activity.description?.contains(query, ignoreCase = true) ?: em
            }
        }
    }

    fun filterActivitiesSubmitted(query: String) {
        _filteredListActivitiesByCourseFlow.value = if (query.isBlank()) {
            _listActivitiesFlow.value
        } else {
            _listActivitiesFlow.value.filter { activity ->
                activity.title.contains(query, ignoreCase = true)
//                        || activity.description?.contains(query, ignoreCase = true) ?: em
            }
        }
    }

    // Handle form events
    fun onActivityEvent(event: ActivityFormEvent) {
        when (event) {
            is ActivityFormEvent.TitleChanged -> stateActivityForm = stateActivityForm.copy(title = event.title)
            is ActivityFormEvent.DescriptionChanged -> stateActivityForm = stateActivityForm.copy(description = event.description)
            is ActivityFormEvent.EndDateChanged -> stateActivityForm = stateActivityForm.copy(endDate = event.date)
            is ActivityFormEvent.GradeChanged -> stateActivityForm = stateActivityForm.copy(grade = event.grade)
            is ActivityFormEvent.StartDateChanged -> stateActivityForm = stateActivityForm.copy(startDate = event.date)
            is ActivityFormEvent.StatusChanged -> stateActivityForm = stateActivityForm.copy(status = event.status)
            is ActivityFormEvent.Submit -> submitActivityData(event.id, event.body)
        }
    }

    // Submit activity data after validation
    private fun submitActivityData(id: String?, activity: ActivityRequestDto) {
        val titleResult = activitiesValidator.validateNames.execute(stateActivityForm.title)
        val descriptionResult = activitiesValidator.validateNames.execute(stateActivityForm.description)
        val gradeResult = activitiesValidator.validateGrade.execute(stateActivityForm.grade.toInt())

        val hasError = listOf(titleResult, descriptionResult, gradeResult).any { !it.successful }

        if (hasError) {
            stateActivityForm = stateActivityForm.copy(
                titleError = titleResult.errorMessage,
                descriptionError = descriptionResult.errorMessage,
                gradeError = gradeResult.errorMessage
            )
            return
        }

        viewModelScope.launch {
            executeActivityRequest(activity, id)
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    // Execute activity request (insert or update)
    private suspend fun executeActivityRequest(activityRequestDto: ActivityRequestDto, id: String?) {
        val resultFlow = if (id != null) {
            updateActivityUseCase(activityRequestDto, id)
        } else {
            insertActivityUseCase(activityRequestDto)
        }

        resultFlow.onEach { result ->
            when (result) {
                is Resource.Error -> _stateAddActivity.value = AddActivityState(error = result.message)
                is Resource.Loading -> _stateAddActivity.value = AddActivityState(isLoading = true)
                is Resource.Success -> {
                    _stateAddActivity.value = AddActivityState(info = result.data)
                    result.data?.let {
                        repositoryBundle.activitiesRepository.insertAllActivities(listOf(it))
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    // Get activities by course
    suspend fun getActivitiesByCourse(id: String) {
        getActivitiesUseCase(id).onEach { result ->
            when (result) {
                is Resource.Error -> _stateGetActivities.value = GetActivitiesState(error = result.message)
                is Resource.Loading -> _stateGetActivities.value = GetActivitiesState(isLoading = true)
                is Resource.Success -> {

                    val activities = result.data?.data ?: emptyList()

                    // Save activities
                    val localActivities = activities.map { it.toLocalActivity() }
                    repositoryBundle.activitiesRepository.insertAllActivities(localActivities)

                    // Save quizzes if questions are not empty
                    activities.forEach { activity ->
                        if (activity.questions.isNotEmpty()) {
                            saveQuizToLocalDatabase(activity)
                        }
                    }

                    _stateGetActivities.value = GetActivitiesState(info = localActivities)


//                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun saveQuizToLocalDatabase(activity: GetActivitiesWithQuizzResponseDto.Activity) {
        val quizEntity = QuizEntity(
            id = activity.quizzId ?: return,
            activityId = activity.idApi,
            title = activity.title
        )
        repositoryBundle.quizzRepository.insertQuiz(quizEntity)

        activity.questions.forEach { questionDto ->
            val questionEntity = QuestionEntity(
                id = questionDto.id,
                quizId = activity.quizzId ?: return,
                text = questionDto.text,
                correctAnswer = questionDto.answer
            )
            repositoryBundle.quizzRepository.insertQuestion(questionEntity)

            questionDto.options.forEach { optionDto ->
                val optionEntity = OptionEntity(
                    id = optionDto.id,
                    questionId = questionDto.id,
                    text = optionDto.text
                )
                repositoryBundle.quizzRepository.insertOption(optionEntity)
            }
        }
    }
    suspend fun deleteActivity(id: String) {
        deleteActivityUseCase(id).onEach { result ->
            when (result) {
                is Resource.Error -> _stateAddActivity.value = _stateAddActivity.value.copy(error = result.message, isLoading = false)
                is Resource.Loading -> _stateAddActivity.value = _stateAddActivity.value.copy(error = null, isLoading = true)
                is Resource.Success -> {
                    _stateAddActivity.value = _stateAddActivity.value.copy(error = null, isLoading = false)
                    result.data?.let {
//                        repositoryBundle.activitiesRepository.insertAllActivities(listOf(it))
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    // Get activities by user
    suspend fun getActivitiesByUser(id: String) {
        getActivitiesByUserUseCase(id).onEach { result ->
            when (result) {
                is Resource.Error -> _stateGetActivities.value = GetActivitiesState(error = result.message)
                is Resource.Loading -> _stateGetActivities.value = GetActivitiesState(isLoading = true)
                is Resource.Success -> {
                    _stateGetActivities.value = GetActivitiesState(info = result.data)
                    _stateGetActivities.value.info?.let {
                        repositoryBundle.activitiesRepository.insertAllActivities(it)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    // Get activities by course locally
    suspend fun getActivitiesLocalByCourse(id: String) {
        repositoryBundle.activitiesRepository.getActivitiesWithFlowByCourse(id).collect {
            _listActivitiesByCourseFlow.value = it
            filterListActivitiesByInput()
        }
    }

    // Get specific activity by ID
    suspend fun getActivityById(id: String) {
        repositoryBundle.activitiesRepository.getActivityWithFlowById(id).collect {
            _activityDetailInfo.value = it
        }
    }

    // Filter activities by user input
    fun filterListActivitiesByInput() {
        viewModelScope.launch {
            _filteredListActivitiesByCourseFlow.value = if (activityInput.value.isNotEmpty()) {
                listActivitiesByCourseFlow.value.filter { it.title.startsWith(activityInput.value) }
                    .sortedByDescending { it.id }
            } else {
                listActivitiesByCourseFlow.value
            }
        }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}

//class ActivityViewmodel(
//    private val activitiesValidator: ActivitiesValidator,
//    private val activityDataValidator: ActivityDataValidator,
//    private val updateActivityUseCase: UpdateActivityUseCase,
//    private val insertActivityUseCase: InsertActivityUseCase,
//    private val getActivitiesUseCase: GetActivitiesUseCase,
//    private val getActivitiesByUserUseCase: GetActivitiesByUserUseCase,
//    private val repositoryBundle: RepositoryBundle
//) : ViewModel() {
//
//    // Reactive state flows for different activity lists
//    private val _filteredListActivitiesFlow = MutableStateFlow<List<LocalActivities>>(emptyList())
//    val filteredListActivitiesFlow: StateFlow<List<LocalActivities>> = _filteredListActivitiesFlow
//
//    private val _listActivitiesFlow = MutableStateFlow<List<LocalActivities>>(emptyList())
//    val listActivitiesFlow: StateFlow<List<LocalActivities>> = _listActivitiesFlow
//
//    private val _filteredListActivitiesByCourseFlow = MutableStateFlow<List<LocalActivities>>(emptyList())
//    val filteredListActivitiesByCourseFlow: StateFlow<List<LocalActivities>> = _filteredListActivitiesByCourseFlow
//
//    private val _listActivitiesByCourseFlow = MutableStateFlow<List<LocalActivities>>(emptyList())
//    val listActivitiesByCourseFlow: StateFlow<List<LocalActivities>> = _listActivitiesByCourseFlow
//
//    private val _filteredListActivitiesByCourseOwnerFlow = MutableStateFlow<List<LocalActivities>>(emptyList())
//    val filteredListActivitiesByCourseOwnerFlow: StateFlow<List<LocalActivities>> = _filteredListActivitiesByCourseOwnerFlow
//
//    private val _listActivitiesByCourseOwnerFlow = MutableStateFlow<List<LocalActivities>>(emptyList())
//    val listActivitiesByCourseOwnerFlow: StateFlow<List<LocalActivities>> = _listActivitiesByCourseOwnerFlow
//
//    // UI State for activity form
//    var stateActivityForm by mutableStateOf(ActivityFormState())
//    private val _stateAddActivity = mutableStateOf(AddActivityState())
//    val stateAddActivity: State<AddActivityState> = _stateAddActivity
//
//    private val _stateGetActivities = mutableStateOf(GetActivitiesState())
//    val stateGetActivities: State<GetActivitiesState> = _stateGetActivities
//
//    // Input fields and state management
//    val titleField = mutableStateOf("")
//    val descripcionField = mutableStateOf("")
//    val dateField = mutableStateOf("")
//    val gradeField = mutableStateOf("")
//    val statusField = mutableStateOf(Status.OPEN)
//
//    private val validationEventChannel = Channel<ValidationEvent>()
//    val validationEvents = validationEventChannel.receiveAsFlow()
//
//    private val _userInfo = MutableStateFlow<List<LocalUser>>(emptyList())
//    val userInfo: StateFlow<List<LocalUser>> = _userInfo
//
//    val activityInput = mutableStateOf("")
//
//    private val _activityDetailInfo = MutableStateFlow<LocalActivities?>(null)
//    val activityDetailInfo: StateFlow<LocalActivities?> = _activityDetailInfo
//
//    init {
//        viewModelScope.launch {
//            repositoryBundle.loginRepository.getUserInfoWithFlow()
//                .collect { _userInfo.value = it }
//
//            filterListActivitiesByInput()
//        }
//    }
//
//    // Handle form events
//    fun onActivityEvent(event: ActivityFormEvent) {
//        when (event) {
//            is ActivityFormEvent.TitleChanged -> stateActivityForm = stateActivityForm.copy(title = event.title)
//            is ActivityFormEvent.DescriptionChanged -> stateActivityForm = stateActivityForm.copy(description = event.description)
//            is ActivityFormEvent.EndDateChanged -> stateActivityForm = stateActivityForm.copy(endDate = event.date)
//            is ActivityFormEvent.GradeChanged -> stateActivityForm = stateActivityForm.copy(grade = event.grade)
//            is ActivityFormEvent.StartDateChanged -> stateActivityForm = stateActivityForm.copy(startDate = event.date)
//            is ActivityFormEvent.StatusChanged -> stateActivityForm = stateActivityForm.copy(status = event.status)
//            is ActivityFormEvent.Submit -> submitActivityData(event.id, event.body)
//        }
//    }
//
//    // Submit activity data after validation
//    private fun submitActivityData(id: String?, activity: ActivityRequestDto) {
//        val titleResult = activitiesValidator.validateNames.execute(stateActivityForm.title)
//        val descriptionResult = activitiesValidator.validateNames.execute(stateActivityForm.description)
//        val gradeResult = activitiesValidator.validateGrade.execute(stateActivityForm.grade.toInt())
//
//        val hasError = listOf(titleResult, descriptionResult, gradeResult).any { !it.successful }
//
//        if (hasError) {
//            stateActivityForm = stateActivityForm.copy(
//                titleError = titleResult.errorMessage,
//                descriptionError = descriptionResult.errorMessage,
//                gradeError = gradeResult.errorMessage
//            )
//            return
//        }
//
//        viewModelScope.launch {
//            executeActivityRequest(activity, id)
//            validationEventChannel.send(ValidationEvent.Success)
//        }
//    }
//
//    // Execute activity request (insert or update)
//    private suspend fun executeActivityRequest(activityRequestDto: ActivityRequestDto, id: String?) {
//        if (id != null) {
//            // Use the updateActivityUseCase when id is not null
//            updateActivityUseCase(activityRequestDto, id).onEach { result ->
//                handleActivityResult(result)
//            }.launchIn(viewModelScope)
//        } else {
//            // Use the insertActivityUseCase when id is null
//            insertActivityUseCase(activityRequestDto).onEach { result ->
//                handleActivityResult(result)
//            }.launchIn(viewModelScope)
//        }
//    }
//
//    // Function to handle the result of the use case
//    private fun handleActivityResult(result: Resource<LocalActivities>) {
//        when (result) {
//            is Resource.Error -> _stateAddActivity.value = AddActivityState(error = result.message)
//            is Resource.Loading -> _stateAddActivity.value = AddActivityState(isLoading = true)
//            is Resource.Success -> {
//                _stateAddActivity.value = AddActivityState(info = result.data)
//                _stateAddActivity.value.info?.let {
//                    viewModelScope.launch {
//                        repositoryBundle.activitiesRepository.insertAllActivities(listOf(it))
//                        delay(300)
//                    }
//                }
//            }
//        }
//    }
//    // Get activities by course
//    suspend fun getActivitiesByCourse(id: String) {
//        getActivitiesUseCase(id).onEach { result ->
//            when (result) {
//                is Resource.Error -> _stateGetActivities.value = GetActivitiesState(error = result.message)
//                is Resource.Loading -> _stateGetActivities.value = GetActivitiesState(isLoading = true)
//                is Resource.Success -> {
//                    _stateGetActivities.value = GetActivitiesState(info = result.data?.toLocal())
//                    _stateGetActivities.value.info?.let {
//                        repositoryBundle.activitiesRepository.insertAllActivities(it)
//                        delay(1000)
//                    }
//                }
//            }
//        }.launchIn(viewModelScope)
//    }
//
//    // Get activities by user
//    suspend fun getActivitiesByUser(id: String) {
//        getActivitiesByUserUseCase(id).onEach { result ->
//            when (result) {
//                is Resource.Error -> _stateGetActivities.value = GetActivitiesState(error = result.message)
//                is Resource.Loading -> _stateGetActivities.value = GetActivitiesState(isLoading = true)
//                is Resource.Success -> {
//                    _stateGetActivities.value = GetActivitiesState(info = result.data)
//                    _stateGetActivities.value.info?.let {
//                        repositoryBundle.activitiesRepository.insertAllActivities(it)
//                        delay(1000)
//                    }
//                }
//            }
//        }.launchIn(viewModelScope)
//    }
//
//    // Get activities by course locally
//    suspend fun getActivitiesLocalByCourse(id: String) {
//        repositoryBundle.activitiesRepository.getActivitiesWithFlowByCourse(id).collect {
//            _listActivitiesByCourseFlow.value = it
//            filterListActivitiesByInput()
//        }
//    }
//
//    // Get specific activity by ID
//    suspend fun getActivityById(id: String) {
//        repositoryBundle.activitiesRepository.getActivityWithFlowById(id).collect {
//            _activityDetailInfo.value = it
//        }
//    }
//
//    // Filter activities by user input
//    fun filterListActivitiesByInput() {
//        viewModelScope.launch {
//            _filteredListActivitiesByCourseFlow.value = if (activityInput.value.isNotEmpty()) {
//                listActivitiesByCourseFlow.value.filter { it.title.startsWith(activityInput.value) }
//                    .sortedByDescending { it.id }
//            } else {
//                listActivitiesByCourseFlow.value
//            }
//        }
//    }
//
//    sealed class ValidationEvent {
//        object Success : ValidationEvent()
//    }
//}


//class ActivityViewmodel(
//    private val activitiesValidator: ActivitiesValidator,
//    private val activityDataValidator: ActivityDataValidator,
//    private val updateActivityUseCase: UpdateActivityUseCase,
//    private val insertActivityUseCase: InsertActivityUseCase,
//    private val getActivitiesUseCase: GetActivitiesUseCase,
//    private val getActivitiesByUserUseCase: GetActivitiesByUserUseCase,
////    private val activitiesRepositoryImpl: ActivitiesRepositoryImpl,
////    private val sendGradeActivityUseCase: GradeActivityUseCase,
//    private val repositoryBundle: RepositoryBundle
//): ViewModel() {
//
//    var filteredListActivitiesFLow: Flow<List<LocalActivities>> = emptyFlow()
//    var listActivitiesFlow: Flow<List<LocalActivities>> = emptyFlow()
//
//    var filteredListActivitiesByCourseFLow: Flow<List<LocalActivities>> = emptyFlow()
//    var listActivitiesByCourseFlow: Flow<List<LocalActivities>> = emptyFlow()
//
//    var filteredListActivitiesByCourseOwnerFLow: Flow<List<LocalActivities>> = emptyFlow()
//    var listActivitiesByCourseOwnerFlow: Flow<List<LocalActivities>> = emptyFlow()
//
//    var stateActivityForm by mutableStateOf(ActivityFormState())
//
//    private val _stateAddActivity = mutableStateOf(AddActivityState())
//    val stateAddActivity: State<AddActivityState> = _stateAddActivity
//
//    private val _stateGetActivities = mutableStateOf(GetActivitiesState())
//    val stateGetActivities: State<GetActivitiesState> = _stateGetActivities
//
//
//    val titleField = mutableStateOf("")
//    val descripcionField = mutableStateOf("")
//    val dateField = mutableStateOf("")
//    val gradeField = mutableStateOf("")
//    val statusField = mutableStateOf(Status.OPEN)
//
//    private val validationEventChannel = Channel<ValidationEvent>()
//    val validationEvents = validationEventChannel.receiveAsFlow()
//
//    var userInfo: Flow<List<LocalUser>> = emptyFlow()
//    val activityInput = mutableStateOf("")
//
//    var activityDetailInfo: Flow<LocalActivities?>  = emptyFlow()
//
//
//    init {
//        viewModelScope.launch {
//            userInfo = repositoryBundle.loginRepository.getUserInfoWithFlow()
//            filteredListActivitiesByCourseFLow = if (activityInput.value.isNotBlank()){
//                listActivitiesByCourseFlow.map { list ->
//                    list.filter { it.title.startsWith(activityInput.value) }
//                        .sortedByDescending { activity -> activity.id }
//                }
//            }else{
//                listActivitiesByCourseFlow
//            }
//        }
//    }
//    fun onActivityEvent(event: ActivityFormEvent) {
//        when(event) {
//
//            is ActivityFormEvent.TitleChanged -> {
//                stateActivityForm = stateActivityForm.copy(title = event.title)
//            }
//            is ActivityFormEvent.DescriptionChanged ->{
//                stateActivityForm = stateActivityForm.copy(description = event.description)
//            }
//            is ActivityFormEvent.EndDateChanged -> {
//                stateActivityForm = stateActivityForm.copy(endDate = event.date)
//            }
//            is ActivityFormEvent.GradeChanged -> {
//                stateActivityForm = stateActivityForm.copy(grade = event.grade)
//            }
//            is ActivityFormEvent.StartDateChanged -> {
//                stateActivityForm = stateActivityForm.copy(startDate = event.date)
//            }
//            is ActivityFormEvent.StatusChanged -> {
//                stateActivityForm = stateActivityForm.copy(status = event.status)
//            }
//            is ActivityFormEvent.Submit -> {
//                submitActivityData(event.id, event.body)
//            }
//        }
//    }
//    private fun submitActivityData(id: String?, activity: ActivityRequestDto) {
//        val titleResult = activitiesValidator.validateNames.execute(stateActivityForm.title)
//        val descriptionResult = activitiesValidator.validateNames.execute(stateActivityForm.description)
////        val startDateResult = activitiesValidator.validateDate.execute(stateActivityForm.startDate)
////        val endDateResult = activitiesValidator.validateDate.execute(stateActivityForm.endDate)
//        val gradeResult = activitiesValidator.validateGrade.execute(stateActivityForm.grade.toInt())
//
//
//
//        val hasError = listOf(
//            titleResult,
//            descriptionResult,
////            startDateResult,
////            endDateResult,
//            gradeResult
//        ).any { !it.successful }
//
//        if(hasError) {
//            stateActivityForm = stateActivityForm.copy(
////                startDateError = startDateResult.errorMessage,
////                endDateError = endDateResult.errorMessage,
//                titleError = titleResult.errorMessage,
//                descriptionError = descriptionResult.errorMessage,
//                gradeError = gradeResult.errorMessage,
//
//            )
//            return
//        }
//        Log.e("final", "final")
//        viewModelScope.launch {
//            Log.e("final2", "final2")
//            executeActivityRequest(activity, id)
//            validationEventChannel.send(ActivityViewmodel.ValidationEvent.Success)
//        }
//    }
//
//    suspend fun executeActivityRequest(activityRequestDto: ActivityRequestDto, id: String?){
//
//        if (id != null){
//            updateActivityUseCase(activityRequestDto, id).onEach { result ->
//                when(result){
//                    is Resource.Error -> {
//                        //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
//                        Log.e("ACTIVITIES:", "Error ${result.message?.uiMessage}")
//                        _stateAddActivity.value = AddActivityState(error = result.message)
//                    }
//                    is Resource.Loading -> {
//                        Timber.tag("ACTIVITIES").e("is loading")
//                        _stateAddActivity.value = AddActivityState(isLoading = true)
//                    }
//                    is Resource.Success -> {
//                        Timber.tag("ACTIVITIES_VM").e("success")
//                        Log.e("ACTIVITIES:", "success")
//                        _stateAddActivity.value = AddActivityState(info = result.data)
//                        Log.e("ACTIVITIES:", "${_stateAddActivity.value.info}")
//                        _stateAddActivity.value.info?.let {
////                            insertUserDb(it)
//                            delay(300)
//
//                        }
//                    }
//                }
//            }.launchIn(viewModelScope)
//        }else {
//            insertActivityUseCase(activityRequestDto).onEach { result ->
//                when(result){
//                    is Resource.Error -> {
//                        //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
//                        Log.e("ACTIVITIES:", "Error ${result.message?.uiMessage}")
//                        _stateAddActivity.value = AddActivityState(error = result.message)
//                    }
//                    is Resource.Loading -> {
//                        Timber.tag("ACTIVITIES").e("is loading")
//                        _stateAddActivity.value = AddActivityState(isLoading = true)
//                    }
//                    is Resource.Success -> {
//                        Timber.tag("ACTIVITIES_VM").e("success")
//                        Log.e("ACTIVITIES:", "success")
//                        _stateAddActivity.value = AddActivityState(info = result.data)
//                        Log.e("ACTIVITIES:", "${_stateAddActivity.value.info}")
//                        _stateAddActivity.value.info?.let {
////                            insertUserDb(it)
//                            delay(300)
//
//                        }
//                    }
//                }
//            }.launchIn(viewModelScope)
//        }
//
//    }
//
//    suspend fun getActivitiesByCourse(id: String){
//        getActivitiesUseCase(id).onEach { result ->
//            when(result){
//                is Resource.Error -> {
//                    //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
//                    Log.e("HOME_VM:", "Error ${result.message?.uiMessage}")
//                    _stateGetActivities.value = GetActivitiesState(error = result.message)
//                }
//                is Resource.Loading -> {
//                    Timber.tag("HOME_VM").e("is loading")
//                    _stateGetActivities.value = GetActivitiesState(isLoading = true)
//                }
//                is Resource.Success -> {
//                    Timber.tag("HOME_VM").e("success")
//                    Log.e("HOME_VM:", "success")
//                    _stateGetActivities.value = GetActivitiesState(info = result.data?.toLocal())
//                    Log.e("HOME_VM:", "${_stateGetActivities.value.info}")
//                    _stateGetActivities.value.info?.let {
//                        repositoryBundle.activitiesRepository.insertAllActivities(it)
//                        delay(1000)
//                    }
//                }
//            }
//
//        }.launchIn(viewModelScope)
//    }
//
//    suspend fun getActivitiesByUser(id: String){
//        getActivitiesByUserUseCase(id).onEach { result ->
//            when(result){
//                is Resource.Error -> {
//                    //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
//                    Log.e("HOME_VM:", "Error ${result.message?.uiMessage}")
//                    _stateGetActivities.value = GetActivitiesState(error = result.message)
//                }
//                is Resource.Loading -> {
//                    Timber.tag("HOME_VM").e("is loading")
//                    _stateGetActivities.value = GetActivitiesState(isLoading = true)
//                }
//                is Resource.Success -> {
//                    Timber.tag("HOME_VM").e("success")
//                    Log.e("HOME_VM:", "success")
//                    _stateGetActivities.value = GetActivitiesState(info = result.data)
//                    Log.e("HOME_VM:", "${_stateGetActivities.value.info}")
//                    _stateGetActivities.value.info?.let {
//                        repositoryBundle.activitiesRepository.insertAllActivities(it)
//                        delay(1000)
//                    }
//                }
//            }
//
//        }.launchIn(viewModelScope)
//    }
//
//
//    suspend fun getActivitiesLocalByCourse(id: String){
//        Log.e("idActicity", id)
//        listActivitiesByCourseFlow = repositoryBundle.activitiesRepository.getActivitiesWithFlowByCourse(id)
//        filteredListActivitiesByCourseFLow = if (activityInput.value.isNotEmpty()){
//            Log.e("is in", "is in")
//            listActivitiesByCourseFlow.map { list ->
//                list.filter { it.title.startsWith(activityInput.value) }
//                    .sortedByDescending { activity -> activity.id }
//            }
//        }else{
//            listActivitiesByCourseFlow
//        }
//        Log.e("ActivitiesVM", listActivitiesByCourseFlow.first().toString())
//        Log.e("ActivitiesFiltereVM", filteredListActivitiesByCourseFLow.first().toString())
//
//    }
//
//    suspend fun getActivityById(id: String){
//        activityDetailInfo = repositoryBundle.activitiesRepository.getActivityWithFlowById(id)
//    }
//
//    fun filterListActivitiesCourseByInput() {
//        viewModelScope.launch {
//            filteredListActivitiesByCourseFLow = if (activityInput.value.isNotEmpty()){
//                listActivitiesByCourseFlow.map { list ->
//                    list.filter { it.title.startsWith(activityInput.value) }
//                        .sortedByDescending { activity -> activity.id }
//                }
//            }else{
//                listActivitiesByCourseFlow
//            }
//        }
//    }
//
//
//    sealed class ValidationEvent {
//        object Success: ValidationEvent()
//    }
//}
