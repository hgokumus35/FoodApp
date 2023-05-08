package com.gokalp.foodapp.di

import com.gokalp.foodapp.core.network.service.FoodAppService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class HomeDI {
    companion object {
        val module = module {
  //          viewModel { HomeViewModel(get()) }

            single<FoodAppService> {
                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                val client : OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.spoonacular.com")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofit.create(FoodAppService::class.java)
            }
/*
            single<HomeRepository> { HomeRepositoryImpl(get(), get()) }

            single {HomeUseCase(get())}

            single {
                Room.databaseBuilder(
                    androidContext(),
                    HomeDatabase::class.java, "HomeDatabase"
                ).allowMainThreadQueries().build()
            }

 */
        }
    }
}