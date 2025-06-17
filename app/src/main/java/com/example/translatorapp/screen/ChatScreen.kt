package com.example.translatorapp.screen

import android.Manifest
import android.annotation.SuppressLint
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.translatorapp.R
import me.bush.translator.Translator
import java.util.Locale


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController) {

    var textFields by remember { mutableStateOf("") }
    var showTranslatedCard by remember { mutableStateOf(false) }
    var translatedText by remember { mutableStateOf("") }
    var isTranslating by remember { mutableStateOf(false) }


    var fromExpanded by remember { mutableStateOf(false) }
    var toExpanded by remember { mutableStateOf(false) }

    val translator = remember {
        Translator()
    }
    val coroutineScope = rememberCoroutineScope()


    val languages = Languages.allLanguages
    val sourceLanguagesList =
        remember { mutableStateListOf(Languages.AUTO).apply { addAll(languages) } }
    val targetLanguagesList = languages


    var selectedFromLanguage by remember { mutableStateOf(Languages.AUTO) }
    var selectedToLanguage by remember { mutableStateOf(languages.first { it.code == "es" }) }

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                startSpeechRecognition(context) { result ->
                    textFields = result
                }
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    val tts = remember {
        TextToSpeech(context, null).apply {
            language = Locale.getDefault()
        }
    }


    DisposableEffect(Unit) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    val chatItems = listOf(
        ChatItem("Hello how are you?", "¿Hola como estas?", true),
        ChatItem("Hola", "Hello", false)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Voice Conversation", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF003366))
            )
        }, bottomBar = {
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())
        ) {
            items(chatItems) { chat ->
                ChatBubble(chat)
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {


                Box(
                    modifier = Modifier
                        .padding(top = 400.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBFE)),
                        elevation = CardDefaults.elevatedCardElevation(1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box {
                                Row(
                                    modifier = Modifier.clickable { fromExpanded = true },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = {
                                            launcher.launch(Manifest.permission.RECORD_AUDIO)
                                        },
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape),
                                        colors = IconButtonDefaults.iconButtonColors(
                                            containerColor = Color(0XFF003366)
                                        )
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.mic),
                                            contentDescription = "Mic",
                                            tint = Color.White
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    selectedFromLanguage.name?.let {
                                        Text(
                                            text = it,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.Black
                                        )
                                    }
                                }

                                DropdownMenu(
                                    expanded = fromExpanded,
                                    onDismissRequest = { fromExpanded = false }
                                ) {
                                    sourceLanguagesList.forEach { language ->
                                        DropdownMenuItem(
                                            text = { Text("${language.flag.orEmpty()} ${language.name}") },
                                            onClick = {
                                                selectedFromLanguage = language
                                                fromExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                            Image(
                                painter = painterResource(R.drawable.swap),
                                contentDescription = "Swap languages",
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        if (selectedFromLanguage != Languages.AUTO) {
                                            val temp = selectedFromLanguage
                                            selectedFromLanguage = selectedToLanguage
                                            selectedToLanguage = temp
                                            textFields = ""
                                            translatedText = ""
                                            showTranslatedCard = false
                                        }
                                    }
                            )
                            Box {
                                Row(
                                    modifier = Modifier.clickable { toExpanded = true },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = {
                                            launcher.launch(Manifest.permission.RECORD_AUDIO)
                                        },
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape),
                                        colors = IconButtonDefaults.iconButtonColors(
                                            containerColor = Color(0XFFFF6600)
                                        )
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.mic),
                                            contentDescription = "Mic",
                                            tint = Color.White
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    selectedToLanguage.name?.let {
                                        Text(
                                            text = it,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.Black
                                        )
                                    }
                                }

                                DropdownMenu(
                                    expanded = toExpanded,
                                    onDismissRequest = { toExpanded = false }
                                ) {
                                    targetLanguagesList.forEach { language ->
                                        DropdownMenuItem(
                                            text = { Text("${language.flag.orEmpty()} ${language.name}") },
                                            onClick = {
                                                selectedToLanguage = language
                                                toExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }


        }

    }
}

data class ChatItem(val original: String, val translated: String, val isUser: Boolean)

@Composable
fun ChatBubble(chat: ChatItem) {
    val alignment = if (chat.isUser) Alignment.End else Alignment.Start
    val bgColor = if (chat.isUser) Color(0xFFF6F2FF) else Color(0xFFF6F2FF)

    Column(
        horizontalAlignment = alignment,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 250.dp)
                .background(bgColor, shape = RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Text(
                text = chat.original,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Divider(modifier = Modifier.fillMaxWidth())
            Text(
                text = chat.translated,
                color = Color(0xFFFF6B00),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
            )
        }
    }
}


