package nl.pdik.level2.task2

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.pdik.level2.task2.model.Equation
import nl.pdik.level2.task2.ui.theme.Task2Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Task2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Task2()
                }
            }
        }
    }
}

@Composable
fun Task2(){
    val context = LocalContext.current
    val equation = remember { mutableStateOf(Equation("-", "-", "?")) }
    Scaffold(  topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { stringResource(id = R.string.next) },
                onClick = {  equation.value = generateEquation()})
        }
        ,
        content = {padding -> ScreenContent(Modifier.padding(padding),context = context, equation = equation)  }
    )
}

@Composable
fun ScreenContent(modifier: Modifier, context: Context, equation: MutableState<Equation>){
    Column(modifier) {
        ScreenTitle()
        Column() {
            EquationHeaders()
            EquationValues(equation.value)
            if(!equation.value.anwser.contains("?") && !equation.value.anwser.contains("✅")){
                AnswerButtons(
                    checkEquation = {
                            check: Boolean ->

                        if(checkConjunctionTable(equation.value,check)){
                            equation.value = Equation("✅","✅","✅")
                            informUser(context = context, R.string.correct)
                        }else{
                            informUser(context = context, R.string.incorrect)
                        }
                    }
                )
            }
        }

    }
}

private fun checkConjunctionTable(equation: Equation, d: Boolean): Boolean {
    val a = equation.firstArg.contains("T");
    val b = equation.secondArg.contains("T");
    val c = equation.anwser.contains("T");

    return (a && b && c) == d
}
@Composable
fun ScreenTitle() {
    Row(
    horizontalArrangement = Arrangement.Center
    )
    {
        Text(
            text = stringResource(id = R.string.title),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.h4
        )
    }

}
@Composable
fun EquationHeaders(){
    Row(
         modifier = Modifier.fillMaxWidth(),
         Arrangement.SpaceAround,
    ) {
        Text(text = stringResource(id = R.string.first),fontWeight = FontWeight.Bold,)
        Text(text = stringResource(id = R.string.second),fontWeight = FontWeight.Bold,)
        Text(text = stringResource(id = R.string.and), fontWeight = FontWeight.Bold,)
    }

}

@Composable
fun EquationValues(equation: Equation){
    Row(
        modifier = Modifier.fillMaxWidth(),
        Arrangement.SpaceAround,
    ) {
        Text(text = equation.firstArg,fontWeight = FontWeight.Bold,)
        Text(text = equation.secondArg,fontWeight = FontWeight.Bold,)
        Text(text = equation.anwser, fontWeight = FontWeight.Bold,)
    }
}
private fun informUser(context: Context, msgId: Int) {
    Toast.makeText(context, context.getString(msgId), Toast.LENGTH_SHORT).show()
}

/**
 * Create Random Equation
 */
private fun generateEquation(): Equation{
    return Equation(generateRandomConjunctionString(), generateRandomConjunctionString(), generateRandomConjunctionString())
}

/**
 * T | F string randomizer
 */
private fun generateRandomConjunctionString(): String {
    val trueString = "T"
    val falseString = "F"
    return  if ((0..1).random() == 0) trueString else falseString
}

@Composable
private fun AnswerButtons(checkEquation: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ){
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff4caf50)),
            onClick = {
                checkEquation(true)
            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.width(100.dp),
        ) {
            Text(text = stringResource(id = R.string.btn_true))
        }
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            onClick = {
                checkEquation(false)
            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.width(100.dp),
        ) {
            Text(text = stringResource(id = R.string.btn_false))
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Task2Theme {
        Task2()
    }
}