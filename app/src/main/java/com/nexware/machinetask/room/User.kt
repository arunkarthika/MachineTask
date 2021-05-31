package com.nexware.machinetask.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(val email: String,
                val first_name: String,
                val last_name: String,
                val avatar: String,
                @PrimaryKey(autoGenerate = false) val id: Int? = null)