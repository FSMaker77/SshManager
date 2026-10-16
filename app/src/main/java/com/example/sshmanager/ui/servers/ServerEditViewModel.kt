package com.example.sshmanager.ui.servers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sshmanager.data.AuthType
import com.example.sshmanager.data.SshServer
import com.example.sshmanager.data.SshServerRepository
import kotlinx.coroutines.launch

data class ServerFormState(
    val id: Long = 0L,
    val name: String = "",
    val host: String = "",
    val port: String = "22",
    val username: String = "",
    val authType: AuthType = AuthType.PASSWORD,
    val password: String = "",
    val privateKey: String = "",
    val passphrase: String = "",
    val nameError: Boolean = false,
    val hostError: Boolean = false,
    val portError: Boolean = false,
    val usernameError: Boolean = false,
    val secretError: Boolean = false,
    val isLoading: Boolean = true,
    val isSaved: Boolean = false,
    val isDeleted: Boolean = false,
) {
    val isEditing: Boolean get() = id != 0L
}

class ServerEditViewModel(
    private val repository: SshServerRepository,
    private val serverId: Long,
) : ViewModel() {

    var formState by mutableStateOf(ServerFormState())
        private set

    init {
        viewModelScope.launch {
            val server = if (serverId > 0L) repository.getById(serverId) else null
            formState = if (server == null) {
                ServerFormState(isLoading = false)
            } else {
                ServerFormState(
                    id = server.id,
                    name = server.name,
                    host = server.host,
                    port = server.port.toString(),
                    username = server.username,
                    authType = server.authType,
                    password = server.password.orEmpty(),
                    privateKey = server.privateKey.orEmpty(),
                    passphrase = server.passphrase.orEmpty(),
                    isLoading = false,
                )
            }
        }
    }

    fun onNameChange(value: String) {
        formState = formState.copy(name = value, nameError = false)
    }

    fun onHostChange(value: String) {
        formState = formState.copy(host = value, hostError = false)
    }

    fun onPortChange(value: String) {
        if (value.length <= 5 && value.all { it.isDigit() }) {
            formState = formState.copy(port = value, portError = false)
        }
    }

    fun onUsernameChange(value: String) {
        formState = formState.copy(username = value, usernameError = false)
    }

    fun onAuthTypeChange(value: AuthType) {
        formState = formState.copy(authType = value, secretError = false)
    }

    fun onPasswordChange(value: String) {
        formState = formState.copy(password = value, secretError = false)
    }

    fun onPrivateKeyChange(value: String) {
        formState = formState.copy(privateKey = value, secretError = false)
    }

    fun onPassphraseChange(value: String) {
        formState = formState.copy(passphrase = value)
    }

    fun save() {
        val current = formState
        val nameError = current.name.isBlank()
        val hostError = current.host.isBlank()
        val portValue = current.port.toIntOrNull()
        val portError = portValue == null || portValue !in 1..65_535
        val usernameError = current.username.isBlank()
        val secretError = when (current.authType) {
            AuthType.PASSWORD -> current.password.isEmpty()
            AuthType.PRIVATE_KEY -> current.privateKey.isBlank()
        }

        formState = current.copy(
            nameError = nameError,
            hostError = hostError,
            portError = portError,
            usernameError = usernameError,
            secretError = secretError,
        )

        if (nameError || hostError || portError || usernameError || secretError) {
            return
        }

        val port = portValue
        viewModelScope.launch {
            val server = SshServer(
                id = current.id,
                name = current.name.trim(),
                host = current.host.trim(),
                port = port,
                username = current.username.trim(),
                authType = current.authType,
                password = current.password.takeIf { current.authType == AuthType.PASSWORD },
                privateKey = current.privateKey.takeIf { current.authType == AuthType.PRIVATE_KEY },
                passphrase = current.passphrase.takeIf {
                    current.authType == AuthType.PRIVATE_KEY && it.isNotBlank()
                },
            )
            repository.save(server)
            formState = formState.copy(isSaved = true)
        }
    }

    fun delete() {
        val id = formState.id
        if (id == 0L) return
        viewModelScope.launch {
            repository.getById(id)?.let { repository.delete(it) }
            formState = formState.copy(isDeleted = true)
        }
    }

    companion object {
        fun factory(
            repository: SshServerRepository,
            serverId: Long,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer { ServerEditViewModel(repository, serverId) }
        }
    }
}
