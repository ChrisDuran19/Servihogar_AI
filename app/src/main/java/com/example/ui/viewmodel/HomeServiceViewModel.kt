package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.AiDiagnosticResult
import com.example.data.model.PaymentMode
import com.example.data.model.PropertyHistoryEntity
import com.example.data.model.ServiceRequestEntity
import com.example.data.model.ServiceStatus
import com.example.data.model.TechnicianEntity
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import com.example.data.model.WalletTransaction
import com.example.data.repository.HomeServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    FIRST_TIME_GUIDE,
    ONBOARDING_LOGIN,
    HOME,
    DESCRIBE_ISSUE,
    AI_DIAGNOSIS_RESULT,
    AVAILABLE_TECHS,
    SERVICE_BOOKING,
    LIVE_TRACKING,
    SECURITY_CODE_1_ARRIVED,
    SERVICE_IN_PROGRESS,
    SECURITY_CODE_2_COMPLETION,
    DIGITAL_HISTORY,
    WALLET,
    PROFILE,
    PRO_MODE
}

data class HomeUiState(
    val currentScreen: AppScreen = AppScreen.FIRST_TIME_GUIDE,
    val isFirstLaunch: Boolean = true,
    val userProfile: UserProfile = UserProfile(),
    val walletBalanceCop: Double = 0.0,
    val serviCoinsBalance: Long = 0,
    val paymentMode: PaymentMode = PaymentMode.AUTOMATIC_WALLET,
    val walletTransactions: List<WalletTransaction> = emptyList(),
    val selectedCategory: String = "Electricidad",
    val userIssueDescription: String = "",
    val attachedMediaUri: String? = null,
    val isRecordingVoice: Boolean = false,
    val isAnalyzing: Boolean = false,
    val currentDiagnostic: AiDiagnosticResult? = null,
    val selectedTechnician: TechnicianEntity? = null,
    val activeService: ServiceRequestEntity? = null,
    val availableTechnicians: List<TechnicianEntity> = emptyList(),
    val recentPropertyHistory: List<PropertyHistoryEntity> = emptyList(),
    val userRolePro: Boolean = false
)

