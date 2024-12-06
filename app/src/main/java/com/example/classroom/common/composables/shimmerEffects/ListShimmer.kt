package proyecto.person.appconsultapopular.common.shimmerEffects

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ListShimmer(quantity: Int){
    LazyColumn{
        items(quantity){
            BoxShimmer(height = 60.dp)
            Spacer(modifier = Modifier.height(10.dp))
            
        }
    }
}