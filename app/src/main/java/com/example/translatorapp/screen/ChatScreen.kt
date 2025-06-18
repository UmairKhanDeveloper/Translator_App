package com.example.translatorapp.screen

import android.Manifest
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.texttranslater.domain.model.language.LanguagesItem
import com.example.translatorapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.bush.translator.Language
import me.bush.translator.Translator
import java.util.Locale

data class TranslationChat(
    val translatedText: String,
    val pronunciation: String? = null,
    val sourceLanguage: LanguagesItem? = null
)

object LanguagesChat {
    fun fromCode(code: String): LanguagesItem? {
        return allLanguages.find { it.code == code }
    }

    val AUTO =
        LanguagesItem(name = "Auto Detect", localName = "Auto Detect", flag = "🌐", code = "auto")
    val allLanguages = listOf(
        LanguagesItem(name = "English", localName = "English", flag = "🇬🇧", code = "en"),
        LanguagesItem(name = "Spanish", localName = "español", flag = "🇪🇸", code = "es"),
        LanguagesItem(name = "Arabic", localName = "العربية", flag = "🇸🇦", code = "ar"),
        LanguagesItem(name = "German", localName = "Deutsch", flag = "🇩🇪", code = "de"),
        LanguagesItem(name = "Hindi", localName = "हिंदी", flag = "🇮🇳", code = "hi"),
        LanguagesItem(name = "Portuguese", localName = "Português", flag = "🇵🇹", code = "pt"),
        LanguagesItem(name = "Turkish", localName = "Türk", flag = "🇹🇷", code = "tr"),
        LanguagesItem(name = "Thai", localName = "ไทย", flag = "🇹🇭", code = "th"),
        LanguagesItem(name = "French", localName = "Français", flag = "🇫🇷", code = "fr"),
        LanguagesItem(name = "Chinese", localName = "中文", flag = "🇨🇳", code = "zh"),
        LanguagesItem(name = "Japanese", localName = "日本語", flag = "🇯🇵", code = "ja"),
        LanguagesItem(name = "Korean", localName = "한국어", flag = "🇰🇷", code = "ko"),
        LanguagesItem(name = "Russian", localName = "русский", flag = "🇷🇺", code = "ru"),
        LanguagesItem(name = "Italian", localName = "Italiano", flag = "🇮🇹", code = "it"),
        LanguagesItem(name = "Dutch", localName = "Nederlands", flag = "🇳🇱", code = "nl"),
        LanguagesItem(name = "Swedish", localName = "Svenska", flag = "🇸🇪", code = "sv"),
        LanguagesItem(name = "Norwegian", localName = "Norsk", flag = "🇳🇴", code = "no"),
        LanguagesItem(name = "Finnish", localName = "Suomi", flag = "🇫🇮", code = "fi"),
        LanguagesItem(name = "Danish", localName = "Dansk", flag = "🇩🇰", code = "da"),
        LanguagesItem(name = "Greek", localName = "Ελληνικά", flag = "🇬🇷", code = "el"),
        LanguagesItem(name = "Czech", localName = "Čeština", flag = "🇨🇿", code = "cs"),
        LanguagesItem(name = "Polish", localName = "Polski", flag = "🇵🇱", code = "pl"),
        LanguagesItem(name = "Hungarian", localName = "Magyar", flag = "🇭🇺", code = "hu"),
        LanguagesItem(name = "Romanian", localName = "Română", flag = "🇷🇴", code = "ro"),
        LanguagesItem(name = "Ukrainian", localName = "Українська", flag = "🇺🇦", code = "uk"),
        LanguagesItem(name = "Bulgarian", localName = "Български", flag = "🇧🇬", code = "bg"),
        LanguagesItem(name = "Vietnamese", localName = "Tiếng Việt", flag = "🇻🇳", code = "vi"),
        LanguagesItem(
            name = "Indonesian",
            localName = "Bahasa Indonesia",
            flag = "🇮🇩",
            code = "id"
        ),
        LanguagesItem(name = "Malay", localName = "Bahasa Melayu", flag = "🇲🇾", code = "ms"),
        LanguagesItem(name = "Swahili", localName = "Kiswahili", flag = "🇰🇪", code = "sw"),
        LanguagesItem(name = "Afrikaans", localName = "Afrikaans", flag = "🇿🇦", code = "af"),
        LanguagesItem(name = "Hebrew", localName = "עברית", flag = "🇮🇱", code = "he"),
        LanguagesItem(name = "Persian", localName = "فارسی", flag = "🇮🇷", code = "fa"),
        LanguagesItem(name = "Urdu", localName = "اردو", flag = "🇵🇰", code = "ur"),
        LanguagesItem(name = "Bengali", localName = "বাংলা", flag = "🇧🇩", code = "bn"),
        LanguagesItem(name = "Tamil", localName = "தமிழ்", flag = "🇱🇰", code = "ta"),
        LanguagesItem(name = "Telugu", localName = "తెలుగు", flag = "🇮🇳", code = "te"),
        LanguagesItem(name = "Kannada", localName = "ಕನ್ನಡ", flag = "🇮🇳", code = "kn"),
        LanguagesItem(name = "Marathi", localName = "मराठी", flag = "🇮🇳", code = "mr"),
        LanguagesItem(name = "Gujarati", localName = "ગુજરાતી", flag = "🇮🇳", code = "gu")
    )
}


