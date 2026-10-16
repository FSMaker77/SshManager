package com.example.sshmanager.ssh

import com.example.sshmanager.data.SshServer
import com.jcraft.jsch.ChannelShell
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import java.io.InputStream
import java.io.OutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

class SshConnectionException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause)

class SshSession(private val server: SshServer) {

    private var session: Session? = null
    private var channel: ChannelShell? = null
    private var stdin: OutputStream? = null
    private var stdout: InputStream? = null

    val isConnected: Boolean
        get() = session?.isConnected == true && channel?.isConnected == true

    suspend fun connect(): Unit = withContext(Dispatchers.IO) {
        disconnect()
        try {
            val jsch = JSch()
            val newSession = jsch.getSession(server.username, server.host, server.port)
            newSession.setConfig("StrictHostKeyChecking", "no")
            newSession.setConfig("serverAliveInterval", "15000")
            newSession.setConfig("serverAliveCountMax", "3")

            when (val auth = server.toAuth()) {
                is SshAuth.Password -> {
                    newSession.setConfig(
                        "PreferredAuthentications",
                        "password,keyboard-interactive,publickey",
                    )
                    newSession.setPassword(auth.password.toByteArray(Charsets.UTF_8))
                }

                is SshAuth.PrivateKey -> {
                    newSession.setConfig(
                        "PreferredAuthentications",
                        "publickey,keyboard-interactive,password",
                    )
                    jsch.addIdentity(
                        "server-${server.id}",
                        auth.privateKey.toByteArray(Charsets.UTF_8),
                        null,
                        auth.passphrase?.toByteArray(Charsets.UTF_8),
                    )
                }
            }

            newSession.connect(CONNECT_TIMEOUT_MS)
            session = newSession

            val newChannel = newSession.openChannel("shell") as ChannelShell
            newChannel.setPtyType("xterm-256color")
            stdin = newChannel.outputStream
            stdout = newChannel.inputStream
            newChannel.connect(CONNECT_TIMEOUT_MS)
            channel = newChannel
        } catch (e: Exception) {
            disconnect()
            throw SshConnectionException(
                e.message ?: "Connessione SSH non riuscita",
                e,
            )
        }
    }

    fun readOutput(): Flow<String> = flow {
        val stream = stdout ?: return@flow
        val reader = stream.reader(Charsets.UTF_8)
        val buffer = CharArray(READ_BUFFER_SIZE)
        val pending = StringBuilder()

        while (currentCoroutineContext().isActive && isConnected) {
            val read = try {
                reader.read(buffer)
            } catch (e: Exception) {
                break
            }
            if (read < 0) break

            pending.append(buffer, 0, read)
            var newline = pending.indexOf("\n")
            while (newline >= 0) {
                emit(pending.substring(0, newline).trimEnd('\r'))
                pending.delete(0, newline + 1)
                newline = pending.indexOf("\n")
            }
        }

        if (pending.isNotEmpty()) {
            emit(pending.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun sendCommand(command: String) = withContext(Dispatchers.IO) {
        val target = stdin ?: throw SshConnectionException("Sessione non connessa")
        try {
            target.write((command + "\n").toByteArray(Charsets.UTF_8))
            target.flush()
        } catch (e: Exception) {
            throw SshConnectionException(
                e.message ?: "Invio del comando non riuscito",
                e,
            )
        }
    }

    fun disconnect() {
        try {
            channel?.disconnect()
        } catch (_: Exception) {
        }
        channel = null
        try {
            session?.disconnect()
        } catch (_: Exception) {
        }
        session = null
        stdin = null
        stdout = null
    }

    private companion object {
        const val CONNECT_TIMEOUT_MS = 15_000
        const val READ_BUFFER_SIZE = 2_048
    }
}
