package com.example.translatorapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.translatorapp.screen.NavEntry
import com.example.translatorapp.ui.theme.TranslatorAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.bush.translator.Language
import me.bush.translator.Translator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TranslatorAppTheme {
                NavEntry()
            }
        }
    }
}


@Composable
fun TranslatorScreen() {
    var inputText by remember { mutableStateOf("") }
    var translatedText by remember { mutableStateOf("") }

    val languages = listOf("ur", "en", "hi", "fr", "de", "es", "ru", "ar")
    var selectedLang by remember { mutableStateOf("ur") }
    var expanded by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope() // ✅ Needed for launching translation

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Enter text to translate:")
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Type here...") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Select target language:")
        Spacer(modifier = Modifier.height(8.dp))

        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(selectedLang.uppercase())
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                languages.forEach { lang ->
                    DropdownMenuItem(
                        text = { Text(lang.uppercase()) },
                        onClick = {
                            selectedLang = lang
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    val result = translateText(inputText, selectedLang)
                    translatedText = result
                }
            },
            enabled = inputText.isNotBlank()
        ) {
            Text("Translate")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (translatedText.isNotBlank()) {
            Text("Translated text:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(translatedText, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

// ✅ This is a suspend function, NOT a Composable
suspend fun translateText(
    input: String,
    targetLang: String
): String {
    val translator = Translator()
    val result = withContext(Dispatchers.IO) {
        translator.translateBlocking(input, Language(targetLang), Language.AUTO)
    }
    return result.translatedText
}
