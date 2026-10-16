package com.example.sshmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.sshmanager.ui.navigation.SshManagerNavHost
import com.example.sshmanager.ui.theme.SshManagerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as SshManagerApp).container
        setContent {
            SshManagerTheme {
                SshManagerNavHost(container = container)
            }
        }
    }
}
