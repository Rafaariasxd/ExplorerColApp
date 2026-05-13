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
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val auth = FirebaseAuth.getInstance()
            val startRoute = if (auth.currentUser != null) "home" else "login"
            val myNavController = rememberNavController()

            NavHost(
                navController = myNavController,
                startDestination = startRoute,
                modifier = Modifier.fillMaxSize()
            ) {

                composable(route = "login") {
                    LoginScreen(
                        onLoginSuccess = {
                            myNavController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        onNavigateToRegister = {
                            myNavController.navigate("register")
                        },
                        onBackClick = {
                            myNavController.popBackStack()
                        }
                    )
                }

                composable(route = "register") {
                    RegisterScreen(
                        onRegisterSuccess = {
                            myNavController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        onNavigateToLogin = {
                            myNavController.navigate("login")
                        },
                        onBackClick = {
                            myNavController.popBackStack()
                        }
                    )
                }

                composable(route = "home") {
                    HomeScreen(OnClickAddPlace = {
                        myNavController.navigate("add_place")
                    })
                }

                composable(route = "add_place") {
                    AddPlaceScreen(onBackClick = {
                        myNavController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    })
                }

            }
        }
    }
}
