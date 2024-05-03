package com.melon.mailmoajo.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.melon.mailmoajo.DAOs.contactDao
import entities.contacts

//@Database(entities = arrayOf(User::class, Student::class), version = 1)
@Database(entities = [contacts::class], version = 1)
abstract  class ContactDatabase :RoomDatabase() {
    abstract  fun contactDao():contactDao

//    companion object {
//        private var instance: ContactDatabase? = null
//
////        @Synchronized
////        fun getInstance(context: Context): ContactDatabase? {
////            if (instance == null) {
////                synchronized(ContactDatabase::class){
////                    instance = Room.databaseBuilder(
////                        context.applicationContext,
////                        ContactDatabase::class.java,
////                        "contact-database"
////                    ).build()
////                }
////            }
////            return instance
////        }
//    }
}