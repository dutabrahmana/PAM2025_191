package com.example.projectakhir.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectakhir.viewmodel.LaporanUiModel
import com.example.projectakhir.viewmodel.LaporanUiState
import com.example.projectakhir.viewmodel.LaporanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanLaporan(
    onBack: () -> Unit,
    viewModel: LaporanViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Laporan Kasus") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is LaporanUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is LaporanUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.loadData() }) {
                            Text("Coba Lagi")
                        }
                    }
                }
                is LaporanUiState.Success -> {
                    if (state.data.isEmpty()) {
                        Text(
                            text = "Belum ada data laporan.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        Column {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                label = { Text("Cari Laporan (Penyakit / Lokasi)") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                                singleLine = true,
                                shape = MaterialTheme.shapes.medium
                            )

                            val filteredData = state.data.filter {
                                it.namaPenyakit.contains(searchQuery, ignoreCase = true) ||
                                it.kota.contains(searchQuery, ignoreCase = true) ||
                                it.kecamatan.contains(searchQuery, ignoreCase = true) ||
                                it.kelurahan.contains(searchQuery, ignoreCase = true)
                            }
                            
                            if (filteredData.isEmpty()) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text("Data tidak ditemukan.")
                                } 
                            } else {
                                LazyColumn(
                                    contentPadding = PaddingValues(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(filteredData) { laporan ->
                                        LaporanCard(laporan)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LaporanCard(laporan: LaporanUiModel) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Nama Penyakit & Tanggal
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = laporan.namaPenyakit,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = laporan.tanggal,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Lokasi
            Text(
                text = "${laporan.kelurahan}, ${laporan.kecamatan}, ${laporan.kota}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            // Statistik (Grid 2x2 simple layout)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                StatItem(label = "Kasus Baru", value = laporan.kasusBaru.toString(), color = Color.Blue)
                StatItem(label = "Dirawat", value = laporan.dirawat.toString(), color = Color(0xFFFFA500)) // Orange
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                StatItem(label = "Sembuh", value = laporan.sembuh.toString(), color = Color(0xFF4CAF50)) // Green
                StatItem(label = "Meninggal", value = laporan.meninggal.toString(), color = Color.Red)
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // Dot indicator
        Surface(
            modifier = Modifier.size(8.dp),
            shape = MaterialTheme.shapes.small,
            color = color
        ) {}
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = "$label: ", style = MaterialTheme.typography.bodySmall)
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
    }
}
