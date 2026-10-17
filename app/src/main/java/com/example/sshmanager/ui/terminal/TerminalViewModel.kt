package com.example.sshmanager.ui.terminal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sshmanager.R
import com.example.sshmanager.data.SshServer
import com.example.sshmanager.data.SshServerRepository
import com.example.sshmanager.ssh.SshConnectionException
import com.example.sshmanager.ssh.SshConnectionState
import com.example.sshmanager.ssh.SshSession
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TerminalViewModel(
    application: Application,
    private val repository: SshServerRepository,
    private val serverId: Long,
) : AndroidViewModel(application) {

    private val _serverName = MutableStateFlow("")
    val serverName: StateFlow<String> = _serverName.asStateFlow()

    private val _connectionState = MutableStateFlow<SshConnectionState>(SshConnectionState.Idle)
    val connectionState: StateFlow<SshConnectionState> = _connectionState.asStateFlow()

    private val _outputLines = MutableStateFlow<List<String>>(emptyList())
    val outputLines: StateFlow<List<String>> = _outputLines.asStateFlow()

    private var server: SshServer? = null
    private var session: SshSession? = null
    private var readerJob: Job? = null

    init {
        viewModelScope.launch {
            val loaded = repository.getById(serverId)
            server = loaded
            _serverName.value = loaded?.name.orEmpty()
            if (loaded == null) {
                _connectionState.value = SshConnectionState.Error(
                    getApplication<Application>().getString(R.string.error_server_not_found),
                )
            }
        }
    }

    fun connect() {
        val target = server ?: return
        val current = _connectionState.value
        if (current is SshConnectionState.Connected || current is SshConnectionState.Connecting) {
            return
        }

        _connectionState.value = SshConnectionState.Connecting
        viewModelScope.launch {
            val newSession = SshSession(target)
            try {
                newSession.connect()
            } catch (e: SshConnectionException) {
                _connectionState.value = SshConnectionState.Error(
                    e.message ?: getString(R.string.error_connection_failed),
                )
                return@launch
            }

            session = newSession
            _connectionState.value = SshConnectionState.Connected
            appendLine(
                getString(
                    R.string.connected_to,
                    target.username,
                    target.host,
                    target.port,
                ),
            )

            readerJob = viewModelScope.launch {
                newSession.readOutput().collect { line -> appendLine(line) }
                if (_connectionState.value is SshConnectionState.Connected) {
                    _connectionState.value = SshConnectionState.Disconnected
                    appendLine(getString(R.string.session_terminated))
                }
            }
        }
    }

    fun sendCommand(command: String) {
        val trimmed = command.trim()
        if (trimmed.isEmpty()) return
        val currentSession = session ?: return

        appendLine("\$ $trimmed")
        viewModelScope.launch {
            try {
                currentSession.sendCommand(trimmed)
            } catch (e: SshConnectionException) {
                _connectionState.value = SshConnectionState.Error(
                    e.message ?: getString(R.string.error_send_failed),
                )
            }
        }
    }

    fun clearOutput() {
        _outputLines.value = emptyList()
    }

    fun disconnect() {
        readerJob?.cancel()
        readerJob = null
        session?.disconnect()
        session = null
        _connectionState.value = SshConnectionState.Disconnected
        appendLine(getString(R.string.disconnected_message))
    }

    override fun onCleared() {
        readerJob?.cancel()
        session?.disconnect()
        session = null
        super.onCleared()
    }

    private fun getString(resId: Int, vararg args: Any): String =
        getApplication<Application>().getString(resId, *args)

    private fun appendLine(line: String) {
        _outputLines.update { current ->
            (current + line).takeLast(MAX_LINES)
        }
    }

    companion object {
        private const val MAX_LINES = 5_000

        fun factory(
            repository: SshServerRepository,
            serverId: Long,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY],
                ) {
                    "Application non disponibile"
                }
                TerminalViewModel(application, repository, serverId)
            }
        }
    }
}
