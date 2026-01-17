package com.example.projectakhir.view.uicontroller

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projectakhir.view.*
import com.example.projectakhir.view.halamankasus.*
import com.example.projectakhir.view.halamanpenyakit.*
import com.example.projectakhir.view.route.*
import com.example.projectakhir.viewmodel.KasusViewModel
import com.example.projectakhir.viewmodel.PenyakitViewModel

@Composable
fun PetaNavigasi() {

    val navController = rememberNavController()

    // ViewModel
    val penyakitViewModel: PenyakitViewModel = viewModel()
    val kasusViewModel: KasusViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = DestinasiSplash.route
    ) {
        /* ================= SPLASH ================= */
        composable(DestinasiSplash.route) {
            HalamanSplash(
                onSplashFinished = {
                    navController.navigate(DestinasiLogin.route) {
                        popUpTo(DestinasiSplash.route) { inclusive = true }
                    }
                }
            )
        }

        /* ================= AUTH ================= */
        composable(DestinasiLogin.route) {
            HalamanLogin(
                onLoginSuccess = {
                    navController.navigate(DestinasiHome.route) {
                        popUpTo(DestinasiLogin.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(DestinasiRegister.route)
                }
            )
        }

        composable(DestinasiRegister.route) {
            HalamanRegister(
                onRegisterSuccess = { navController.popBackStack() },
                onLoginClick = { navController.popBackStack() }
            )
        }

        /* ================= HOME ================= */
        composable(DestinasiHome.route) {
            HalamanHome(
                onPenyakitClick = {
                    navController.navigate(DestinasiPenyakit.route)
                },
                onKasusClick = {
                    navController.navigate(DestinasiKasus.route)
                },
                onLaporanClick = {
                    navController.navigate(DestinasiLaporan.route)
                },
                onLogout = {
                    navController.navigate(DestinasiLogin.route) {
                        popUpTo(DestinasiHome.route) { inclusive = true }
                    }
                }
            )
        }

        /* ================= PENYAKIT ================= */

        composable(DestinasiPenyakit.route) {
            HalamanPenyakit(
                viewModel = penyakitViewModel,
                onDetail = {
                    navController.navigate(DestinasiDetailPenyakit.route)
                },
                onAdd = {
                    penyakitViewModel.updateSelectedPenyakit(null)
                    navController.navigate(DestinasiEntryPenyakit.route)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(DestinasiDetailPenyakit.route) {
            HalamanDetailPenyakit(
                viewModel = penyakitViewModel,
                onEdit = {
                    navController.navigate(DestinasiEntryPenyakit.route)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(DestinasiEntryPenyakit.route) {
            HalamanEntryPenyakit(
                viewModel = penyakitViewModel,
                onSuccess = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        /* ================= KASUS ================= */

        composable(DestinasiKasus.route) {
            HalamanKasus(
                viewModel = kasusViewModel,
                onDetail = { kasus ->
                    kasusViewModel.updateSelectedKasus(kasus)
                    navController.navigate(DestinasiDetailKasus.route)
                },
                onAdd = {
                    kasusViewModel.updateSelectedKasus(null)
                    navController.navigate(DestinasiEntryKasus.route)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(DestinasiDetailKasus.route) {
            HalamanDetailKasus(
                viewModel = kasusViewModel,
                onEdit = {
                    navController.navigate(DestinasiEntryKasus.route)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(DestinasiEntryKasus.route) {
            HalamanEntryKasus(
                viewModel = kasusViewModel,
                onSuccess = {
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        /* ================= LAPORAN ================= */
        composable(DestinasiLaporan.route) {
            HalamanLaporan(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
