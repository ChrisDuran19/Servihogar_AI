package com.example.data.repository

import com.example.data.local.PropertyHistoryDao
import com.example.data.local.ServiceRequestDao
import com.example.data.local.TechnicianDao
import com.example.data.model.AiDiagnosticResult
import com.example.data.model.PropertyHistoryEntity
import com.example.data.model.ServiceRequestEntity
import com.example.data.model.ServiceStatus
import com.example.data.model.TechnicianEntity
import com.example.data.remote.GeminiAiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

class HomeServiceRepository(
    private val serviceRequestDao: ServiceRequestDao,
    private val propertyHistoryDao: PropertyHistoryDao,
    private val technicianDao: TechnicianDao,
    private val aiService: GeminiAiService
) {
    val allRequests: Flow<List<ServiceRequestEntity>> = serviceRequestDao.getAllRequests()
    val activeRequest: Flow<ServiceRequestEntity?> = serviceRequestDao.getActiveRequest()
    val propertyHistory: Flow<List<PropertyHistoryEntity>> = propertyHistoryDao.getAllHistory()
    val allTechnicians: Flow<List<TechnicianEntity>> = technicianDao.getAllTechnicians()

    suspend fun diagnoseProblem(userDescription: String, category: String): AiDiagnosticResult {
        return aiService.diagnoseProblem(userDescription, category)
    }

    suspend fun createServiceRequest(
        category: String,
        userDescription: String,
        mediaUri: String?,
        diagnostic: AiDiagnosticResult
    ): ServiceRequestEntity {
        val newRequest = ServiceRequestEntity(
            id = UUID.randomUUID().toString(),
            category = category,
            userDescription = userDescription,
            mediaUri = mediaUri,
            aiDiagnosisTitle = diagnostic.probableCause,
            aiConfidence = diagnostic.confidenceLevel,
            estimatedTime = diagnostic.estimatedTime,
            estimatedMinPrice = diagnostic.priceRangeMin,
            estimatedMaxPrice = diagnostic.priceRangeMax,
            materialsSuggested = diagnostic.recommendedMaterials.joinToString(", "),
            riskLevel = diagnostic.riskLevel,
            priceFactors = diagnostic.priceFactorsExplanation,
            status = ServiceStatus.DIAGNOSED.name,
            escrowAmount = diagnostic.averagePrice,
            securityCode1 = (100000..999999).random().toString(),
            securityCode2 = (100000..999999).random().toString()
        )
        serviceRequestDao.insertRequest(newRequest)
        return newRequest
    }

    suspend fun assignTechnicianToRequest(requestId: String, tech: TechnicianEntity, date: String?, time: String?) {
        val current = serviceRequestDao.getRequestById(requestId).firstOrNull() ?: return
        val updated = current.copy(
            technicianId = tech.id,
            technicianName = tech.name,
            technicianPhoto = tech.photoUrl,
            technicianRating = tech.rating,
            technicianPhone = tech.phone,
            escrowAmount = tech.priceOffer,
            scheduledDate = date ?: "Hoy",
            scheduledTime = time ?: "Inmediato",
            status = ServiceStatus.BOOKED.name,
            escrowStatus = "HOLDING"
        )
        serviceRequestDao.updateRequest(updated)
    }

    suspend fun updateRequestStatus(requestId: String, status: ServiceStatus) {
        serviceRequestDao.updateStatus(requestId, status.name)
    }

    suspend fun updateServiceDetails(request: ServiceRequestEntity) {
        serviceRequestDao.updateRequest(request)
    }

    suspend fun completeAndReleaseEscrow(requestId: String, satisfactionRating: Float, feedbackText: String) {
        val current = serviceRequestDao.getRequestById(requestId).firstOrNull() ?: return
        val updated = current.copy(
            status = ServiceStatus.COMPLETED.name,
            escrowStatus = "RELEASED",
            securityCode2Verified = true,
            satisfactionRating = satisfactionRating,
            feedbackText = feedbackText,
            completedTimeStamp = System.currentTimeMillis()
        )
        serviceRequestDao.updateRequest(updated)

        // Save to digital property history
        val historyItem = PropertyHistoryEntity(
            id = UUID.randomUUID().toString(),
            propertyAddress = updated.address,
            serviceTitle = updated.aiDiagnosisTitle,
            category = updated.category,
            date = updated.scheduledDate ?: "18/05/2026",
            cost = updated.escrowAmount,
            technicianName = updated.technicianName ?: "Carlos Martínez",
            warrantyDetails = "Garantía de 90 días respaldada por ServiHogar AI",
            materialsUsed = updated.materialsSuggested,
            preventiveTip = "Revisión periódica de carga cada 6 meses recomendada por IA."
        )
        propertyHistoryDao.insertHistory(historyItem)
    }

    suspend fun seedInitialDataIfEmpty() {
        // Seed initial Technicians if empty
        val existingTechs = technicianDao.getAllTechnicians().firstOrNull()
        if (existingTechs.isNullOrEmpty()) {
            val sampleTechs = listOf(
                TechnicianEntity(
                    id = "tech_1",
                    name = "Carlos Martínez",
                    category = "Electricidad",
                    rating = 4.9,
                    reviewCount = 128,
                    distanceKm = 1.2,
                    etaMinutes = 15,
                    priceOffer = 150000.0,
                    certifications = "Electricista Certificado CONTE",
                    aiTrustIndex = 98,
                    photoUrl = "https://images.unsplash.com/photo-1540569014015-19a7be504e3a?w=150",
                    phone = "+57 300 123 4567",
                    vehicleInfo = "Moto Pulsar NS 200 - Placa ABC-123",
                    latOffset = 0.005f,
                    lngOffset = 0.003f
                ),
                TechnicianEntity(
                    id = "tech_2",
                    name = "Jorge Ramírez",
                    category = "Electricidad",
                    rating = 4.8,
                    reviewCount = 96,
                    distanceKm = 2.3,
                    etaMinutes = 20,
                    priceOffer = 140000.0,
                    certifications = "Técnico Electricista Sena",
                    aiTrustIndex = 95,
                    photoUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
                    phone = "+57 311 987 6543",
                    vehicleInfo = "Moto Yamaha FZ - Placa XYZ-890",
                    latOffset = -0.004f,
                    lngOffset = 0.007f
                ),
                TechnicianEntity(
                    id = "tech_3",
                    name = "Luis Fernando",
                    category = "Electricidad",
                    rating = 4.7,
                    reviewCount = 74,
                    distanceKm = 3.1,
                    etaMinutes = 25,
                    priceOffer = 155000.0,
                    certifications = "Técnico Eléctrico Industrial",
                    aiTrustIndex = 93,
                    photoUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150",
                    phone = "+57 320 555 4321",
                    vehicleInfo = "Camioneta Chevrolet N300 - Placa QWE-456",
                    latOffset = 0.008f,
                    lngOffset = -0.005f
                )
            )
            technicianDao.insertAll(sampleTechs)
        }

        // Property History starts clean and empty. It fills up as the user completes real services.
    }
}
