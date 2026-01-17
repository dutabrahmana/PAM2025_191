package com.example.projectakhir.view.halamankasus

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectakhir.model.Kasus
import com.example.projectakhir.viewmodel.KasusViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanKasus(
    viewModel: KasusViewModel,
    onDetail: (Kasus) -> Unit,
    onAdd: () -> Unit,
    onBack: () -> Unit
) {
    val list = viewModel.list.collectAsState().value
    val listPenyakit = viewModel.listPenyakit.collectAsState().value
    var searchQuery by remember { mutableStateOf("") }

    val penyakitMap = remember(listPenyakit) { listPenyakit.associateBy { it.id_penyakit } }

    val filteredList = list.filter { kasus ->
        val namaPenyakit = penyakitMap[kasus.id_penyakit]?.nama_penyakit.orEmpty()
        
        namaPenyakit.contains(searchQuery, ignoreCase = true) ||
        kasus.kota.contains(searchQuery, ignoreCase = true) ||
        kasus.kecamatan.contains(searchQuery, ignoreCase = true) ||
        kasus.kelurahan.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Data Kasus") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.getAll() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Text("+")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Cari Penyakit / Lokasi...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium
                    )
                }

                items(filteredList) { kasus ->
                    val namaPenyakit = penyakitMap[kasus.id_penyakit]?.nama_penyakit ?: "Unknown"

                    ElevatedCard(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                            .clickable { 
                                viewModel.updateSelectedKasus(kasus)
                                onDetail(kasus) 
                            },
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Header: Penyakit & Location
                            Row(
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Location",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = namaPenyakit,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "${kasus.kota}, ${kasus.kecamatan}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            
                            HorizontalDivider()

                            // Stats Grid (4 Columns)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                // 1. Baru (Biru Muda)
                                StatItemCompact(
                                    label = "Baru",
                                    value = kasus.jumlah_kasus_baru.toString(),
                                    color = androidx.compose.ui.graphics.Color(0xFFBBDEFB),
                                    textColor = androidx.compose.ui.graphics.Color(0xFF0D47A1),
                                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                                )
                                // 2. Dirawat (Kuning Muda)
                                StatItemCompact(
                                    label = "Rawat",
                                    value = kasus.jumlah_dalam_perawatan.toString(),
                                    color = androidx.compose.ui.graphics.Color(0xFFFFF9C4),
                                    textColor = androidx.compose.ui.graphics.Color(0xFFF57F17),
                                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                                )
                                // 3. Sembuh (Hijau Muda)
                                StatItemCompact(
                                    label = "Sembuh",
                                    value = kasus.jumlah_sembuh.toString(),
                                    color = androidx.compose.ui.graphics.Color(0xFFC8E6C9),
                                    textColor = androidx.compose.ui.graphics.Color(0xFF1B5E20),
                                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                                )
                                // 4. Meninggal (Merah Muda)
                                StatItemCompact(
                                    label = "Wafat",
                                    value = kasus.jumlah_meninggal.toString(),
                                    color = androidx.compose.ui.graphics.Color(0xFFFFCDD2),
                                    textColor = androidx.compose.ui.graphics.Color(0xFFB71C1C),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatItemCompact(
    label: String, 
    value: String, 
    color: androidx.compose.ui.graphics.Color, 
    textColor: androidx.compose.ui.graphics.Color, 
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = color,
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = textColor
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = textColor.copy(alpha = 0.8f),
                fontSize = 10.sp
            )
        }
    }
}
