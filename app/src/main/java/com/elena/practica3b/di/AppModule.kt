package com.elena.practica3b.di

import android.content.Context
import androidx.room.Room
import com.elena.practica3b.data.ImageRepositoryImpl
import com.elena.practica3b.data.local.AppDatabase
import com.elena.practica3b.data.local.dao.HistorialDao
import com.elena.practica3b.repository.ImageRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * AppModule:
 * Módulo de Dagger Hilt que proporciona las dependencias necesarias para Firebase Authentication,
 * Firestore y Storage, asegurando su disponibilidad en toda la aplicación como Singletons.
 *
 * Funcionalidades:
 * - Proporciona instancias únicas de FirebaseAuth, FirebaseFirestore y FirebaseStorage.
 * - Inyecta ImageRepository como dependencia en la aplicación utilizando su implementación (ImageRepositoryImpl).
 *
 * Proveedores:
 * - provideFirebaseAuth(): Retorna una única instancia de FirebaseAuth para la autenticación de usuarios.
 * - provideFirebaseFirestore(): Retorna una única instancia de FirebaseFirestore para la gestión de datos en la nube.
 * - provideFirebaseStorage(): Retorna una única instancia de FirebaseStorage para el almacenamiento de imágenes.
 * - provideImageRepository(): Proporciona un repositorio de imágenes que gestiona la carga y recuperación de imágenes en Firebase.
 *
 * Se utiliza @Singleton para asegurar que cada dependencia sea única en toda la aplicación.
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideImageRepository(auth: FirebaseAuth, storage: FirebaseStorage, db: FirebaseFirestore
    ):ImageRepository = ImageRepositoryImpl(
        auth = auth,
        storage = storage,
        db = db
    )

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "historial_db"
        )
            .fallbackToDestructiveMigration()
            .build()


    @Provides
    fun provideHistorialDao(db: AppDatabase): HistorialDao = db.historialDao()


}