class HomeServiceViewModel(
    private val repository: HomeServiceRepository
) : ViewModel() {

    private val _isFirstLaunch = MutableStateFlow(true)
    private val _currentScreen = MutableStateFlow(AppScreen.FIRST_TIME_GUIDE)
    private val _userProfile = MutableStateFlow(UserProfile())
    private val _walletBalanceCop = MutableStateFlow(0.0)
    private val _serviCoinsBalance = MutableStateFlow(0L)
    private val _paymentMode = MutableStateFlow(PaymentMode.AUTOMATIC_WALLET)
    private val _walletTransactions = MutableStateFlow<List<WalletTransaction>>(emptyList())

    private val _selectedCategory = MutableStateFlow("Electricidad")
    private val _userDescription = MutableStateFlow("")
    private val _attachedMediaUri = MutableStateFlow<String?>(null)
    private val _isRecordingVoice = MutableStateFlow(false)
    private val _isAnalyzingWithAi = MutableStateFlow(false)
    private val _currentAiDiagnostic = MutableStateFlow<AiDiagnosticResult?>(null)
    private val _selectedTechnician = MutableStateFlow<TechnicianEntity?>(null)
    private val _createdRequestId = MutableStateFlow<String?>(null)
    private val _userRolePro = MutableStateFlow(false)

    val activeService: StateFlow<ServiceRequestEntity?> = repository.activeRequest
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val propertyHistoryList: StateFlow<List<PropertyHistoryEntity>> = repository.propertyHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val availableTechnicians: StateFlow<List<TechnicianEntity>> = repository.allTechnicians
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uiState: StateFlow<HomeUiState> = combine(
        _currentScreen,
        _userProfile,
        _walletBalanceCop,
        _selectedCategory,
        _userDescription
    ) { screen, profile, wallet, category, desc ->
        HomeUiState(
            currentScreen = screen,
            isFirstLaunch = _isFirstLaunch.value,
            userProfile = profile,
            walletBalanceCop = wallet,
            serviCoinsBalance = (wallet / 1000).toLong(),
            paymentMode = _paymentMode.value,
            walletTransactions = _walletTransactions.value,
            selectedCategory = category,
            userIssueDescription = desc,
            attachedMediaUri = _attachedMediaUri.value,
            isRecordingVoice = _isRecordingVoice.value,
            isAnalyzing = _isAnalyzingWithAi.value,
            currentDiagnostic = _currentAiDiagnostic.value,
            selectedTechnician = _selectedTechnician.value,
            activeService = activeService.value,
            availableTechnicians = availableTechnicians.value,
            recentPropertyHistory = propertyHistoryList.value,
            userRolePro = profile.role == UserRole.TECHNICIAN
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    fun finishFirstTimeGuide(goToLogin: Boolean = false) {
        _isFirstLaunch.value = false
        _currentScreen.value = if (goToLogin) AppScreen.ONBOARDING_LOGIN else AppScreen.HOME
    }

    fun openFirstTimeGuideManually() {
        _currentScreen.value = AppScreen.FIRST_TIME_GUIDE
    }

    fun completeOnboardingAuth(
        name: String,
        email: String,
        phone: String,
        role: UserRole,
        idNumber: String,
        profession: String,
        education: String,
        skillsAndKnowledge: String,
        city: String,
        address: String,
        otpChannel: String
    ) {
        val updatedProfile = UserProfile(
            name = name,
            email = email,
            phone = phone,
            role = role,
            idNumber = idNumber,
            profession = profession,
            education = education,
            skillsAndKnowledge = skillsAndKnowledge,
            city = city,
            address = address,
            otpChannel = otpChannel,
            faceBiometricVerified = true,
            aiConfidence = 98,
            walletBalanceCop = _walletBalanceCop.value,
            serviCoinsBalance = _serviCoinsBalance.value,
            paymentMode = _paymentMode.value
        )
        _userProfile.value = updatedProfile
        _userRolePro.value = (role == UserRole.TECHNICIAN)
        _currentScreen.value = AppScreen.HOME
    }

    fun quickLoginBiometric() {
        _currentScreen.value = AppScreen.HOME
    }

    fun rechargeWallet(amountCop: Double, method: String) {
        val newBalance = _walletBalanceCop.value + amountCop
        val newCoins = (newBalance / 1000).toLong()
        _walletBalanceCop.value = newBalance
        _serviCoinsBalance.value = newCoins

        val newTx = WalletTransaction(
            id = "tx_${System.currentTimeMillis()}",
            type = "RECHARGE",
            amountCop = amountCop,
            serviCoinsAmount = (amountCop / 1000).toLong(),
            method = method,
            status = "APPROVED",
            date = "Hoy",
            description = "Recarga de Saldo vía $method"
        )
        _walletTransactions.value = listOf(newTx) + _walletTransactions.value
        _userProfile.value = _userProfile.value.copy(
            walletBalanceCop = newBalance,
            serviCoinsBalance = newCoins
        )
    }

    fun togglePaymentMode(isAutomatic: Boolean) {
        val mode = if (isAutomatic) PaymentMode.AUTOMATIC_WALLET else PaymentMode.MANUAL_PAYMENT
        _paymentMode.value = mode
        _userProfile.value = _userProfile.value.copy(paymentMode = mode)
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun navigateToActiveServiceScreen() {
        val active = activeService.value ?: return
        _currentScreen.value = when (active.status) {
            ServiceStatus.BOOKED.name, ServiceStatus.EN_CAMINO.name -> AppScreen.LIVE_TRACKING
            ServiceStatus.ARRIVED_SECURITY_1.name -> AppScreen.SECURITY_CODE_1_ARRIVED
            ServiceStatus.IN_PROGRESS.name -> AppScreen.SERVICE_IN_PROGRESS
            ServiceStatus.AWAITING_SECURITY_2.name -> AppScreen.SECURITY_CODE_2_COMPLETION
            else -> AppScreen.HOME
        }
    }

    fun updateSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun updateIssueDescription(description: String) {
        _userDescription.value = description
    }

    fun attachMediaSample() {
        _attachedMediaUri.value = "content://media/external/images/sample_breaker_1.jpg"
    }

    fun toggleVoiceRecording() {
        _isRecordingVoice.value = !_isRecordingVoice.value
        if (!_isRecordingVoice.value) {
            _userDescription.value = "Mi breaker principal salta al encender el aire acondicionado de la sala."
        }
    }

    fun toggleUserRolePro() {
        _userRolePro.value = !_userRolePro.value
        if (_userRolePro.value) {
            _currentScreen.value = AppScreen.PRO_MODE
        } else {
            _currentScreen.value = AppScreen.HOME
        }
    }

    fun analyzeIssueWithAi() {
        viewModelScope.launch {
            _isAnalyzingWithAi.value = true
            val category = _selectedCategory.value
            val desc = _userDescription.value.ifBlank { "Tengo un fallo técnico en mi hogar" }
            val result = repository.diagnoseProblem(desc, category)
            _currentAiDiagnostic.value = result
            _isAnalyzingWithAi.value = false

            val newReq = repository.createServiceRequest(
                category = category,
                userDescription = desc,
                mediaUri = _attachedMediaUri.value,
                diagnostic = result
            )
            _createdRequestId.value = newReq.id
            _currentScreen.value = AppScreen.AI_DIAGNOSIS_RESULT
        }
    }

    fun selectTechnician(tech: TechnicianEntity) {
        _selectedTechnician.value = tech
        _currentScreen.value = AppScreen.SERVICE_BOOKING
    }

    fun confirmServiceBooking(date: String, time: String) {
        val reqId = _createdRequestId.value ?: activeService.value?.id ?: return
        val tech = _selectedTechnician.value ?: availableTechnicians.value.firstOrNull() ?: return
        viewModelScope.launch {
            repository.assignTechnicianToRequest(reqId, tech, date, time)
            _currentScreen.value = AppScreen.LIVE_TRACKING
        }
    }

    fun simulateTechnicianArrival() {
        val reqId = activeService.value?.id ?: _createdRequestId.value ?: return
        viewModelScope.launch {
            repository.updateRequestStatus(reqId, ServiceStatus.ARRIVED_SECURITY_1)
            _currentScreen.value = AppScreen.SECURITY_CODE_1_ARRIVED
        }
    }

    fun verifyCode1AndStartWork(codeEntered: String): Boolean {
        val currentReq = activeService.value ?: return false
        viewModelScope.launch {
            repository.updateRequestStatus(currentReq.id, ServiceStatus.IN_PROGRESS)
            _currentScreen.value = AppScreen.SERVICE_IN_PROGRESS
        }
        return true
    }

    fun finishWorkAndRequestCode2() {
        val reqId = activeService.value?.id ?: return
        viewModelScope.launch {
            repository.updateRequestStatus(reqId, ServiceStatus.AWAITING_SECURITY_2)
            _currentScreen.value = AppScreen.SECURITY_CODE_2_COMPLETION
        }
    }

    fun verifyCode2AndReleaseEscrow(codeEntered: String, rating: Float, comment: String): Boolean {
        val currentReq = activeService.value ?: return false
        viewModelScope.launch {
            repository.completeAndReleaseEscrow(currentReq.id, rating, comment)
        }
        return true
    }

    fun cancelService() {
        val active = activeService.value ?: return
        viewModelScope.launch {
            repository.updateRequestStatus(active.id, ServiceStatus.CANCELLED)
            _currentScreen.value = AppScreen.HOME
        }
    }
}

class HomeServiceViewModelFactory(
    private val repository: HomeServiceRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeServiceViewModel::class.java)) {
            return HomeServiceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
