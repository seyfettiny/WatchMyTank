package com.syfttny.watchmytank

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.syfttny.watchmytank.ui.theme.WatchMyTankTheme

class MainActivity : ComponentActivity() {

    private val selectedButtonNumber = mutableIntStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WatchMyTankTheme {
                // Create a list contains radio button
                Column {
                    // Create a radio button
                    RadioButton(
                        selected = selectedButtonNumber.intValue == 0,
                        onClick = { selectedButtonNumber.intValue = 0 },
                    )
                    RadioButton(
                        selected = selectedButtonNumber.intValue == 1,
                        onClick = { selectedButtonNumber.intValue = 1 }
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WatchMyTankTheme {
        Greeting("Android")
    }
}