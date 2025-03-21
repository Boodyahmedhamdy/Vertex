package com.iti.vertex

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.iti.vertex.ui.navigation.VertexNavHost
import com.iti.vertex.ui.navigation.routes.Routes
import com.iti.vertex.ui.navigation.routes.topLevelRoutes
import com.iti.vertex.ui.theme.VertexTheme

private const val TAG = "MainActivity"
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val backStackEntry by  navController.currentBackStackEntryAsState()
            val currentRoute by remember {
                derivedStateOf { backStackEntry?.toRoute<Routes>() ?: Routes.HomeScreenRoute }
            }
            var titleState by rememberSaveable {
                mutableStateOf(getString(currentRoute.title))
            }
            // route doesn't update
            Log.i(TAG, "onCreate: route: ${currentRoute.title} ${getString(currentRoute.title)}")

            VertexTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = titleState)
                            },
                            navigationIcon = {
                                if(currentRoute is Routes.LocationPickerScreenRoute) {
                                    IconButton(onClick = { navController.navigateUp() }) {
                                        Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                                    }
                                }
                            }
                        )
                    },
                    bottomBar = {
                        NavigationBar {
                            val currentDestination = backStackEntry?.destination
                            topLevelRoutes.forEach { topLevelRoute ->
                                val isSelected = currentDestination?.hierarchy?.any {
                                    it.hasRoute(topLevelRoute.route::class)
                                } ?: true
                                NavigationBarItem(
                                    icon = { Icon(imageVector = if(isSelected) topLevelRoute.selectedIcon else topLevelRoute.unSelectedIcon, contentDescription = null) },

                                    selected = isSelected,

                                    label = { Text(text = getString(topLevelRoute.name)) },

                                    onClick = {
                                        titleState = getString(topLevelRoute.name)
                                        navController.navigate(topLevelRoute.route) {
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },

                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()

                ) { innerPadding ->

                    VertexNavHost(
                        navController = navController,
                        onAddToFavoriteButtonClicked = {navController.navigate(Routes.LocationPickerScreenRoute)},
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )

                }
            }
        }
    }
}
