package com.simbirsoft.smoke.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun getContext(): Context
    fun getPrefs(): SharedPreferences
    fun getStore(): FirebaseFirestore
}

@Module
class AppModule(private val context: Context) {
    @Provides
    @Singleton
    fun firestore() = Firebase.firestore

    @Provides
    @Singleton
    fun context() = context

    @Provides
    @Singleton
    fun prefs(context: Context): SharedPreferences = context.getSharedPreferences("hookah", Context.MODE_PRIVATE)
}

@Retention(AnnotationRetention.RUNTIME)
@Scope
annotation class LowerScope