package com.proyek2.blkindramayu.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface MemberDAO {
    @Query("SELECT * from member")
    fun getAll() : List<MemberEntity>

    @Insert(onConflict = REPLACE)
    fun insert(member: MemberEntity)

    @Delete
    fun delete(member: MemberEntity)

}