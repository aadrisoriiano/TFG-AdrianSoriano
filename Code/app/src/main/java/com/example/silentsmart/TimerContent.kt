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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.silentsmart.R
import com.example.silentsmart.database.entity.Temporizador
import com.example.silentsmart.ui.theme.WdxFontFamily

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimerContent(viewModel: MainViewModel) {
    val context = LocalContext.current
    val temporizadores = viewModel.temporizadores.collectAsState(initial = emptyList()).value
    val activeTimer = viewModel.activeTimer.collectAsState().value
    val remainingSeconds = viewModel.remainingSeconds.collectAsState().value
    val isTimerRunning = viewModel.isTimerRunning.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TimerSection(
            activeTimer = activeTimer,
            remainingSeconds = remainingSeconds,
            isRunning = isTimerRunning,
            onPauseResume = { viewModel.pauseOrResumeTimer(context) }
        )
        Spacer(modifier = Modifier.height(4.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(temporizadores.orEmpty().filterNotNull(), key = { it.id }) { temporizador ->
                TimerCard(
                    temporizador = temporizador,
                    onPlay = { viewModel.startTimer(temporizador, context) }
                )
            }
        }
    }
}

@Composable
fun TimerSection(
    activeTimer: Temporizador?,
    remainingSeconds: Int,
    isRunning: Boolean,
    onPauseResume: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(30.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.LightGray)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val display = if (activeTimer != null)
                String.format(
                    "%dh %02dm %02ds",
                    remainingSeconds / 3600,
                    (remainingSeconds % 3600) / 60,
                    remainingSeconds % 60
                )
            else "00h 00m 00s"
            Text(
                text = display,
                fontSize = 50.sp,
                color = Color.Black,
                fontFamily = WdxFontFamily,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .width(52.dp)
                        .height(28.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFB0B0B0)),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = onPauseResume,
                        modifier = Modifier.size(28.dp),
                        enabled = activeTimer != null
                    ) {
                        Icon(
                            imageVector = if (isRunning) Icons.Default.Clear else Icons.Default.PlayArrow,
                            contentDescription = if (isRunning) "Pausar" else "Continuar"
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun TimerCard(
    temporizador: Temporizador,
    onPlay: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.7f)
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${temporizador.horas}h ${temporizador.minutos}m",
                fontSize = 32.sp,
                color = Color.Black,
                fontFamily = WdxFontFamily,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Favorito */ }) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito"
                    )
                }
                Box(
                    modifier = Modifier
                        .width(52.dp)
                        .height(28.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFB0B0B0)),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = onPlay,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Iniciar")
                    }
                }
                IconButton(onClick = { /* Modo, solo visual */ }) {
                    val iconRes = when (temporizador.modo) {
                        Modo.SILENCIO -> R.drawable.volume_off
                        Modo.VIBRACION -> R.drawable.vibration
                        Modo.SONIDO -> R.drawable.volume_up
                        else -> R.drawable.volume_off
                    }
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = "Modo: ${temporizador.modo}"
                    )
                }
            }
        }
    }
}
