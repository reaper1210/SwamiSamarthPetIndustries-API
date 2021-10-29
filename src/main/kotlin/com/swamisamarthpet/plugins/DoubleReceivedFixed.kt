package com.swamisamarthpet.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.util.*
import io.ktor.utils.io.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf

object DoubleReceiveFixed: ApplicationFeature<Application, Unit, Unit> {
    override val key: AttributeKey<Unit> = AttributeKey("DoubleReceiveFixed")
    override fun install(pipeline: Application, configure: Unit.() -> Unit) {
        pipeline.receivePipeline.intercept(ApplicationReceivePipeline.Before) { request ->
            val cache = call.attributes.getCache()
            if (cache is CachedTransformationResult.Success<*> && cache.type == request.typeInfo) {
                proceedWith(ApplicationReceiveRequest(cache.type, cache.value))
                return@intercept
            }
            if (cache == null) {
                call.attributes.putCache(RequestAlreadyConsumedResult)
            }
            val shouldReceiveEntirely = request.value is ByteReadChannel && !cache.isByteArray()
            val byteArray = if (shouldReceiveEntirely) {
                (request.value as ByteReadChannel).toByteArray()
            } else {
                (cache as? CachedTransformationResult.Success<*>)?.value as? ByteArray
            }
            if (shouldReceiveEntirely) {
                @OptIn(ExperimentalStdlibApi::class)
                call.attributes.putSuccessfulCache(typeOf<ByteArray>(), byteArray as ByteArray)
            }
            try {
                val value = byteArray?.let { ByteReadChannel(it) } ?: cache ?: request.value
                proceedWith(ApplicationReceiveRequest(request.typeInfo, value))
            } catch (cause: Throwable) {
                call.attributes.putFailureCache(request.typeInfo, cause)
                throw cause
            }
        }
    }
    @OptIn(ExperimentalStdlibApi::class)
    private val RequestAlreadyConsumedResult =
        CachedTransformationResult.Failure(typeOf<Any>(), RequestAlreadyConsumedException())
    private fun CachedTransformationResult<*>?.isByteArray(): Boolean {
        return (this is CachedTransformationResult.Success<*> && this.value is ByteArray)
    }
    private fun <T : Any> Attributes.putSuccessfulCache(type: KType, value: T) {
        putCache(CachedTransformationResult.Success(type, value))
    }
    private fun Attributes.putFailureCache(type: KType, cause: Throwable) {
        putCache(CachedTransformationResult.Failure(type, cause))
    }
    private fun Attributes.putCache(value: CachedTransformationResult<*>) {
        put(CacheKey, value)
    }
    private fun Attributes.getCache(): CachedTransformationResult<*>? {
        return getOrNull(CacheKey)
    }
    private val CacheKey = AttributeKey<CachedTransformationResult<*>>("LastReceiveRequest")
}