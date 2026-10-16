# 📱 SshManager

[🇮🇹 Italiano](#-italiano) | [🇬🇧 English](#-english-1)

---

## 🇮🇹 Italiano

**SshManager** è un'applicazione Android moderna nativa, sviluppata in **Kotlin** e **Jetpack Compose**, progettata per gestire ed eseguire collegamenti e comandi SSH direttamente dal tuo smartphone in modo semplice, veloce e sicuro.

### ✨ Caratteristiche Principali
- **Interfaccia Moderna (Jetpack Compose & Material 3):** UI pulita, reattiva e intuitiva con supporto alla navigazione fluida.
- **Gestione Server:** Aggiungi, modifica ed elimina i tuoi server SSH salvando Host, Porta, Username e credenziali (Password o Chiave Privata).
- **Terminale in Tempo Reale:** Esegui comandi remoti e visualizza l'output live grazie all'integrazione con la libreria **JSch** (`xterm-256color`).
- **Architettura Locale:** Salvataggio dei dati sicuro ed efficiente tramite **Room Database** (architettura MVVM).

### 🛠️ Tecnologie Utilizzate
- **Linguagem:** Kotlin
- **UI Framework:** Jetpack Compose, Material 3
- **Database:** Room (KSP)
- **SSH Library:** JSch (`com.github.mwiede`)
- **Architettura:** MVVM (ViewModel, StateFlow)

### 🚀 Come Compilare e Installare
Assicurati di avere configurato un JDK compatibile (es. JDK 21) e di avere un dispositivo Android connesso con il *Debug USB* attivo.

1. Clona il repository:
   ```bash
   git clone [https://github.com/FSMaker77/SshManager.git](https://github.com/FSMaker77/SshManager.git)
   cd SshManager
