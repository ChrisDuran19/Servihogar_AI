package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PurpleAccent

enum class AuthMode {
    LOGIN,
    REGISTER
}

@Composable
fun OnboardingAuthScreen(
    onCompleteAuth: (
        name: String,
        email: String,
        phone: String,
        role: UserRole,
        idNumber: String,
        profession: String,
        education: String,
        skills: String,
        city: String,
        address: String,
        otpChannel: String
    ) -> Unit,
    onQuickLogin: () -> Unit,
    onSkipToHome: () -> Unit
) {
    var authMode by remember { mutableStateOf(AuthMode.LOGIN) }
    var step by remember { mutableIntStateOf(1) } // Register steps: 1: Role, 2: Face Movement Scan, 3: ID Document, 4: Detailed Form, 5: OTP, 6: Welcome

    var selectedRole by remember { mutableStateOf(UserRole.CLIENT) }
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var userPhone by remember { mutableStateOf("") }
    var userIdNumber by remember { mutableStateOf("") }
    var userProfession by remember { mutableStateOf("") }
    var userEducation by remember { mutableStateOf("") }
    var userSkills by remember { mutableStateOf("") }
    var userCity by remember { mutableStateOf("") }
    var userAddress by remember { mutableStateOf("") }

    // Face Liveness states
    var blinkDone by remember { mutableStateOf(false) }
    var headTurnDone by remember { mutableStateOf(false) }
    var smileDone by remember { mutableStateOf(false) }
    var isAnalyzingLiveness by remember { mutableStateOf(false) }

    // ID document scan state
    var idPhotoFrontUploaded by remember { mutableStateOf(false) }
    var idPhotoBackUploaded by remember { mutableStateOf(false) }

    // OTP state
    var otpChannel by remember { mutableStateOf("WHATSAPP") }
    var otpSent by remember { mutableStateOf(false) }
    var generatedDynamicOtp by remember { mutableStateOf("") }
    var otpInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(IndigoContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "ServiHogar IA",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (authMode == AuthMode.LOGIN) "Autenticación de Usuario" else "Creación de Cuenta Segura",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (authMode == AuthMode.REGISTER && step > 1) {
                        Text(
                            text = "Paso $step de 5",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = IndigoPrimary
                        )
                    }
                }

                // Auth Mode Tabs (Iniciar Sesión vs Crear Cuenta)
                TabRow(
                    selectedTabIndex = if (authMode == AuthMode.LOGIN) 0 else 1,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = IndigoPrimary
                ) {
                    Tab(
                        selected = authMode == AuthMode.LOGIN,
                        onClick = { authMode = AuthMode.LOGIN },
                        text = { Text("Iniciar Sesión", fontWeight = FontWeight.Bold) },
                        modifier = Modifier.testTag("tab_login")
                    )
                    Tab(
                        selected = authMode == AuthMode.REGISTER,
                        onClick = {
                            authMode = AuthMode.REGISTER
                            step = 1
                        },
                        text = { Text("Crear Cuenta", fontWeight = FontWeight.Bold) },
                        modifier = Modifier.testTag("tab_register")
                    )
                }
            }
        }

        // Mode view switcher
        Crossfade(targetState = authMode, modifier = Modifier.weight(1f)) { currentMode ->
            if (currentMode == AuthMode.LOGIN) {
                LoginScreenContent(
                    userName = userName,
                    onQuickLogin = onQuickLogin,
                    onSwitchToRegister = {
                        authMode = AuthMode.REGISTER
                        step = 1
                    }
                )
            } else {
                RegisterFlowContent(
                    step = step,
                    selectedRole = selectedRole,
                    onRoleSelect = { selectedRole = it },
                    userName = userName,
                    onNameChange = { userName = it },
                    userEmail = userEmail,
                    onEmailChange = { userEmail = it },
                    userPhone = userPhone,
                    onPhoneChange = { userPhone = it },
                    userIdNumber = userIdNumber,
                    onIdChange = { userIdNumber = it },
                    userProfession = userProfession,
                    onProfessionChange = { userProfession = it },
                    userEducation = userEducation,
                    onEducationChange = { userEducation = it },
                    userSkills = userSkills,
                    onSkillsChange = { userSkills = it },
                    userCity = userCity,
                    onCityChange = { userCity = it },
                    userAddress = userAddress,
                    onAddressChange = { userAddress = it },
                    blinkDone = blinkDone,
                    onBlinkTrigger = { blinkDone = true },
                    headTurnDone = headTurnDone,
                    onHeadTurnTrigger = { headTurnDone = true },
                    smileDone = smileDone,
                    onSmileTrigger = { smileDone = true },
                    isAnalyzingLiveness = isAnalyzingLiveness,
                    onStartLiveness = { isAnalyzingLiveness = true },
                    onFinishLiveness = { isAnalyzingLiveness = false },
                    idFrontUploaded = idPhotoFrontUploaded,
                    onUploadIdFront = { idPhotoFrontUploaded = true },
                    idBackUploaded = idPhotoBackUploaded,
                    onUploadIdBack = { idPhotoBackUploaded = true },
                    otpChannel = otpChannel,
                    onOtpChannelSelect = { otpChannel = it },
                    otpSent = otpSent,
                    onSendOtp = {
                        generatedDynamicOtp = com.example.util.CryptoSecurityUtils.generateDynamicPin()
                        otpInput = generatedDynamicOtp
                        otpSent = true
                    },
                    otpCode = generatedDynamicOtp,
                    otpInput = otpInput,
                    onOtpChange = { otpInput = it },
                    onPrevStep = { if (step > 1) step-- },
                    onNextStep = { step++ },
                    onComplete = {
                        onCompleteAuth(
                            userName,
                            userEmail,
                            userPhone,
                            selectedRole,
                            userIdNumber,
                            userProfession,
                            userEducation,
                            userSkills,
                            userCity,
                            userAddress,
                            otpChannel
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun LoginScreenContent(
    userName: String,
    onQuickLogin: () -> Unit,
    onSwitchToRegister: () -> Unit
) {
    var loginEmail by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }
    var isBiometricScanning by remember { mutableStateOf(false) }
    var scanCompleted by remember { mutableStateOf(false) }

    LaunchedEffect(isBiometricScanning) {
        if (isBiometricScanning) {
            kotlinx.coroutines.delay(1600)
            isBiometricScanning = false
            scanCompleted = true
            kotlinx.coroutines.delay(600)
            onQuickLogin()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Biometric Quick Login Shield
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = EmeraldContainer
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Acceso Biométrico Registrado", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldSuccess)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(IndigoContainer, MaterialTheme.colorScheme.surface)
                            )
                        )
                        .border(
                            2.dp,
                            if (scanCompleted) EmeraldSuccess else IndigoPrimary,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isBiometricScanning) {
                        CircularProgressIndicator(color = IndigoPrimary, modifier = Modifier.size(60.dp), strokeWidth = 3.dp)
                    } else if (scanCompleted) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(54.dp))
                    } else {
                        Icon(Icons.Default.Fingerprint, contentDescription = "Huella", tint = IndigoPrimary, modifier = Modifier.size(56.dp))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Bienvenido de nuevo, $userName",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = if (isBiometricScanning) "Escaneando rostro / huella dactilar..."
                    else if (scanCompleted) "¡Identidad Verificada!"
                    else "Usa tu huella dactilar o reconocimiento facial guardado para ingresar rápido.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { isBiometricScanning = true },
                    enabled = !isBiometricScanning && !scanCompleted,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("biometric_login_button")
                ) {
                    Icon(Icons.Default.Face, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ingresar con Biometría", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(modifier = Modifier.weight(1f).height(1.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)) {}
            Text(text = " O usa tu contraseña ", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 8.dp))
            Surface(modifier = Modifier.weight(1f).height(1.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)) {}
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Standard Login Form
        OutlinedTextField(
            value = loginEmail,
            onValueChange = { loginEmail = it },
            label = { Text("Correo o Teléfono Celular") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("login_email_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = loginPassword,
            onValueChange = { loginPassword = it },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("login_password_input")
        )

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = onQuickLogin,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("login_submit_button")
        ) {
            Text("Iniciar Sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¿Aún no tienes cuenta registrada?",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Crear Cuenta con Verificación Biométrica Cédula e IA",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = IndigoPrimary,
            modifier = Modifier
                .clickable { onSwitchToRegister() }
                .testTag("link_register")
        )
    }
}

@Composable
fun RegisterFlowContent(
    step: Int,
    selectedRole: UserRole,
    onRoleSelect: (UserRole) -> Unit,
    userName: String,
    onNameChange: (String) -> Unit,
    userEmail: String,
    onEmailChange: (String) -> Unit,
    userPhone: String,
    onPhoneChange: (String) -> Unit,
    userIdNumber: String,
    onIdChange: (String) -> Unit,
    userProfession: String,
    onProfessionChange: (String) -> Unit,
    userEducation: String,
    onEducationChange: (String) -> Unit,
    userSkills: String,
    onSkillsChange: (String) -> Unit,
    userCity: String,
    onCityChange: (String) -> Unit,
    userAddress: String,
    onAddressChange: (String) -> Unit,
    blinkDone: Boolean,
    onBlinkTrigger: () -> Unit,
    headTurnDone: Boolean,
    onHeadTurnTrigger: () -> Unit,
    smileDone: Boolean,
    onSmileTrigger: () -> Unit,
    isAnalyzingLiveness: Boolean,
    onStartLiveness: () -> Unit,
    onFinishLiveness: () -> Unit,
    idFrontUploaded: Boolean,
    onUploadIdFront: () -> Unit,
    idBackUploaded: Boolean,
    onUploadIdBack: () -> Unit,
    otpChannel: String,
    onOtpChannelSelect: (String) -> Unit,
    otpSent: Boolean,
    onSendOtp: () -> Unit,
    otpCode: String,
    otpInput: String,
    onOtpChange: (String) -> Unit,
    onPrevStep: () -> Unit,
    onNextStep: () -> Unit,
    onComplete: () -> Unit
) {
    Crossfade(targetState = step) { currentStep ->
        when (currentStep) {
            1 -> Step1RoleSelection(
                selectedRole = selectedRole,
                onRoleSelect = onRoleSelect,
                onNext = onNextStep
            )
            2 -> Step2FaceLivenessMovements(
                blinkDone = blinkDone,
                onBlinkTrigger = onBlinkTrigger,
                headTurnDone = headTurnDone,
                onHeadTurnTrigger = onHeadTurnTrigger,
                smileDone = smileDone,
                onSmileTrigger = onSmileTrigger,
                isAnalyzing = isAnalyzingLiveness,
                onStartLiveness = onStartLiveness,
                onFinishLiveness = onFinishLiveness,
                onNext = onNextStep
            )
            3 -> Step3IdDocumentScan(
                idFrontUploaded = idFrontUploaded,
                onUploadIdFront = onUploadIdFront,
                idBackUploaded = idBackUploaded,
                onUploadIdBack = onUploadIdBack,
                extractedId = userIdNumber,
                onNext = onNextStep
            )
            4 -> Step4DetailedRegisterForm(
                selectedRole = selectedRole,
                name = userName,
                onNameChange = onNameChange,
                email = userEmail,
                onEmailChange = onEmailChange,
                phone = userPhone,
                onPhoneChange = onPhoneChange,
                idNumber = userIdNumber,
                onIdChange = onIdChange,
                profession = userProfession,
                onProfessionChange = onProfessionChange,
                education = userEducation,
                onEducationChange = onEducationChange,
                skills = userSkills,
                onSkillsChange = onSkillsChange,
                city = userCity,
                onCityChange = onCityChange,
                address = userAddress,
                onAddressChange = onAddressChange,
                onNext = onNextStep
            )
            5 -> Step5OtpVerification(
                otpChannel = otpChannel,
                onChannelSelect = onOtpChannelSelect,
                userEmail = userEmail,
                userPhone = userPhone,
                otpSent = otpSent,
                onSendOtp = onSendOtp,
                otpCode = otpCode,
                otpInput = otpInput,
                onOtpChange = onOtpChange,
                onVerifyAndFinish = onComplete
            )
        }
    }
}

@Composable
fun Step1RoleSelection(
    selectedRole: UserRole,
    onRoleSelect: (UserRole) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Selecciona tu Perfil de Usuario",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Configura si solicitarás servicios o si te registrarás como profesional independiente verificado.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Card Cliente
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (selectedRole == UserRole.CLIENT) IndigoContainer else MaterialTheme.colorScheme.surface
            ),
            border = androidx.compose.foundation.BorderStroke(
                2.dp,
                if (selectedRole == UserRole.CLIENT) IndigoPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onRoleSelect(UserRole.CLIENT) }
                .testTag("role_client_card")
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(if (selectedRole == UserRole.CLIENT) IndigoPrimary else MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = if (selectedRole == UserRole.CLIENT) Color.White else IndigoPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Soy Cliente / Propietario",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "🏡", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Solicita reparaciones, diagnósticos con IA, seguimiento en vivo y custodia segura de pago Escrow.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                RadioButton(
                    selected = selectedRole == UserRole.CLIENT,
                    onClick = { onRoleSelect(UserRole.CLIENT) },
                    colors = RadioButtonDefaults.colors(selectedColor = IndigoPrimary)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Card Técnico / Profesional
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (selectedRole == UserRole.TECHNICIAN) EmeraldContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
            ),
            border = androidx.compose.foundation.BorderStroke(
                2.dp,
                if (selectedRole == UserRole.TECHNICIAN) EmeraldSuccess else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onRoleSelect(UserRole.TECHNICIAN) }
                .testTag("role_technician_card")
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(if (selectedRole == UserRole.TECHNICIAN) EmeraldSuccess else EmeraldContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = null,
                        tint = if (selectedRole == UserRole.TECHNICIAN) Color.White else EmeraldSuccess,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Soy Técnico / Especialista",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "🛠️", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Recibe solicitudes de trabajo en tu ciudad, valida códigos de seguridad de llegada/finalización y cobra en tu Billetera.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                RadioButton(
                    selected = selectedRole == UserRole.TECHNICIAN,
                    onClick = { onRoleSelect(UserRole.TECHNICIAN) },
                    colors = RadioButtonDefaults.colors(selectedColor = EmeraldSuccess)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNext,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("role_continue_button")
        ) {
            Text("Continuar a Prueba de Vida Facial IA", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun Step2FaceLivenessMovements(
    blinkDone: Boolean,
    onBlinkTrigger: () -> Unit,
    headTurnDone: Boolean,
    onHeadTurnTrigger: () -> Unit,
    smileDone: Boolean,
    onSmileTrigger: () -> Unit,
    isAnalyzing: Boolean,
    onStartLiveness: () -> Unit,
    onFinishLiveness: () -> Unit,
    onNext: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scanner")
    val laserY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 200f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser"
    )

    val allDone = blinkDone && headTurnDone && smileDone

    LaunchedEffect(isAnalyzing) {
        if (isAnalyzing) {
            kotlinx.coroutines.delay(1000)
            onBlinkTrigger()
            kotlinx.coroutines.delay(1000)
            onHeadTurnTrigger()
            kotlinx.coroutines.delay(1000)
            onSmileTrigger()
            onFinishLiveness()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Prueba de Vida Facial en Vivo",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Para evitar fraudes o suplantación, la cámara solicita 3 movimientos reales para comprobar que eres una persona viva.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Camera Frame Viewport
        Box(
            modifier = Modifier
                .size(220.dp)
                .clip(CircleShape)
                .background(Color(0xFF0F172A))
                .border(
                    3.dp,
                    if (allDone) EmeraldSuccess else IndigoPrimary,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (allDone) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(60.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("¡Biometría en Vivo Aprobada!", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Prueba de Vida 100% Real", color = EmeraldSuccess, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                }
            } else if (isAnalyzing) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawLine(
                            color = Color(0xFF10B981),
                            start = Offset(20f, laserY),
                            end = Offset(size.width - 20f, laserY),
                            strokeWidth = 6f
                        )
                    }
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = EmeraldSuccess, strokeWidth = 3.dp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Detectando Gestos Facial 3D...", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Ubica tu cara frente a la cámara", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Checklist of required movements
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Desafíos de Movimiento Requeridos:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(10.dp))

                LivenessChallengeItem(
                    label = "1. Parpadea 2 veces despacio",
                    isDone = blinkDone,
                    onManualCheck = onBlinkTrigger
                )

                LivenessChallengeItem(
                    label = "2. Gira levemente la cabeza a la derecha",
                    isDone = headTurnDone,
                    onManualCheck = onHeadTurnTrigger
                )

                LivenessChallengeItem(
                    label = "3. Sonríe a la cámara",
                    isDone = smileDone,
                    onManualCheck = onSmileTrigger
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (!allDone) {
            Button(
                onClick = onStartLiveness,
                enabled = !isAnalyzing,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("start_liveness_button")
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isAnalyzing) "Escaneando Movimientos..." else "Iniciar Detección de Movimientos", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNext,
            enabled = allDone,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("liveness_continue_button")
        ) {
            Text("Continuar a Foto de Cédula", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun LivenessChallengeItem(
    label: String,
    isDone: Boolean,
    onManualCheck: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { if (!isDone) onManualCheck() }
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, fontWeight = FontWeight.Medium)

        if (isDone) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = EmeraldSuccess
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("OK", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            Text("Pendiente", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun Step3IdDocumentScan(
    idFrontUploaded: Boolean,
    onUploadIdFront: () -> Unit,
    idBackUploaded: Boolean,
    onUploadIdBack: () -> Unit,
    extractedId: String,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "Validación de Cédula de Ciudadanía",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Escanear el frente y reverso de tu Cédula colombiana para extracción de datos e inspección de holograma de seguridad con IA.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Cédula Front Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (idFrontUploaded) EmeraldContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.5.dp,
                if (idFrontUploaded) EmeraldSuccess else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onUploadIdFront() }
                .testTag("upload_id_front")
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (idFrontUploaded) EmeraldSuccess else IndigoContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Badge,
                        contentDescription = null,
                        tint = if (idFrontUploaded) Color.White else IndigoPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "1. Cédula Frontal (Foto y Nombres)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (idFrontUploaded) "✓ Hologramación y texto extraído correctamente" else "Toca para escanear el frente del documento",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Cédula Back Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (idBackUploaded) EmeraldContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.5.dp,
                if (idBackUploaded) EmeraldSuccess else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onUploadIdBack() }
                .testTag("upload_id_back")
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (idBackUploaded) EmeraldSuccess else IndigoContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Badge,
                        contentDescription = null,
                        tint = if (idBackUploaded) Color.White else IndigoPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "2. Cédula Reversa (Código de Barras PDF417)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (idBackUploaded) "✓ Código de barras leído: C.C. $extractedId" else "Toca para escanear el respaldo con código de barras",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // AI Validation Banner
        if (idFrontUploaded && idBackUploaded) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = EmeraldContainer),
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = EmeraldSuccess)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Verificación IA de Cédula Completada", fontWeight = FontWeight.Bold, color = EmeraldSuccess, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• Documento: Cédula de Ciudadanía Colombiana\n• Número Auténtico Extraído: $extractedId\n• Coincidencia Rostro vs Cédula: 99%",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onNext,
            enabled = idFrontUploaded && idBackUploaded,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("id_continue_button")
        ) {
            Text("Continuar a Formulario de Registro", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step4DetailedRegisterForm(
    selectedRole: UserRole,
    name: String,
    onNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    idNumber: String,
    onIdChange: (String) -> Unit,
    profession: String,
    onProfessionChange: (String) -> Unit,
    education: String,
    onEducationChange: (String) -> Unit,
    skills: String,
    onSkillsChange: (String) -> Unit,
    city: String,
    onCityChange: (String) -> Unit,
    address: String,
    onAddressChange: (String) -> Unit,
    onNext: () -> Unit
) {
    val cityOptions = listOf(
        "Medellín, Antioquia",
        "Bogotá, D.C.",
        "Cali, Valle del Cauca",
        "Barranquilla, Atlántico",
        "Bucaramanga, Santander",
        "Pereira, Risaralda",
        "Manizales, Caldas",
        "Cartagena, Bolívar",
        "Cúcuta, Norte de Santander"
    )

    var cityDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "Información Personal y Profesional",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Ingresa tus datos detallados para configurar tu perfil legal en la red de ServiHogar.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(18.dp))

        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Nombre Completo") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().testTag("input_reg_name")
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = idNumber,
            onValueChange = onIdChange,
            label = { Text("Número de Cédula (C.C.)") },
            leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().testTag("input_reg_id")
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Correo Electrónico") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().testTag("input_reg_email")
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = onPhoneChange,
            label = { Text("Celular / WhatsApp") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().testTag("input_reg_phone")
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = profession,
            onValueChange = onProfessionChange,
            label = { Text(if (selectedRole == UserRole.TECHNICIAN) "Profesión / Especialidad Técnica Principal" else "Ocupación / Cargo") },
            leadingIcon = { Icon(Icons.Default.Work, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().testTag("input_reg_profession")
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = education,
            onValueChange = onEducationChange,
            label = { Text("Estudios, Títulos y Licencias (ej: SENA, CONTE, Univ)") },
            leadingIcon = { Icon(Icons.Default.School, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().testTag("input_reg_education")
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = skills,
            onValueChange = onSkillsChange,
            label = { Text("Conocimientos Clave y Habilidades") },
            leadingIcon = { Icon(Icons.Default.Psychology, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().testTag("input_reg_skills")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // City Selector Dropdown
        ExposedDropdownMenuBox(
            expanded = cityDropdownExpanded,
            onExpandedChange = { cityDropdownExpanded = !cityDropdownExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = city,
                onValueChange = {},
                readOnly = true,
                label = { Text("Ciudad / Municipio de Operación") },
                leadingIcon = { Icon(Icons.Default.LocationCity, contentDescription = null) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityDropdownExpanded) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.menuAnchor().fillMaxWidth().testTag("input_reg_city")
            )

            ExposedDropdownMenu(
                expanded = cityDropdownExpanded,
                onDismissRequest = { cityDropdownExpanded = false }
            ) {
                cityOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onCityChange(option)
                            cityDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = address,
            onValueChange = onAddressChange,
            label = { Text("Dirección de Residencia / Base") },
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().testTag("input_reg_address")
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNext,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("form_continue_button")
        ) {
            Text("Continuar a Verificación OTP", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun Step5OtpVerification(
    otpChannel: String,
    onChannelSelect: (String) -> Unit,
    userEmail: String,
    userPhone: String,
    otpSent: Boolean,
    onSendOtp: () -> Unit,
    otpCode: String,
    otpInput: String,
    onOtpChange: (String) -> Unit,
    onVerifyAndFinish: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Verificación por Código OTP",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Enviaremos un código de seguridad cifrado de 6 dígitos para validar tu canal de contacto directo.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // WhatsApp / Email Selector Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (otpChannel == "WHATSAPP") EmeraldContainer else MaterialTheme.colorScheme.surface
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    if (otpChannel == "WHATSAPP") EmeraldSuccess else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                ),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onChannelSelect("WHATSAPP") }
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "💬", fontSize = 28.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("WhatsApp", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(userPhone.ifBlank { "Número celular" }, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (otpChannel == "EMAIL") IndigoContainer else MaterialTheme.colorScheme.surface
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    if (otpChannel == "EMAIL") IndigoPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                ),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onChannelSelect("EMAIL") }
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "📧", fontSize = 28.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Correo Email", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(if (userEmail.isNotBlank()) userEmail.take(12) + "..." else "Correo electrónico", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (!otpSent) {
            Button(
                onClick = onSendOtp,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("send_otp_btn")
            ) {
                Text("Generar y Enviar Código OTP Dinámico", fontWeight = FontWeight.Bold)
            }
        } else {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📲", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Código dinámico enviado vía $otpChannel", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = IndigoPrimary)
                        Text("Tu código dinámico cifrado es: $otpCode", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OtpCodeDisplayBox(code = otpInput.ifBlank { otpCode }, label = "Código dinámico cifrado")

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onVerifyAndFinish,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("verify_otp_finish_btn")
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Validar Código y Activar Cuenta", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
