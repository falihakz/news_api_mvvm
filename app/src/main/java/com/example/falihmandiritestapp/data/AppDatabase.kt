package com.example.falihmandiritestapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.falihmandiritestapp.BuildConfig
import com.example.falihmandiritestapp.data.dao.ArticleDao
import com.example.falihmandiritestapp.data.entity.Article
import java.io.File

@Database(entities = [Article::class], version = 1)
@TypeConverters(EntityFieldConverters::class)
abstract class AppDatabase :RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private const val DB_NAME = BuildConfig.APPLICATION_ID + "_database"
        private var DB_PATH = ""

        internal fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {

                        if(BuildConfig.DEBUG){
                            // Debug app should use external file
                            var file: File? = null
                            try {
                                file = context.getExternalFilesDir(null)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                            if (file != null) {
                                DB_PATH = file.absolutePath + "/databases/"
                            }
                        }

                        INSTANCE = Room.databaseBuilder<AppDatabase>(
                            context.applicationContext,
                            AppDatabase::class.java, DB_PATH + DB_NAME
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}