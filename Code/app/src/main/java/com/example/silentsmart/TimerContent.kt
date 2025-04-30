package com.example.silentsmart

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.silentsmart.database.entity.Temporizador

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimerContent(viewModel: MainViewModel) {
    val temporizadores = viewModel.temporizadores.collectAsState(initial = emptyList()).value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TimerSection()
        Spacer(modifier = Modifier.height(4.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 20.dp),
            modifier = Modifier.fillMaxWidth()
                //.padding(horizontal = 8.dp)
        ) {
            items(temporizadores.orEmpty().filterNotNull(), key = { it.id }) { temporizador ->
                TimerCard(temporizador)
            }
        }
    }
}

@Composable
fun TimerSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.LightGray)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "1 : 50 : 30", // Aquí puedes mostrar el temporizador grande
                fontSize = 32.sp,
                color = Color.Black
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { /* Acción para parar */ }) {
                    Icon(Icons.Default.Clear, contentDescription = "Parar")
                }
                IconButton(onClick = { /* Acción para vibración */ }) {
                    Icon(Icons.Default.Clear, contentDescription = "Vibración")
                }
                IconButton(onClick = { /* Acción para sonido */ }) {
                    Icon(Icons.Default.Clear, contentDescription = "Sonido")
                }
            }
        }
    }
}

@Composable
fun TimerCard(temporizador: Temporizador) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.7f),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${temporizador.horas}h ${temporizador.minutos}m",
                fontSize = 20.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Favorito */ }) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder, // Cambia a Favorite si es favorito
                        contentDescription = "Favorito"
                    )
                }
                IconButton(onClick = { /* Play */ }) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Iniciar")
                }
                IconButton(onClick = { /* Modo */ }) {
                    Icon(Icons.Default.Clear, contentDescription = "Modo") // Cambia el icono según modo
                }
            }
        }
    }
}
