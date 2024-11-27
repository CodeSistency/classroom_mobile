package com.example.classroom.presentation.screens.Quizz

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classroom.data.local.db.QuizWithQuestions
import com.example.classroom.data.remote.dto.quizz.AnswerDto
import com.example.classroom.data.remote.dto.quizz.AnswerQuizzDataDto
import com.example.classroom.data.remote.dto.quizz.AnswerQuizzDto
import com.example.classroom.data.remote.dto.quizz.CreateQuizzDto
import com.example.classroom.data.remote.dto.quizz.CreatedQuizDataDto
import com.example.classroom.data.remote.dto.quizz.Question
import com.example.classroom.data.remote.dto.quizz.QuestionDto
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.AnswerEntity
import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.domain.model.entity.OptionEntity
import com.example.classroom.domain.model.entity.QuestionEntity
import com.example.classroom.domain.model.entity.QuizEntity
import com.example.classroom.domain.model.entity.Status
import com.example.classroom.domain.use_case.quizz.AnswerQuizzUseCase
import com.example.classroom.domain.use_case.quizz.CreateQuizzUseCase
import com.example.classroom.presentation.screens.Quizz.states.AnswerQuizzState
import com.example.classroom.presentation.screens.Quizz.states.CreateQuizzState
import com.example.classroom.presentation.screens.course.states.GetCourseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.common.Resource
import proyecto.person.appconsultapopular.common.apiUtils.GenericCodeModel


