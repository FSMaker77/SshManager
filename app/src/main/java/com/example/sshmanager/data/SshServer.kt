package com.example.sshmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sshmanager.ssh.SshAuth

enum class AuthType {
    PASSWORD,
    PRIVATE_KEY,
}

@Entity(tableName = "ssh_servers")
data class SshServer(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val host: String,
    val port: Int,
    val username: String,
    val authType: AuthType,
    val password: String? = null,
    val privateKey: String? = null,
    val passphrase: String? = null,
) {
    fun toAuth(): SshAuth = when (authType) {
        AuthType.PASSWORD -> SshAuth.Password(password.orEmpty())
        AuthType.PRIVATE_KEY -> SshAuth.PrivateKey(
            privateKey = privateKey.orEmpty(),
            passphrase = passphrase?.takeIf { it.isNotBlank() },
        )
    }
}