class TranslatorChat {
    suspend fun translate(
        text: String,
        target: LanguagesItem,
        source: LanguagesItem
    ): com.example.translatorapp.screen.Translation {
        println("Attempting to translate '$text' from ${source.name} to ${target.name}")
        val translatedDummyText = "Translated: \"$text\" into ${target.name}"
        return Translation(
            translatedText = translatedDummyText,
            pronunciation = "Pro-nun-ci-a-tion Placeholder",
            sourceLanguage = source
        )
    }
}

data class ChatItem(
    val original: String,
    val translated: String,
    val isUser: Boolean,
    val originalLang: LanguagesItem? = null
)

private fun getSpeechRecognizerIntent(
    prompt: String? = null,
    languageCode: String? = null
): Intent {
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        if (!languageCode.isNullOrBlank() && languageCode != "auto") {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
        }
        putExtra(RecognizerIntent.EXTRA_PROMPT, prompt)
        putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
    }
    return intent
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavHostController) {

    val chatItems = remember { mutableStateListOf<ChatItem>() }

    var fromExpanded by remember { mutableStateOf(false) }
    var toExpanded by remember { mutableStateOf(false) }
    val languages = LanguagesChat.allLanguages
    val sourceLanguagesList = remember { mutableStateListOf(LanguagesChat.AUTO).apply { addAll(languages) } }
    val targetLanguagesList = remember { mutableStateListOf(LanguagesChat.allLanguages) }

    var selectedFromLanguage by remember { mutableStateOf(LanguagesChat.AUTO) }
    var selectedToLanguage by remember { mutableStateOf(languages.first { it.code == "es" }) }


    var isTranslating by remember { mutableStateOf(false) }

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val translator = remember { Translator() }


    val tts = remember {
        TextToSpeech(context, null)
    }


    LaunchedEffect(selectedToLanguage) {
        if (selectedToLanguage != LanguagesChat.AUTO) {
            val locale = try {
                Locale.forLanguageTag(selectedToLanguage.code)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Invalid locale for TTS: ${selectedToLanguage.code}", Toast.LENGTH_SHORT).show()
                Locale.getDefault()
            }

            val result = tts.setLanguage(locale)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(
                    context,
                    "TTS language ${selectedToLanguage.name} not fully supported or data missing. Result code: $result",
                    Toast.LENGTH_LONG
                ).show()
            } else if (result == TextToSpeech.LANG_AVAILABLE || result == TextToSpeech.LANG_COUNTRY_AVAILABLE || result == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE) {
                println("TTS language set to ${selectedToLanguage.name}")
            } else {
                Toast.makeText(
                    context,
                    "TTS language setting failed. Result code: $result",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            println("TTS language not set for AUTO.")
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    var lastMicClickedIsLeft by remember { mutableStateOf<Boolean?>(null) }


    val leftSpeechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val data: Intent? = result.data
            val recognizedTextList = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val recognizedText = recognizedTextList?.get(0) ?: ""

            if (recognizedText.isNotBlank()) {
                coroutineScope.launch {
                    isTranslating = true
                    try {
                        val sourceCode = if (selectedFromLanguage == LanguagesChat.AUTO) {
                            Language.AUTO
                        } else {
                            Language.values().firstOrNull { it.code == selectedFromLanguage.code }
                                ?: Language.AUTO.also {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "Source language ${selectedFromLanguage.name} not fully supported by translator, attempting AUTO.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }


                        val targetCode =
                            Language.values().firstOrNull { it.code == selectedToLanguage.code }
                                ?: run {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "Target language ${selectedToLanguage.name} not supported by translator.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    isTranslating = false
                                    return@launch
                                }

                        println("Translating (Left Mic): '$recognizedText' from ${sourceCode.code} to ${targetCode.code}")

                        val translation = withContext(Dispatchers.IO) {
                            translator.translate(
                                text = recognizedText,
                                target = targetCode,
                                source = sourceCode
                            )
                        }

                        println("Translated: '${translation.translatedText}' (Detected Source: ${translation.sourceLanguage?.code})")
                        val actualSourceLang = translation.sourceLanguage?.code?.let { LanguagesChat.fromCode(it) } ?: selectedFromLanguage

                        chatItems.add(
                            ChatItem(
                                original = recognizedText,
                                translated = translation.translatedText,
                                isUser = true,
                                originalLang = actualSourceLang
                            )
                        )

                    } catch (e: Exception) {
                        e.printStackTrace()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                "Translation failed: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } finally {
                        isTranslating = false
                    }
                }
            } else {
                Toast.makeText(context, "No speech recognized", Toast.LENGTH_SHORT).show()
            }
        } else if (result.resultCode != android.app.Activity.RESULT_CANCELED) {
            Toast.makeText(context, "Speech recognition failed. Result Code: ${result.resultCode}", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(context, "Speech recognition cancelled.", Toast.LENGTH_SHORT).show()
        }
    }
    val rightSpeechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val data: Intent? = result.data
            val recognizedTextList = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val recognizedText = recognizedTextList?.get(0) ?: ""

            if (recognizedText.isNotBlank()) {
                coroutineScope.launch {
                    isTranslating = true
                    try {
                        val sourceCode = Language.values().firstOrNull { it.code == selectedToLanguage.code }
                            ?: run {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "Source language ${selectedToLanguage.name} not supported by translator for detection.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                isTranslating = false
                                return@launch
                            }
                        val targetCode = if (selectedFromLanguage == LanguagesChat.AUTO) {
                            Language.values().firstOrNull { it.code == selectedFromLanguage.code }
                                ?: Language.AUTO.also {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "User's language not supported by translator, defaulting target to English.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                        } else {
                            Language.values().firstOrNull { it.code == selectedFromLanguage.code }
                                ?: run {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "User's language ${selectedFromLanguage.name} not supported as translation target.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    isTranslating = false
                                    return@launch
                                }
                        }

                        println("Translating (Right Mic): '$recognizedText' from ${sourceCode.code} to ${targetCode.code}")

                        val translation = withContext(Dispatchers.IO) {
                            translator.translate(
                                text = recognizedText,
                                target = targetCode,
                                source = sourceCode
                            )
                        }

                        println("Translated: '${translation.translatedText}' (Detected Source: ${translation.sourceLanguage?.code})")


                        val actualSourceLang = translation.sourceLanguage?.code?.let { LanguagesChat.fromCode(it) } ?: selectedToLanguage

                        chatItems.add(
                            ChatItem(
                                original = recognizedText,
                                translated = translation.translatedText,
                                isUser = false,
                                originalLang = actualSourceLang
                            )
                        )

                    } catch (e: Exception) {
                        e.printStackTrace()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                "Translation failed: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } finally {
                        isTranslating = false
                    }
                }
            } else {
                Toast.makeText(context, "No speech recognized", Toast.LENGTH_SHORT).show()
            }
        } else if (result.resultCode != android.app.Activity.RESULT_CANCELED) {
            Toast.makeText(context, "Speech recognition failed. Result Code: ${result.resultCode}", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(context, "Speech recognition cancelled.", Toast.LENGTH_SHORT).show()
        }
    }



    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(context, "Permission granted. Speak now...", Toast.LENGTH_SHORT).show()
            when (lastMicClickedIsLeft) {
                true -> {
                    val languageCode = selectedFromLanguage.code
                    val intent = getSpeechRecognizerIntent(
                        prompt = "Speak in ${selectedFromLanguage.name}",
                        languageCode = languageCode
                    )
                    if (intent.resolveActivity(context.packageManager) != null) {
                        leftSpeechRecognizerLauncher.launch(intent)
                    } else {
                        Toast.makeText(
                            context,
                            "Speech input not supported on this device",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                false -> {
                    val languageCode = selectedToLanguage.code
                    val intent = getSpeechRecognizerIntent(
                        prompt = "Speak in ${selectedToLanguage.name}",
                        languageCode = languageCode
                    )
                    if (intent.resolveActivity(context.packageManager) != null) {
                        rightSpeechRecognizerLauncher.launch(intent)
                    } else {
                        Toast.makeText(
                            context,
                            "Speech input not supported on this device",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                null -> {
                    Toast.makeText(context, "Mic click state lost, cannot start speech.", Toast.LENGTH_SHORT).show()
                }
            }
            lastMicClickedIsLeft = null
        } else {
            Toast.makeText(
                context,
                "Microphone permission is required for voice input",
                Toast.LENGTH_SHORT
            ).show()
            lastMicClickedIsLeft = null
        }
    }

    val requestSpeechRecognition = { isLeftMic: Boolean ->
        lastMicClickedIsLeft = isLeftMic
        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Voice Conversation",
                        style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF003366))
            )
        },
        bottomBar = {}

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                if (chatItems.isEmpty() && !isTranslating) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.mic),
                            contentDescription = "Mic",
                            tint = Color.LightGray,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Tap the mic to start a conversation",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = 16.dp,
                            bottom = 120.dp,
                            start = 16.dp,
                            end = 16.dp
                        ),
                        reverseLayout = true,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(chatItems.reversed()) { chat ->
                            ChatBubble(
                                chat = chat,
                                tts = tts,
                                clipboardManager = clipboardManager
                            )
                        }
                    }
                }
            }

            if (isTranslating) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 4.dp
                    )
                }
            }

            VoiceTranslatorBar(
                selectedFromLanguage = selectedFromLanguage,
                selectedToLanguage = selectedToLanguage,
                fromExpanded = fromExpanded,
                toExpanded = toExpanded,
                sourceLanguagesList = sourceLanguagesList,
                targetLanguagesList = targetLanguagesList.first(),
                onFromExpandChange = { fromExpanded = it },
                onToExpandChange = { toExpanded = it },
                onFromLanguageSelect = { selectedFromLanguage = it },
                onToLanguageSelect = { selectedToLanguage = it },
                onSwapLanguages = {
                    if (selectedFromLanguage != LanguagesChat.AUTO) {
                        val temp = selectedFromLanguage
                        selectedFromLanguage = selectedToLanguage
                        selectedToLanguage = temp
                    } else {
                        Toast.makeText(
                            context,
                            "Cannot swap when source is Auto Detect",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                onLeftMicClick = { requestSpeechRecognition(true) },
                onRightMicClick = { requestSpeechRecognition(false) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 90.dp)
            )
        }
    }
}

