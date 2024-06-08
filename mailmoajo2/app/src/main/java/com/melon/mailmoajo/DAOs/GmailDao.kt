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


    @Query("SELECT COUNT(*) FROM Gmails")
    fun getGmailCount(): Int


    @Query("SELECT * FROM Gmails ORDER BY receivedTime DESC LIMIT 1")
    fun getMostRecentMail(): Gmails?

//
//    @Query("SELECT * FROM Gmails WHERE sender = :sender")
//    fun getMatchingMail(sender:String): List<Gmails>



    @Query("UPDATE Gmails SET mailfolderid = :nId WHERE sender = :sender")
    fun updateMatchingMail(nId:Int,sender:String):Int

    @Query("SELECT * FROM Gmails WHERE sender = :sender")
    fun getMatchingMail(sender:String): List<Gmails>

    @Query("UPDATE Gmails SET mailfolderid = :mailfolderid WHERE nId = :nId")
    fun updateMailBynId(nId:Int, mailfolderid:Int)

    @Query("""
            SELECT nId, title, receivedTime, sender, mailfolderid FROM outlookmails
            WHERE sender  = :sender
        ORDER BY receivedTime DESC
    """)
    fun getAddressMatchingMailsOrderedByTime(sender: String? = null): List<mails>
}