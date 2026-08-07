package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ServiceStatus {
    DIAGNOSED,
    SEARCHING_TECH,
    BOOKED,
    EN_CAMINO,
    ARRIVED_SECURITY_1,
    IN_PROGRESS,
    AWAITING_SECURITY_2,
    COMPLETED,
    CANCELLED
}

enum class EscrowStatus {
    PENDING,
    HOLDING,
    RELEASED,
    DISPUTED
}

enum class UserRole {
    CLIENT,
    TECHNICIAN
}

enum class PaymentMode {
    AUTOMATIC_WALLET,
    MANUAL_PAYMENT
}

data class UserProfile(
    val id: String = "user_001",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val role: UserRole = UserRole.CLIENT,
    val idNumber: String = "",
    val profession: String = "",
    val education: String = "",
    val skillsAndKnowledge: String = "",
    val city: String = "",
    val experienceYears: Int = 0,
    val address: String = "",
    val profilePhotoUri: String? = null,
    val idFrontUri: String? = null,
    val idBackUri: String? = null,
    val faceBiometricVerified: Boolean = false,
    val aiConfidence: Int = 0,
    val aiMatchReason: String = "",
    val otpChannel: String = "WHATSAPP",
    val isOtpVerified: Boolean = false,
    val walletBalanceCop: Double = 0.0,
    val serviCoinsBalance: Long = 0,
    val paymentMode: PaymentMode = PaymentMode.AUTOMATIC_WALLET
)

data class WalletTransaction(
    val id: String,
    val type: String, // RECHARGE, PAYMENT_ESCROW, RELEASE_ESCROW, BONUS
    val amountCop: Double,
    val serviCoinsAmount: Long,
    val method: String, // Nequi, PSE, Daviplata, Bancolombia, Davivienda, Tarjeta
    val status: String, // APPROVED, PENDING, COMPLETED
    val date: String,
    val description: String
)

data class AiBiometricResult(
    val faceMatchConfidence: Int = 98,
    val idNumberExtracted: String = "1.037.654.890",
    val fullNameExtracted: String = "Andrés Restrepo",
    val isRealPerson: Boolean = true,
    val verificationNotes: String = "Identidad validada exitosamente mediante biometría facial e IA."
)

@Entity(tableName = "service_requests")
data class ServiceRequestEntity(
    @PrimaryKey val id: String,
    val category: String,
    val userDescription: String,
    val mediaUri: String? = null,
    val aiDiagnosisTitle: String,
    val aiConfidence: Int,
    val estimatedTime: String,
    val estimatedMinPrice: Double,
    val estimatedMaxPrice: Double,
    val materialsSuggested: String,
    val riskLevel: String,
    val priceFactors: String,
    val status: String = ServiceStatus.DIAGNOSED.name,
    val securityCode1: String = "872193",
    val securityCode1Verified: Boolean = false,
    val securityCode2: String = "531628",
    val securityCode2Verified: Boolean = false,
    val escrowAmount: Double,
    val escrowStatus: String = EscrowStatus.PENDING.name,
    val technicianId: String? = null,
    val technicianName: String? = null,
    val technicianPhoto: String? = null,
    val technicianRating: Double? = null,
    val technicianPhone: String? = null,
    val scheduledDate: String? = null,
    val scheduledTime: String? = null,
    val address: String = "Cra 45 # 123 - 45, Medellín",
    val propertyNotes: String? = null,
    val startTimeStamp: Long? = null,
    val completedTimeStamp: Long? = null,
    val satisfactionRating: Float? = null,
    val feedbackText: String? = null,
    val beforePhotos: String? = null,
    val duringPhotos: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "property_history")
data class PropertyHistoryEntity(
    @PrimaryKey val id: String,
    val propertyAddress: String,
    val serviceTitle: String,
    val category: String,
    val date: String,
    val cost: Double,
    val technicianName: String,
    val warrantyDetails: String,
    val materialsUsed: String,
    val preventiveTip: String? = null
)

@Entity(tableName = "technicians")
data class TechnicianEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val rating: Double,
    val reviewCount: Int,
    val distanceKm: Double,
    val etaMinutes: Int,
    val priceOffer: Double,
    val certifications: String,
    val aiTrustIndex: Int,
    val photoUrl: String,
    val phone: String,
    val vehicleInfo: String,
    val latOffset: Float = 0f,
    val lngOffset: Float = 0f
)

data class AiDiagnosticResult(
    val probableCause: String,
    val confidenceLevel: Int,
    val estimatedTime: String,
    val priceRangeMin: Double,
    val priceRangeMax: Double,
    val averagePrice: Double,
    val recommendedMaterials: List<String>,
    val riskLevel: String,
    val priceFactorsExplanation: String
)
