package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PurpleAccent

@Composable
fun DescribeIssueScreen(
    selectedCategory: String,
    userDescription: String,
    isRecordingVoice: Boolean,
    isAnalyzing: Boolean,
    attachedMediaUri: String?,
    onCategorySelect: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onToggleVoice: () -> Unit,
    onAttachPhoto: () -> Unit,
    onAnalyzeWithAi: () -> Unit,
    onBack: () -> Unit
) {
    val categories = listOf("Electricidad", "Plomería", "Construcción", "Aires Acondicionados", "Pintura", "Cerrajería", "Remodelación")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Nav Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Describe tu problema",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Step Progress Bar
            StepProgressBar(currentStep = 1)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Cuéntanos con detalle qué sucede",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Puedes escribir, hablar o subir fotos/videos de la falla.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Categories Selector Row
            Text(
                text = "Categoría del servicio:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSelected = cat.equals(selectedCategory, ignoreCase = true)
                    Surface(
                        onClick = { onCategorySelect(cat) },
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) IndigoPrimary else MaterialTheme.colorScheme.surface,
                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                        modifier = Modifier.testTag("category_chip_$cat")
                    ) {
                        Text(
                            text = cat,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Main Text Description Input
            OutlinedTextField(
                value = userDescription,
                onValueChange = onDescriptionChange,
                placeholder = { Text("Ej: Mi breaker se dispara cada vez que enciendo el aire acondicionado...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .testTag("issue_description_input"),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = IndigoPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                trailingIcon = {
                    if (userDescription.isNotEmpty()) {
                        IconButton(onClick = { onDescriptionChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Voice & Photo Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Photo button
                Surface(
                    onClick = onAttachPhoto,
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("attach_photo_button")
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Subir foto",
                            tint = IndigoPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (attachedMediaUri != null) "Foto añadida ✓" else "Subir foto",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (attachedMediaUri != null) EmeraldSuccess else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Voice Note button
                Surface(
                    onClick = onToggleVoice,
                    shape = RoundedCornerShape(14.dp),
                    color = if (isRecordingVoice) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isRecordingVoice) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("record_voice_button")
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Grabar audio",
                            tint = if (isRecordingVoice) MaterialTheme.colorScheme.error else IndigoPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isRecordingVoice) "Grabando... 🔴" else "Grabar audio",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isRecordingVoice) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Attached Media Preview Grid
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Fotos del problema",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Mock Photo Card 1
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(80.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = IndigoPrimary)
                            Text("Tablero", fontSize = 10.sp, color = IndigoPrimary)
                        }
                    }
                }

                // Mock Photo Card 2
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(80.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = PurpleAccent)
                            Text("Breaker", fontSize = 10.sp, color = PurpleAccent)
                        }
                    }
                }

                // Add button
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .size(80.dp)
                        .clickable { onAttachPhoto() }
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // Bottom CTA Button
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = onAnalyzeWithAi,
                    enabled = !isAnalyzing && userDescription.isNotBlank(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("analyze_with_ai_button")
                ) {
                    if (isAnalyzing) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Analizando con Inteligencia Artificial...", fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Default.Psychology, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Analizar con IA", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
