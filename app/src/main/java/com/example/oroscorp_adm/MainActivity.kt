
package com.example.oroscorp_adm

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.example.oroscorp_adm.components.BotaoTema
import com.example.oroscorp_adm.ui.theme.OroscorpadmTheme
import com.example.oroscorp_adm.ui.theme.TemaGlobal
import com.example.oroscorp_adm.view.screens.AppNavigation

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        TemaGlobal.carregar(this)

        setContent {

            OroscorpadmTheme(
                darkTheme = TemaGlobal.modoClaro,
                dynamicColor = false
            ) {

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {

                    AppNavigation()

                    BotaoTema(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .navigationBarsPadding()
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}
