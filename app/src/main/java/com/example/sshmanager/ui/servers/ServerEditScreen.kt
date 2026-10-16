package com.example.sshmanager.ui.servers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sshmanager.R
import com.example.sshmanager.data.AuthType
import com.example.sshmanager.data.SshServerRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerEditScreen(
    repository: SshServerRepository,
    serverId: Long,
    onNavigateBack: () -> Unit,
) {
    val viewModel: ServerEditViewModel = viewModel(
        factory = ServerEditViewModel.factory(repository, serverId),
    )
    val form = viewModel.formState

    LaunchedEffect(form.isSaved, form.isDeleted) {
        if (form.isSaved || form.isDeleted) {
            onNavigateBack()
        }
    }

    var showPassword by rememberSaveable { mutableStateOf(false) }
    var showPassphrase by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(
                            if (form.isEditing) R.string.edit_server else R.string.new_server,
                        ),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        if (form.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            OutlinedTextField(
                value = form.name,
                onValueChange = viewModel::onNameChange,
                label = { Text(stringResource(R.string.field_name)) },
                isError = form.nameError,
                supportingText = if (form.nameError) {
                    { Text(stringResource(R.string.error_required)) }
                } else {
                    null
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = form.host,
                onValueChange = viewModel::onHostChange,
                label = { Text(stringResource(R.string.field_host)) },
                isError = form.hostError,
                supportingText = if (form.hostError) {
                    { Text(stringResource(R.string.error_required)) }
                } else {
                    null
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                modifier = Modifier.fillMaxWidth(),
            )

            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(
                    value = form.port,
                    onValueChange = viewModel::onPortChange,
                    label = { Text(stringResource(R.string.field_port)) },
                    isError = form.portError,
                    supportingText = if (form.portError) {
                        { Text(stringResource(R.string.error_port_invalid)) }
                    } else {
                        null
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                )

                OutlinedTextField(
                    value = form.username,
                    onValueChange = viewModel::onUsernameChange,
                    label = { Text(stringResource(R.string.field_username)) },
                    isError = form.usernameError,
                    supportingText = if (form.usernameError) {
                        { Text(stringResource(R.string.error_required)) }
                    } else {
                        null
                    },
                    singleLine = true,
                    modifier = Modifier.weight(2f),
                )
            }

            Text(
                text = stringResource(R.string.auth_method),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = form.authType == AuthType.PASSWORD,
                    onClick = { viewModel.onAuthTypeChange(AuthType.PASSWORD) },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                ) {
                    Text(stringResource(R.string.auth_password))
                }
                SegmentedButton(
                    selected = form.authType == AuthType.PRIVATE_KEY,
                    onClick = { viewModel.onAuthTypeChange(AuthType.PRIVATE_KEY) },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                ) {
                    Text(stringResource(R.string.auth_private_key))
                }
            }

            when (form.authType) {
                AuthType.PASSWORD -> {
                    OutlinedTextField(
                        value = form.password,
                        onValueChange = viewModel::onPasswordChange,
                        label = { Text(stringResource(R.string.field_password)) },
                        isError = form.secretError,
                        supportingText = if (form.secretError) {
                            { Text(stringResource(R.string.error_password_required)) }
                        } else {
                            null
                        },
                        singleLine = true,
                        visualTransformation = if (showPassword) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) {
                                        Icons.Default.VisibilityOff
                                    } else {
                                        Icons.Default.Visibility
                                    },
                                    contentDescription = stringResource(
                                        if (showPassword) {
                                            R.string.hide_password
                                        } else {
                                            R.string.show_password
                                        },
                                    ),
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                AuthType.PRIVATE_KEY -> {
                    OutlinedTextField(
                        value = form.privateKey,
                        onValueChange = viewModel::onPrivateKeyChange,
                        label = { Text(stringResource(R.string.field_private_key)) },
                        isError = form.secretError,
                        supportingText = if (form.secretError) {
                            { Text(stringResource(R.string.error_key_required)) }
                        } else {
                            null
                        },
                        minLines = 4,
                        maxLines = 8,
                        textStyle = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    OutlinedTextField(
                        value = form.passphrase,
                        onValueChange = viewModel::onPassphraseChange,
                        label = { Text(stringResource(R.string.field_passphrase)) },
                        singleLine = true,
                        visualTransformation = if (showPassphrase) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            IconButton(onClick = { showPassphrase = !showPassphrase }) {
                                Icon(
                                    imageVector = if (showPassphrase) {
                                        Icons.Default.VisibilityOff
                                    } else {
                                        Icons.Default.Visibility
                                    },
                                    contentDescription = stringResource(
                                        if (showPassphrase) {
                                            R.string.hide_password
                                        } else {
                                            R.string.show_password
                                        },
                                    ),
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            Button(
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.save))
            }

            if (form.isEditing) {
                TextButton(
                    onClick = viewModel::delete,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(R.string.delete_server),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
    }
}
