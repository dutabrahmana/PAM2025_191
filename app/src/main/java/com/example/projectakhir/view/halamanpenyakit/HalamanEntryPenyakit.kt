package com.example.projectakhir.view.halamanpenyakit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectakhir.viewmodel.PenyakitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanEntryPenyakit(
    viewModel: PenyakitViewModel = viewModel(),
    onSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val selected = viewModel.selectedPenyakit
    val context = androidx.compose.ui.platform.LocalContext.current

    var nama by remember { mutableStateOf(selected?.nama_penyakit ?: "") }
    var kategori by remember { mutableStateOf(selected?.kategori ?: "") }
    var gejala by remember { mutableStateOf(selected?.gejala ?: "") }
    var penularan by remember { mutableStateOf(selected?.cara_penularan ?: "") }
    var tingkat by remember { mutableStateOf(selected?.tingkat_keparahan ?: "") }
    var deskripsi by remember { mutableStateOf(selected?.deskripsi ?: "") }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (selected == null) "Tambah Penyakit" else "Edit Penyakit") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding() // Adjust for keyboard
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Hint
            Text(
                text = "Silakan lengkapi data penyakit di bawah ini:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )

            OutlinedTextField(
                value = nama,
                onValueChange = { nama = it },
                label = { Text("Nama Penyakit") },
                placeholder = { Text("Contoh: Demam Berdarah") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )

            OutlinedTextField(
                value = kategori,
                onValueChange = { kategori = it },
                label = { Text("Kategori") },
                placeholder = { Text("Contoh: Virus / Bakteri") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )

            OutlinedTextField(
                value = tingkat,
                onValueChange = { tingkat = it },
                label = { Text("Tingkat Keparahan") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )

            OutlinedTextField(
                value = penularan,
                onValueChange = { penularan = it },
                label = { Text("Cara Penularan") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4,
                shape = MaterialTheme.shapes.medium
            )

            OutlinedTextField(
                value = gejala,
                onValueChange = { gejala = it },
                label = { Text("Gejala Umum") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 6,
                shape = MaterialTheme.shapes.medium
            )

            OutlinedTextField(
                value = deskripsi,
                onValueChange = { deskripsi = it },
                label = { Text("Deskripsi Singkat") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 6,
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium,
                onClick = {
                    if (selected == null) {
                        viewModel.insert(
                            nama, kategori, gejala,
                            penularan, tingkat, deskripsi
                        )
                    } else {
                        viewModel.update(
                            selected.id_penyakit,
                            nama, kategori, gejala,
                            penularan, tingkat, deskripsi
                        )
                    }
                    android.widget.Toast.makeText(context, "Data Berhasil Disimpan", android.widget.Toast.LENGTH_SHORT).show()
                    onSuccess()
                }
            ) {
                Text(text = "Simpan Data", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
