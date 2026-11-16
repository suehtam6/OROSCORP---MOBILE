package com.example.oroscorp_adm.view.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.oroscorp_adm.model.Setor

/** Compatibility wrapper. Prefer SetoresScreen with API-derived SetorInfo. */
@Composable
fun SetoresScreen2(setor: Setor, modifier: Modifier = Modifier) {
    SetoresScreen(onVoltar = {}, onAbrirSetor = {},
        setores = listOf(SetorInfo(setor.id, setor.nome, setor.descricao, null, null, null)))
}
