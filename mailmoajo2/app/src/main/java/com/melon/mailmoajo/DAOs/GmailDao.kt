package com.melon.mailmoajo.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import entities.Gmails

@Dao
interface GmailDao {

    @Query("SELECT * FROM Gmails")
    fun getAll(): List<Gmails>

    @Insert
    fun insert(mails: Gmails)

    @Update
    fun update(mails: Gmails)

    @Delete
    fun delete(mails: Gmails)

    @Insert(entity = Gmails::class)
    fun init(mails: Gmails)

    @Query("DELETE FROM Gmails")
    fun resetmails()
}