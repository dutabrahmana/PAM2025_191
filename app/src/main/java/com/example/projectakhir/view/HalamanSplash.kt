package com.example.projectakhir.view

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectakhir.R
import kotlinx.coroutines.delay


@Composable
fun HalamanSplash(
    onSplashFinished: () -> Unit
) {
    // Sate untuk animasi scale
    val scale = remember { androidx.compose.animation.core.Animatable(0f) }

    // Efek samping untuk timer delay dan animasi
    LaunchedEffect(Unit) {
        // Animasi Scale dari 0 ke 1 dengan efek bounce
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = { FastOutSlowInEasing.transform(it) }
            )
        )
        delay(2000) // Tunggu total 3 detik (termasuk animasi)
        onSplashFinished()
    }

    // Tampilan UI
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 1. Background Image Full Screen
        Image(
            painter = painterResource(id = R.drawable.sage_bg),
            contentDescription = "Background Nature",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize() // Pastikan Column mengisi seluruh layar agar Arrangement.Top berfungsi
                .padding(16.dp)
                .padding(top = 60.dp) // Jarak dari status bar
        ) {
            // Logo Icon dengan Animasi Scale
            Box(
                modifier = Modifier
                    .scale(scale.value)
                    .size(150.dp) // Ukuran lingkaran background logo
                    .background(Color.White.copy(alpha = 0.6f), shape = CircleShape), // Lingkaran transparan
                contentAlignment = Alignment.Center
            ) {
                 Icon(
                    imageVector = Icons.Default.LocalHospital,
                    contentDescription = "Logo Aplikasi",
                    modifier = Modifier.size(100.dp),
                    tint = MaterialTheme.colorScheme.primary // Logo Hijau
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Nama Aplikasi
            Text(
                text = "Health Care",
                style = MaterialTheme.typography.displayMedium, // Ukuran besar
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary, // Text Hijau
                modifier = Modifier.scale(scale.value)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Aplikasi Pendataan dan Pencatatan\nKasus Penyakit Menular",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f), // Text Hijau
                textAlign = TextAlign.Center,
                modifier = Modifier.scale(scale.value)
            )
        }
        
        // Footer / Loading Indicator (Optional)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary, // Loading Hijau
                modifier = Modifier.size(32.dp),
                strokeWidth = 3.dp
            )
        }
    }
}
