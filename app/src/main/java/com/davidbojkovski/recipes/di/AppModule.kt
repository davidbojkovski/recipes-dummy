package com.davidbojkovski.recipes.di

import android.content.Context
import com.davidbojkovski.recipes.models.RecipeModel
import com.davidbojkovski.recipes.cache.MemoryCache
import com.davidbojkovski.recipes.cache_aware_image.DiskImageCache
import com.davidbojkovski.recipes.cache_aware_image.ImageRepository
import com.davidbojkovski.recipes.cache_aware_image.ImageRepositoryImpl
import com.davidbojkovski.recipes.repository.RecipeRepository
import com.davidbojkovski.recipes.repository.RecipeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(Logging)
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }

    @Provides
    @Singleton
    @Named("recipesMemoryCache")
    fun provideRecipesMemoryCache(): MemoryCache<String, List<RecipeModel?>?> {
        return MemoryCache<String, List<RecipeModel?>?>()
    }

    @Provides
    @Singleton
    @Named("recipeMemoryCache")
    fun provideRecipeMemoryCache(): MemoryCache<String, RecipeModel?> {
        return MemoryCache<String, RecipeModel?>()
    }

    @Provides
    @Singleton
    @Named("tagsMemoryCache")
    fun provideTagsMemoryCache(): MemoryCache<String, List<String?>?> {
        return MemoryCache<String, List<String?>?>()
    }

    @Provides
    @Singleton
    @Named("imageMemoryCache")
    fun provideImageMemoryCache(): MemoryCache<String, ByteArray> {
        return MemoryCache<String, ByteArray>()
    }

    @Provides
    @Singleton
    fun provideRecipeRepository(
        httpClient: HttpClient,
        @Named("recipesMemoryCache") recipesMemoryCache: MemoryCache<String, List<RecipeModel?>?>,
        @Named("recipeMemoryCache") recipeMemoryCache: MemoryCache<String, RecipeModel?>,
        @Named("tagsMemoryCache") tagsMemoryCache: MemoryCache<String, List<String?>?>,
    ): RecipeRepository {
        return RecipeRepositoryImpl(
            httpClient,
            recipesMemoryCache,
            recipeMemoryCache,
            tagsMemoryCache
        )
    }

    @Provides
    @Singleton
    @Named("imageCacheDir")
    fun provideImageCacheDirectory(@ApplicationContext context: Context): File {
        val cacheDir = File(context.cacheDir, "image_cache")
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
        return cacheDir
    }

    @Provides
    @Singleton
    fun provideDiskImageCache(@Named("imageCacheDir") cacheDir: File): DiskImageCache {
        return DiskImageCache(cacheDir)
    }

    @Provides
    @Singleton
    fun provideImageRepository(
        @Named("imageMemoryCache") imageMemoryCache: MemoryCache<String, ByteArray>,
        diskImageCache: DiskImageCache,
        httpClient: HttpClient
    ): ImageRepository {
        return ImageRepositoryImpl(imageMemoryCache, diskImageCache, httpClient)
    }
}