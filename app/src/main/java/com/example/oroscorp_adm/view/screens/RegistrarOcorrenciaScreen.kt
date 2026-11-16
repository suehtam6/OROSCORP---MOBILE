package com.example.oroscorp_adm.view.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oroscorp_adm.R
import com.example.oroscorp_adm.model.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Calendar

/** Payload de UI; não equivale a contrato de POST ainda não documentado. */
data class OcorrenciaFormulario(
    val idSetor: Int,
    val idFuncionario: Int?,
    val idCamera: Int?,
    val idMotivo: Int,
    val idEpi: Int?,
    val prioridade: String,
    val descricao: String,
    val data: String,
    val horario: String,
    val evidenciaUri: String?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarOcorrenciaScreen(
    onBackClick: () -> Unit,
    onRegistrarSucesso: () -> Unit = {},
    funcionarios: List<Funcionario> = emptyList(),
    setores: List<Setor> = emptyList(),
    cameras: List<Camera> = emptyList(),
    motivos: List<MotivoOcorrencia> = emptyList(),
    epis: List<Epi> = emptyList(),
    onRegistrar: ((OcorrenciaFormulario) -> Unit)? = null,
    carregando: Boolean = false,
    erroApi: String? = null
) {
    val context = LocalContext.current
    val fundo = colorResource(R.color.Fundo)
    val laranja = colorResource(R.color.Laranja)
    val cartao = colorResource(R.color.Cartao)
    var idFuncionario by remember { mutableStateOf<Int?>(null) }
    var idSetor by remember { mutableStateOf<Int?>(null) }
    var idCamera by remember { mutableStateOf<Int?>(null) }
    var idMotivo by remember { mutableStateOf<Int?>(null) }
    var idEpi by remember { mutableStateOf<Int?>(null) }
    var prioridade by remember { mutableStateOf("Média") }
    var descricao by remember { mutableStateOf("") }
    var evidencia by remember { mutableStateOf<String?>(null) }
    var erroValidacao by remember { mutableStateOf<String?>(null) }
    var data by remember { mutableStateOf(SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(Date())) }
    var horario by remember { mutableStateOf(SimpleDateFormat("HH:mm", Locale("pt", "BR")).format(Date())) }

    val imagens = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        evidencia = uri?.toString()
    }

    Column(modifier = Modifier.fillMaxSize().background(fundo)) {
        Row(
            modifier = Modifier.fillMaxWidth().height(95.dp).background(Color.Black).padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(painterResource(R.drawable.logo), contentDescription = "Logo OROSCORP",
                modifier = Modifier.weight(1f).height(75.dp), contentScale = ContentScale.Fit,
                alignment = Alignment.CenterStart)
            Box(modifier = Modifier.size(55.dp).border(2.5.dp, laranja, RoundedCornerShape(18.dp))
                .clickable(onClick = onBackClick), contentAlignment = Alignment.Center) {
                Text("←", color = laranja, fontSize = 34.sp)
            }
        }
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
            Text("Registrar\nocorrência manual", color = Color.White, fontSize = 28.sp,
                fontWeight = FontWeight.Bold, lineHeight = 34.sp)
            Spacer(Modifier.height(8.dp))
            Text("Os campos são preenchidos com os dados disponíveis da API.",
                color = Color.LightGray, fontSize = 13.sp)
            Spacer(Modifier.height(18.dp))
            Column(Modifier.fillMaxWidth().background(cartao, RoundedCornerShape(16.dp)).padding(12.dp)) {
                SeletorOcorrencia("Funcionário", "Selecionar funcionário",
                    funcionarios.map { it.id to it.nome }, idFuncionario) { idFuncionario = it }
                Spacer(Modifier.height(12.dp))
                SeletorOcorrencia("Setor", "Selecionar setor", setores.map { it.id to it.nome }, idSetor) {
                    idSetor = it
                    idCamera = null
                }
                Spacer(Modifier.height(12.dp))
                SeletorOcorrencia("Câmera", "Selecionar câmera",
                    cameras.filter { idSetor == null || it.id_setor == idSetor }.map { it.id to it.nome }, idCamera) { idCamera = it }
                Spacer(Modifier.height(12.dp))
                SeletorOcorrencia("Tipo de ocorrência", "Selecionar motivo",
                    motivos.map { it.id to it.nome }, idMotivo) { idMotivo = it }
                Spacer(Modifier.height(12.dp))
                SeletorOcorrencia("EPI relacionado", "Selecionar EPI", epis.map { it.id to it.nome }, idEpi) { idEpi = it }
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(Modifier.weight(1f)) {
                        Text("Data", color = Color.LightGray, fontSize = 13.sp)
                        Spacer(Modifier.height(6.dp))
                        OutlinedButton(onClick = {
                            val now = Calendar.getInstance()
                            DatePickerDialog(context, { _, year, month, day ->
                                data = "%02d/%02d/%04d".format(day, month + 1, year)
                            }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)).show()
                        }, modifier = Modifier.fillMaxWidth()) { Text(data, color = Color.White, fontSize = 12.sp) }
                    }
                    Column(Modifier.weight(1f)) {
                        Text("Horário", color = Color.LightGray, fontSize = 13.sp)
                        Spacer(Modifier.height(6.dp))
                        OutlinedButton(onClick = {
                            val now = Calendar.getInstance()
                            TimePickerDialog(context, { _, hour, minute ->
                                horario = "%02d:%02d".format(hour, minute)
                            }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true).show()
                        }, modifier = Modifier.fillMaxWidth()) { Text(horario, color = Color.White, fontSize = 12.sp) }
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text("Prioridade", color = Color.LightGray, fontSize = 13.sp)
                Spacer(Modifier.height(7.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Baixa", "Média", "Alta", "Crítica").forEach { valor ->
                        OutlinedButton(
                            onClick = { prioridade = valor }, modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 2.dp),
                            border = BorderStroke(1.dp, if (prioridade == valor) laranja else Color.Gray)
                        ) { Text(valor, fontSize = 10.sp, color = if (prioridade == valor) laranja else Color.White) }
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text("Descrição", color = Color.LightGray, fontSize = 13.sp)
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(value = descricao, onValueChange = { descricao = it },
                    modifier = Modifier.fillMaxWidth().height(110.dp),
                    placeholder = { Text("Descreva o ocorrido...", color = Color.Gray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                        focusedContainerColor = fundo, unfocusedContainerColor = fundo,
                        focusedBorderColor = laranja))
                Spacer(Modifier.height(12.dp))
                OutlinedButton(onClick = { imagens.launch("image/*") }, modifier = Modifier.fillMaxWidth()) {
                    Text(if (evidencia == null) "Selecionar evidência" else "Evidência selecionada",
                        color = Color.White)
                }
            }
            if (!erroValidacao.isNullOrBlank() || !erroApi.isNullOrBlank()) {
                Spacer(Modifier.height(12.dp))
                Text(erroValidacao ?: erroApi.orEmpty(), color = Color(0xFFFFA085), fontSize = 12.sp)
            }
            Spacer(Modifier.height(18.dp))
            Button(
                enabled = !carregando,
                onClick = {
                    erroValidacao = when {
                        idSetor == null -> "Selecione um setor"
                        idMotivo == null -> "Selecione o motivo da ocorrência"
                        descricao.isBlank() -> "Informe a descrição"
                        else -> null
                    }
                    if (erroValidacao == null && idSetor != null && idMotivo != null) {
                        if (onRegistrar != null) onRegistrar(
                            OcorrenciaFormulario(idSetor!!, idFuncionario, idCamera, idMotivo!!,
                                idEpi, prioridade, descricao, data, horario, evidencia))
                        else onRegistrarSucesso()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = laranja),
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(14.dp)
            ) { Text(if (carregando) "Enviando..." else "Registrar ocorrência", color = Color.Black, fontWeight = FontWeight.Bold) }
            Spacer(Modifier.height(35.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeletorOcorrencia(
    titulo: String, placeholder: String, opcoes: List<Pair<Int, String>>,
    selecionado: Int?, onSelecionar: (Int?) -> Unit
) {
    var aberto by remember { mutableStateOf(false) }
    Column {
        Text(titulo, color = Color.LightGray, fontSize = 13.sp)
        Spacer(Modifier.height(6.dp))
        ExposedDropdownMenuBox(expanded = aberto, onExpandedChange = { aberto = it }) {
            OutlinedTextField(
                value = opcoes.firstOrNull { it.first == selecionado }?.second ?: "",
                onValueChange = {}, readOnly = true, modifier = Modifier.fillMaxWidth().menuAnchor(),
                placeholder = { Text(placeholder, color = Color.Gray, fontSize = 12.sp) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = aberto) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF171717), unfocusedContainerColor = Color(0xFF171717))
            )
            ExposedDropdownMenu(expanded = aberto, onDismissRequest = { aberto = false }) {
                DropdownMenuItem(text = { Text("Nenhum") }, onClick = { onSelecionar(null); aberto = false })
                opcoes.forEach { (id, nome) ->
                    DropdownMenuItem(text = { Text(nome) }, onClick = { onSelecionar(id); aberto = false })
                }
            }
        }
    }
}
