package com.example.qrisapp.ui.screens

import LoadingDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qrisapp.viewmodel.PinViewModel

@Composable
fun PinScreen(
    viewModel: PinViewModel,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    val maxPinLength = 6

    LaunchedEffect(viewModel.isSuccess) {
        if (viewModel.isSuccess) {
            onSuccess()
        }
    }

    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    if (viewModel.isLoading) {
        LoadingDialog(true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Logo
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color(0xFF2D5D5A), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Lock, null, tint = Color.White, modifier = Modifier.size(40.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("AYOK Pay", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF2D5D5A))
        Spacer(modifier = Modifier.height(20.dp))
        Text("Masukan PIN Untuk Melanjutkan", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))
        Text("Silahkan masukan 6 digit PIN anda", color = Color.Gray)

        Spacer(modifier = Modifier.height(40.dp))

        // PIN Dots Indicator
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            repeat(maxPinLength) { index ->
                val filled = index < viewModel.pinInput.length
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(if (filled) Color(0xFF2D5D5A) else Color(0xFFE0E0E0), CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = viewModel.errorMessage?.takeIf { it.isNotBlank() } ?: "",
            color = Color.Red,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Keypad Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(40.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F4F5))
        ) {
            val keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "clear", "0", "delete")

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .padding(24.dp)
                    .height(320.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(keys) { key ->
                    when (key) {
                        "clear" -> {
                            IconButton(onClick = {
                                if (viewModel.pinInput.isNotEmpty()) {
                                    viewModel.onPinChange("")
                                }
                            }) {
                                Icon(Icons.Default.Clear, null, tint = Color.Gray, modifier = Modifier.size(30.dp))
                            }
                        }
                        "delete" -> {
                            IconButton(onClick = {
                                if (viewModel.pinInput.isNotEmpty()) {
                                    viewModel.onPinChange(viewModel.pinInput.dropLast(1))
                                }
                            }) {
                                Icon(Icons.AutoMirrored.Filled.Backspace, null, tint = Color.Gray)
                            }
                        }
                        else -> {
                            // Tombol Angka
                            Box(
                                modifier = Modifier
                                    .aspectRatio(1.5f)
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(Color.White)
                                    .clickable {
                                        viewModel.resetError()
                                        if (viewModel.pinInput.length < maxPinLength) {
                                            viewModel.onPinChange(viewModel.pinInput + key)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(key, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}