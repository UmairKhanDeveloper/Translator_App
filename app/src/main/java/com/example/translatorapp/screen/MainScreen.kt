package com.example.translatorapp.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.translatorapp.R

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavController) {
    var textFields by remember { mutableStateOf("") }
    var showTranslatedCard by remember { mutableStateOf(false) }

    var selectedFrom by remember { mutableStateOf(countries[0]) }
    var selectedTo by remember { mutableStateOf(countries[1]) }

    var fromExpanded by remember { mutableStateOf(false) }
    var toExpanded by remember { mutableStateOf(false) }



    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "language Translator",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White, modifier = Modifier.padding(start = 20.dp)
                )

            }, navigationIcon = {
                Icon(
                    painter = painterResource(R.drawable.menu),
                    contentDescription = "",
                    tint = Color.White
                )
            }, actions = {

            }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0XFF003366))
        )
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding(), bottom = 80.dp)
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))
            Card(
                modifier = Modifier
                    .width(320.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBFE)),
                elevation = CardDefaults.elevatedCardElevation(1.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DropdownMenu(
                        expanded = fromExpanded,
                        onDismissRequest = { fromExpanded = false }
                    ) {
                        countries.forEach { country ->
                            DropdownMenuItem(
                                text = { Text("${country.flag} ${country.language}") },
                                onClick = {
                                    selectedFrom = country
                                    fromExpanded = false
                                }
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = toExpanded,
                        onDismissRequest = { toExpanded = false }
                    ) {
                        countries.forEach { country ->
                            DropdownMenuItem(
                                text = { Text("${country.flag} ${country.language}") },
                                onClick = {
                                    selectedTo = country
                                    toExpanded = false
                                }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.clickable { fromExpanded = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray.copy(alpha = 0.1f))
                        ) {
                            Image(
                                painter = painterResource(getFlagResId(selectedFrom.code)),
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = selectedFrom.language,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }

                    // 🔄 Swap Icon (optional action)
                    Image(
                        painter = painterResource(R.drawable.swap),
                        contentDescription = "",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                val temp = selectedFrom
                                selectedFrom = selectedTo
                                selectedTo = temp
                            }
                    )

                    Row(
                        modifier = Modifier.clickable { toExpanded = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray.copy(alpha = 0.1f))
                        ) {
                            Image(
                                painter = painterResource(getFlagResId(selectedTo.code)),
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = selectedTo.language,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .height(220.dp)
                    .width(320.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBFE)),
                elevation = CardDefaults.elevatedCardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 10.dp, end = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Text(
                            text = "English",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0XFF003366)
                        )
                        IconButton(onClick = { }) {
                            Icon(
                                painter = painterResource(id = R.drawable.close),
                                contentDescription = "Close",
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
                            unfocusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))
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
                                showTranslatedCard=true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6600)),
                            shape = CircleShape,
                            modifier = Modifier

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
            if (showTranslatedCard){
                Card(
                    modifier = Modifier
                        .padding(10.dp)
                        .height(220.dp)
                        .width(320.dp),
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
                            Text(
                                text = "Spanish",
                                color = Color(0xFF1A1A1A),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                            Icon(
                                painter = painterResource(R.drawable.speaker),
                                contentDescription = "Speaker Icon",
                                tint = Color(0XFF003366)
                            )
                        }


                        Text(
                            text = "¿Hola como estas?",
                            color = Color.Black,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding( bottom = 20.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.copy),
                                contentDescription = "Copy",
                                modifier = Modifier.padding(end = 10.dp),
                                tint =Color(0XFF003366)
                            )
                            Icon(
                                painter = painterResource(R.drawable.share),
                                contentDescription = "Share",
                                modifier = Modifier.padding(end = 10.dp),
                                tint = Color(0XFF003366)
                            )
                            Icon(
                                painter = painterResource(R.drawable.star),
                                contentDescription = "Star",
                                tint = Color(0XFF003366)
                            )
                        }
                    }
                }
            }



        }


    }

}


data class Country(
    val name: String,
    val code: String,
    val flag: String,
    val language: String
)

val countries = listOf(
    Country("United States", "US", "🇺🇸", "English"),
    Country("Pakistan", "PK", "🇵🇰", "Urdu"),
    Country("India", "IN", "🇮🇳", "Hindi"),
    Country("United Kingdom", "GB", "🇬🇧", "English"),
    Country("France", "FR", "🇫🇷", "French"),
    Country("Germany", "DE", "🇩🇪", "German"),
    Country("China", "CN", "🇨🇳", "Chinese"),
    Country("Japan", "JP", "🇯🇵", "Japanese"),
    Country("Saudi Arabia", "SA", "🇸🇦", "Arabic"),
    Country("Russia", "RU", "🇷🇺", "Russian"),
    Country("Brazil", "BR", "🇧🇷", "Portuguese"),
    Country("Canada", "CA", "🇨🇦", "English / French"),
    Country("Spain", "ES", "🇪🇸", "Spanish"),
    Country("Italy", "IT", "🇮🇹", "Italian"),
    Country("Turkey", "TR", "🇹🇷", "Turkish"),
    Country("South Korea", "KR", "🇰🇷", "Korean"),
    Country("Bangladesh", "BD", "🇧🇩", "Bengali"),
    Country("Afghanistan", "AF", "🇦🇫", "Pashto / Dari"),
    Country("Indonesia", "ID", "🇮🇩", "Indonesian"),
    Country("Iran", "IR", "🇮🇷", "Persian")
)


@Composable
fun CountryListScreen() {
    LazyColumn {
        items(countries) { country ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 🇺🇸 Flag Emoji
                Text(
                    text = country.flag,
                    fontSize = 28.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = country.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Language: ${country.language}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun getFlagResId(code: String): Int {
    return when (code.lowercase()) {
        "us" -> R.drawable.flags_us
        "pk" -> R.drawable.flags_us
        "in" -> R.drawable.flags_us
        "es" -> R.drawable.flags_us
        "fr" -> R.drawable.flags_us
        "de" -> R.drawable.spain
        "sa" -> R.drawable.spain
        "ru" -> R.drawable.spain
        "br" -> R.drawable.spain
        else -> R.drawable.spain
    }
}
