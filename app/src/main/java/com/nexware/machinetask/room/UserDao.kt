package com.nexware.machinetask.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nexware.machinetask.room.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllUsers(userRoomList : List<User>)

    @Query("select * from user_table")
    fun getData(): List<User>
}