package com.melon.mailmoajo.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import entities.Gmails
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


    @Query("SELECT COUNT(*) FROM OutlookMails")
    fun getOutlookCount(): Int


    @Query("SELECT * FROM OutlookMails ORDER BY receivedTime DESC LIMIT 1")
    fun getMostRecentMail(): OutlookMails?
}