@Composable
fun VoiceTranslatorBar(
    selectedFromLanguage: LanguagesItem,
    selectedToLanguage: LanguagesItem,
    fromExpanded: Boolean,
    toExpanded: Boolean,
    sourceLanguagesList: List<LanguagesItem>,
    targetLanguagesList: List<LanguagesItem>,
    onFromExpandChange: (Boolean) -> Unit,
    onToExpandChange: (Boolean) -> Unit,
    onFromLanguageSelect: (LanguagesItem) -> Unit,
    onToLanguageSelect: (LanguagesItem) -> Unit,
    onSwapLanguages: () -> Unit,
    onLeftMicClick: () -> Unit,
    onRightMicClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBFE)),
            elevation = CardDefaults.elevatedCardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onFromExpandChange(true) },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(
                        onClick = onLeftMicClick,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(0XFF003366)
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.mic),
                            contentDescription = "Speak ${selectedFromLanguage.name}",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = selectedFromLanguage.name ?: "",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    DropdownMenu(
                        expanded = fromExpanded,
                        onDismissRequest = { onFromExpandChange(false) }
                    ) {
                        sourceLanguagesList.forEach { language ->
                            DropdownMenuItem(
                                text = { Text("${language.flag.orEmpty()} ${language.name}") },
                                onClick = {
                                    onFromLanguageSelect(language)
                                    onFromExpandChange(false)
                                }
                            )
                        }
                    }
                }

                Image(
                    painter = painterResource(R.drawable.swap),
                    contentDescription = "Swap languages",
                    modifier = Modifier
                        .size(36.dp)
                        .clickable(onClick = onSwapLanguages)
                        .padding(horizontal = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onToExpandChange(true) },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = selectedToLanguage.name ?: "",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        textAlign = TextAlign.End
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = onRightMicClick,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(0XFFFF6600)
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.mic),
                            contentDescription = "Speak ${selectedToLanguage.name}",
                            tint = Color.White
                        )
                    }

                    DropdownMenu(
                        expanded = toExpanded,
                        onDismissRequest = { onToExpandChange(false) }
                    ) {
                        targetLanguagesList.forEach { language ->
                            DropdownMenuItem(
                                text = { Text("${language.flag.orEmpty()} ${language.name}") },
                                onClick = {
                                    onToLanguageSelect(language)
                                    onToExpandChange(false)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ChatBubble(
    chat: ChatItem,
    tts: TextToSpeech,
    clipboardManager: ClipboardManager
) {
    val context = LocalContext.current
    val isUser = chat.isUser
    val bubbleColor = if (isUser) Color(0xFFFFFBFE) else Color(0xFFFFFBFE)
    val alignment = if (isUser) Arrangement.End else Arrangement.Start
    val horizontalAlign = if (isUser) Alignment.End else Alignment.Start

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = alignment
    ) {
        Column(
            horizontalAlignment = horizontalAlign
        ) {
            Column(
                modifier = Modifier
                    .background(bubbleColor, shape = RoundedCornerShape(16.dp))
                    .padding(12.dp)
                    .widthIn(max = 300.dp),
                horizontalAlignment = horizontalAlign
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f, fill = false)) {
                        Text(
                            text = chat.original,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            ),
                        )
                        if (chat.originalLang != null && chat.originalLang != LanguagesChat.AUTO) {
                            Text(
                                text = "(${chat.originalLang.name})",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                    IconButton(
                        onClick = { clipboardManager.setText(AnnotatedString(chat.original)) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.copy),
                            contentDescription = "Copy Original",
                            tint = Color(0xFF003366)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Gray.copy(alpha = 0.3f)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f, fill = false)) {
                        Text(
                            text = chat.translated,
                            color = Color(0xFFFF6B00),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }

                    Row {
                        IconButton(
                            onClick = {
                                if (tts.engines.isEmpty()) {
                                    Toast.makeText(
                                        context,
                                        "No TTS engine found",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@IconButton
                                }
                                if (tts.isSpeaking) {
                                    tts.stop()
                                }
                                val result =
                                    tts.speak(chat.translated, TextToSpeech.QUEUE_FLUSH, null, null)
                                if (result == TextToSpeech.ERROR) {
                                    Toast.makeText(
                                        context,
                                        "Error speaking text",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.speaker),
                                contentDescription = "Speak Translated",
                                tint = Color(0xFF003366)
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        IconButton(
                            onClick = { clipboardManager.setText(AnnotatedString(chat.translated)) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.copy),
                                contentDescription = "Copy Translated",
                                tint = Color(0xFF003366)
                            )
                        }
                    }
                }
            }
        }
    }
}


private fun getTranslatorLanguage(languageItem: LanguagesItem): Language? {
    if (languageItem == LanguagesChat.AUTO) return Language.AUTO
    return Language.values().firstOrNull { it.code == languageItem.code }
}
