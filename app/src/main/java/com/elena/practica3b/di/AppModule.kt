package com.elena.practica3b.di

import android.content.Context
import androidx.room.Room
import com.elena.practica3b.data.local.AppDatabase
import com.elena.practica3b.data.local.dao.HistorialDao
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
 * Firestore, Storage y la base de datos local Room, asegurando su disponibilidad en toda la aplicación como Singletons.
 *
 * Funcionalidades:
 * - Proporciona instancias únicas de FirebaseAuth, FirebaseFirestore y FirebaseStorage.
 * - Proporciona instancia de la base de datos local Room (AppDatabase).
 * - Proporciona el DAO para acceso a datos del historial.
 *
 * Proveedores:
 * - provideFirebaseAuth(): Retorna una única instancia de FirebaseAuth para la autenticación de usuarios.
 * - provideFirebaseFirestore(): Retorna una única instancia de FirebaseFirestore para la gestión de datos en la nube.
 * - provideFirebaseStorage(): Retorna una única instancia de FirebaseStorage para el almacenamiento de imágenes.
 * - provideDatabase(): Construye y provee la instancia de la base de datos local Room con fallback para migraciones destructivas.
 * - provideHistorialDao(): Proporciona el DAO para manipulación de la tabla historial.
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
