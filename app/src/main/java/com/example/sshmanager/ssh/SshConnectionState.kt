package com.example.sshmanager.ssh

sealed interface SshConnectionState {

    data object Idle : SshConnectionState

    data object Connecting : SshConnectionState

    data object Connected : SshConnectionState

    data object Disconnected : SshConnectionState

    data class Error(val message: String) : SshConnectionState
}
