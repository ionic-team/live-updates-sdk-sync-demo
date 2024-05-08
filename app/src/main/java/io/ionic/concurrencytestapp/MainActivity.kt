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
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        LiveUpdateInfo(liveUpdateViewModel)
        UpdateButtons(liveUpdateViewModel)
    }
}

@Composable
fun LiveUpdateInfo(liveUpdateViewModel: UpdateViewModel) {
    val status = liveUpdateViewModel.status.observeAsState()
    val startTime = liveUpdateViewModel.startTime.observeAsState()
    val stopTime = liveUpdateViewModel.stopTime.observeAsState()
    val duration = liveUpdateViewModel.duration.observeAsState()

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Status: ${status.value ?: "Idle"}")
        }
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Start Time: ${startTime.value ?: "N/A"}")
        }
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Stop Time: ${stopTime.value ?: "N/A"}")
        }
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Duration: ${duration.value ?: "N/A"}")
        }
    }
}

@Composable
fun UpdateButtons(liveUpdateViewModel: UpdateViewModel) {
    val enabled = liveUpdateViewModel.enabled.observeAsState()

    FilledTonalButton(
        onClick = liveUpdateViewModel::syncAllAtOnce,
        enabled = enabled.value ?: true
    ) {
        Text("Sync all apps at once")
    }
    FilledTonalButton(
        onClick = liveUpdateViewModel::syncStaggered,
        enabled = enabled.value ?: true
    ) {
        Text("Sync staggered")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ConcurrencyTestAppTheme {
        Page()
    }
}