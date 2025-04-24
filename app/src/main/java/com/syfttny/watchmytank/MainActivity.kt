package com.syfttny.watchmytank

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.syfttny.watchmytank.core.ui.theme.WatchMyTankTheme
import com.syfttny.watchmytank.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WatchMyTankTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun PlaceholderContent() {
    val selectedButtonNumber = remember { mutableIntStateOf(0) }
    Column {
        Text("Watch My Tank - Coming Soon!")
        RadioButton(
            selected = selectedButtonNumber.intValue == 0,
            onClick = { selectedButtonNumber.intValue = 0 },
        )
        Text("Option 1")
        RadioButton(
            selected = selectedButtonNumber.intValue == 1,
            onClick = { selectedButtonNumber.intValue = 1 }
        )
        Text("Option 2")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WatchMyTankTheme {
        PlaceholderContent()
    }
}