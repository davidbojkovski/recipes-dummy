package com.davidbojkovski.recipes.cache_aware_image

interface ImageRepository {
    suspend fun getImage(url: String): ByteArray?
}