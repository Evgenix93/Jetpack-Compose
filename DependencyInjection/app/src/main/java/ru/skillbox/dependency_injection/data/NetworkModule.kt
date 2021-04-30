package ru.skillbox.dependency_injection.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import ru.skillbox.dependency_injection.utils.ApplicationVersionInterceptor
import ru.skillbox.dependency_injection.utils.LoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @LoggingInterceptor
    fun provideLoggingInterceptor() : Interceptor{
        return HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @ApplicationVersionInterceptor
    fun provideAppVersionInterceptor(): Interceptor{
        return AppVersionInterceptor()
    }

    @Provides
      fun provideOkHttpClient(
            @LoggingInterceptor loggingInterceptor: Interceptor,
            @ApplicationVersionInterceptor appVersionInterceptor: Interceptor
      ): OkHttpClient{
      return  OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
              .addNetworkInterceptor(appVersionInterceptor)
                .followRedirects(true)
                .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
       return Retrofit.Builder()
                .baseUrl("https://google.com")
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): Api {
        return retrofit.create()
    }

}