package com.example.projectakhir.view.halamankasus



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import com.example.projectakhir.viewmodel.KasusViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanDetailKasus(
    viewModel: KasusViewModel,
    onEdit: () -> Unit,
    onBack: () -> Unit
) {
    val kasus = viewModel.selectedKasus ?: return
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detail Kasus", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refreshDetail() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header Location
            ElevatedCard(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "${kasus.kelurahan}, ${kasus.kecamatan}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                         color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = kasus.kota,
                        style = MaterialTheme.typography.titleMedium,
                         color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                         Icon(Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(16.dp))
                         Spacer(modifier = Modifier.width(4.dp))
                         Text(
                            text = "Laporan: ${kasus.tanggal_laporan}",
                            style = MaterialTheme.typography.labelLarge,
                             color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // Statistics Grid using Row of Cards
            Text(
                text = "Statistik Kasus",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    label = "Baru", 
                    value = kasus.jumlah_kasus_baru.toString(), 
                    color = Color(0xFFBBDEFB), // Biru Muda
                    textColor = Color(0xFF0D47A1),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Dirawat", 
                    value = kasus.jumlah_dalam_perawatan.toString(), 
                    color = Color(0xFFFFF9C4), // Kuning Muda
                    textColor = Color(0xFFF57F17),
                    modifier = Modifier.weight(1f)
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    label = "Sembuh", 
                    value = kasus.jumlah_sembuh.toString(), 
                    color = Color(0xFFC8E6C9), // Hijau Muda (Existing is good)
                    textColor = Color(0xFF1B5E20),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Meninggal", 
                    value = kasus.jumlah_meninggal.toString(), 
                    color = Color(0xFFFFCDD2), // Merah Muda
                    textColor = Color(0xFFB71C1C),
                    modifier = Modifier.weight(1f)
                )
            }
            
            if (kasus.catatan.isNotEmpty()) {
                OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                     Column(Modifier.padding(16.dp)) {
                         Text("Catatan Tambahan", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                         Spacer(modifier = Modifier.height(4.dp))
                         Text(kasus.catatan, style = MaterialTheme.typography.bodyMedium)
                     }
                }
            }

            Spacer(Modifier.weight(1f))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedButton(
                    onClick = { showDeleteConfirmation = true },
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                     border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                ) { Text("Hapus") }
                
                Button(onClick = onEdit, modifier = Modifier.weight(1f).height(50.dp)) { Text("Edit Data") }
            }
            

            if (showDeleteConfirmation) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirmation = false },
                    title = { Text("Hapus Data") },
                    text = { Text("Apakah Anda yakin ingin menghapus data kasus ini?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.delete(kasus.id_kasus)
                                showDeleteConfirmation = false
                                onBack()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Hapus")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteConfirmation = false }) {
                            Text("Batal")
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun StatCard(label: String, value: String, color: Color, textColor: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = textColor)
            Text(label, style = MaterialTheme.typography.bodyMedium, color = textColor.copy(alpha=0.8f))
        }
    }
}

