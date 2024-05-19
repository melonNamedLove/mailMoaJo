package com.melon.mailmoajo.Database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.melon.mailmoajo.DAOs.contactDao
import com.melon.mailmoajo.DAOs.mailfolderDao
import entities.contacts
import entities.orderedMailFolders

//@Database(entities = arrayOf(User::class, Student::class), version = 1)
@Database(entities = arrayOf(contacts::class, orderedMailFolders::class), version = 2, exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ])
abstract  class MailMoaJoDatabase :RoomDatabase() {
    abstract  fun contactDao():contactDao
    abstract  fun mailfolderDao():mailfolderDao
//    abstract  fun mailfolderDao():mailfolderDao

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