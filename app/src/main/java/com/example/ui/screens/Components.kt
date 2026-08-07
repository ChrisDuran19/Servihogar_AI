package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.SlateBackground
import com.example.ui.viewmodel.AppScreen

@Composable
fun ServiHogarTopBar(
    userName: String = "Andrés",
    userRolePro: Boolean = false,
    walletBalanceCop: Double = 150000.0,
    faceVerified: Boolean = true,
    onToggleRole: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onWalletClick: () -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onProfileClick() }
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(IndigoPrimary, PurpleAccent)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userName.take(1),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }

                    if (faceVerified) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(EmeraldSuccess)
                                .border(1.5.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = userName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "IA Verificado",
                            tint = EmeraldSuccess,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Text(
                        text = if (userRolePro) "Técnico Profesional" else "Cliente Propietario",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Wallet Balance Chip
                Surface(
                    onClick = onWalletClick,
                    shape = RoundedCornerShape(16.dp),
                    color = EmeraldContainer,
                    modifier = Modifier.testTag("topbar_wallet_chip")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = "Billetera",
                            tint = EmeraldSuccess,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$${(walletBalanceCop / 1000).toInt()}k",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldSuccess
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Role Switcher Chip
                Surface(
                    onClick = onToggleRole,
                    shape = RoundedCornerShape(16.dp),
                    color = if (userRolePro) PurpleAccent else MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.testTag("role_switch_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (userRolePro) Icons.Default.Build else Icons.Default.Person,
                            contentDescription = "Rol",
                            tint = if (userRolePro) Color.White else IndigoPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (userRolePro) "Pro" else "Cliente",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (userRolePro) Color.White else IndigoPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ServiHogarBottomBar(
    currentScreen: AppScreen,
    userRolePro: Boolean,
    onNavigate: (AppScreen) -> Unit,
    activeCount: Int = 0
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentScreen == AppScreen.HOME,
            onClick = { onNavigate(AppScreen.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = IndigoPrimary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_home")
        )

        NavigationBarItem(
            selected = currentScreen == AppScreen.WALLET,
            onClick = { onNavigate(AppScreen.WALLET) },
            icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Billetera") },
            label = { Text("Billetera", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = IndigoPrimary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_wallet")
        )

        // Center Action (+)
        NavigationBarItem(
            selected = currentScreen == AppScreen.DESCRIBE_ISSUE,
            onClick = { onNavigate(AppScreen.DESCRIBE_ISSUE) },
            icon = {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(IndigoPrimary, PurpleAccent)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Diagnosticar",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            label = { Text("IA Request", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = IndigoPrimary) },
            modifier = Modifier.testTag("nav_add_request")
        )

        NavigationBarItem(
            selected = currentScreen == AppScreen.PROFILE,
            onClick = { onNavigate(AppScreen.PROFILE) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = IndigoPrimary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_profile")
        )

        NavigationBarItem(
            selected = currentScreen == AppScreen.DIGITAL_HISTORY,
            onClick = { onNavigate(AppScreen.DIGITAL_HISTORY) },
            icon = {
                if (activeCount > 0) {
                    BadgedBox(badge = { Badge { Text("$activeCount") } }) {
                        Icon(Icons.Default.List, contentDescription = "Servicios")
                    }
                } else {
                    Icon(Icons.Default.List, contentDescription = "Servicios")
                }
            },
            label = { Text("Servicios", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = IndigoPrimary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_services")
        )
    }
}

@Composable
fun StepProgressBar(currentStep: Int) {
    // 1: Descripción, 2: Diagnóstico IA, 3: Selección Técnico, 4: En Camino / En progreso
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val steps = listOf("Descripción", "Diagnóstico IA", "Técnico")
        steps.forEachIndexed { index, stepName ->
            val stepNumber = index + 1
            val isCompleted = stepNumber < currentStep
            val isCurrent = stepNumber == currentStep

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCompleted -> EmeraldSuccess
                                isCurrent -> IndigoPrimary
                                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    } else {
                        Text(
                            text = "$stepNumber",
                            color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (index < steps.size - 1) {
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(3.dp)
                            .background(
                                if (stepNumber < currentStep) EmeraldSuccess else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun OtpCodeDisplayBox(code: String, label: String, expiryTime: String = "04:59") {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Digits Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                code.padStart(6, '0').forEach { digit ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        border = androidx.compose.foundation.BorderStroke(1.dp, IndigoPrimary.copy(alpha = 0.3f))
                    ) {
                        Box(
                            modifier = Modifier.size(width = 40.dp, height = 50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = digit.toString(),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = IndigoPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = PurpleAccent,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "El código expira en $expiryTime",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun SimulatedTrackingMapCanvas(
    techName: String = "Carlos Martínez",
    etaMinutes: Int = 15,
    progressPercent: Float = 0.6f
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val w = size.width
                val h = size.height

                // Map background roads
                drawRect(color = Color(0xFFE2E8F0))

                // Green parks
                drawCircle(center = Offset(w * 0.2f, h * 0.3f), radius = 60f, color = Color(0xFFDCFCE7))
                drawCircle(center = Offset(w * 0.8f, h * 0.7f), radius = 80f, color = Color(0xFFDCFCE7))

                // Grid roads
                val roadColor = Color.White
                val roadStroke = 20f

                // Horizontal roads
                drawLine(roadColor, Offset(0f, h * 0.25f), Offset(w, h * 0.25f), roadStroke)
                drawLine(roadColor, Offset(0f, h * 0.5f), Offset(w, h * 0.5f), roadStroke)
                drawLine(roadColor, Offset(0f, h * 0.75f), Offset(w, h * 0.75f), roadStroke)

                // Vertical roads
                drawLine(roadColor, Offset(w * 0.3f, 0f), Offset(w * 0.3f, h), roadStroke)
                drawLine(roadColor, Offset(w * 0.65f, 0f), Offset(w * 0.65f, h), roadStroke)

                // Route path from Tech to User Home
                val startPoint = Offset(w * 0.2f, h * 0.25f)
                val cornerPoint = Offset(w * 0.65f, h * 0.25f)
                val destination = Offset(w * 0.65f, h * 0.75f)

                val routePath = Path().apply {
                    moveTo(startPoint.x, startPoint.y)
                    lineTo(cornerPoint.x, cornerPoint.y)
                    lineTo(destination.x, destination.y)
                }

                drawPath(
                    path = routePath,
                    color = Color(0xFF4F46E5),
                    style = Stroke(
                        width = 10f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f), 0f)
                    )
                )

                // Current position on route based on progress
                val currentTechPos = if (progressPercent < 0.5f) {
                    val p = progressPercent * 2
                    Offset(startPoint.x + (cornerPoint.x - startPoint.x) * p, startPoint.y)
                } else {
                    val p = (progressPercent - 0.5f) * 2
                    Offset(cornerPoint.x, cornerPoint.y + (destination.y - cornerPoint.y) * p)
                }

                // Tech location pin
                drawCircle(color = Color(0xFF7C3AED), radius = 22f, center = currentTechPos)
                drawCircle(color = Color.White, radius = 10f, center = currentTechPos)

                // User destination pin
                drawCircle(color = Color(0xFFEF4444), radius = 22f, center = destination)
                drawCircle(color = Color.White, radius = 10f, center = destination)
            }

            // Overlay ETA badge
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                shadowElevation = 4.dp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(EmeraldSuccess)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$techName va en camino • ETA: $etaMinutes min",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
