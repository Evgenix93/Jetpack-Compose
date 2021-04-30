package com.example.leroymerlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.leroymerlin.ui.theme.LeroyMerlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
         DefaultPreview()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LeroyMerlinTheme {
        Scaffold(topBar = {
            TopAppBar(modifier = Modifier.height(110.dp)) {
                Column() {
                    Text("Поиск товаров", fontSize = 25.sp)
                    Spacer(modifier = Modifier.size(5.dp))
                    FilledTextField()
                }
            }
        }, bottomBar = {
            BottomNavigation() {
                repeat(5) {
                    when (it) {
                        0 -> MenuItem("Главная", R.drawable.ic_outline_search_24)
                        1 -> MenuItem("Мой список", R.drawable.ic_baseline_bookmark_border_24)
                        2 -> MenuItem("Магазины", R.drawable.ic_outline_storefront_24)
                        3 -> MenuItem("Профиль", R.drawable.ic_outline_person_24)
                        4 -> MenuItem("Корзина", R.drawable.ic_outline_local_grocery_store_24)
                    }
                }
            }
        }) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Spacer(Modifier.size(5.dp))
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    Card(
                        modifier = Modifier
                            .size(120.dp)
                            .padding(5.dp),
                        backgroundColor = Color(98, 0, 238)
                    ) {
                        Column() {
                            Box(modifier = Modifier.weight(1f)) { Text("Каталог") }
                            Box(
                                modifier = Modifier
                                    .weight(2f)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.BottomEnd
                            ) {
                                Image(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(10.dp),
                                    painter = painterResource(R.drawable.ic_baseline_menu_24),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                    repeat(5) {
                        when (it) {
                            0 -> MakeCard("Сад", R.drawable.plant)
                            1 -> MakeCard("Освещение", R.drawable.lamp)
                            2 -> MakeCard("Инструменты", R.drawable.drill)
                            3 -> MakeCard("Стройматериалы", R.drawable.brick2)
                            4 -> MakeCard("Декор", R.drawable.decor)
                        }
                    }
                    Card(
                        modifier = Modifier
                            .size(120.dp)
                            .padding(5.dp),
                        backgroundColor = Color(200, 200, 200)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                modifier = Modifier.size(50.dp),
                                painter = painterResource(R.drawable.ic_baseline_navigate_next_24),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                            Text(text = "Посмотреть еще", textAlign = TextAlign.Center)
                        }
                    }
                }
                Spacer(Modifier.size(15.dp))
                Text("Предложение ограничено", modifier = Modifier.padding(5.dp), fontSize = 20.sp)
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    repeat(7) {
                        MakeProduct("Product", "Price", R.drawable.ic_baseline_android_24)
                    }
                }
                Spacer(Modifier.size(15.dp))
                Text("Лучшая цена", modifier = Modifier.padding(5.dp), fontSize = 20.sp)
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    repeat(7) {
                        MakeProduct("Product", "Price", R.drawable.ic_baseline_android_24)
                    }
                }
                Spacer(Modifier.size(65.dp))
            }
        }
    }
}

@Composable
fun MakeCard(text: String, image: Int) {
    Card(
        modifier = Modifier
            .size(120.dp)
            .padding(5.dp),
        backgroundColor = Color(200, 200, 200)
    ) {

        Column() {
            Box(modifier = Modifier.weight(1f)) { Text(text) }
            Box(
                modifier = Modifier
                    .weight(2f)
                    .requiredWidth(140.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    modifier = Modifier.size(55.dp),
                    painter = painterResource(image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }

    }

}

@Composable
fun MakeProduct(text: String, price: String, image: Int) {
    Column(
        Modifier
            .padding(5.dp)
            .background(color = Color(200, 200, 200))
    ) {
        Image(
            modifier = Modifier.size(150.dp), painter = painterResource(image),
            contentDescription = null
        )
        Text(text, fontSize = 18.sp)
        Text(price, fontSize = 13.sp)
    }
}

@Composable
fun MenuItem(text: String, image: Int) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .clickable() { },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier.size(33.dp),
            painter = painterResource(image),
            contentDescription = null
        )
        Text(text, fontSize = 15.sp)
    }
}

@Composable
fun FilledTextField() {
    var text by remember { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = { text = it },
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            backgroundColor = Color.White
        )
    )
}
