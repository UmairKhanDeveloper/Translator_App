package com.example.translatorapp.screen

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.texttranslater.domain.model.language.LanguagesItem
import com.example.translatorapp.R
import com.example.translatorapp.db.MainViewModel
import com.example.translatorapp.db.NoteDataBase
import com.example.translatorapp.db.Repository
import kotlinx.coroutines.launch
import me.bush.translator.Language
import me.bush.translator.Translator
import java.util.Locale



@Composable
fun SettingItem(icon: Int, text: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                onClick()
            }
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = text,
            modifier = Modifier.size(24.dp),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, color = Color.Black, fontSize = 20.sp)
    }
}


data class ScreenTranslation(
    val translatedText: String,
    val pronunciation: String? = null,
    val sourceLanguage: LanguagesItem? = null
)

object Languages {
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




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current
    val translateDatabase = remember { NoteDataBase.getDataBase(context) }
    val repository = remember { Repository(translateDatabase) }
    val viewModel = remember { MainViewModel(repository) }
     val notesList by viewModel.allTranslate.observeAsState(initial = emptyList())

    var textFields by remember { mutableStateOf("") }
    var showTranslatedCard by remember { mutableStateOf(false) }
    var translatedText by remember { mutableStateOf("") }
    var isTranslating by remember { mutableStateOf(false) }


    var fromExpanded by remember { mutableStateOf(false) }
    var toExpanded by remember { mutableStateOf(false) }


    val translator = remember { Translator() }
    val coroutineScope = rememberCoroutineScope()


    val languages = Languages.allLanguages
    val sourceLanguagesList =
        remember { mutableStateListOf(Languages.AUTO).apply { addAll(languages) } }
    val targetLanguagesList = languages


    var selectedFromLanguage by remember { mutableStateOf(Languages.AUTO) }
    var selectedToLanguage by remember { mutableStateOf(languages.first { it.code == "es" }) }

    val clipboardManager = LocalClipboardManager.current



