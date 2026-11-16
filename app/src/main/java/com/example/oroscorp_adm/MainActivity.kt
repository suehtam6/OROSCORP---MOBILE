package com.example.oroscorp_adm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.oroscorp_adm.ui.theme.OroscorpadmTheme
import com.example.oroscorp_adm.view.screens.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OroscorpadmTheme { AppNavigation() }
        }
    }
}
