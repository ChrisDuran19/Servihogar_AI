package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PurpleAccent

data class GuideStep(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val accentColor: Color,
    val badgeText: String,
    val descriptionLines: List<String>,
    val highlights: List<Pair<String, String>>
)

@Composable
fun FirstTimeNavigationGuideScreen(
    onFinishGuide: () -> Unit,
    onGoToLogin: () -> Unit
) {
    var currentStepIndex by remember { mutableIntStateOf(0) }

    val guideSteps = listOf(
        GuideStep(
            title = "1. Diagnóstico Inteligente con IA",
            subtitle = "Evaluación inmediata de problemas del hogar",
            icon = Icons.Default.AutoAwesome,
            accentColor = PurpleAccent,
            badgeText = "IA Generativa 98% Precisión",
            descriptionLines = listOf(
                "Escribe o graba por nota de voz qué está sucediendo (ej: 'El breaker se dispara al prender el aire').",
                "Sube fotos de la falla o tablero eléctrico.",
                "La IA genera un diagnóstico instantáneo con precio estimado en pesos colombianos y lista de repuestos."
            ),
            highlights = listOf(
                "Ahorro de Tiempo" to "Cotización en segundos sin esperar visitas preliminares.",
                "Transparencia Total" to "Rango de precio justo antes de contratar a cualquier técnico."
            )
        ),
        GuideStep(
            title = "2. Selección y Rastreo GPS en Vivo",
            subtitle = "Técnicos verificados en tu ciudad",
            icon = Icons.Default.Map,
            accentColor = IndigoPrimary,
            badgeText = "Geolocalización Real",
            descriptionLines = listOf(
                "Explora perfiles de especialistas cercanos con calificaciones y mapa en vivo.",
                "Sigue en tiempo real la ruta exacta del técnico en su motocicleta hasta tu dirección.",
                "Tiempo estimado de llegada dinámico (ETA) e interacción directa."
            ),
            highlights = listOf(
                "Seguridad Geográfica" to "Ruta en mapa con placa del vehículo y teléfono verificado.",
                "Técnicos de Confianza" to "Filtra por calificación de estrellas y especialidad."
            )
        ),
        GuideStep(
            title = "3. Código de Seguridad en 2 Pasos",
            subtitle = "Garantía de servicio e inicio seguro",
            icon = Icons.Default.Shield,
            accentColor = EmeraldSuccess,
            badgeText = "Protección Doble Clave",
            descriptionLines = listOf(
                "Código 1 (Llegada): Clave única de 6 dígitos que entregas al técnico al tocar tu puerta para confirmar su presencia.",
                "Código 2 (Finalización): Segunda clave que ingresa el técnico SOLO cuando estés 100% satisfecho con el trabajo.",
                "Evita suplantación de identidad y asegura la liberación justa del pago."
            ),
            highlights = listOf(
                "Clave de Llegada" to "Bloquea falsas visitas y verifica la identidad.",
                "Clave de Cierre" to "Liberación de dinero únicamente tras revisión completa."
            )
        ),
        GuideStep(
            title = "4. Billetera Digital & ServiCoins",
            subtitle = "Pagos automatizados y sistema de recompensas",
            icon = Icons.Default.AccountBalanceWallet,
            accentColor = Color(0xFF0284C7),
            badgeText = "Custodia Escrow Segura",
            descriptionLines = listOf(
                "Paga o recarga tu saldo fácilmente mediante Nequi, Bancolombia o Tarjeta.",
                "Tus fondos permanecen retenidos en garantía de forma segura hasta que autorices el servicio.",
                "Gana ServiCoins por cada servicio completado e intercámbialos por mantenimiento gratis."
            ),
            highlights = listOf(
                "Modo Automático" to "Descuento directo del saldo al validar la clave final.",
                "Transacciones Transparentes" to "Historial detallado con facturas digitales."
            )
        ),
        GuideStep(
            title = "5. Verificación Biométrica de Identidad",
            subtitle = "Red 100% segura de clientes y profesionales",
            icon = Icons.Default.Fingerprint,
            accentColor = Color(0xFFD97706),
            badgeText = "Biometría Cédula + Rostro",
            descriptionLines = listOf(
                "Registro con cámara en vivo realizando 3 movimientos (parpadeo, giro, sonrisa) para verificar persona real.",
                "Escaneo de Cédula de Ciudadanía Colombiana con extracción de código de barras e inspección holográfica.",
                "Ingreso rápido por huella o reconocimiento facial para la segunda vez que abras la aplicación."
            ),
            highlights = listOf(
                "Cero Cuentas Falsas" to "Tanto clientes como técnicos pasan validación judicial/biométrica.",
                "Ingreso en 1 Segundo" to "Acceso instantáneo con tu rostro o huella."
            )
        )
    )

    val currentStep = guideSteps[currentStepIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 3.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
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
                                text = "Guía de Primera Instalación",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Paso ${currentStepIndex + 1} de ${guideSteps.size}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Text(
                        text = "Omitir",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = IndigoPrimary,
                        modifier = Modifier
                            .clickable { onFinishGuide() }
                            .padding(8.dp)
                            .testTag("skip_guide_button")
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Progress Bar Dots
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    guideSteps.indices.forEach { index ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    if (index <= currentStepIndex) IndigoPrimary
                                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                )
                        )
                    }
                }
            }
        }

        // Body Content
        Crossfade(
            targetState = currentStepIndex,
            modifier = Modifier.weight(1f)
        ) { stepIdx ->
            val step = guideSteps[stepIdx]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Feature Hero Icon Badge
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(step.accentColor.copy(alpha = 0.12f))
                        .border(2.dp, step.accentColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = step.icon,
                        contentDescription = null,
                        tint = step.accentColor,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = step.accentColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = step.badgeText,
                        color = step.accentColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = step.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = step.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Explanation Cards
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "¿Cómo Funciona?",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = IndigoPrimary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        step.descriptionLines.forEach { line ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = step.accentColor,
                                    modifier = Modifier
                                        .size(18.dp)
                                        .padding(top = 2.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = line,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Highlights Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    step.highlights.forEach { (title, desc) ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = IndigoContainer.copy(alpha = 0.4f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = title,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IndigoPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = desc,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // First time launch disclaimer card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Smartphone, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Nota de Primera Instalación: Esta guía interactiva solo se mostrará la primera vez que abras la app. En futuras ocasiones entrarás directo al Inicio.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        // Bottom Action Buttons
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (currentStepIndex > 0) {
                    OutlinedButton(
                        onClick = { currentStepIndex-- },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Anterior")
                    }
                }

                Button(
                    onClick = {
                        if (currentStepIndex < guideSteps.size - 1) {
                            currentStepIndex++
                        } else {
                            onFinishGuide()
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    modifier = Modifier.weight(2f).height(48.dp).testTag("guide_next_button")
                ) {
                    Text(
                        text = if (currentStepIndex < guideSteps.size - 1) "Siguiente Módulo" else "¡Entendido, Comenzar!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
