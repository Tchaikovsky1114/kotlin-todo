package com.example.todolist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [TodoEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract  fun getTodoDao(): TodoDao

    // 싱글턴 패턴을 구현하기 위한 companion Object.
    // 클래스의 모든 인스턴스가 공유하는 객체를 만들고 싶을 때 사용한다.
    companion object {
        private const val databaseName = "db_todo"
        private var appDatabase: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if(appDatabase == null) {
                appDatabase = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                databaseName)
                .fallbackToDestructiveMigration()
                .build()
            }
            return appDatabase
        }
    }
}