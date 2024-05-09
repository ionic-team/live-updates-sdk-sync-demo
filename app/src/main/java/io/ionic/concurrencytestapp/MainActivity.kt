package io.ionic.concurrencytestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.ionic.concurrencytestapp.ui.theme.ConcurrencyTestAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConcurrencyTestAppTheme {
                Page()
            }
        }
    }
}

@Composable
fun Page() {
    Scaffold(modifier = Modifier.fillMaxSize()) {
        LiveUpdateGui(it)
    }
}

@Composable
fun LiveUpdateGui(paddingValues: PaddingValues) {
    val liveUpdateViewModel = viewModel { UpdateViewModel() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UpdateButtons(liveUpdateViewModel)
        LiveUpdateInfo(liveUpdateViewModel)
    }
}

@Composable
fun LiveUpdateInfo(liveUpdateViewModel: UpdateViewModel) {
    val logItems = liveUpdateViewModel.log

    LazyColumn {
        items(logItems.size) {
            Row(
                modifier = Modifier.padding(2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(logItems[it], fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun UpdateButtons(liveUpdateViewModel: UpdateViewModel) {
    val enabled = liveUpdateViewModel.enabled.observeAsState()
    val context = LocalContext.current

    FilledTonalButton(
        onClick = { liveUpdateViewModel.syncAllAtOnce(context) },
        enabled = enabled.value ?: true
    ) {
        Text("Sync all apps at once")
    }
    FilledTonalButton(
        onClick = { liveUpdateViewModel.syncStaggered(context) },
        enabled = enabled.value ?: true
    ) {
        Text("Sync staggered")
    }
    FilledTonalButton(
        onClick = { liveUpdateViewModel.reset(context) }
    ) {
        Text("Reset")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ConcurrencyTestAppTheme {
        Page()
    }
}