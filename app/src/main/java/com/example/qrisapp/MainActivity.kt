package com.example.qrisapp

// Android
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

// Compose - UI
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

// Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.qrisapp.data.AuthRepository
import com.example.qrisapp.data.BalanceRepository
import com.example.qrisapp.data.PaymentRepository
import com.example.qrisapp.data.SessionManager
import com.example.qrisapp.ui.screens.DashboardScreen
import com.example.qrisapp.ui.screens.LoginScreen
import com.example.qrisapp.ui.screens.PinScreen
import com.example.qrisapp.ui.screens.ProfileScreen
import com.example.qrisapp.ui.screens.ScanQrScreen

// Screens
import com.example.qrisapp.ui.theme.QrisAppTheme
import com.example.qrisapp.viewmodel.DashboardViewModel
import com.example.qrisapp.viewmodel.LoginViewModel
import com.example.qrisapp.viewmodel.PinViewModel
import com.example.qrisapp.viewmodel.ProfileViewModel
import com.example.qrisapp.viewmodel.ScanQrViewModel


/**
 * Navigation Routes untuk aplikasi Presensi
 */
object Routes {
    const val Login = "login"

    const val Home = "home"

    const val Pin = "pin"

    const val Scan = "scan"

    const val Profile = "profile"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupWindow()
        enableEdgeToEdge()
        setContent {
            QrisAppTheme {
                QrisNavigation()
            }
        }
    }

    /**
     * Setup window properties untuk edge-to-edge display
     */
    private fun setupWindow() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
        }
    }

    /**
     * Main navigation composable dengan semua routes
     */
    @Composable
    private fun QrisNavigation() {
        val navController = rememberNavController()
        val context = this@MainActivity
        
        // Repositories and Managers
        val authRepository = AuthRepository()
        val balanceRepository = BalanceRepository()
        val paymentRepository = PaymentRepository()
        val sessionManager = SessionManager(context)

        // Check login status to determine start destination
        var startDestination by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(Unit) {
            val user = sessionManager.getUser()
            startDestination = if (user != null) {
                Routes.Pin
            } else {
                Routes.Login
            }
        }

        if (startDestination == null) {
            return
        }

        NavHost(
            navController = navController,
            startDestination = startDestination!!,
            modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
        ) {

            // Login Screen
            composable(Routes.Login) {
                val loginViewModel: LoginViewModel = viewModel(
                    factory = LoginViewModel.Factory(authRepository, sessionManager)
                )
                
                LoginScreen(
                    viewModel = loginViewModel,
                    onLoginSuccess = {
                        navController.navigate(Routes.Pin) {
                            popUpTo(Routes.Login) { inclusive = true }
                        }
                    }
                )
            }

            // PIN Screen
            composable(Routes.Pin) {
                val pinViewModel: PinViewModel = viewModel(
                    factory = PinViewModel.Factory(authRepository, sessionManager)
                )
                
                PinScreen(
                    viewModel = pinViewModel,
                    onSuccess = {
                        navController.navigate(Routes.Home) {
                            popUpTo(Routes.Pin) { inclusive = true }
                        }
                    }
                )
            }

            // Dashboard / Home Screen
            composable(Routes.Home) {
                val dashboardViewModel: DashboardViewModel = viewModel(
                    factory = DashboardViewModel.Factory(sessionManager, balanceRepository, paymentRepository)
                )

                DashboardScreen(
                    viewModel = dashboardViewModel,
                    onScanQrClick = {
                        navController.navigate(Routes.Scan)
                    },
                    onSettingsClick = {
                        navController.navigate(Routes.Profile)
                    }
                )
            }

            // Profile Screen
            composable(Routes.Profile) {
                val profileViewModel: ProfileViewModel = viewModel(
                    factory = ProfileViewModel.Factory(sessionManager)
                )
                
                ProfileScreen(
                    viewModel = profileViewModel,
                    onLogoutSuccess = {
                        navController.navigate(Routes.Login) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            // Scan QR Screen
            composable(Routes.Scan) {
                val scanQrViewModel: ScanQrViewModel = viewModel(
                    factory = ScanQrViewModel.Factory(sessionManager, balanceRepository, paymentRepository)
                )

                ScanQrScreen(
                    viewModel = scanQrViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onSuccess = {
                        navController.navigate(Routes.Home) {
                            popUpTo(Routes.Home) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
