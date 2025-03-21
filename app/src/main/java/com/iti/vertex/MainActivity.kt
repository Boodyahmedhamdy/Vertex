package com.iti.vertex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val backStackEntry by  navController.currentBackStackEntryAsState()
            var titleState by remember {
                val route = backStackEntry?.toRoute<Routes>() ?: Routes.HomeScreenRoute
                mutableStateOf(getString(route.title))
            }

            VertexTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                /*val route = backStackEntry?.toRoute<Routes>() ?: Routes.HomeScreenRoute
                                titleState = getString(route.title)*/
                                Text(text = titleState)
                            }
                        )
                    },
                    bottomBar = {
                        NavigationBar {
                            val currentDistenation = backStackEntry?.destination
                            topLevelRoutes.forEach { topLevelRoute ->
                                NavigationBarItem(
                                    icon = { Icon(imageVector = topLevelRoute.icon, contentDescription = null) },
                                    selected = currentDistenation?.hierarchy?.any {
                                        it.hasRoute(topLevelRoute.route::class)
                                    } ?: true,
                                    label = { Text(text = getString(topLevelRoute.name)) },
                                    onClick = {
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
                        modifier = Modifier.fillMaxSize().padding(innerPadding)
                    )

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VertexTheme {
        Greeting("Android")
    }
}