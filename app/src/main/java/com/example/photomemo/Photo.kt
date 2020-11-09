package com.example.photomemo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = 'photo_table')
data class Photo (
        @PrimaryKey @ColumnInfo(name = "uri") val uri: String,
        @ColumnInfo(name = "memo") val memo: String
){

}