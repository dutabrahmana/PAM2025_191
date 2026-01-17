package com.example.projectakhir.view.halamankasus

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectakhir.viewmodel.KasusViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanEntryKasus(
    viewModel: KasusViewModel = viewModel(),
    onSuccess: () -> Unit,
    onBack: () -> Unit
) {
    // ================= STATE =================
    val context = androidx.compose.ui.platform.LocalContext.current
    val listPenyakit by viewModel.listPenyakit.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var selectedPenyakitName by remember { mutableStateOf("") }
    
    // Edit Mode Check
    val kasuToEdit = viewModel.selectedKasus
    val isEditMode = kasuToEdit != null
    
    // Date Picker State
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                 // Allow all dates for edit mode, or keep restriction? 
                 // Usually for edit, we allow keeping old dates.
                 // For now, keeping logic same but defaulting to allowing if it matches current.
                val today = java.util.Calendar.getInstance().apply {
                    set(java.util.Calendar.HOUR_OF_DAY, 0)
                    set(java.util.Calendar.MINUTE, 0)
                    set(java.util.Calendar.SECOND, 0)
                    set(java.util.Calendar.MILLISECOND, 0)
                }.timeInMillis
                return utcTimeMillis >= today
            }
        }
    )

    // Form Data Initialization
    var idPenyakit by remember { mutableStateOf(kasuToEdit?.id_penyakit ?: "") }
    var kelurahan by remember { mutableStateOf(kasuToEdit?.kelurahan ?: "") }
    var kecamatan by remember { mutableStateOf(kasuToEdit?.kecamatan ?: "") }
    var kota by remember { mutableStateOf(kasuToEdit?.kota ?: "") }
    var tanggal by remember { mutableStateOf(kasuToEdit?.tanggal_laporan ?: "") }
    var baru by remember { mutableStateOf(kasuToEdit?.jumlah_kasus_baru?.toString() ?: "") }
    var dirawat by remember { mutableStateOf(kasuToEdit?.jumlah_dalam_perawatan?.toString() ?: "") }
    var sembuh by remember { mutableStateOf(kasuToEdit?.jumlah_sembuh?.toString() ?: "") }
    var meninggal by remember { mutableStateOf(kasuToEdit?.jumlah_meninggal?.toString() ?: "") }
    var catatan by remember { mutableStateOf(kasuToEdit?.catatan ?: "") }

    // Pre-fill Penyakit Name if Edit Mode
    LaunchedEffect(key1 = listPenyakit, key2 = kasuToEdit) {
        if (isEditMode && listPenyakit.isNotEmpty()) {
            val p = listPenyakit.find { it.id_penyakit == kasuToEdit?.id_penyakit }
            if (p != null) {
                selectedPenyakitName = p.nama_penyakit
            }
        }
    }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (isEditMode) "Edit Kasus" else "Tambah Kasus") },
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
                .imePadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Pilih Penyakit
            CardSection(title = "Info Penyakit") {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        readOnly = true,
                        value = selectedPenyakitName,
                        onValueChange = {},
                        label = { Text("Nama Penyakit") },
                        placeholder = { Text("Pilih Penyakit") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listPenyakit.forEach { p ->
                            DropdownMenuItem(
                                text = { Text(p.nama_penyakit) },
                                onClick = {
                                    selectedPenyakitName = p.nama_penyakit
                                    idPenyakit = p.id_penyakit
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
            }

            // 2. Lokasi & Tanggal
            CardSection(title = "Lokasi & Waktu") {
                OutlinedTextField(
                    value = kelurahan,
                    onValueChange = { kelurahan = it },
                    label = { Text("Kelurahan") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = kecamatan,
                    onValueChange = { kecamatan = it },
                    label = { Text("Kecamatan") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = kota,
                    onValueChange = { kota = it },
                    label = { Text("Kota/Kabupaten") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Date Picker Interaction
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = tanggal,
                        onValueChange = {},
                        label = { Text("Tanggal Laporan") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(Icons.Default.DateRange, contentDescription = "Pilih Tanggal")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    // Overlay invisible button to catch clicks on the text field to open date picker
                    Surface(
                        modifier = Modifier
                            .matchParentSize()
                            .padding(top = 8.dp), // Adjust to not cover label
                        color = androidx.compose.ui.graphics.Color.Transparent,
                        onClick = { showDatePicker = true }
                    ) {}
                }
            }
            
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                tanggal = formatter.format(Date(millis))
                            }
                            showDatePicker = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Batal")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            // 3. Statistik Kasus
            CardSection(title = "Data Statistik") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = baru,
                        onValueChange = { if (it.all { char -> char.isDigit() }) baru = it },
                        label = { Text("Kasus Baru") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = dirawat,
                        onValueChange = { if (it.all { char -> char.isDigit() }) dirawat = it },
                        label = { Text("Dirawat") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = sembuh,
                        onValueChange = { if (it.all { char -> char.isDigit() }) sembuh = it },
                        label = { Text("Sembuh") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = meninggal,
                        onValueChange = { if (it.all { char -> char.isDigit() }) meninggal = it },
                        label = { Text("Meninggal") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 4. Catatan Tambahan
            CardSection(title = "Catatan") {
                OutlinedTextField(
                    value = catatan,
                    onValueChange = { catatan = it },
                    label = { Text("Catatan Tambahan (Opsional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (isEditMode) {
                        viewModel.update(
                            idKasus = kasuToEdit!!.id_kasus,
                            idPenyakit = idPenyakit,
                            kelurahan = kelurahan,
                            kecamatan = kecamatan,
                            kota = kota,
                            tanggal = tanggal,
                            baru = baru.toIntOrNull() ?: 0,
                            dirawat = dirawat.toIntOrNull() ?: 0,
                            sembuh = sembuh.toIntOrNull() ?: 0,
                            meninggal = meninggal.toIntOrNull() ?: 0,
                            catatan = catatan
                        )
                    } else {
                        viewModel.insert(
                            idPenyakit = idPenyakit,
                            kelurahan = kelurahan,
                            kecamatan = kecamatan,
                            kota = kota,
                            tanggal = tanggal,
                            baru = baru.toIntOrNull() ?: 0,
                            dirawat = dirawat.toIntOrNull() ?: 0,
                            sembuh = sembuh.toIntOrNull() ?: 0,
                            meninggal = meninggal.toIntOrNull() ?: 0,
                            catatan = catatan
                        )
                    }
                    android.widget.Toast.makeText(context, "Data Kasus Berhasil Disimpan", android.widget.Toast.LENGTH_SHORT).show()
                    onSuccess()
                }
            ) {
                Text(if (isEditMode) "Simpan Perubahan" else "Simpan Data Kasus")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CardSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            content()
        }
    }
}
