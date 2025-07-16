package com.ditadigitali.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ditadigitali.ui.theme.Primary
import com.ditadigitali.ui.theme.Secondary
import com.ditadigitali.ui.theme.Typography
import com.ditadigitali.utils.NetworkUtil

@Composable
fun SettingsScreen(onSave: (String) -> Unit, onCancel: () -> Unit) {
    val context = LocalContext.current
    var serverIp by remember { mutableStateOf(NetworkUtil.getServerIp(context)) }

    Box (
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.5f)
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(
                text = "Configurazione Server",
                style = Typography.titleLarge
            )
            Spacer(Modifier.height(40.dp))
            OutlinedTextField(
                value = serverIp,
                onValueChange = { serverIp = it },
                label = { Text("Server IP") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(40.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onCancel,
                    modifier = Modifier.width(150.dp).height(45.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = Secondary
                    )
                ) {
                    Text("Cancella")
                }

                Spacer(Modifier.width(8.dp))

                Button(
                    onClick = { onSave(serverIp) },
                    modifier = Modifier.width(150.dp).height(45.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = Secondary
                    )
                ) {
                    Text("Salva")
                }
            }
        }
    }
}