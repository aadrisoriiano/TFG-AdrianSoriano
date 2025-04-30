import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.silentsmart.BottomNavigationBar

@Composable
fun BottomNavigationBarWithDivider(selectedTab: String, onTabSelected: (String) -> Unit) {
    Column {
        HorizontalDivider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(horizontal = 32.dp))
        BottomNavigationBar(selectedTab = selectedTab, onTabSelected = onTabSelected)
    }
}
