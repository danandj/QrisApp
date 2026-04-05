package com.example.qrisapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qrisapp.viewmodel.ProfileViewModel

val ProfileBg = Color(0xFFF8F9FB)
val LogoutRed = Color(0xFFFDE2DE)
val LogoutText = Color(0xFF963E3E)
val DeepTeal = Color(0xFF2D5D5A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogoutSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isLoggedOut) {
        if (uiState.isLoggedOut) {
            onLogoutSuccess()
        }
    }

    val versionName = remember {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (_: Exception) {
            "1.0.0"
        }
    }

    Scaffold(
        containerColor = ProfileBg,
        topBar = {
            TopAppBar(
                title = { Text("Profile", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2D5D5A)),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // --- AVATAR SECTION ---
            Box(contentAlignment = Alignment.BottomEnd) {
                // Main Photo
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(DeepTeal)
                        .border(4.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(80.dp))
                }

                // Edit Button (Pencil)
                Surface(
                    modifier = Modifier
                        .size(42.dp)
                        .offset(x = (-4).dp, y = (-4).dp)
                        .border(3.dp, Color.White, CircleShape),
                    shape = CircleShape,
                    color = DeepTeal,
                    shadowElevation = 4.dp
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = uiState.username, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
            Text(text = uiState.fullName, color = Color(0xFF6C8A91), fontSize = 18.sp)

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { viewModel.logout() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LogoutRed),
                shape = RoundedCornerShape(32.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = LogoutText)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("LOGOUT", color = LogoutText, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "AYOK Pay v$versionName • STTI Tanjungpinang",
                fontSize = 13.sp,
                color = Color.LightGray,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
