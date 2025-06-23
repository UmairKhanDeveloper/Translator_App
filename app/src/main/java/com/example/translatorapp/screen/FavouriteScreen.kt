///package com.example.translatorapp.screen

import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.translatorapp.db.fav_db.FavMainViewModel
import com.example.translatorapp.db.fav_db.FavRepository
import com.example.translatorapp.db.fav_db.Favorite
import com.example.translatorapp.db.fav_db.FavoriteDatabase
import com.example.translatorapp.screen.Languages

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteScreen(navController: NavController) {
    val context = LocalContext.current


    val db = remember { FavoriteDatabase.getDatabase(context) }
    val repository1 = remember { FavRepository(db) }
    val viewModel1 = remember { FavMainViewModel(repository1) }

    val favorites by viewModel1.allFavorite.observeAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }



    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Favourite",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }, actions = {
                    IconButton(onClick = {
                      showDialog=true
                    }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0XFF003366))
            )
        }
    ) {
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text("Confirm Delete")
                },
                text = {
                    Text("Are you sure you want to delete all favorite items?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            favorites.forEach {
                                viewModel1.delete(
                                    Favorite(
                                        id = it.id,
                                        simpleText = it.simpleText,
                                        translateText = it.translateText,
                                        languageCodeSimpleText1 = it.languageCodeSimpleText1,
                                        languageCodeTranslatedText1 = it.languageCodeTranslatedText1

                                    )
                                )
                            }
                            showDialog = false
                        }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                        }
                    ) {
                        Text("No")
                    }
                }
            )
        }
        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding()),
                contentAlignment = Alignment.Center
            ) {
                Text("No favorites found", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding(), bottom = 84.dp)
            ) {
                items(favorites) { item ->
                    FavoriteItem(favorite = item)
                }
            }
        }
    }
}


@Composable
fun FavoriteItem(favorite: Favorite) {
    val languageItem = Languages.fromCode(favorite.languageCodeSimpleText1)
    val languageTranslated = Languages.fromCode(favorite.languageCodeTranslatedText1)


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (languageItem != null) {
                    languageItem.code?.let {
                        Text(
                            text = it.uppercase(),
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF003366),
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = favorite.simpleText,
                    fontSize = 14.sp,
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Favorite",
                    tint = Color(0xFF003366),
                    modifier = Modifier
                        .size(30.dp)
                        .padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (languageTranslated != null) {
                    languageTranslated.code?.let {
                        Text(
                            text = it.uppercase(),
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFEF6C00),
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = favorite.translateText,
                    fontSize = 14.sp,
                    color = Color(0xFFEF6C00),
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


