package com.example.qrisapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qrisapp.model.Payment
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import com.example.qrisapp.viewmodel.DashboardViewModel

val DashboardTeal = Color(0xFF355E5B)
val LightBg = Color(0xFFF8F9FB)
val CardTransBg = Color(0xFFF4F6F7)
val TextGray = Color(0xFF7A7A7A)

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onScanQrClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = LightBg,
        topBar = {
            TopAppBar(
                title = {
                    Text("AYO Pay", fontWeight = FontWeight.ExtraBold, color = DashboardTeal)
                },
                actions = {
                    // Avatar Placeholder
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(40.dp)
                            .background(DashboardTeal, CircleShape)
                            .clip(CircleShape)
                    ) {
                        Icon(Icons.Default.NotificationsNone, contentDescription = null, tint = Color.White, modifier = Modifier.fillMaxSize())
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LightBg)
            )
        },
        bottomBar = { BottomNavigation(
            onScanQrClick = {
                onScanQrClick()
            },
            onSettingsClick = {
                onSettingsClick()
            }
        ) }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = { viewModel.loadDashboardData() },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                // 1. Balance Card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        shape = RoundedCornerShape(60.dp),
                        colors = CardDefaults.cardColors(containerColor = DashboardTeal)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("TOTAL BALANCE", color = Color.White.copy(alpha = 0.7f), letterSpacing = 1.sp, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text("Rp", color = Color.White, fontSize = 24.sp, modifier = Modifier.padding(bottom = 8.dp, end = 4.dp))
                                val balanceText = try {
                                    String.format("%,d", uiState.balance.toLong()).replace(',', '.')
                                } catch (_: Exception) {
                                    "Gagal memuat saldo"
                                }
                                Text(balanceText, color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.Black)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Surface(
                                color = Color.White.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Outlined.VerifiedUser, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(uiState.userName, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // 2. QR Button
                item {
                    Button(
                        onClick = {
                            onScanQrClick()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DashboardTeal),
                        shape = RoundedCornerShape(45.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                modifier = Modifier.size(48.dp),
                                shape = CircleShape,
                                color = Color.White
                            ) {
                                Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = DashboardTeal, modifier = Modifier.padding(12.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Bayar via QR", fontSize = 20.sp, fontWeight = FontWeight.Bold,  color = Color.White)
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(Icons.Default.ChevronRight, contentDescription = null,  tint = Color.White)
                        }
                    }
                }

                // 3. Transaction Header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Riwayat Transaksi", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Lihat Semua", color = DashboardTeal, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }

                // 4. Transaction List
                items(uiState.transactions) { transaction ->
                    TransactionItem(transaction)
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun TransactionItem(item: Payment) {
    val amountText = try {
        String.format("%,d", (item.jumlah_bayar ?: 0.0).toLong()).replace(',', '.')
    } catch (_: Exception) {
        "0"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = CardTransBg)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Payment, contentDescription = null, tint = DashboardTeal)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.referensi_id, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                Text(item.tanggal_bayar, fontSize = 12.sp, color = TextGray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Transaction ID: " + item.transaksi_id.toString(), fontWeight = FontWeight.Bold, fontSize = 10.sp)
                Text("Rp $amountText", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                Text("Berhasil", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFF2E8B57))
            }
        }
    }
}

@Composable
fun BottomNavigation(
    onScanQrClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Dashboard", "Scan", "Settings")
    val icons = listOf(
        Icons.Filled.GridView,
        Icons.Outlined.QrCodeScanner,
        Icons.Outlined.Settings
    )

    val activeColor = Color(0xFF355E5B) // Warna ungu sesuai gambar baru
    val inactiveColor = Color.LightGray

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp), // Beri ruang bawah
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(80.dp),
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp, bottomStart = 40.dp, bottomEnd = 40.dp),
            color = Color.White,
            shadowElevation = 3.dp
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                items.forEachIndexed { index, item ->
                    if (index == 1) {
                        Spacer(modifier = Modifier.width(60.dp))
                    } else {
                        val isSelected = selectedItem == index
                        val interactionSource = remember { MutableInteractionSource() }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    selectedItem = index
                                    if (index == 2) {
                                        onSettingsClick()
                                    }
                                },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = icons[index],
                                contentDescription = item,
                                tint = if (isSelected) activeColor else inactiveColor,
                                modifier = Modifier.size(26.dp)
                            )
                            Text(
                                text = item,
                                fontSize = 11.sp,
                                color = if (isSelected) activeColor else inactiveColor,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        val interactionSource = remember { MutableInteractionSource() }
        Box(
            modifier = Modifier
                .offset(y = (-40).dp)
                .size(70.dp)
                .background(activeColor, CircleShape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    selectedItem = 1
                    onScanQrClick()
                }
                .padding(15.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.QrCodeScanner,
                contentDescription = "Scan",
                tint = Color.White,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}