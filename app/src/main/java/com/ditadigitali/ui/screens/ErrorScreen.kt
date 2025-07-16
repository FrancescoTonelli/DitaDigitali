package com.ditadigitali.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ditadigitali.ui.components.FloatingButton
import com.ditadigitali.ui.theme.Primary
import com.ditadigitali.ui.theme.Secondary
import com.ditadigitali.ui.theme.Typography

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit, onSettings: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Errore di connessione",
                modifier = Modifier.padding(16.dp),
                style = Typography.titleLarge
            )
            Text(
                text = "Non è stato possibile raggiungere il server.\n" +
                    "Controllare di essere connessi alla rete giusta,\n" +
                    "e che l'indirizzo IP del server sia corretto.\n",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
            Text("Errore: $message", modifier = Modifier.padding(top = 24.dp, bottom = 8.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = Secondary
                )
            ) { Text("Riprova") }
        }
        FloatingButton(
            onClick = onSettings,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            icon = Icons.Default.Settings,
            contentDescription = "Settings"
        )
    }
}