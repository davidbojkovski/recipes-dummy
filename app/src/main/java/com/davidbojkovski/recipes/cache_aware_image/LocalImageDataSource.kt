package com.davidbojkovski.recipes.cache_aware_image

interface LocalImageDataSource {
    suspend fun getCachedImage(url: String): ByteArray?
    suspend fun saveImage(url: String, data: ByteArray)
}