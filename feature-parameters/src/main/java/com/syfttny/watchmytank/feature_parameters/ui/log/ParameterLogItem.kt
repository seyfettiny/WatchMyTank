package com.syfttny.watchmytank.feature_parameters.ui.log

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.syfttny.watchmytank.core.ui.components.ParameterRangeBar
import com.syfttny.watchmytank.core.ui.theme.WatchMyTankTheme
import com.syfttny.watchmytank.domain.model.ParameterRangeDefinition
import com.syfttny.watchmytank.domain.model.ParameterRangeDefaults
import com.syfttny.watchmytank.domain.model.ParameterType

@Composable
fun ParameterLogItem(
    parameterType: ParameterType,
    rangeDefinition: ParameterRangeDefinition,
    valueString: String, 
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    
    val currentValueDouble by remember(valueString) {
        derivedStateOf { valueString.toDoubleOrNull() }
    }

    Card(
        modifier = modifier.padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = parameterType.displayName,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = valueString,
                    onValueChange = onValueChange,
                    modifier = Modifier.weight(1f), 
                    label = { Text("Value") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
                if (parameterType.unit.isNotBlank()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = parameterType.unit,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 4.dp, end = 4.dp) 
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            ParameterRangeBar(
                rangeDefinition = rangeDefinition,
                currentValue = currentValueDouble 
            )
        }
    }
}

@Preview
@Composable
private fun ParameterLogItemPreview() {
    WatchMyTankTheme {
        ParameterLogItem(
            parameterType = ParameterType.PH,
            rangeDefinition = ParameterRangeDefaults.PH,
            valueString = "7.2",
            onValueChange = {}
        )
    }
}

@Preview
@Composable
private fun ParameterLogItemEmptyPreview() {
    WatchMyTankTheme {
        ParameterLogItem(
            parameterType = ParameterType.AMMONIA,
            rangeDefinition = ParameterRangeDefaults.AMMONIA_PPM,
            valueString = "",
            onValueChange = {}
        )
    }
} 