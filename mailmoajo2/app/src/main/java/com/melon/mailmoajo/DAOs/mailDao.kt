package com.melon.mailmoajo.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import entities.mails
import entities.orderedMailFolders
@Dao
interface mailDao {

    @Query("""
        SELECT * FROM (
            SELECT nId, title, receivedTime, sender, mailfolderid FROM gmails
            WHERE mailfolderid  = :mailfolderid
            UNION ALL
            SELECT nId, title, receivedTime, sender, mailfolderid FROM outlookmails
            WHERE mailfolderid  = :mailfolderid
        ) AS combined_results
        ORDER BY receivedTime DESC
    """)
    fun getAllMailsOrderedByTime(mailfolderid: Int? = null): List<mails>


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