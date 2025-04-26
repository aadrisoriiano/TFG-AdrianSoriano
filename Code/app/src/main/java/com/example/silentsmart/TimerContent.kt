import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
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
import com.example.silentsmart.MainViewModel
import com.example.silentsmart.database.entity.Temporizador



@Composable
fun TimerContent(viewModel: MainViewModel) {

    val temporizadores = viewModel.temporizadores.collectAsState(initial = emptyList()).value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Temporizador grande
        TimerSection()

        // Grid de temporizadores
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
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
                text = "1 : 50 : 30", // Aquí deberías mostrar el tiempo real
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
                IconButton(onClick = { /* Acción para reiniciar */ }) {
                    Icon(Icons.Default.Add, contentDescription = "Reiniciar")
                }
                IconButton(onClick = { /* Acción para continuar */ }) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Continuar")
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
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray), // Fondo gris
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
            IconButton(
                onClick = { /* Acción para iniciar temporizador */ },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Iniciar")
            }
        }
    }
}
