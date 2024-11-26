package com.example.classroom.domain.use_case.quizz

import com.example.classroom.common.handlingError
import com.example.classroom.data.remote.dto.posts.PostRequestDto
import com.example.classroom.data.remote.dto.quizz.AnswerQuizzDto
import com.example.classroom.data.remote.dto.quizz.AnswerQuizzResponseDto
import com.example.classroom.data.remote.dto.quizz.QuizzResponseDto
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalPost
import com.example.classroom.domain.model.entity.toLocal
import kotlinx.coroutines.flow.Flow
import proyecto.person.appconsultapopular.common.Resource
import proyecto.person.appconsultapopular.common.apiUtils.catchError



class AnswerQuizzUseCase(
    private val repositoryBundle: RepositoryBundle
) {
    suspend operator fun invoke(body: AnswerQuizzDto, idQuizz: String) : Flow<Resource<AnswerQuizzResponseDto>> {
        return handlingError<AnswerQuizzResponseDto> {
            val data = repositoryBundle.quizzRepository.answerQuizzRemote(body, idQuizz)
            if (data.statusCode.value == 200 || data.statusCode.value == 201) { // Allow both 200 and 201
                data.responseData!!
            }else{
                throw catchError(data.statusCode.value, null, message = data.messageError?.message)
            }
        }
    }
}