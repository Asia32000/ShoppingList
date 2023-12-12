package com.example.shoppinglist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppinglist.ui.theme.ShoppingListTheme
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContent {
            ShoppingListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting3(auth)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting3(auth: FirebaseAuth, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var displayTextLogin by remember { mutableStateOf("") }
    var displayTextPass by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.requiredHeight(15.dp))
        Text(
            modifier = Modifier
                .padding(16.dp, top = 30.dp),
            text = "Username",
            color = Color.Gray
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp),
            value = displayTextLogin,
            onValueChange = { displayTextLogin = it },
            trailingIcon = {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = "clear text",
                    modifier = Modifier
                        .clickable {
                            displayTextLogin = ""
                        }
                )
            }
        )

        Text(
            modifier = Modifier
                .padding(16.dp, top = 30.dp),
            text = "Password",
            color = Color.Gray
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp),
            value = displayTextPass,
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = { displayTextPass = it },
            trailingIcon = {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = "clear text",
                    modifier = Modifier
                        .clickable {
                            displayTextPass = ""
                        }
                )
            }
        )
        Spacer(modifier = Modifier.requiredHeight(32.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start=128.dp, end=128.dp),
            onClick = {
                auth.createUserWithEmailAndPassword(
                    displayTextLogin,
                    displayTextPass
                ).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(context, "Rejestracja się powiodła.", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context, "Nie udało się zarejestrować", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = (Color.Blue.copy(alpha = 0.3f)),
                contentColor = Color.Black
            )
        )
        {
            Text(
                text = "Zarejestruj"
            )
        }

        Spacer(modifier = Modifier.requiredHeight(16.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start=128.dp, end=128.dp),
            onClick = {
                auth.signInWithEmailAndPassword(
                    displayTextLogin,
                    displayTextPass
                ).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(context, "Logowanie się powiodło.", Toast.LENGTH_SHORT).show()
                        context.startActivity(Intent(context,MainActivity::class.java))
                    }else{
                        Toast.makeText(context, "Nie udało się zalogować", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = (Color.Blue.copy(alpha = 0.3f)),
                contentColor = Color.Black
            )
        )
        {
            Text(
                text = "Zaloguj"
            )
        }
    }
}