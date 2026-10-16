package com.example.sshmanager.ssh

sealed interface SshAuth {

    data class Password(val password: String) : SshAuth

    data class PrivateKey(
        val privateKey: String,
        val passphrase: String?,
    ) : SshAuth
}
