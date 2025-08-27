package com.davidbojkovski.recipes.cache

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MemoryCache<K, V> @Inject constructor(
    private val maxSize: Int = 50,
    private val dispatcher: CoroutineContext = Dispatchers.IO
) : Cache<K, V> {
    private val cache = LinkedHashMap<K, V>(maxSize, 0.75f, true)

    override suspend fun get(key: K): V? {
        return withContext(dispatcher) {
            cache[key]
        }
    }

    override suspend fun put(key: K, value: V) {
        withContext(dispatcher) {
            if (cache.size >= maxSize) {
                val oldestKey = cache.keys.first()
                cache.remove(oldestKey)
            }
            cache[key] = value
        }
    }

    override suspend fun clear() {
        withContext(dispatcher) {
            cache.clear()
        }
    }
}