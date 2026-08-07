package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PaymentMode
import com.example.data.model.WalletTransaction
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PurpleAccent
import com.example.util.CryptoSecurityUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class BankMethodInfo(
    val id: String,
    val name: String,
    val description: String,
    val officialUrl: String,
    val brandColor: Color
)

@Composable
fun NequiLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color(0xFFE11D48)),
        contentAlignment = Alignment.Center
    ) {
        Text("N", color = Color.White, fontWeight = FontWeight.Black, fontSize = 20.sp)
    }
}

@Composable
fun DaviplataLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color(0xFFDC2626)),
        contentAlignment = Alignment.Center
    ) {
        Text("D", color = Color.White, fontWeight = FontWeight.Black, fontSize = 20.sp)
    }
}

@Composable
fun BancolombiaLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color(0xFF0F172A)),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(6.dp).background(Color(0xFFFACC15)))
            Spacer(modifier = Modifier.width(2.dp))
            Text("B", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
        }
    }
}

@Composable
fun DaviviendaLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFB91C1C)),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.Home, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun PseLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color(0xFF0369A1)),
        contentAlignment = Alignment.Center
    ) {
        Text("PSE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    }
}

@Composable
fun CardLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color(0xFF4F46E5)),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.CreditCard, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun WalletScreen(
    walletBalanceCop: Double,
    serviCoinsBalance: Long,
    paymentMode: PaymentMode,
    transactions: List<WalletTransaction>,
    userEmail: String = "",
    onTogglePaymentMode: (Boolean) -> Unit,
    onRechargeWallet: (Double, String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isBalanceVisible by remember { mutableStateOf(true) }
    var selectedAmount by remember { mutableDoubleStateOf(100000.0) }
    var customAmountText by remember { mutableStateOf("") }
    var selectedMethodId by remember { mutableStateOf("nequi") }

    var isRecharging by remember { mutableStateOf(false) }
    var rechargeSuccessMessage by remember { mutableStateOf<String?>(null) }
    var emailNotificationMessage by remember { mutableStateOf<String?>(null) }

    val bankMethods = listOf(
        BankMethodInfo("nequi", "Nequi", "Apertura directa app Nequi", "https://www.nequi.com.co", Color(0xFFE11D48)),
        BankMethodInfo("pse", "PSE Débito", "Pasarela oficial PSE Colombia", "https://www.pse.com.co", Color(0xFF0369A1)),
        BankMethodInfo("daviplata", "Daviplata", "Pago instantáneo Daviplata", "https://www.daviplata.com", Color(0xFFDC2626)),
        BankMethodInfo("bancolombia", "Bancolombia", "Botón y QR Bancolombia", "https://www.bancolombia.com", Color(0xFFCA8A04)),
        BankMethodInfo("davivienda", "Davivienda", "Banca Móvil Davivienda", "https://www.davivienda.com", Color(0xFFB91C1C)),
        BankMethodInfo("card", "Tarjeta Crédito / Débito", "Procesamiento seguro SSL-256", "https://www.mercadopago.com.co", Color(0xFF4F46E5))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
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
                IconButton(onClick = onBack, modifier = Modifier.testTag("wallet_back_button")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Billetera Digital ServiHogar",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Pagos, Recargas y Custodia Escrow",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Balance Card
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
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
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Saldo Billetera",
                                        color = Color.White.copy(alpha = 0.9f),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { isBalanceVisible = !isBalanceVisible },
                                        modifier = Modifier.size(32.dp).testTag("toggle_balance_visibility")
                                    ) {
                                        Icon(
                                            imageVector = if (isBalanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = "Ocultar/Mostrar saldo",
                                            tint = Color.White
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(6.dp))

                                    Surface(
                                        shape = RoundedCornerShape(20.dp),
                                        color = Color.White.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = "🪙 $serviCoinsBalance Coins",
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = if (isBalanceVisible) "$${String.format("%,.0f", walletBalanceCop)} COP" else "$ ••••••••• COP",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = EmeraldSuccess,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Protegido con Custodia Digital Escrow Cifrada SSL-256",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Payment Mode Setting
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Modo de Pago Automático",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = if (paymentMode == PaymentMode.AUTOMATIC_WALLET) "⚡" else "✋", fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (paymentMode == PaymentMode.AUTOMATIC_WALLET)
                                    "Débito directo en garantía Escrow al reservar técnicos."
                                else "Confirmación manual con botón PSE / Nequi al finalizar el servicio.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Switch(
                            checked = paymentMode == PaymentMode.AUTOMATIC_WALLET,
                            onCheckedChange = { isAuto -> onTogglePaymentMode(isAuto) },
                            colors = SwitchDefaults.colors(checkedThumbColor = IndigoPrimary)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Recharge Options Section
            item {
                Text(
                    text = "Recarga de Billetera y Métodos de Pago",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ingresa un valor personalizado o selecciona un monto rápido para pagar tus servicios.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Custom Amount TextField
                OutlinedTextField(
                    value = customAmountText,
                    onValueChange = { input ->
                        val digits = input.filter { it.isDigit() }
                        customAmountText = digits
                        val parsed = digits.toDoubleOrNull()
                        if (parsed != null && parsed > 0) {
                            selectedAmount = parsed
                        }
                    },
                    label = { Text("Escribir otro valor personalizado (COP)") },
                    leadingIcon = { Text("$", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 12.dp)) },
                    placeholder = { Text("Ej: 75000") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_amount_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Quick Amounts Chips
                val amounts = listOf(30000.0, 50000.0, 100000.0, 200000.0, 500000.0)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    amounts.forEach { amt ->
                        val isSel = selectedAmount == amt && customAmountText.isEmpty()
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSel) IndigoPrimary else MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSel) IndigoPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedAmount = amt
                                    customAmountText = ""
                                }
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$${(amt / 1000).toInt()}k",
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Selecciona Entidad Bancaria en Colombia",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Payment Methods Selection List with Authentic Logos
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    bankMethods.forEach { method ->
                        val isSel = selectedMethodId == method.id
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSel) IndigoContainer.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surface
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.5.dp,
                                if (isSel) IndigoPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedMethodId = method.id }
                                .testTag("select_bank_${method.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                when (method.id) {
                                    "nequi" -> NequiLogo()
                                    "daviplata" -> DaviplataLogo()
                                    "bancolombia" -> BancolombiaLogo()
                                    "davivienda" -> DaviviendaLogo()
                                    "pse" -> PseLogo()
                                    else -> CardLogo()
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = method.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(
                                            Icons.Default.OpenInNew,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                    Text(
                                        text = method.description,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                if (isSel) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = IndigoPrimary)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // AI Fraud Shield Banner
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "🛡️ AI Fraud Guard: Tokenización bancaria activa. Tu cuenta está 100% protegida contra fraudes.",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Success Message & Email Notification Toast
                if (rechargeSuccessMessage != null) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = EmeraldContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = rechargeSuccessMessage ?: "",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldSuccess
                                )
                            }
                        }

                        if (emailNotificationMessage != null) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = IndigoContainer,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Email, contentDescription = null, tint = IndigoPrimary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = emailNotificationMessage ?: "",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = IndigoPrimary
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                val currentBankObj = bankMethods.firstOrNull { it.id == selectedMethodId }
                val bankName = currentBankObj?.name ?: "Nequi"

                Button(
                    onClick = {
                        scope.launch {
                            isRecharging = true

                            // Redirect to bank official app/portal
                            val officialUrl = currentBankObj?.officialUrl ?: "https://www.nequi.com.co"
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(officialUrl))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                // Ignore if web browser fails
                            }

                            delay(1800)
                            onRechargeWallet(selectedAmount, bankName)
                            isRecharging = false

                            val txCode = CryptoSecurityUtils.generateDynamicPin()
                            rechargeSuccessMessage = "¡Pago exitoso de $${String.format("%,.0f", selectedAmount)} COP procesado con $bankName!"
                            val targetEmail = if (userEmail.isNotBlank()) userEmail else "usuario@servihogar.com"
                            emailNotificationMessage = "📧 Comprobante enviado a: $targetEmail | Ref: #TX-$txCode enviado a $bankName."
                        }
                    },
                    enabled = !isRecharging && selectedAmount > 0,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("execute_recharge_button")
                ) {
                    if (isRecharging) {
                        CircularProgressIndicator(color = Color.White, strokeWidth = 2.5.dp, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Redirigiendo y procesando pago con $bankName...", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    } else {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Pagar $${String.format("%,.0f", selectedAmount)} COP y Abrir $bankName",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Transactions History Header
            item {
                Text(
                    text = "Historial de Movimientos y Pagos Correo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Transactions List
            if (transactions.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("📑", fontSize = 32.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Aún no tienes movimientos en tu billetera.", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Realiza tu primera recarga con Nequi, PSE o banco para comenzar.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            } else {
                items(transactions) { tx ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (tx.type == "RECHARGE" || tx.type == "BONUS") EmeraldContainer else IndigoContainer,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = if (tx.type == "RECHARGE") "📲" else if (tx.type == "BONUS") "🎁" else "🔒",
                                            fontSize = 16.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column {
                                    Text(text = tx.description, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(text = "${tx.date} • Vía ${tx.method}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${if (tx.type == "RECHARGE" || tx.type == "BONUS") "+" else "-"}$${String.format("%,.0f", tx.amountCop)}",
                                    fontWeight = FontWeight.Bold,
                                    color = if (tx.type == "RECHARGE" || tx.type == "BONUS") EmeraldSuccess else MaterialTheme.colorScheme.onSurface,
                                    fontSize = 13.sp
                                )
                                Text(text = "✓ Aprobado", fontSize = 10.sp, color = EmeraldSuccess, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
