package com.example.classroom.domain.use_case.quizz

import com.example.classroom.common.handlingError
import com.example.classroom.data.remote.dto.quizz.AnswerQuizzDto
import com.example.classroom.data.remote.dto.quizz.CreateQuizzDto
import com.example.classroom.data.remote.dto.quizz.CreateQuizzResponseDto
import com.example.classroom.data.remote.dto.quizz.QuizzResponseDto
import com.example.classroom.data.repository.RepositoryBundle
import kotlinx.coroutines.flow.Flow
import proyecto.person.appconsultapopular.common.Resource
import proyecto.person.appconsultapopular.common.apiUtils.catchError



class CreateQuizzUseCase(
    private val repositoryBundle: RepositoryBundle
) {
    suspend operator fun invoke(body: CreateQuizzDto) : Flow<Resource<CreateQuizzResponseDto>> {
        return handlingError<CreateQuizzResponseDto> {
            val data = repositoryBundle.quizzRepository.createQuizzRemote(body)
            if (data.statusCode.value == 200 || data.statusCode.value == 201) { // Allow both 200 and 201
                data.responseData!!
            }else{
                throw catchError(data.statusCode.value, null, message = data.messageError?.message)
            }
        }
    }
}