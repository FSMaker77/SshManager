package com.example.sshmanager.data

import kotlinx.coroutines.flow.Flow

class SshServerRepository(private val dao: SshServerDao) {

    val servers: Flow<List<SshServer>> = dao.observeAll()

    suspend fun getById(id: Long): SshServer? = dao.getById(id)

    suspend fun save(server: SshServer): Long =
        if (server.id == 0L) {
            dao.insert(server)
        } else {
            dao.update(server)
            server.id
        }

    suspend fun delete(server: SshServer) = dao.delete(server)
}
