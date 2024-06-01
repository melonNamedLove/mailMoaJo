package com.melon.mailmoajo.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import entities.mails

@Dao
interface GmailDao {

    @Query("SELECT * FROM mails")
    fun getAll(): List<mails>

    @Insert
    fun insert(mails: mails)

    @Update
    fun update(mails: mails)

    @Delete
    fun delete(mails: mails)

    @Insert(entity = mails::class)
    fun init(mails: mails)

    @Query("DELETE FROM mails")
    fun resetmails()
}