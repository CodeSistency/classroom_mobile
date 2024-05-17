package proyecto.person.appconsultapopular.common


import proyecto.person.appconsultapopular.common.apiUtils.handlingMessageApi
import com.example.classroom.common.apiUtils.GenericCodeFormat
import io.ktor.client.plugins.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.net.UnknownHostException

fun <T> handlingError(operation: suspend ()-> T): Flow<Resource<T>> = flow{
    try {
        emit(Resource.Loading())
        emit(Resource.Success(operation()))

    } catch (e: RedirectResponseException){
        Timber.tag("HANDLED_ERROR_3xx").e("Error 3xx ${e.message}")
        e.printStackTrace()
        emit(Resource.Error(message = handlingMessageApi(GenericCodeFormat.MOVED_PERMANENTLY)))

    } catch (e: ClientRequestException){
        Timber.tag("HANDLED_ERROR_4xx").e("Error 4xx ${e.message}")
        e.printStackTrace()
        emit(Resource.Error(message = handlingMessageApi(GenericCodeFormat.BAD_REQUEST)))

    } catch (e: ServerResponseException){
        Timber.tag("HANDLED_ERROR_5xx").e("Error 5xx ${e.message}")
        e.printStackTrace()
        emit(Resource.Error(message = handlingMessageApi(GenericCodeFormat.INTERNAL_SERVER_ERROR)))

    }
    catch (e: UnknownHostException){
        Timber.tag("HANDLED_ERROR_HOST").e("Error HOST ${e.message}")
        e.printStackTrace()
        emit(Resource.Error(message = handlingMessageApi(GenericCodeFormat.INTERNAL_SERVER_ERROR)))

    }  catch (e: SerializationException) {
        Timber.tag("HANDLED_ERROR_SERIALIZATION").e("Error SERIALIZATION ${e.message}")
        e.printStackTrace()
        emit(Resource.Error(message = handlingMessageApi(GenericCodeFormat.SERIALIZATION_ERROR)))

    } catch (e: InvalidException){
        Timber.tag("HANDLED_ERROR").e("Unexpected controlled error ${e.message}")
        e.printStackTrace()

        val objectDefault = InvalidExceptionModel(GenericCodeFormat.INVALID_ERROR.codeInternal!!, false)
        val jsonDefault = Json.encodeToString(objectDefault)

        val data = Json.decodeFromString<InvalidExceptionModel>(e.message ?: jsonDefault)

        val format = if (data.isForApi){
            GenericCodeFormat.values().find { it.codeApi == data.code }
        }else{
            GenericCodeFormat.values().find { it.codeInternal == data.code }
        }

        emit(Resource.Error(message = handlingMessageApi(format ?: GenericCodeFormat.INVALID_ERROR, message = data.message)))

    } catch (e: Exception){
        Timber.tag("HANDLED_ERROR").e("Unexpected error ${e.message} ${e.cause} ${e.localizedMessage}")
        e.printStackTrace()
        if (e.message?.contains("timeout") == true){
            emit(Resource.Error(message = handlingMessageApi(GenericCodeFormat.TIMEOUT)))
        }else{
            emit(Resource.Error(message = handlingMessageApi(GenericCodeFormat.GENERIC_ERROR)))
        }
    }
}

