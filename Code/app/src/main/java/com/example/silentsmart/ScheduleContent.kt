import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.silentsmart.MainViewModel
import com.example.silentsmart.database.entity.Horario

@Composable
fun ScheduleContent(viewModel: MainViewModel) {
    val horarios = viewModel.horarios.collectAsState(initial = emptyList()).value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(horarios.orEmpty().filterNotNull(), key = { it.id }) { horario ->
            ScheduleCard(horario)
        }
    }
}

@Composable
fun ScheduleCard(horario: Horario) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray), // Fondo gris

    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = horario.diaSemana,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${horario.horaInicio} - ${horario.horaFin}",
                fontSize = 18.sp
            )
            // Puedes añadir más información o botones aquí si lo necesitas
        }
    }
}
