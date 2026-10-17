# SshManager

An Android SSH client built with Kotlin and Jetpack Compose. Manage your SSH servers and run remote commands from a real-time terminal.

- [Italiano](#italiano)
- [English](#english)

---

## Italiano

**SshManager** è un'applicazione Android nativa sviluppata in **Kotlin** e **Jetpack Compose**, progettata per gestire server SSH ed eseguire comandi remoti direttamente dallo smartphone in modo semplice e veloce.

### Caratteristiche

- **Interfaccia moderna** (Jetpack Compose & Material 3): UI pulita, reattiva e con tema chiaro/scuro automatico.
- **Gestione server**: aggiungi, modifica ed elimina i tuoi server SSH (Host, Porta, Utente e credenziali: Password o Chiave Privata).
- **Terminale in tempo reale**: esegui comandi remoti e visualizza l'output live grazie alla libreria **JSch** (`xterm-256color`).
- **Archiviazione locale**: salvataggio dei dati tramite **Room Database** (pattern MVVM).

### Tecnologie

- **Linguaggio:** Kotlin
- **UI:** Jetpack Compose, Material 3
- **Database:** Room (KSP)
- **SSH:** JSch (`com.github.mwiede`)
- **Architettura:** MVVM (ViewModel, StateFlow)

### Compilazione

Requisiti: JDK 21 e Android SDK 37.

```bash
git clone https://github.com/FSMaker77/SshManager.git
cd SshManager
./gradlew :app:assembleDebug
```

L'APK viene generato in `app/build/outputs/apk/debug/app-debug.apk`.

---

## English

**SshManager** is a native Android application written in **Kotlin** and **Jetpack Compose**, designed to manage SSH servers and run remote commands directly from your phone.

### Features

- **Modern UI** (Jetpack Compose & Material 3): clean, responsive, with automatic dark/light theme.
- **Server management**: add, edit and delete SSH servers (Host, Port, Username and credentials: Password or Private Key).
- **Real-time terminal**: run remote commands and view live output, powered by the **JSch** library (`xterm-256color`).
- **Local storage**: data stored locally using **Room Database** (MVVM pattern).

### Technologies

- **Language:** Kotlin
- **UI:** Jetpack Compose, Material 3
- **Database:** Room (KSP)
- **SSH:** JSch (`com.github.mwiede`)
- **Architecture:** MVVM (ViewModel, StateFlow)

### Build

Requirements: JDK 21 and Android SDK 37.

```bash
git clone https://github.com/FSMaker77/SshManager.git
cd SshManager
./gradlew :app:assembleDebug
```

The APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.
