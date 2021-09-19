package swaix.dev.mensaeventi.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import swaix.dev.mensaeventi.MensaApp
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.api.ApiHelper
import swaix.dev.mensaeventi.api.ApiHelperImpl
import swaix.dev.mensaeventi.api.ApiService
import swaix.dev.mensaeventi.repository.EventRepository
import swaix.dev.mensaeventi.utils.TAG
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun providesBaseUrl(context: MensaApp) = context.getString(R.string.BASE_URL)

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
        Log.d(TAG, "provideRetrofit: $baseUrl")
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun providesApiHelper(apiService: ApiService): ApiHelper = ApiHelperImpl(apiService)

    @Singleton
    @Provides
    fun providesEventRepository(helper: ApiHelper): EventRepository = EventRepository(helper)


}