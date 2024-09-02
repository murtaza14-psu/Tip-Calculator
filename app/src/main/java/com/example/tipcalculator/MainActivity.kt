package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import java.text.NumberFormat
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   TipCalculatorLayout()
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

@Composable
fun TipCalculatorLayout(){
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 35.dp)
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(modifier = Modifier
            .padding(bottom = 32.dp)
            .fillMaxWidth())
        Text(
            text = stringResource(id = R.string.tip_amount, "Rs0.00"),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(150.dp))

    }
}

/*
When the app first runs, the TextField starts with an empty value
(like an empty box). As soon as you type something in it, the onValueChange
function kicks in, which updates a special "memory" (called amountInput) that
stores whatever you’ve typed.

This amountInput is being watched by Jetpack Compose. So, when you type,
Compose knows that something has changed and needs to update the screen
(this is called recomposition).

Now, because of the remember { } block, whatever you typed is remembered even
when the screen updates. This prevents the TextField from going back to being
empty every time the screen refreshes. So, the text box shows what you typed,
even when the app updates/redraws it.

In short, it’s like having a sticky note that keeps track of what you wrote,
so even if the app redraws the text box, the sticky note reminds it of what you’ve
typed so far!

 */
@Composable
fun EditNumberField(modifier: Modifier=Modifier){
    var amount by remember { mutableStateOf("") }
    TextField(value = amount,
        onValueChange ={amount=it},
        modifier=modifier
        )
}
/*There are two parameters.  amount that accepts a double value and
* tip percent that accepts a double value and if nothin is passed it ets the
* the value automaticLLY to 15. */
private fun calculateTip(amount: Double, tipPercent:Double = 15.0):String{
    val tip = tipPercent / 100 * amount
    return NumberFormat.getCurrencyInstance().format(tip)//this line converts the currency into the format according to location o the device and returns as a string
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TipCalculatorTheme {
        TipCalculatorLayout()
    }
}