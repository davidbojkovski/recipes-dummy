package com.davidbojkovski.recipes.cache_aware_image

import android.util.Log
import com.davidbojkovski.recipes.cache.MemoryCache
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val memoryCache: MemoryCache<String, ByteArray>,
    private val diskCache: LocalImageDataSource,
    private val httpClient: HttpClient,
) : ImageRepository {

    companion object {
        val TAG: String = ImageRepositoryImpl::class.java.simpleName
    }

    override suspend fun getImage(url: String): ByteArray? {
        memoryCache.get(url)?.let {
            Log.d(TAG, "returning image from memory")
            return it
        }

        diskCache.getCachedImage(url)?.let {
            memoryCache.put(url, it)
            Log.d(TAG, "returning image from disk")
            return it
        }

        try {
            val imageByteArray = httpClient.get(url).readRawBytes()
            diskCache.saveImage(url, imageByteArray)
            memoryCache.put(url, imageByteArray)
            return imageByteArray
        } catch (e: Exception) {
            Log.d(TAG, "Exception: ${e.localizedMessage}")
            return null
        }
    }
}