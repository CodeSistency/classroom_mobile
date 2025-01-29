package com.example.classroom.presentation.screens.activity.studentEvaluations
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classroom.common.uiState.UiState
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalActivitySubmission
import com.example.classroom.domain.model.entity.toLocal
import com.example.classroom.domain.use_case.evaluations.getActivitiesSubmittedByStudent.GetActivitiesSubmitedByStudent
import com.example.classroom.presentation.screens.activity.addActivity.states.GetActivitiesState
import com.example.classroom.presentation.screens.activity.studentEvaluations.states.StudentEvaluationsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
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

    private val _statetEvaluationsList = mutableStateOf(StudentEvaluationsState())
    val stateEvaluationsList: State<StudentEvaluationsState> = _statetEvaluationsList

    private val _stateStudentEvaluations = mutableStateOf(StudentEvaluationsState())
    val stateStudentEvaluations: State<StudentEvaluationsState> = _stateStudentEvaluations


    fun observeLocalEvaluationsList(activityId: String, studentId: String) {
        viewModelScope.launch {
            repositoryBundle.submissionsRepository
                .getSubmissionsForStudent(activityId, studentId)
                .distinctUntilChanged() // Only emit new data if it's actually different
                .collect { evaluations ->
                    Log.e("observeLocalEvaluations", "Received local data: $evaluations")

                    if (!evaluations.isNullOrEmpty()) {
                        // Only update if evaluations is non-null and non-empty
                        _stateStudentEvaluations.value = _stateStudentEvaluations.value.copy(
                            info = evaluations,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }
    // Fetch evaluations remotely and sync with the local database


    // Load evaluations from the local database only
    fun observeLocalEvaluations(courseId: String, studentId: String) {
        viewModelScope.launch {
            repositoryBundle.submissionsRepository
                .getSubmissionsForStudent(courseId, studentId)
                .distinctUntilChanged() // Only emit new data if it's actually different
                .collect { evaluations ->
                    Log.e("observeLocalEvaluations", "Received local data: $evaluations")

                    if (!evaluations.isNullOrEmpty()) {
                        // Only update if evaluations is non-null and non-empty
                        _stateStudentEvaluations.value = _stateStudentEvaluations.value.copy(
                            info = evaluations,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }
    // Fetch evaluations remotely and sync with the local database
    fun getActivitiesByStudent(courseId: String, userId: String) {
        viewModelScope.launch {
            getActivitiesSubmitedByStudent(courseId, userId).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        Log.e("getActivitiesByStudent", "Error loading remote data")
                        // Preserve `info` if it already has data to prevent UI from clearing
                        _stateStudentEvaluations.value = _stateStudentEvaluations.value.copy(
                            error = result.message,
                            isLoading = false,
                            info = _stateStudentEvaluations.value.info // Keep existing data
                        )
                    }
                    is Resource.Loading -> {
                        Log.e("getActivitiesByStudent", "Loading remote data")
                        // Only set loading without resetting info
                        _stateStudentEvaluations.value = _stateStudentEvaluations.value.copy(
                            isLoading = true,
                            info = _stateStudentEvaluations.value.info // Keep existing data
                        )
                    }
                    is Resource.Success -> {
                        Log.e("getActivitiesByStudent", "Successfully fetched remote data")
                        result.data?.let { evaluations ->
                            // Insert the data into the local database
                            repositoryBundle.submissionsRepository.addSubmissionsWithoutDuplicates(evaluations)
                            _stateStudentEvaluations.value = _stateStudentEvaluations.value.copy(
                                isLoading = false,
                                error = null,
                                info = evaluations
                                )
                        }
                        // Reset `isLoading` and error without clearing `info`

                    }
                }
            }
        }
    }

    fun cleanData(){
        _stateStudentEvaluations.value = StudentEvaluationsState(false, null, null)
    }
}

