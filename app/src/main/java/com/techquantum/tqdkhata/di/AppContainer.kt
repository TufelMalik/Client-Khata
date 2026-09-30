package com.techquantum.tqdkhata.di

import android.content.Context

class AppContainer(context: Context) {
    val appModule: AppModule = AppModuleImpl(context)
    val networkModule: NetworkModule = NetworkModuleImpl()
    val repositoryModule: RepositoryModule = RepositoryModuleImpl(context, appModule)
}
