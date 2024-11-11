package com.example.classroom.presentation.screens.activity.studentEvaluations
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classroom.common.uiState.UiState
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalActivitySubmission
import com.example.classroom.domain.model.entity.LocalStudentEvaluation
import com.example.classroom.domain.model.entity.toLocal
import com.example.classroom.domain.use_case.evaluations.getActivitiesSubmittedByStudent.GetActivitiesSubmitedByStudent
import com.example.classroom.presentation.screens.activity.addActivity.states.GetActivitiesState
import com.example.classroom.presentation.screens.activity.studentEvaluations.states.StudentEvaluationsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.common.Resource
import timber.log.Timber

class StudentEvaluationsViewModel(
    private val repositoryBundle: RepositoryBundle,
    private val getActivitiesSubmitedByStudent: GetActivitiesSubmitedByStudent,
) : ViewModel() {

    private val _stateStudentEvaluations = mutableStateOf(StudentEvaluationsState())
    val stateStudentEvaluations: State<StudentEvaluationsState> = _stateStudentEvaluations

    private val _uiState = MutableStateFlow<UiState<List<LocalActivitySubmission>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<LocalActivitySubmission>>> = _uiState.asStateFlow()

    // Load evaluations for a specific student in a course using Flow
    fun loadStudentEvaluations(activityId: String, studentId: String) {
        viewModelScope.launch {
            Log.e("students", "Loading evaluations for activityId: $activityId, studentId: $studentId")

            // Set loading state at the beginning of the call
            _uiState.value = UiState.Loading

            repositoryBundle.submissionsRepository
                .getSubmissionsForStudentAndCourse(activityId, studentId)
                .catch { e ->
                    Log.e("students", "Error loading evaluations: ${e.message}")
                    _uiState.value = UiState.Error("Failed to load evaluations: ${e.message}")
                }
                .collect { evaluations ->
                    Log.e("students", "Success! Loaded evaluations: $evaluations")
                    _uiState.value = UiState.Success(evaluations)
                }
        }
    }

    suspend fun getActivitiesByStudent(courseId: String, userId: String){
        getActivitiesSubmitedByStudent(courseId, userId).onEach { result ->
            when(result){
                is Resource.Error -> {
                    //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
                    Log.e("HOME_VM:", "Error ${result.message?.uiMessage}")
                    _stateStudentEvaluations.value = StudentEvaluationsState(error = result.message)
                }
                is Resource.Loading -> {
                    Timber.tag("HOME_VM").e("is loading")
                    _uiState.value = UiState.Loading
                    _stateStudentEvaluations.value = StudentEvaluationsState(isLoading = true)
                }
                is Resource.Success -> {
                    Timber.tag("HOME_VM").e("success")
                    Log.e("HOME_VM:", "success")
                    _stateStudentEvaluations.value = StudentEvaluationsState(info = result.data)
                    Log.e("HOME_VM:", "${_stateStudentEvaluations.value.info}")
                    _stateStudentEvaluations.value.info?.let {
//                        repositoryBundle.activitiesRepository.insertAllActivities(it)
//                        delay(1000)
                    }
                }
            }

        }.launchIn(viewModelScope)
    }

}