class QuizzViewModel(
    private val repositoryBundle: RepositoryBundle,
    private val answerQuizzUseCase: AnswerQuizzUseCase,
    private val createQuizzUseCase: CreateQuizzUseCase
) : ViewModel() {

    private val _userInfo = MutableStateFlow<LocalUser?>(null)
    val userInfo: StateFlow<LocalUser?> = _userInfo

    init {
        viewModelScope.launch {
            repositoryBundle.loginRepository.getUserInfoWithFlow()
                .firstOrNull()
                ?.firstOrNull()
                ?.let { _userInfo.value = it }

//            observeUserInput()
        }
    }

    private val quizzRepository = repositoryBundle.quizzRepository

    private val _stateCreateQuizz = MutableStateFlow(CreateQuizzState())
    val stateCreateQuizz: StateFlow<CreateQuizzState> = _stateCreateQuizz

    private val _stateAnswerQuizz = MutableStateFlow(AnswerQuizzState())
    val stateAnswerQuizz: StateFlow<AnswerQuizzState> = _stateAnswerQuizz

    val quizState = mutableStateOf<QuizWithQuestions?>(null)

    val questions = mutableStateOf(mutableListOf<QuestionDto>())


    fun addQuestion() {
        questions.value = (questions.value + QuestionDto(
            text = "",
            options = mutableListOf("", ""),
            answer = -1
        )).toMutableList()
    }

    fun addOption(questionIndex: Int) {
        val updatedQuestions = questions.value.toMutableList()
        if (updatedQuestions[questionIndex].options.size < 4) {
            updatedQuestions[questionIndex].options.add("")
        }
        questions.value = updatedQuestions
    }

    fun updateQuestionText(questionIndex: Int, newText: String) {
        val updatedQuestions = questions.value.toMutableList()
        updatedQuestions[questionIndex] = updatedQuestions[questionIndex].copy(text = newText)
        questions.value = updatedQuestions
    }

    fun updateOptionText(questionIndex: Int, optionIndex: Int, newText: String) {
        val updatedQuestions = questions.value.toMutableList()
        val updatedOptions = updatedQuestions[questionIndex].options.toMutableList()
        updatedOptions[optionIndex] = newText
        updatedQuestions[questionIndex] = updatedQuestions[questionIndex].copy(options = updatedOptions)
        questions.value = updatedQuestions
    }

    fun setCorrectAnswer(questionIndex: Int, answerIndex: Int) {
        val updatedQuestions = questions.value.toMutableList()
        updatedQuestions[questionIndex] = updatedQuestions[questionIndex].copy(answer = answerIndex)
        questions.value = updatedQuestions
    }

    // State for form fields
    val title = mutableStateOf("")
    val description = mutableStateOf("")
    val grade = mutableStateOf("")
    val startDate = mutableStateOf("")
    val endDate = mutableStateOf("")
    val email = mutableStateOf("")
    val digital = mutableStateOf(true) // Default to true
    var status = mutableStateOf(Status.OPEN)
    val courseId = mutableStateOf("")

    // Error states for validation
    val titleError = mutableStateOf<String?>(null)
    val descriptionError = mutableStateOf<String?>(null)
    val gradeError = mutableStateOf<String?>(null)
    val startDateError = mutableStateOf<String?>(null)
    val endDateError = mutableStateOf<String?>(null)
    val emailError = mutableStateOf<String?>(null)
    val statusError = mutableStateOf<String?>(null)
    val courseIdError = mutableStateOf<String?>(null)

//    val questions = mutableStateOf(mutableListOf<Question>())

    // Validation logic
    fun validateTitle() {
        titleError.value = if (title.value.isBlank()) "Title is required" else null
    }

    fun validateDescription() {
        descriptionError.value = if (description.value.isBlank()) "Description is required" else null
    }

    fun validateGrade() {
        gradeError.value = if (grade.value.toIntOrNull() == null) "Grade must be a number" else null
    }

    fun validateStartDate() {
        startDateError.value = if (startDate.value.isBlank()) "Start date is required" else null
    }

    fun validateEndDate() {
        endDateError.value = if (endDate.value.isBlank()) "End date is required" else null
    }

    fun validateEmail() {
        emailError.value = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) "Invalid email" else null
    }

    fun validateStatus() {
        statusError.value = if (status.value == Status.NO_SELECTED) "Debe seleccionar un estado" else null
    }

    fun validateCourseId() {
        courseIdError.value = if (courseId.value.toIntOrNull() == null) "Course ID must be a number" else null
    }

    fun createQuiz(activityId: Int, title: String, questions: List<QuestionDto>) {
        viewModelScope.launch {
            quizzRepository.createQuiz(activityId, title, questions)
        }
    }

    fun loadQuiz(quizId: Int) {
        viewModelScope.launch {
            quizState.value = quizzRepository.loadQuiz(quizId)
        }
    }

    fun submitAnswers(answers: List<AnswerEntity>) {
        viewModelScope.launch {
            quizzRepository.submitAnswers(answers)
        }
    }

    private val selectedOptions = mutableStateOf(mutableMapOf<Int, Int>())

    fun selectOption(questionId: Int, optionId: Int) {
        selectedOptions.value[questionId] = optionId
    }

    fun getSelectedOption(questionId: Int): Int? {
        return selectedOptions.value[questionId]
    }




    private suspend fun saveQuizzToLocalDatabase(createdQuizData: CreatedQuizDataDto) {
        val quizEntity = QuizEntity(
            id = createdQuizData.id,
            activityId = createdQuizData.activityId,
            title = createdQuizData.activity.title
        )
        quizzRepository.insertQuiz(quizEntity)

        // Save questions
        createdQuizData.question.forEach { questionDto ->
            val questionEntity = QuestionEntity(
                id = questionDto.id,
                quizId = createdQuizData.id,
                text = questionDto.text,
                correctAnswer = questionDto.answer
            )
            quizzRepository.insertQuestion(questionEntity)

            // Save options
            questionDto.options.forEach { optionDto ->
                val optionEntity = OptionEntity(
                    id = optionDto.id,
                    questionId = questionDto.id,
                    text = optionDto.text
                )
                quizzRepository.insertOption(optionEntity)
            }
        }
    }

    fun answerQuizzRemote(quizId: String) {
        viewModelScope.launch {
            val selectedAnswers = quizState.value?.questions?.mapNotNull { questionWithOptions ->
                val selectedOptionId = selectedOptions.value[questionWithOptions.question.id]
                selectedOptionId?.let {
                    AnswerDto(
                        questionId = questionWithOptions.question.id,
                        optionId = it
                    )
                }
            } ?: emptyList()

            // Ensure there are answers to submit
            if (selectedAnswers.isEmpty()) {
                _stateAnswerQuizz.value = AnswerQuizzState(error = GenericCodeModel("Please answer all questions.", ""))
                return@launch
            }

            val dto = AnswerQuizzDto(
                userId = _userInfo.value?.id ?: return@launch,
                answers = selectedAnswers
            )

            answerQuizzUseCase(dto, idQuizz = quizId).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _stateAnswerQuizz.value = AnswerQuizzState(error = result.message)
                    }
                    is Resource.Loading -> {
                        _stateAnswerQuizz.value = AnswerQuizzState(isLoading = true)
                    }
                    is Resource.Success -> {
                        val response = result.data
                        _stateAnswerQuizz.value = AnswerQuizzState(info = response)

                        // Optionally save the response locally
                        response?.data?.let { saveAnswerDataToLocalDatabase(it) }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private suspend fun saveAnswerDataToLocalDatabase(data: AnswerQuizzDataDto) {
        data.submission.answers.forEach { answer ->
            val answerEntity = AnswerEntity(
                questionId = answer.optionId,
                selectedOptionId = answer.optionId
            )
            quizzRepository.insertAnswer(answerEntity)
        }
    }

    fun createQuizRemote(idCourse: String, questions: List<QuestionDto>) {
        viewModelScope.launch {
            // Validation
            validateTitle()
            validateDescription()
            validateStartDate()
            validateEndDate()

            // Check if there are form-level errors
            if (listOf(
                    titleError.value, descriptionError.value,
                    startDateError.value, endDateError.value
                ).any { it != null }
            ) return@launch

            // Validate questions
            val invalidQuestions = questions.filter { question ->
                question.text.isBlank() || question.options.size < 2 || question.options.any { it.isBlank() } || question.answer !in question.options.indices
            }

            if (invalidQuestions.isNotEmpty()) {
                _stateCreateQuizz.value = CreateQuizzState(error = GenericCodeModel("Asegúrate de que todas las preguntas tengan al menos dos opciones válidas y una opción correcta.", ""))
                return@launch
            }

            // Validation passed, create DTO and submit
            userInfo.value?.let {
                val dto = CreateQuizzDto(
                    title = title.value,
                    description = description.value,
                    grade = grade.value.toIntOrNull() ?: 0, // Default grade to 0 if not provided
                    startDate = startDate.value,
                    endDate = endDate.value,
                    email = it.email,
                    digital = digital.value,
                    statusId = status.value.id,
                    courseId = idCourse.toInt(),
                    questions = questions.map {
                        Question(
                            text = it.text,
                            answer = it.answer,
                            options = it.options,
                        )
                    }
                )

                // Call the repository method
                createQuizzUseCase(dto).onEach { result ->
                    when (result) {
                        is Resource.Error -> _stateCreateQuizz.value = CreateQuizzState(error = result.message)
                        is Resource.Loading -> _stateCreateQuizz.value = CreateQuizzState(isLoading = true)
                        is Resource.Success -> {
                            val response = result.data

                            _stateCreateQuizz.value = CreateQuizzState(info = response)

                            // Save the quiz data into the local database
                            response?.data?.let { createdQuizData ->
                                saveQuizzToLocalDatabase(createdQuizData)
                            }
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }
//    fun createQuizRemote(idCourse: String) {
//        viewModelScope.launch {
//            // Validation
//            validateTitle()
//            validateDescription()
////            validateGrade()
//            validateStartDate()
//            validateEndDate()
////            validateEmail()
////            validateStatus()
////            validateCourseId()
//
//            // Check if there are errors
//            if (listOf(
//                    titleError.value, descriptionError.value, gradeError.value,
//                    startDateError.value, endDateError.value, emailError.value,
//                    statusError.value, courseIdError.value
//                ).any { it != null }
//            ) return@launch
//
//            userInfo.value?.let {
//                val dto = CreateQuizzDto(
//                    title = title.value,
//                    description = description.value,
//                    grade = grade.value.toInt(),
//                    startDate = startDate.value,
//                    endDate = endDate.value,
//                    email = it.email,
//                    digital = digital.value,
//                    statusId = status.value.id,
//                    courseId = idCourse.toInt(),
//                    questions = questions.value
//                )
//
//                // Call the repository method
//
//                createQuizzUseCase(dto).onEach { result ->
//                    when (result) {
//                        is Resource.Error -> _stateCreateQuizz.value = CreateQuizzState(error = result.message)
//                        is Resource.Loading -> _stateCreateQuizz.value = CreateQuizzState(isLoading = true)
//                        is Resource.Success -> {
//                            val response = result.data
//
//                            _stateCreateQuizz.value = CreateQuizzState(info = response)
//
//                            // Save the quiz data into the local database
//                            response?.data?.let { createdQuizData ->
//                                saveQuizzToLocalDatabase(createdQuizData)
//                            }
//                        }
//                    }
//                }.launchIn(viewModelScope)
//            }
//
//        }
//    }
}
