package com.cashmate

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.cashmate.navigation.BottomBar
import com.cashmate.navigation.NavHostComposable
import com.cashmate.security.BiometricAuthManager
import com.cashmate.ui.theme.CashMateTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private lateinit var biometricAuthManager: BiometricAuthManager
    private var isAuthenticated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        biometricAuthManager = BiometricAuthManager()
    }

    override fun onResume() {
        super.onResume()
        if (!isAuthenticated) {
            authenticateUser()
        }
    }

    private fun authenticateUser() {
        biometricAuthManager.authenticate(
            context = this,
            onError = {
                isAuthenticated = false
                Toast.makeText(this, "There was an error in the authentication", Toast.LENGTH_SHORT).show()
            },
            onSuccess = {
                isAuthenticated = true
                setMainContent()
            },
            onFail = {
                isAuthenticated = false
                Toast.makeText(this, "The authentication failed, try again", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun setMainContent() {
        setContent {
            val navController = rememberNavController()
            CashMateTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            BottomBar(
                                onNavigate = { navController.navigate(it) },
                            )
                        },
                    ) { innerPadding ->
                        NavHostComposable(innerPadding, navController)
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        isAuthenticated = false
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
    CashMateTheme {
        Greeting("Android")
    }
}