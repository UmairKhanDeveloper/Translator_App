package com.example.translatorapp.screen

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.texttranslater.domain.model.language.LanguagesItem
import com.example.translatorapp.R
import me.bush.translator.Language
import me.bush.translator.Translation
import me.bush.translator.Translator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    var textFields by remember { mutableStateOf("") }
    var showTranslatedCard by remember { mutableStateOf(false) }
    var translatedText by remember { mutableStateOf("") }

    var fromExpanded by remember { mutableStateOf(false) }
    var toExpanded by remember { mutableStateOf(false) }

    val translator = Translator()
    val translation = translator.translate("Bush's translator is so cool!", Language.RUSSIAN, Language.AUTO)
    println(translation.translatedText)
    println(translation.pronunciation)
    println(translation.sourceLanguage)

    Language.ENGLISH == Language("english") == Language("en") == Language("eng")

    Translator translator = new Translator();
    Translation translation = translator.translateBlocking("...", Language.Companion.INSTANCE.invoke("spanish"));
    translation.getTranslatedText();

    val languages = listOf(
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
        LanguagesItem(name = "Indonesian", localName = "Bahasa Indonesia", flag = "🇮🇩",code = "id"),
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

    var selectedFromLanguage by remember { mutableStateOf(languages.first { it.code == "en" }) }
    var selectedToLanguage by remember { mutableStateOf(languages.first { it.code == "es" }) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "language Translator",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                },
                navigationIcon = {
                    Icon(
                        painter = painterResource(R.drawable.menu),
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0XFF003366))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .background(color = Color.White),
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
                                languages.forEach { language ->
                                    DropdownMenuItem(
                                        text = { Text("${language.flag} ${language.name}") },
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
                                val temp = selectedFromLanguage
                                selectedFromLanguage = selectedToLanguage
                                selectedToLanguage = temp
                                textFields = ""
                                translatedText = ""
                                showTranslatedCard = false
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
                                languages.forEach { language ->
                                    DropdownMenuItem(
                                        text = { Text("${language.flag} ${language.name}") },
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
                        IconButton(onClick = { textFields = "" }) {
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
                        maxLines = 5,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Color.Gray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            disabledContainerColor = Color.Transparent,
                            errorContainerColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { },
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
                                translatedText =
                                    "Translated: ${textFields.take(50)}${if (textFields.length > 50) "..." else ""}" // Simple placeholder
                                showTranslatedCard = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFF6600)
                            ),
                            shape = CircleShape,
                            // Disable button if input is empty (optional)
                            enabled = textFields.isNotBlank()
                        ) {
                            Text(
                                text = "Translate",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            if (showTranslatedCard) {
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
                                modifier = Modifier.clickable { }
                            )
                        }

                        Text(
                            text = translatedText,
                            color = Color.Black,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 8.dp)
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
                                    .clickable { },
                                tint = Color(0XFF003366)
                            )
                            Icon(
                                painter = painterResource(R.drawable.share),
                                contentDescription = "Share",
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .clickable {},
                                tint = Color(0XFF003366)
                            )
                            Icon(
                                painter = painterResource(R.drawable.star),
                                contentDescription = "Favorite",
                                modifier = Modifier.clickable { },
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