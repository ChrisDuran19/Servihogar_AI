package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PurpleAccent

@Composable
fun ProModeScreen(
    onVerifyCodeFromClient: (String) -> Unit,
    onBack: () -> Unit
) {
    var otpEntered by remember { mutableStateOf("") }
    var codeAcceptedMessage by remember { mutableStateOf<String?>(null) }

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
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Ecosistema Profesional Técnico",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(shape = RoundedCornerShape(12.dp), color = PurpleAccent) {
                            Text("PRO", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    Text(
                        text = "Gestión de agenda, ingresos y asistente IA",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Earnings Dashboard Banner
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(IndigoPrimary, PurpleAccent)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Text("Ingresos de este mes (Escrow liberado)", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("$1.450.000 COP", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ProStatChip("Servicios", "18 completados")
                            ProStatChip("Puntualidad", "99.2%")
                            ProStatChip("Calificación", "4.9 ★")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Security Code Validator Tool for Techs
            Text(
                text = "Validación de Código de Seguridad del Cliente",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Ingresa el código OTP de 6 dígitos que te entregó el cliente para iniciar o finalizar el servicio:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = otpEntered,
                            onValueChange = { otpEntered = it },
                            placeholder = { Text("Ej: 872193") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("pro_otp_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Button(
                            onClick = {
                                if (otpEntered.length >= 5) {
                                    onVerifyCodeFromClient(otpEntered)
                                    codeAcceptedMessage = "¡Código de seguridad validado correctamente! Estado actualizado."
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                            modifier = Modifier.testTag("pro_verify_otp_button")
                        ) {
                            Text("Validar")
                        }
                    }

                    if (codeAcceptedMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = codeAcceptedMessage!!,
                            color = EmeraldSuccess,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // AI Technician Assistant Section
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Psychology, contentDescription = null, tint = IndigoPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Asistente IA para el Profesional", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = IndigoPrimary)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("• Materiales sugeridos para llevar hoy: Breaker 20A, Cable AWG 12, Multímetro dielectrico.", fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• Sugerencia de precio justo IA: $140.000 - $160.000 COP según distancia y repuestos.", fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• Ruta optimizada: Tu próximo servicio está a solo 8 minutos de tu ubicación actual.", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Incoming Request Opportunities
            Text("Oportunidades de servicios cercanos", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))

            JobOpportunityCard("Electricidad", "Instalación de interruptores conmutados", "Cra 70 # 45 - 12", "1.5 km", "$130.000")
            Spacer(modifier = Modifier.height(10.dp))
            JobOpportunityCard("Aires Acondicionados", "Mantenimiento preventivo minisplit 12000 BTU", "Calle 33 # 80 - 20", "2.8 km", "$180.000")
        }
    }
}

@Composable
fun ProStatChip(label: String, value: String) {
    Column {
        Text(label, fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun JobOpportunityCard(category: String, title: String, address: String, distance: String, price: String) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(category, fontSize = 11.sp, color = IndigoPrimary, fontWeight = FontWeight.Bold)
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text("$address • $distance", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(price, fontWeight = FontWeight.Bold, color = EmeraldSuccess, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = { },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    Text("Aceptar", fontSize = 11.sp)
                }
            }
        }
    }
}
