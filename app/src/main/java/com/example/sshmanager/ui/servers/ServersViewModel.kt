package com.example.sshmanager.ui.servers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sshmanager.data.SshServer
import com.example.sshmanager.data.SshServerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ServersViewModel(private val repository: SshServerRepository) : ViewModel() {

    val servers: StateFlow<List<SshServer>> = repository.servers
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    fun deleteServer(server: SshServer) {
        viewModelScope.launch { repository.delete(server) }
    }

    companion object {
        fun factory(repository: SshServerRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer { ServersViewModel(repository) }
        }
    }
}
