package co.edu.rafaelarias.exploracolapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.edu.rafaelarias.exploracolapp.ui.theme.ExploraColAppTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExploraColAppTheme {
                val myNavController = rememberNavController()


                val startDestination = if (FirebaseAuth.getInstance().currentUser != null) {
                    "home"
                } else {
                    "login"
                }

                NavHost(
                    navController = myNavController,
                    startDestination = startDestination,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(route = "login") {
                        LoginScreen(
                            onLoginSuccess = { myNavController.navigate("home") },
                            onNavigateToRegister = { myNavController.navigate("register") },
                            onBackClick = { myNavController.popBackStack() }
                        )
                    }
                    composable(route = "register") {
                        RegisterScreen(
                            onRegisterSuccess = { myNavController.navigate("login") },
                            onNavigateToLogin = { myNavController.navigate("login") },
                            onBackClick = { myNavController.popBackStack() }
                        )
                    }
                    composable(route = "home") {
                        HomeScreen(
                            onLogout = {
                                myNavController.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}


