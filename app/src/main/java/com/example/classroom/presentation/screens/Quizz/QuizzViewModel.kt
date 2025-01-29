package com.example.classroom.presentation.screens.Quizz

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classroom.data.local.db.daos.QuizWithQuestions
import com.example.classroom.data.remote.dto.quizz.AnswerDto
import com.example.classroom.data.remote.dto.quizz.AnswerQuizzDataDto
import com.example.classroom.data.remote.dto.quizz.AnswerQuizzDto
import com.example.classroom.data.remote.dto.quizz.CreateQuizzDto
import com.example.classroom.data.remote.dto.quizz.CreatedQuizDataDto
import com.example.classroom.data.remote.dto.quizz.Question
import com.example.classroom.data.remote.dto.quizz.QuestionDto
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.AnswerEntity
import com.example.classroom.domain.model.entity.LocalActivitySubmission
import com.example.classroom.domain.model.entity.LocalPost
import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.domain.model.entity.OptionEntity
import com.example.classroom.domain.model.entity.QuestionEntity
import com.example.classroom.domain.model.entity.QuizEntity
import com.example.classroom.domain.model.entity.Status
import com.example.classroom.domain.model.entity.toLocalActivities
import com.example.classroom.domain.use_case.quizz.AnswerQuizzUseCase
import com.example.classroom.domain.use_case.quizz.CreateQuizzUseCase
import com.example.classroom.presentation.screens.Quizz.states.AnswerQuizzState
import com.example.classroom.presentation.screens.Quizz.states.CreateQuizzState
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

    val quizState = MutableStateFlow<QuizWithQuestions?>(null)

    // Store selected options for each question
    val selectedOptions = MutableStateFlow<Map<Int, Int>>(emptyMap())

    val questions = mutableStateOf(mutableListOf<QuestionDto>())


    fun addOption(questionIndex: Int) {
        val updatedQuestions = questions.value.toMutableList()
        val question = updatedQuestions[questionIndex]
        if (question.options.size < 4) {
            // Create a new list to force recomposition
            val updatedOptions = question.options + ""
            updatedQuestions[questionIndex] = question.copy(options = updatedOptions.toMutableList())
            questions.value = updatedQuestions
        }
    }

    fun deleteOption(questionIndex: Int, optionIndex: Int) {
        val updatedQuestions = questions.value.toMutableList()
        val question = updatedQuestions[questionIndex]
        if (question.options.size > 2) {
            // Create a new list to force recomposition
            val updatedOptions = question.options.toMutableList().also { it.removeAt(optionIndex) }
            updatedQuestions[questionIndex] = question.copy(options = updatedOptions)
            questions.value = updatedQuestions
        }
    }

    fun updateOptionText(questionIndex: Int, optionIndex: Int, newText: String) {
        val updatedQuestions = questions.value.toMutableList()
        val updatedOptions = updatedQuestions[questionIndex].options.toMutableList()
        updatedOptions[optionIndex] = newText // Change option text
        updatedQuestions[questionIndex] = updatedQuestions[questionIndex].copy(options = updatedOptions)
        questions.value = updatedQuestions // Trigger recomposition
    }
    fun addQuestion() {
        questions.value =
            (questions.value + QuestionDto(text = "", options = mutableListOf("", ""), answer = -1)).toMutableList()
    }

    fun deleteQuestion(index: Int) {
        if (questions.value.size > 1) {
            val updatedQuestions = questions.value.toMutableList()
            updatedQuestions.removeAt(index)
            questions.value = updatedQuestions
        }
    }

