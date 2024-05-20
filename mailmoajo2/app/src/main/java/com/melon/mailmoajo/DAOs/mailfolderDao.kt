package com.melon.mailmoajo.DAOs

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import entities.orderedMailFolders

@Dao
interface mailfolderDao{

    @Query("SELECT * FROM orderedMailFolders")
    fun getAll(): List<orderedMailFolders>

    @Insert
    fun insert(mailfolders: orderedMailFolders)

    @Update
    fun update(mailfolders: orderedMailFolders)

    @Delete
    fun delete(mailfolders: orderedMailFolders)

    @Insert(entity = orderedMailFolders::class)
    fun init(mailfolders: orderedMailFolders)

    @Query("DELETE FROM orderedMailFolders WHERE id = :folderid")
    fun deleteMailFolderByFolderId(folderid: String)

//    @Query("SELECT * FROM User") // 테이블의 모든 값을 가져와라
//    fun getAll(): List<User>
//
//    @Query("DELETE FROM User WHERE name = :name") // 'name'에 해당하는 유저를 삭제해라
//    fun deleteUserByName(name: String)
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