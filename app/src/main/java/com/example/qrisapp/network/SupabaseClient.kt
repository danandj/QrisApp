package com.example.qrisapp.network

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {

    private const val SUPABASE_URL = "https://ehmdqudjzpjvyxysaaid.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVobWRxdWRqenBqdnl4eXNhYWlkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzE4NDk3OTcsImV4cCI6MjA4NzQyNTc5N30.S07Mw4rj8NlycgIO_H1sb3Td9f-USa5IkITAA25XePQ"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Postgrest)
    }
}