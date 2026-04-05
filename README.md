# AYOK Pay

**AYOK Pay** adalah aplikasi pembayaran berbasis QRIS yang dirancang untuk memudahkan pengguna dalam melakukan transaksi digital secara cepat, aman, dan efisien. Aplikasi ini memungkinkan pengguna untuk memindai kode QR, mengelola saldo, dan memantau riwayat transaksi secara real-time.

## 🚀 Fitur Utama
- **Login & Keamanan PIN**: Autentikasi ganda untuk memastikan keamanan akun pengguna.
- **Scan QRIS**: Pembayaran mudah dengan memindai kode QR menggunakan kamera ponsel (didukung oleh ML Kit).
- **Dashboard Interaktif**: Ringkasan saldo dan riwayat transaksi terbaru.
- **Manajemen Profil**: Informasi pengguna dan opsi logout yang aman.
- **Refresh Data**: Tarik untuk menyegarkan (Pull-to-refresh) data dashboard.

## 🛠️ Tech Stack
- **Bahasa**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Modern Android UI toolkit)
- **Backend Service**: [Supabase](https://supabase.com/) (PostgreSQL Database & Auth)
- **Networking**: [Ktor](https://ktor.io/) (Digunakan oleh Supabase SDK)
- **Local Storage**: [Jetpack DataStore](https://developer.android.com/topic/libraries/architecture/datastore) (Preferences)
- **Image Processing**: [Google ML Kit](https://developers.google.com/ml-kit) (Barcode Scanning)
- **Camera Library**: [CameraX](https://developer.android.com/jetpack/androidx/releases/camera)

## 📦 Library & Dependency Utama
- **Navigation Compose**: Navigasi antar layar yang seamless.
- **Supabase-kt**: Integrasi database real-time dengan Supabase.
- **Material 3**: Implementasi desain Material Design terbaru dari Google.
- **Kotlinx Serialization**: Untuk pengolahan data JSON yang type-safe.

## 🏗️ Arsitektur & Design Pattern
Aplikasi ini mengikuti prinsip **Clean Architecture** dengan implementasi **MVVM (Model-View-ViewModel)**:
- **Repository Pattern**: Memisahkan logika pengambilan data (Network/Local) dari ViewModel.
- **Observer Pattern**: Menggunakan `StateFlow` untuk mengirimkan state data ke UI secara reaktif.
- **Singleton**: Digunakan pada `SupabaseClient` untuk memastikan satu instance koneksi ke backend.
- **Dependency Injection (Manual)**: Injeksi repository ke dalam ViewModel melalui Factory.

## 📁 Struktur Folder Project
```text
app/src/main/java/com/example/qrisapp/
├── data/           # Repositori data & SessionManager (DataStore)
├── model/          # Data classes (Entity/DTO)
├── network/        # Konfigurasi Supabase Client
├── ui/             # Komponen antarmuka pengguna
│   ├── components/ # Komponen UI reusable (LoadingDialog, dll)
│   ├── screens/    # Layar utama (Dashboard, Login, Scan, dll)
│   └── theme/      # Konfigurasi warna, tipografi, dan tema aplikasi
└── viewmodel/      # Business logic & State management per layar
```

## 📝 Lisensi
Dikembangkan oleh **STTI Tanjungpinang**.