//    fun addOption(questionIndex: Int) {
//        val updatedQuestions = questions.value.toMutableList()
//        val question = updatedQuestions[questionIndex]
//        if (question.options.size < 4) {
//            question.options.add("")
//            updatedQuestions[questionIndex] = question
//            questions.value = updatedQuestions
//        }
//    }
//
//    fun deleteOption(questionIndex: Int, optionIndex: Int) {
//        val updatedQuestions = questions.value.toMutableList()
//        val question = updatedQuestions[questionIndex]
//        if (question.options.size > 2) {
//            question.options.removeAt(optionIndex)
//            updatedQuestions[questionIndex] = question
//            questions.value = updatedQuestions
//        }
//    }

    fun updateQuestionText(questionIndex: Int, newText: String) {
        val updatedQuestions = questions.value.toMutableList()
        updatedQuestions[questionIndex] = updatedQuestions[questionIndex].copy(text = newText)
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
    val grade = mutableStateOf("0.0")
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
            Log.e("quizz id", quizId.toString())
            quizState.value = quizzRepository.loadQuiz(quizId)

            Log.e("quizz state", quizState.value.toString())

        }
    }

    fun submitAnswers(answers: List<AnswerEntity>) {
        viewModelScope.launch {
            quizzRepository.submitAnswers(answers)
        }
    }







    private suspend fun saveQuizzToLocalDatabase(createdQuizData: CreatedQuizDataDto) {

        Log.e("quizz response data", createdQuizData.toString())

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


    // Get selected option for a question
    fun getSelectedOption(questionId: Int): Int? {
        return selectedOptions.value[questionId]
    }

    // Select an option for a question
    fun selectOption(questionId: Int, optionId: Int) {
        val updatedMap = selectedOptions.value.toMutableMap()
        updatedMap[questionId] = optionId
        selectedOptions.value = updatedMap
    }

    fun answerQuizzRemote(quizId: String) {
        viewModelScope.launch {
            // Validate quizState exists
            val currentQuiz = quizState.value
            if (currentQuiz == null) {
                _stateAnswerQuizz.value = AnswerQuizzState(error = GenericCodeModel("Quiz not loaded.", ""))
                return@launch
            }

            // Map selected answers
            val selectedAnswers = currentQuiz.questions.map { questionWithOptions ->
                val selectedOptionId = selectedOptions.value[questionWithOptions.question.id]
                if (selectedOptionId == null) {
                    // Missing answer for this question
                    _stateAnswerQuizz.value = AnswerQuizzState(
                        error = GenericCodeModel("Por favor contesta todas las preguntas. Respuesta faltante: ${questionWithOptions.question.text}", "")
                    )
                    return@launch
                }

                // Ensure the selected option matches the backend's `answer` index
                val correctOptionIndex = questionWithOptions.options.indexOfFirst { it.id == selectedOptionId }
                if (correctOptionIndex == -1) {
                    _stateAnswerQuizz.value = AnswerQuizzState(
                        error = GenericCodeModel("Opción seleccionada inválida para la pregunta: ${questionWithOptions.question.text}", "")
                    )
                    return@launch
                }

                AnswerDto(
                    questionId = questionWithOptions.question.id,
                    optionId = selectedOptionId // Ensure the selectedOptionId matches the backend's expected value
                )
            }

            // Ensure there are answers to submit
            if (selectedAnswers.isEmpty()) {
                _stateAnswerQuizz.value = AnswerQuizzState(error = GenericCodeModel("No answers selected.", ""))
                return@launch
            }

            // Build DTO
            val dto = AnswerQuizzDto(
                userId = _userInfo.value?.idApi?.toInt() ?: return@launch,
                answers = selectedAnswers
            )

            // Make remote call
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
                        response?.data?.let { answer ->
                            saveAnswerDataToLocalDatabase(answer)
                            _userInfo.value?.let {
                                repositoryBundle.submissionsRepository.addOrUpdateSubmission(
                                    LocalActivitySubmission(
                                        courseId = answer.quizz.activity.courseId.toString(),
                                        id = 0,
                                        documentUrl = null,
                                        activityId = answer.quizz.activityId.toString(),
                                        grade = answer.grade.toDouble(),
                                        studentId = it.idApi,
                                        comment = "quizz",
                                        submissionDate = answer.submission.createDate,

                                        )
                                )
                            }


                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }


//    fun answerQuizzRemote(quizId: String) {
//        viewModelScope.launch {
//            // Validate quizState exists
//            val currentQuiz = quizState.value
//            if (currentQuiz == null) {
//                _stateAnswerQuizz.value = AnswerQuizzState(error = GenericCodeModel("Quiz not loaded.", ""))
//                return@launch
//            }
//
//            // Map selected answers
//            val selectedAnswers = currentQuiz.questions.map { questionWithOptions ->
//                val selectedOptionId = selectedOptions.value[questionWithOptions.question.id]
//                if (selectedOptionId == null) {
//                    // Missing answer for this question
//                    _stateAnswerQuizz.value = AnswerQuizzState(
//                        error = GenericCodeModel("Por favor contesta todas las preguntas. Respuesta faltante: ${questionWithOptions.question.text}", "")
//                    )
//                    return@launch
//                }
//                AnswerDto(
//                    questionId = questionWithOptions.question.id,
//                    optionId = selectedOptionId
//                )
//            }
//
//            // Ensure there are answers to submit
//            if (selectedAnswers.isEmpty()) {
//                _stateAnswerQuizz.value = AnswerQuizzState(error = GenericCodeModel("No answers selected.", ""))
//                return@launch
//            }
//
//            // Build DTO
//            val dto = AnswerQuizzDto(
//                userId = _userInfo.value?.idApi?.toInt() ?: return@launch,
//                answers = selectedAnswers
//            )
//
//            // Make remote call
//            answerQuizzUseCase(dto, idQuizz = quizId).onEach { result ->
//                when (result) {
//                    is Resource.Error -> {
//                        _stateAnswerQuizz.value = AnswerQuizzState(error = result.message)
//                    }
//                    is Resource.Loading -> {
//                        _stateAnswerQuizz.value = AnswerQuizzState(isLoading = true)
//                    }
//                    is Resource.Success -> {
//                        val response = result.data
//                        _stateAnswerQuizz.value = AnswerQuizzState(info = response)
//
//                        // Optionally save the response locally
//                        response?.data?.let { saveAnswerDataToLocalDatabase(it) }
//                    }
//                }
//            }.launchIn(viewModelScope)
//        }
//    }

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
            userInfo.value?.let { it ->
                val dto = CreateQuizzDto(
                    title = title.value,
                    description = description.value,
                    grade = grade.value.toString(), // Default grade to 0 if not provided
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
                            result.data?.let {
                                repositoryBundle.activitiesRepository.insertActivity(it.toLocalActivities(idCourse, startDate.value, endDate.value))
    
                                it.data.activity.post?.let {
                                    repositoryBundle.postsRepositoryImpl.insertPost(
                                        LocalPost(
                                            createdAt = it.createdAt,
                                            id = 0,
                                            courseId = it.courseId.toString(),
                                            content = it.content,
                                            authorId = it.authorId.toString(),
                                            title = it.title,
                                            mediaUrl = it.file,
                                            idApi = it.id.toString(),
                                        )
                                    )
                                }
                                
                                //POR HACER

//                                repositoryBundle.postsRepositoryImpl.insertPost(
//                                    LocalPost(
//
//                                    )
//                                )
                            }
                            response?.data?.let { createdQuizData ->
                                saveQuizzToLocalDatabase(createdQuizData)
                            }

                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }



    fun cleanData() {
        _stateAnswerQuizz.value = AnswerQuizzState(isLoading = false, null, null
        )
        _stateCreateQuizz.value = CreateQuizzState(false, null, null)

        selectedOptions.value = emptyMap()

        quizState.value = null

        questions.value = mutableListOf()

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
