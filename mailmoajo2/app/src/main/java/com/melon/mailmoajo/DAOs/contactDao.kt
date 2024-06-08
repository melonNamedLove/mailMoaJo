package com.melon.mailmoajo.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import entities.contacts

@Dao
interface contactDao{

    @Query("SELECT * FROM contacts")
    fun getAll(): List<contacts>

    @Insert
    fun insert(contact: contacts)

    @Update
    fun update(contact: contacts)

    @Delete
    fun delete(contact: contacts)

//    @Query("SELECT * FROM User") // 테이블의 모든 값을 가져와라
//    fun getAll(): List<User>
//
    @Query("DELETE FROM contacts WHERE nId = :nId")
    fun deleteUserByNId(nId: Int)



}

//@Dao
//interface UserDao {
//    @Query("SELECT * FROM user")
//    fun getAll(): List<User>
//
//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<User>
//
//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): User
//
//    @Insert
//    fun insertAll(vararg users: User)
//
//    @Delete
//    fun delete(user: User)
//}