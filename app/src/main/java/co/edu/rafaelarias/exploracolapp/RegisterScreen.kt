package co.edu.rafaelarias.exploracolapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.rafaelarias.exploracolapp.ui.theme.ExploraColAppTheme
import co.edu.rafaelarias.exploracolapp.ui.theme.validateEmail
import co.edu.rafaelarias.exploracolapp.ui.theme.validatePassword
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var acceptedTerms by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()

    val primaryOrange = Color(0xFFE45D25)
    val lightGrayBg = Color(0xFFF8F9FE)
    val inputBg = Color(0xFFE5E5EA)

    Surface(
        modifier = modifier.fillMaxSize(),
        color = lightGrayBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize() // <-- Quitamos el verticalScroll para que sea FIJO
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.Start)
                    .offset(x = (-12).dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = primaryOrange
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Explorando Colombia",
                color = primaryOrange,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp)) // Espacios reducidos para que todo quepa

            Text(
                text = "Crea tu cuenta",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = "Empieza tu aventura por el realismo mágico",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                RegisterField(
                    label = "NOMBRE COMPLETO", value = name, onValueChange = { name = it },
                    placeholder = "Tu nombre", leadingIcon = Icons.Default.Person, inputBg = inputBg
                )
                Spacer(modifier = Modifier.height(12.dp)) // Reducido de 20 a 12
                RegisterField(
                    label = "CORREO ELECTRÓNICO", value = email, onValueChange = { email = it; errorMessage = "" },
                    placeholder = "hola@test.com", leadingIcon = Icons.Default.Email, inputBg = inputBg
                )
                Spacer(modifier = Modifier.height(12.dp))
                RegisterField(
                    label = "CONTRASEÑA", value = password, onValueChange = { password = it; errorMessage = "" },
                    placeholder = "........", leadingIcon = Icons.Default.Lock, inputBg = inputBg, isPassword = true
                )
                Spacer(modifier = Modifier.height(12.dp))
                RegisterField(
                    label = "CONFIRMAR CONTRASEÑA", value = confirmPassword, onValueChange = { confirmPassword = it; errorMessage = "" },
                    placeholder = "........", leadingIcon = Icons.Default.Refresh, inputBg = inputBg, isPassword = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Checkbox(
                    checked = acceptedTerms,
                    onCheckedChange = { acceptedTerms = it },
                    colors = CheckboxDefaults.colors(checkedColor = primaryOrange)
                )
                Text(
                    text = buildAnnotatedString {
                        append("Acepto los ")
                        withStyle(style = SpanStyle(color = primaryOrange, fontWeight = FontWeight.Bold)) { append("términos y condiciones") }
                        append(" así como el tratamiento de datos personales.")
                    },
                    fontSize = 12.sp, color = Color.Gray, lineHeight = 16.sp
                )
            }

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = errorMessage, color = Color.Red, fontSize = 13.sp, modifier = Modifier.fillMaxWidth())
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val emailValidation = validateEmail(email)
                    if (!emailValidation.first) { errorMessage = emailValidation.second; return@Button }
                    val passwordValidation = validatePassword(password)
                    if (!passwordValidation.first) { errorMessage = passwordValidation.second; return@Button }
                    if (password != confirmPassword) { errorMessage = "Las contraseñas no coinciden."; return@Button }
                    if (!acceptedTerms) { errorMessage = "Debes aceptar los términos y condiciones."; return@Button }

                    isLoading = true
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        isLoading = false
                        if (task.isSuccessful) onRegisterSuccess() else errorMessage = "Error: ${task.exception?.message}"
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp), // Reducido de 64 a 56
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                enabled = !isLoading
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().background(brush = Brush.horizontalGradient(colors = listOf(primaryOrange, Color(0xFFFF8A65)))),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Registrarse", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                HorizontalDivider(modifier = Modifier.weight(1f), thickness = 0.5.dp, color = Color.LightGray)
                Text(text = " O REGÍSTRATE CON ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray, modifier = Modifier.padding(horizontal = 8.dp))
                HorizontalDivider(modifier = Modifier.weight(1f), thickness = 0.5.dp, color = Color.LightGray)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SocialButton(text = "Google", modifier = Modifier.weight(1f), icon = Icons.Default.Email)
                SocialButton(text = "Apple", modifier = Modifier.weight(1f), icon = Icons.Default.Lock)
            }

            // --- AQUÍ ESTÁ EL RESORTE MÁGICO ---
            Spacer(modifier = Modifier.weight(1f))

            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                Text(text = "¿Ya tienes una cuenta? ", color = Color.Gray, fontSize = 14.sp)
                Text(
                    text = "Inicia sesión",
                    color = primaryOrange,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
        }
    }
}

@Composable
fun RegisterField(
    label: String, value: String, onValueChange: (String) -> Unit, placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector, inputBg: Color,
    modifier: Modifier = Modifier, isPassword: Boolean = false
) {
    Column(modifier = modifier) {
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = value, onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().height(56.dp).clip(RoundedCornerShape(28.dp)),
            placeholder = { Text(placeholder, color = Color.Gray) },
            leadingIcon = { Icon(leadingIcon, contentDescription = null, tint = Color.Gray) },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = inputBg, unfocusedContainerColor = inputBg,
                disabledContainerColor = inputBg, focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )
    }
}

// Asegurándonos de que SocialButton esté disponible en este archivo también
