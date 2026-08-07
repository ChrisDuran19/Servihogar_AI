package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.AppDatabase
import com.example.data.remote.GeminiAiService
import com.example.data.repository.HomeServiceRepository
import com.example.ui.screens.AiDiagnosticResultScreen
import com.example.ui.screens.AvailableTechsScreen
import com.example.ui.screens.DescribeIssueScreen
import com.example.ui.screens.FirstTimeNavigationGuideScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LiveTrackingScreen
import com.example.ui.screens.OnboardingAuthScreen
import com.example.ui.screens.ProModeScreen
import com.example.ui.screens.PropertyDigitalHistoryScreen
import com.example.ui.screens.SecurityCodeArrivalScreen
import com.example.ui.screens.SecurityCodeCompletionScreen
import com.example.ui.screens.ServiceDetailBookingScreen
import com.example.ui.screens.ServiceInProgressScreen
import com.example.ui.screens.ServiHogarBottomBar
import com.example.ui.screens.ServiHogarTopBar
import com.example.ui.screens.UserProfileScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.ServiHogarTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.HomeServiceViewModel
import com.example.ui.viewmodel.HomeServiceViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)
        val geminiService = GeminiAiService()
        val repository = HomeServiceRepository(
            serviceRequestDao = database.serviceRequestDao(),
            propertyHistoryDao = database.propertyHistoryDao(),
            technicianDao = database.technicianDao(),
            aiService = geminiService
        )
        val viewModelFactory = HomeServiceViewModelFactory(repository)

        setContent {
            ServiHogarTheme {
                val viewModel: HomeServiceViewModel = viewModel(factory = viewModelFactory)
                ServiHogarMainApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ServiHogarMainApp(viewModel: HomeServiceViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val isFullScreenMode = uiState.currentScreen == AppScreen.ONBOARDING_LOGIN || uiState.currentScreen == AppScreen.FIRST_TIME_GUIDE

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (!isFullScreenMode) {
                ServiHogarTopBar(
                    userName = uiState.userProfile.name.split(" ").firstOrNull() ?: "Usuario",
                    userRolePro = uiState.userRolePro,
                    walletBalanceCop = uiState.walletBalanceCop,
                    faceVerified = uiState.userProfile.faceBiometricVerified,
                    onToggleRole = { viewModel.toggleUserRolePro() },
                    onProfileClick = { viewModel.navigateTo(AppScreen.PROFILE) },
                    onWalletClick = { viewModel.navigateTo(AppScreen.WALLET) }
                )
            }
        },
        bottomBar = {
            if (!isFullScreenMode) {
                ServiHogarBottomBar(
                    currentScreen = uiState.currentScreen,
                    userRolePro = uiState.userRolePro,
                    onNavigate = { screen -> viewModel.navigateTo(screen) },
                    activeCount = if (uiState.activeService != null) 1 else 0
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (!isFullScreenMode) innerPadding else androidx.compose.foundation.layout.PaddingValues(0.dp))
        ) {
            when (uiState.currentScreen) {
                AppScreen.FIRST_TIME_GUIDE -> {
                    FirstTimeNavigationGuideScreen(
                        onFinishGuide = {
                            viewModel.finishFirstTimeGuide(goToLogin = false)
                        },
                        onGoToLogin = {
                            viewModel.finishFirstTimeGuide(goToLogin = true)
                        }
                    )
                }

                AppScreen.ONBOARDING_LOGIN -> {
                    OnboardingAuthScreen(
                        onCompleteAuth = { name, email, phone, role, idNumber, profession, education, skills, city, address, otpChannel ->
                            viewModel.completeOnboardingAuth(
                                name, email, phone, role, idNumber, profession, education, skills, city, address, otpChannel
                            )
                        },
                        onQuickLogin = {
                            viewModel.quickLoginBiometric()
                        },
                        onSkipToHome = {
                            viewModel.navigateTo(AppScreen.HOME)
                        }
                    )
                }

                AppScreen.HOME -> {
                    HomeScreen(
                        activeService = uiState.activeService,
                        recentHistory = uiState.recentPropertyHistory,
                        onSelectCategory = { category ->
                            viewModel.updateSelectedCategory(category)
                        },
                        onStartAiRequest = {
                            viewModel.navigateTo(AppScreen.DESCRIBE_ISSUE)
                        },
                        onNavigateToActiveService = {
                            viewModel.navigateToActiveServiceScreen()
                        },
                        onViewAllHistory = {
                            viewModel.navigateTo(AppScreen.DIGITAL_HISTORY)
                        },
                        onOpenGuide = {
                            viewModel.openFirstTimeGuideManually()
                        }
                    )
                }

                AppScreen.WALLET -> {
                    WalletScreen(
                        walletBalanceCop = uiState.walletBalanceCop,
                        serviCoinsBalance = uiState.serviCoinsBalance,
                        paymentMode = uiState.paymentMode,
                        transactions = uiState.walletTransactions,
                        userEmail = uiState.userProfile.email,
                        onTogglePaymentMode = { isAuto ->
                            viewModel.togglePaymentMode(isAuto)
                        },
                        onRechargeWallet = { amount, method ->
                            viewModel.rechargeWallet(amount, method)
                        },
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.PROFILE -> {
                    UserProfileScreen(
                        userProfile = uiState.userProfile,
                        onNavigateToWallet = { viewModel.navigateTo(AppScreen.WALLET) },
                        onToggleRole = { viewModel.toggleUserRolePro() },
                        onReVerifyFace = { viewModel.navigateTo(AppScreen.ONBOARDING_LOGIN) },
                        onBack = { viewModel.navigateTo(AppScreen.HOME) },
                        onOpenGuide = { viewModel.openFirstTimeGuideManually() }
                    )
                }

                AppScreen.DESCRIBE_ISSUE -> {
                    DescribeIssueScreen(
                        selectedCategory = uiState.selectedCategory,
                        userDescription = uiState.userIssueDescription,
                        isRecordingVoice = uiState.isRecordingVoice,
                        isAnalyzing = uiState.isAnalyzing,
                        attachedMediaUri = uiState.attachedMediaUri,
                        onCategorySelect = { category -> viewModel.updateSelectedCategory(category) },
                        onDescriptionChange = { text -> viewModel.updateIssueDescription(text) },
                        onToggleVoice = { viewModel.toggleVoiceRecording() },
                        onAttachPhoto = { viewModel.attachMediaSample() },
                        onAnalyzeWithAi = { viewModel.analyzeIssueWithAi() },
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.AI_DIAGNOSIS_RESULT -> {
                    uiState.currentDiagnostic?.let { diagnostic ->
                        AiDiagnosticResultScreen(
                            diagnostic = diagnostic,
                            onViewAvailableTechs = {
                                viewModel.navigateTo(AppScreen.AVAILABLE_TECHS)
                            },
                            onBack = { viewModel.navigateTo(AppScreen.DESCRIBE_ISSUE) }
                        )
                    } ?: run {
                        viewModel.navigateTo(AppScreen.DESCRIBE_ISSUE)
                    }
                }

                AppScreen.AVAILABLE_TECHS -> {
                    AvailableTechsScreen(
                        technicians = uiState.availableTechnicians,
                        onSelectTechnician = { tech ->
                            viewModel.selectTechnician(tech)
                        },
                        onBack = { viewModel.navigateTo(AppScreen.AI_DIAGNOSIS_RESULT) }
                    )
                }

                AppScreen.SERVICE_BOOKING -> {
                    ServiceDetailBookingScreen(
                        technician = uiState.selectedTechnician,
                        onConfirmBooking = { date, time ->
                            viewModel.confirmServiceBooking(date, time)
                        },
                        onBack = { viewModel.navigateTo(AppScreen.AVAILABLE_TECHS) }
                    )
                }

                AppScreen.LIVE_TRACKING -> {
                    LiveTrackingScreen(
                        activeService = uiState.activeService,
                        onTechnicianArrived = {
                            viewModel.simulateTechnicianArrival()
                        },
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.SECURITY_CODE_1_ARRIVED -> {
                    SecurityCodeArrivalScreen(
                        activeService = uiState.activeService,
                        onVerifyCode1 = { code ->
                            viewModel.verifyCode1AndStartWork(code)
                        },
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.SERVICE_IN_PROGRESS -> {
                    ServiceInProgressScreen(
                        activeService = uiState.activeService,
                        onFinishWork = {
                            viewModel.finishWorkAndRequestCode2()
                        },
                        onCancelService = {
                            viewModel.cancelService()
                        },
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.SECURITY_CODE_2_COMPLETION -> {
                    SecurityCodeCompletionScreen(
                        activeService = uiState.activeService,
                        onVerifyCode2AndRelease = { code, rating, comment ->
                            viewModel.verifyCode2AndReleaseEscrow(code, rating, comment)
                        },
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.DIGITAL_HISTORY -> {
                    PropertyDigitalHistoryScreen(
                        historyList = uiState.recentPropertyHistory,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.PRO_MODE -> {
                    ProModeScreen(
                        onVerifyCodeFromClient = { code ->
                            viewModel.verifyCode1AndStartWork(code)
                        },
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }
            }
        }
    }
}
