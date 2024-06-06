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
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalActivities
import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.domain.model.entity.Status
import com.example.classroom.domain.model.entity.toCoursesLocal
import com.example.classroom.domain.model.entity.toLocal
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
    private val getActivitiesByUserUseCase: GetActivitiesByUserUseCase,
//    private val activitiesRepositoryImpl: ActivitiesRepositoryImpl,
//    private val sendGradeActivityUseCase: GradeActivityUseCase,
    private val repositoryBundle: RepositoryBundle
): ViewModel() {

    var filteredListActivitiesFLow: Flow<List<LocalActivities>> = emptyFlow()
    var listActivitiesFlow: Flow<List<LocalActivities>> = emptyFlow()

    var filteredListActivitiesByCourseFLow: Flow<List<LocalActivities>> = emptyFlow()
    var listActivitiesByCourseFlow: Flow<List<LocalActivities>> = emptyFlow()

    var filteredListActivitiesByCourseOwnerFLow: Flow<List<LocalActivities>> = emptyFlow()
    var listActivitiesByCourseOwnerFlow: Flow<List<LocalActivities>> = emptyFlow()

    var stateActivityForm by mutableStateOf(ActivityFormState())

    private val _stateAddActivity = mutableStateOf(AddActivityState())
    val stateAddActivity: State<AddActivityState> = _stateAddActivity

    private val _stateGetActivities = mutableStateOf(GetActivitiesState())
    val stateGetActivities: State<GetActivitiesState> = _stateGetActivities


    val titleField = mutableStateOf("")
    val descripcionField = mutableStateOf("")
    val dateField = mutableStateOf("")
    val gradeField = mutableStateOf("")
    val statusField = mutableStateOf(Status.OPEN)

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    var userInfo: Flow<List<LocalUser>> = emptyFlow()
    val activityInput = mutableStateOf("")

    var activityDetailInfo: Flow<LocalActivities?>  = emptyFlow()


    init {
        viewModelScope.launch {
            userInfo = repositoryBundle.loginRepository.getUserInfoWithFlow()
            filteredListActivitiesByCourseFLow = if (activityInput.value.isNotBlank()){
                listActivitiesByCourseFlow.map { list ->
                    list.filter { it.title.startsWith(activityInput.value) }
                        .sortedByDescending { activity -> activity.id }
                }
            }else{
                listActivitiesByCourseFlow
            }
        }
    }
    fun onActivityEvent(event: ActivityFormEvent) {
        when(event) {

            is ActivityFormEvent.TitleChanged -> {
                stateActivityForm = stateActivityForm.copy(title = event.title)
            }
            is ActivityFormEvent.DescriptionChanged ->{
                stateActivityForm = stateActivityForm.copy(description = event.description)
            }
            is ActivityFormEvent.EndDateChanged -> {
                stateActivityForm = stateActivityForm.copy(endDate = event.date)
            }
            is ActivityFormEvent.GradeChanged -> {
                stateActivityForm = stateActivityForm.copy(grade = event.grade)
            }
            is ActivityFormEvent.StartDateChanged -> {
                stateActivityForm = stateActivityForm.copy(startDate = event.date)
            }
            is ActivityFormEvent.StatusChanged -> {
                stateActivityForm = stateActivityForm.copy(status = event.status)
            }
            is ActivityFormEvent.Submit -> {
                submitActivityData(event.id, event.body)
            }
        }
    }
    private fun submitActivityData(id: String?, activity: ActivityRequestDto) {
        val titleResult = activitiesValidator.validateNames.execute(stateActivityForm.title)
        val descriptionResult = activitiesValidator.validateNames.execute(stateActivityForm.description)
//        val startDateResult = activitiesValidator.validateDate.execute(stateActivityForm.startDate)
//        val endDateResult = activitiesValidator.validateDate.execute(stateActivityForm.endDate)
        val gradeResult = activitiesValidator.validateGrade.execute(stateActivityForm.grade.toInt())



        val hasError = listOf(
            titleResult,
            descriptionResult,
//            startDateResult,
//            endDateResult,
            gradeResult
        ).any { !it.successful }

        if(hasError) {
            stateActivityForm = stateActivityForm.copy(
//                startDateError = startDateResult.errorMessage,
//                endDateError = endDateResult.errorMessage,
                titleError = titleResult.errorMessage,
                descriptionError = descriptionResult.errorMessage,
                gradeError = gradeResult.errorMessage,

            )
            return
        }
        Log.e("final", "final")
        viewModelScope.launch {
            Log.e("final2", "final2")
            executeActivityRequest(activity, id)
            validationEventChannel.send(ActivityViewmodel.ValidationEvent.Success)
        }
    }

    suspend fun executeActivityRequest(activityRequestDto: ActivityRequestDto, id: String?){

        if (id != null){
            updateActivityUseCase(activityRequestDto, id).onEach { result ->
                when(result){
                    is Resource.Error -> {
                        //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
                        Log.e("ACTIVITIES:", "Error ${result.message?.uiMessage}")
                        _stateAddActivity.value = AddActivityState(error = result.message)
                    }
                    is Resource.Loading -> {
                        Timber.tag("ACTIVITIES").e("is loading")
                        _stateAddActivity.value = AddActivityState(isLoading = true)
                    }
                    is Resource.Success -> {
                        Timber.tag("ACTIVITIES_VM").e("success")
                        Log.e("ACTIVITIES:", "success")
                        _stateAddActivity.value = AddActivityState(info = result.data)
                        Log.e("ACTIVITIES:", "${_stateAddActivity.value.info}")
                        _stateAddActivity.value.info?.let {
//                            insertUserDb(it)
                            delay(300)

                        }
                    }
                }
            }.launchIn(viewModelScope)
        }else {
            insertActivityUseCase(activityRequestDto).onEach { result ->
                when(result){
                    is Resource.Error -> {
                        //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
                        Log.e("ACTIVITIES:", "Error ${result.message?.uiMessage}")
                        _stateAddActivity.value = AddActivityState(error = result.message)
                    }
                    is Resource.Loading -> {
                        Timber.tag("ACTIVITIES").e("is loading")
                        _stateAddActivity.value = AddActivityState(isLoading = true)
                    }
                    is Resource.Success -> {
                        Timber.tag("ACTIVITIES_VM").e("success")
                        Log.e("ACTIVITIES:", "success")
                        _stateAddActivity.value = AddActivityState(info = result.data)
                        Log.e("ACTIVITIES:", "${_stateAddActivity.value.info}")
                        _stateAddActivity.value.info?.let {
//                            insertUserDb(it)
                            delay(300)

                        }
                    }
                }
            }.launchIn(viewModelScope)
        }

    }

    suspend fun getActivitiesByCourse(id: String){
        getActivitiesUseCase(id).onEach { result ->
            when(result){
                is Resource.Error -> {
                    //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
                    Log.e("HOME_VM:", "Error ${result.message?.uiMessage}")
                    _stateGetActivities.value = GetActivitiesState(error = result.message)
                }
                is Resource.Loading -> {
                    Timber.tag("HOME_VM").e("is loading")
                    _stateGetActivities.value = GetActivitiesState(isLoading = true)
                }
                is Resource.Success -> {
                    Timber.tag("HOME_VM").e("success")
                    Log.e("HOME_VM:", "success")
                    _stateGetActivities.value = GetActivitiesState(info = result.data?.toLocal())
                    Log.e("HOME_VM:", "${_stateGetActivities.value.info}")
                    _stateGetActivities.value.info?.let {
                        repositoryBundle.activitiesRepository.insertAllActivities(it)
                        delay(1000)
                    }
                }
            }

        }.launchIn(viewModelScope)
    }

    suspend fun getActivitiesByUser(id: String){
        getActivitiesByUserUseCase(id).onEach { result ->
            when(result){
                is Resource.Error -> {
                    //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
                    Log.e("HOME_VM:", "Error ${result.message?.uiMessage}")
                    _stateGetActivities.value = GetActivitiesState(error = result.message)
                }
                is Resource.Loading -> {
                    Timber.tag("HOME_VM").e("is loading")
                    _stateGetActivities.value = GetActivitiesState(isLoading = true)
                }
                is Resource.Success -> {
                    Timber.tag("HOME_VM").e("success")
                    Log.e("HOME_VM:", "success")
                    _stateGetActivities.value = GetActivitiesState(info = result.data)
                    Log.e("HOME_VM:", "${_stateGetActivities.value.info}")
                    _stateGetActivities.value.info?.let {
                        repositoryBundle.activitiesRepository.insertAllActivities(it)
                        delay(1000)
                    }
                }
            }

        }.launchIn(viewModelScope)
    }


    suspend fun getActivitiesLocalByCourse(id: String){
        Log.e("idActicity", id)
        listActivitiesByCourseFlow = repositoryBundle.activitiesRepository.getActivitiesWithFlowByCourse(id)
        filteredListActivitiesByCourseFLow = if (activityInput.value.isNotEmpty()){
            Log.e("is in", "is in")
            listActivitiesByCourseFlow.map { list ->
                list.filter { it.title.startsWith(activityInput.value) }
                    .sortedByDescending { activity -> activity.id }
            }
        }else{
            listActivitiesByCourseFlow
        }
        Log.e("ActivitiesVM", listActivitiesByCourseFlow.first().toString())
        Log.e("ActivitiesFiltereVM", filteredListActivitiesByCourseFLow.first().toString())

    }

    suspend fun getActivityById(id: String){
        activityDetailInfo = repositoryBundle.activitiesRepository.getActivityWithFlowById(id)
    }

    fun filterListActivitiesCourseByInput() {
        viewModelScope.launch {
            filteredListActivitiesByCourseFLow = if (activityInput.value.isNotEmpty()){
                listActivitiesByCourseFlow.map { list ->
                    list.filter { it.title.startsWith(activityInput.value) }
                        .sortedByDescending { activity -> activity.id }
                }
            }else{
                listActivitiesByCourseFlow
            }
        }
    }

//    fun filterListActivitiesCourseOwnerByInput() {
//        viewModelScope.launch {
//            filteredListActivitiesByCourseFLow = if (activityInput.value.isNotEmpty()){
//                listActivitiesFlow.map { list ->
//                    list.filter { it.title.startsWith(activityInput.value) }
//                        .sortedByDescending { activity -> activity.id }
//                }
//            }else{
//                listActivitiesFlow
//            }
//        }
//    }

//    suspend fun onCreateActivtyClick(activity: ActivityRequestDto, id: String?){
//            val isValid = mutableStateOf(false)
//
//            when (val result = activityDataValidator.validateTitle(activity.title)) {
//                is com.example.classroom.common.validator.Result.Error -> {
//                    when (result.error) {
//                        ActivityDataValidator.InputError.TOO_SHORT -> {
//                            isValid.value = false
//                        }
//                    }
//                }
//
//                is com.example.classroom.common.validator.Result.Success -> {
//                    isValid.value = true
//                }
//            }
//
//            when (val result =
//                activity.description?.let { activityDataValidator.validateDescription(it) }) {
//                is com.example.classroom.common.validator.Result.Error -> {
//                    when (result.error) {
//                        ActivityDataValidator.InputError.TOO_SHORT -> {
//                            isValid.value = false
//                        }
//                    }
//                }
//
//                is com.example.classroom.common.validator.Result.Success -> {
//                    isValid.value = true
//                }
//
//                else -> {
//                    isValid.value = true
//                }
//            }
//
//
//
//            when (val result = activityDataValidator.validateDate()) {
//                is com.example.classroom.common.validator.Result.Error -> {
//                    when (result.error) {
//                        ActivityDataValidator.GradeError.TOO_MUCH -> {
//                            isValid.value = false
//                        }
//                        ActivityDataValidator.GradeError.TOO_LITTLE -> {
//                            isValid.value = false
//                        }
//                    }
//                }
//
//                is com.example.classroom.common.validator.Result.Success -> {
//                    isValid.value = true
//                }
//            }
//
//            when (val result = activity.grade?.let { activityDataValidator.validateGrade(it) }) {
//                is com.example.classroom.common.validator.Result.Error -> {
//                    when (result.error) {
//                        ActivityDataValidator.GradeError.TOO_MUCH -> {
//                            isValid.value = false
//                        }
//                        ActivityDataValidator.GradeError.TOO_LITTLE -> {
//                            isValid.value = false
//                        }
//                    }
//                }
//
//                is com.example.classroom.common.validator.Result.Success -> {
//                    isValid.value = true
//                }
//
//                else -> {
//                    isValid.value = true
//                }
//            }
//
//
//            if (isValid.value){
//                if (id != null){
////                updateActivityUseCase(activity).onEach { result ->
////                    when(result){
////                        is Resource.Error -> {
////                            //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
////                            Log.e("AUTH_VM:", "Error ${result.message?.uiMessage}")
////                            _stateAddActivity.value = AddActivityState(error = result.message)
////                        }
////                        is Resource.Loading -> {
////                            Timber.tag("AUTH_VM").e("is loading")
////                            _stateAddActivity.value = AddActivityState(isLoading = true)
////                        }
////                        is Resource.Success -> {
////                            Timber.tag("AUTH_VM").e("success")
////                            Log.e("AUTH_VM:", "success")
////                            _stateAddActivity.value = AddActivityState(info = result.data)
////                            Log.e("AUTH_VM:", "${stateAddActivity.value.info}")
////                            _stateAddActivity.value.info?.let {
////
////                                delay(1000)
////
////                            }
////                        }
////                    }
////
////                }.launchIn(viewModelScope)
//                }else{
////                    insertActivityUseCase(activity).onEach { result ->
////                    when(result){
////                        is Resource.Error -> {
////                            //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
////                            Log.e("AUTH_VM:", "Error ${result.message?.uiMessage}")
////                            _stateAddActivity.value = AddActivityState(error = result.message)
////                        }
////                        is Resource.Loading -> {
////                            Timber.tag("AUTH_VM").e("is loading")
////                            _stateAddActivity.value = AddActivityState(isLoading = true)
////                        }
////                        is Resource.Success -> {
////                            Timber.tag("AUTH_VM").e("success")
////                            Log.e("AUTH_VM:", "success")
////                            _stateAddActivity.value = AddActivityState(info = result.data)
////                            Log.e("AUTH_VM:", "${stateAddActivity.value.info}")
////                            _stateAddActivity.value.info?.let {
////
////                                delay(1000)
////
////                            }
////                        }
////                    }
////
////                }.launchIn(viewModelScope)
//                }
//            }
//        }

    sealed class ValidationEvent {
        object Success: ValidationEvent()
    }
}
