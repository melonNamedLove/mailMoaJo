package com.melon.mailmoajo.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import entities.OutlookMails

@Dao
interface OutlookDao {

    @Query("SELECT * FROM OutlookMails")
    fun getAll(): List<OutlookMails>

    @Insert
    fun insert(mails: OutlookMails)

    @Update
    fun update(mails: OutlookMails)

    @Delete
    fun delete(mails: OutlookMails)

    @Insert(entity = OutlookMails::class)
    fun init(mails: OutlookMails)

    @Query("DELETE FROM OutlookMails")
    fun resetmails()
}