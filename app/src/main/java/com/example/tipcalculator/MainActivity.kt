package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

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
    
    // used hoisting here to make the edit text fun stateless and this fun stateful
    //state is a variable or data that can change over time and which the compose closely
    // monitors

    var amountInput by remember { mutableStateOf("") }
    var tipInput by remember { mutableStateOf("") }
    var roundUp by remember { mutableStateOf(false)}

    // ?: is the elvis operator that returns the before expression when the value is not null
    //and returns the expression after if the value is null.
    val amount = amountInput.toDoubleOrNull()?:0.0
    val tipPercent = tipInput.toDoubleOrNull()?:0.0
    val tip = calculateTip(amount, tipPercent, roundUp)
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 35.dp)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(
            label = R.string.bill_amount,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next),
            value = amountInput,
            onValueChange = {amountInput=it},
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth())

        EditNumberField(
            label = R.string.how_was_the_service,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done),
            value = tipInput,
            onValueChange = {tipInput=it},
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth())

        RoundTip(roundUP = roundUp,
            onRoundupChanged ={roundUp = it},
            modifier = Modifier.padding(bottom = 32.dp))

        Text(
            text = stringResource(id = R.string.tip_amount, tip),
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
fun EditNumberField(
    @StringRes label : Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    //accepts a string value and returns nothing
    onValueChange: (String)->Unit,
    modifier: Modifier=Modifier){

    TextField(value = value,
        onValueChange =onValueChange,
        label = { Text(text = stringResource(label))},
        //keyboard options lets us specify what kind of keyboard to display when clicked on textfield
        //also the bellow code adds a next button in the bottom right of keyboard
        keyboardOptions = keyboardOptions,
        //single line property wraps the text into a horizontally scrollable single line.
        singleLine = true,
        modifier=modifier
        )
}

@Composable
fun RoundTip(
    roundUP:Boolean,
    onRoundupChanged:(Boolean)->Unit,
    modifier: Modifier=Modifier
){
    Row(modifier= modifier
        .fillMaxWidth()
        .size(48.dp),
        verticalAlignment = Alignment.CenterVertically)
    {
        Text(text = stringResource(id = R.string.round_up_tip))

        Switch(modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.End),
            checked = roundUP,
            onCheckedChange = onRoundupChanged )

    }
}
/*There are two parameters.  amount that accepts a double value and
* tip percent that accepts a double value and if nothin is passed it ets the
* the value automaticLLY to 15. */
private fun calculateTip(amount: Double,
                         tipPercent:Double = 15.0,
                         roundUP: Boolean):String{
    var tip = tipPercent / 100 * amount
    if (roundUP){
        tip = kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)
//this line converts the currency into the format according to location of
// the device and returns as a string
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TipCalculatorTheme {
        TipCalculatorLayout()
    }
}