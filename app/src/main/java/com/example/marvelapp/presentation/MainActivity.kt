package com.example.marvelapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.marvelapp.R
import com.example.marvelapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //
        val  navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_container) as NavHostFragment
        //
        navController = navHostFragment.navController
        //Vinculado o bottom navigation com o navController
        binding.bottomNavMain.setupWithNavController(
            navController
        )
        //Define quais são os destinos iniciais(top level destination)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.charactersFragment,
                R.id.favoritesFragment,
                R.id.aboutFragment,
            )
        )
        //Configura toolbar com controle e appBarconfig
        binding.toolbarApp.setupWithNavController(
            navController,appBarConfiguration
        )
        //Listener quando o destino é acionado
        navController.addOnDestinationChangedListener{_,destination,_ ->
            //Verifica se o destino é inicial
            val isTopLevelDestination = appBarConfiguration.topLevelDestinations.contains(destination.id)
            //Se não for destino inicial, seta icone de voltar
            if(!isTopLevelDestination){
                binding.toolbarApp.setNavigationIcon(R.drawable.ic_back)
            }

        }
    }
}