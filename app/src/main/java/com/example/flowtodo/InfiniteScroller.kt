import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun InfiniteScroller() {
    val itemsList = List(20) { "Item $it" }
    var selectedItem by remember { mutableStateOf<Int?>(null) }
    val listState = rememberLazyListState()

    // Ortadaki öğeyi bulmak için scroll konumunu takip ediyoruz
    LaunchedEffect(listState.firstVisibleItemIndex) {
        val middleIndex = listState.layoutInfo.visibleItemsInfo.size / 2
        val middleItem = listState.layoutInfo.visibleItemsInfo[middleIndex].index
        selectedItem = middleItem
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp), // Yalnızca 3 öğe görünmesi için padding
        verticalArrangement = Arrangement.Center

        ) {
        items(itemsList) { item ->
            Button(
                onClick = { selectedItem = itemsList.indexOf(item) },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .then(
                        if (itemsList.indexOf(item) == selectedItem) {
                            Modifier.background(MaterialTheme.colorScheme.primary)
                        } else {
                            Modifier.background(Color.Transparent)
                        }
                    )
            ) {
                Text(item)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    InfiniteScroller()
}