    val speechRecognitionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val spokenText: ArrayList<String>? =
                    result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                spokenText?.let {
                    if (it.isNotEmpty()) {
                        textFields = it[0]
                    }
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(context, "Speech input cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    )


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, if (selectedFromLanguage != Languages.AUTO) selectedFromLanguage.code else Locale.getDefault().language)
                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
                }
                try {
                    speechRecognitionLauncher.launch(speechIntent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(context, "Speech recognizer not available on this device", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Microphone permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    )



    val tts = remember {
        TextToSpeech(context, null).apply {
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.translate),
                        contentDescription = "",
                        modifier = Modifier.size(width = 99.dp, height = 88.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Translate on the Go",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(40.dp))

                    Column {
                        SettingItem(icon = R.drawable.group_share, "Share App") {
                            val packageName = context.packageName
                            val appLink =
                                "https://play.google.com/store/apps/details?id=$packageName"

                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "Check out this app!")
                                putExtra(Intent.EXTRA_TEXT, "Hey! Try this app: $appLink")
                            }
                            context.startActivity(Intent.createChooser(intent, "Share App via"))
                        }
                        SettingItem(icon = R.drawable.star, text = "Rate Us") {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("market://details?id=" + context.packageName)
                                setPackage("com.android.vending")
                            }
                            try {
                                context.startActivity(intent)
                            } catch (e: ActivityNotFoundException) {
                                Toast.makeText(context, "Play Store not found", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        SettingItem(icon = R.drawable.shield_done, "Privacy Policy") {
                            Toast.makeText(context, "Privacy Policy Clicked", Toast.LENGTH_SHORT).show()
                        }
                        SettingItem(icon = R.drawable.feedback, text = "Feedback") {
                            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:")
                                putExtra(Intent.EXTRA_EMAIL, arrayOf("support@yourapp.com"))
                                putExtra(Intent.EXTRA_SUBJECT, "App Feedback")
                                putExtra(Intent.EXTRA_TEXT, "App Version: ${context.packageManager.getPackageInfo(context.packageName, 0).versionName}\n\nFeedback:\n") // Include app version
                            }
                            try {
                                context.startActivity(Intent.createChooser(emailIntent, "Send Feedback"))
                            } catch (e: ActivityNotFoundException) {
                                Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        SettingItem(icon = R.drawable.history, text = "History") {
                            navController.navigate("history_screen")
                        }

                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Language Translator",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            modifier = Modifier.padding(start = 20.dp)
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.menu),
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0XFF003366))
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
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
                                selectedFromLanguage.flag?.let {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(Color.Transparent),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = it,
                                            fontSize = 20.sp
                                        )
                                    }
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
                                        isTranslating = false
                                    } else {
                                        Toast.makeText(context, "Cannot swap when source is Auto Detect", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        )

                        Box {
                            Row(
                                modifier = Modifier.clickable { toExpanded = true },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                selectedToLanguage.flag?.let {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(Color.Transparent),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = it,
                                            fontSize = 20.sp
                                        )
                                    }
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

                Spacer(modifier = Modifier.height(20.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBFE)),
                    elevation = CardDefaults.elevatedCardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            selectedFromLanguage.name?.let {
                                Text(
                                    text = it,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0XFF003366)
                                )
                            }
                            IconButton(
                                onClick = {
                                    textFields = ""
                                    translatedText = ""
                                    showTranslatedCard = false
                                    isTranslating = false
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.close),
                                    contentDescription = "Clear Text",
                                    tint = Color.Gray
                                )
                            }
                        }
                        TextField(
                            value = textFields,
                            onValueChange = { textFields = it },
                            placeholder = {
                                Text(
                                    text = "Enter text here...",
                                    color = Color(0xFFAAA5B3)
                                )
                            },
                            textStyle = TextStyle(
                                color = Color.Black,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Start
                            ),
                            singleLine = false,
                            maxLines = 3,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color.Gray
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 100.dp, max = 160.dp)
                                .verticalScroll(rememberScrollState())
                                .padding(vertical = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    when (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)) {
                                        PackageManager.PERMISSION_GRANTED -> {
                                            val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                                putExtra(RecognizerIntent.EXTRA_LANGUAGE, if (selectedFromLanguage != Languages.AUTO) selectedFromLanguage.code else Locale.getDefault().language)
                                                putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...") // Optional: add a prompt
                                            }
                                            try {
                                                speechRecognitionLauncher.launch(speechIntent)
                                            } catch (e: ActivityNotFoundException) {
                                                Toast.makeText(context, "Speech recognizer not available", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        PackageManager.PERMISSION_DENIED -> {
                                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                        }
                                        else -> {
                                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                        }
                                    }
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
                            Button(
                                onClick = {
                                    if (textFields.isNotBlank() && !isTranslating) {
                                        isTranslating = true
                                        showTranslatedCard = false
                                        translatedText = ""

                                        coroutineScope.launch {
                                            val translationResult =
                                                selectedToLanguage.code?.let { Language(it) }?.let {
                                                    selectedFromLanguage.code?.let { Language(it) }
                                                        ?.let { it1 ->
                                                            translator.translate(
                                                                text = textFields,
                                                                target = it,
                                                                source = it1
                                                            )
                                                        }
                                                }


                                            if (translationResult != null) {
                                                translatedText = translationResult.translatedText
                                            }
                                            showTranslatedCard = true
                                            isTranslating = false



                                        }
                                    }

                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF6600)
                                ),
                                shape = CircleShape,
                                enabled = textFields.isNotBlank() && !isTranslating
                            ) {
                                if (isTranslating) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text(
                                        text = "Translate",
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                if (showTranslatedCard && translatedText.isNotBlank()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBFE)),
                        elevation = CardDefaults.elevatedCardElevation(2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                selectedToLanguage.name?.let {
                                    Text(
                                        text = it,
                                        color = Color(0xFF1A1A1A),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp
                                    )
                                }
                                Icon(
                                    painter = painterResource(R.drawable.speaker),
                                    contentDescription = "Speak Text",
                                    tint = Color(0XFF003366),
                                    modifier = Modifier.clickable {
                                        if (translatedText.isNotBlank()) {
                                            val targetLocale = selectedToLanguage.code?.let {
                                                try {
                                                    Locale(it)
                                                } catch (e: Exception) {
                                                    Locale.getDefault()
                                                }
                                            } ?: Locale.getDefault()

                                            val result = tts.setLanguage(targetLocale)

                                            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                                Toast.makeText(context, "Language not supported for speech", Toast.LENGTH_SHORT).show()
                                                tts.language = Locale.getDefault()
                                            }


                                            tts.speak(
                                                translatedText,
                                                TextToSpeech.QUEUE_FLUSH,
                                                null,
                                                null
                                            )
                                        }
                                    }
                                )

                            }

                            Text(
                                text = translatedText,
                                color = Color.Black,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .weight(1f)
                                    .verticalScroll(rememberScrollState())
                                    .padding(vertical = 8.dp)
                                    .clickable {
                                        if (translatedText.isNotBlank()) {
                                            clipboardManager.setText(AnnotatedString(translatedText))
                                            Toast.makeText(
                                                context,
                                                "Copied to clipboard",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.copy),
                                    contentDescription = "Copy",
                                    modifier = Modifier
                                        .padding(end = 16.dp)
                                        .clickable {
                                            if (translatedText.isNotBlank()) {
                                                clipboardManager.setText(AnnotatedString(translatedText))
                                                Toast.makeText(
                                                    context,
                                                    "Copied to clipboard",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        },
                                    tint = Color(0XFF003366)
                                )
                                Icon(
                                    painter = painterResource(R.drawable.share),
                                    contentDescription = "Share",
                                    modifier = Modifier
                                        .padding(end = 16.dp)
                                        .clickable {
                                            if (translatedText.isNotBlank()) {
                                                val intent = Intent().apply {
                                                    action = Intent.ACTION_SEND
                                                    putExtra(Intent.EXTRA_TEXT, translatedText)
                                                    type = "text/plain"
                                                }
                                                val shareIntent =
                                                    Intent.createChooser(intent, "Share via")
                                                context.startActivity(shareIntent)
                                            }
                                        },
                                    tint = Color(0XFF003366)
                                )
                                Icon(
                                    painter = painterResource(R.drawable.star),
                                    contentDescription = "Favorite",
                                    modifier = Modifier.clickable {

                                    },
                                    tint = Color(0XFF003366)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

