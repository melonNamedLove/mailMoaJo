package com.melon.mailmoajo.Database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.melon.mailmoajo.DAOs.contactDao
import com.melon.mailmoajo.DAOs.mailDao
import com.melon.mailmoajo.DAOs.mailfolderDao
import entities.contacts
import entities.mails
import entities.orderedMailFolders

//@Database(entities = arrayOf(User::class, Student::class), version = 1)
@Database(entities = arrayOf(contacts::class, orderedMailFolders::class, mails::class), version = 3, exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 1, to = 2),
        AutoMigration (from = 2, to = 3)
    ]
)
abstract  class MailMoaJoDatabase :RoomDatabase() {
    abstract  fun contactDao():contactDao
    abstract  fun mailfolderDao():mailfolderDao
    abstract  fun mailDao(): mailDao
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