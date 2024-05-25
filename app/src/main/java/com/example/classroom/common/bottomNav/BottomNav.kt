package com.example.classroom.common.bottomNav

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.classroom.R
import com.example.classroom.presentation.navigation.Destination
import com.example.classroom.presentation.navigation.Icon
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.Azul2
import proyecto.person.appconsultapopular.common.SnackbarDelegate

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldBottomNav(
    navController: NavController,
    topBar: @Composable () -> Unit,
    content: @Composable() () -> Unit,
    items: List<Destination>,
    scaffoldState: ScaffoldState,
    snackbarHost: SnackbarHostState,
    snackbarDelegate: SnackbarDelegate,
    action: () -> Unit,
    isFloatingAction: Boolean,

    ) {
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHost)

        },
        topBar = topBar,
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .height(65.dp)
                    .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)),
                cutoutShape = CircleShape,
                backgroundColor = Azul2,
                elevation = 22.dp
            ) {
                BottomNav(navController = navController, items)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        floatingActionButton = {
            if (isFloatingAction){
                FloatingActionButton(
                    shape = CircleShape,
                    onClick = {
                        action()
//                    Destination.HOME.screenRoute.let {
//                        navController.navigate(it) {
//                            popUpTo(navController.graph.findStartDestination().id) {
//                                saveState = true
//                            }
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                    }
//                    Destination.HOME.screenRoute.let { navController.navigate(it) }
                    },
                    contentColor = Color.White,
                    backgroundColor = Azul2
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add icon")
                }

            }
        }
    ) {
        content()
    }
}
@Composable
fun BottomNav(navController: NavController, items: List<Destination>) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination

    BottomNavigation(
        modifier = Modifier
            .padding(12.dp, 0.dp, 12.dp, 0.dp)
            .height(100.dp),
        backgroundColor = Azul2,
        elevation = 0.dp
    ) {
        items.forEach {
            BottomNavigationItem(
                icon = {
                    it.icon?.let {
                        when (val icon = it) {
                            is Icon.Resource -> {
                                // Load and display the drawable resource
                                Icon(painter = painterResource(id = icon.resId),
                                    contentDescription = null,
                                    modifier = Modifier.size(30.dp),
                                    )
                            }
                            is Icon.Vector -> {
                                // Display the ImageVector
                                Icon(imageVector = icon.imageVector,
                                    contentDescription = null,
                                    modifier = Modifier.size(30.dp),)
                            }
                        }

                    }
                },
                label = {
                    it.title?.let {
                        Text(
                            text = it,
                            //color = Color.Gray
                        )
                    }
                },
                selected = currentRoute?.hierarchy?.any { it.route == it.route } == true,

                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(alpha = 0.4f),
                onClick = {
                    it.screenRoute?.let { it1 ->
                        navController.navigate(it1) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}