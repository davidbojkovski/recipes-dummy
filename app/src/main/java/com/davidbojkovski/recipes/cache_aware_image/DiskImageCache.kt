package com.davidbojkovski.recipes.cache_aware_image

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DiskImageCache @Inject constructor(
    private val cacheDir: File,
    private val dispatcher: CoroutineContext = Dispatchers.IO
) : LocalImageDataSource {

    private suspend fun getFileForUrl(url: String): File {
        return withContext(dispatcher) {
            val fileName = url.hashCode().toString()
            File(cacheDir, fileName)
        }
    }

    override suspend fun getCachedImage(url: String): ByteArray? {
        return withContext(dispatcher) {
            val file = getFileForUrl(url)
            try {
                if (file.exists()) {
                    file.readBytes()
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.d("DiskImageCache", "Exception: ${e.localizedMessage}")
                null
            }
        }
    }

    override suspend fun saveImage(url: String, data: ByteArray) {
        withContext(dispatcher) {
            val file = getFileForUrl(url)
            file.writeBytes(data)
        }
    }
}