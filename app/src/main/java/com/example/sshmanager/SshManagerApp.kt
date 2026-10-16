package com.example.sshmanager

import android.app.Application
import android.content.Context
import com.example.sshmanager.data.AppDatabase
import com.example.sshmanager.data.SshServerRepository

class AppContainer(context: Context) {

    private val database = AppDatabase.getInstance(context)

    val repository = SshServerRepository(database.sshServerDao())
}

class SshManagerApp : Application() {

    val container: AppContainer by lazy { AppContainer(this) }
}
