package com.example.sshmanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SshServerDao {

    @Query("SELECT * FROM ssh_servers ORDER BY name COLLATE NOCASE ASC")
    fun observeAll(): Flow<List<SshServer>>

    @Query("SELECT * FROM ssh_servers WHERE id = :id")
    suspend fun getById(id: Long): SshServer?

    @Insert
    suspend fun insert(server: SshServer): Long

    @Update
    suspend fun update(server: SshServer)

    @Delete
    suspend fun delete(server: SshServer)
}
