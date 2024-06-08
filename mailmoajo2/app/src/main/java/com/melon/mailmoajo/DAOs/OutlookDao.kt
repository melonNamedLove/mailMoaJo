package com.melon.mailmoajo.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import entities.Gmails
import entities.OutlookMails
import entities.mails

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


    @Query("UPDATE OutlookMails SET mailfolderid = :nId WHERE sender = :sender")
    fun updateMatchingMail(nId:Int,sender:String): Int

    @Query("SELECT * FROM OutlookMails WHERE sender = :sender")
    fun getMatchingMail(sender:String): List<OutlookMails>

    @Query("UPDATE OutlookMails SET mailfolderid = :mailfolderid WHERE nId = :nId")
    fun updateMailBynId(nId:Int, mailfolderid:Int)


    @Query("""
            SELECT nId, title, receivedTime, sender, mailfolderid FROM OutlookMails
            WHERE sender  = :sender
        ORDER BY receivedTime DESC
    """)
    fun getAddressMatchingMailsOrderedByTime(sender: String? = null): List<OutlookMails>
}