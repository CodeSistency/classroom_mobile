package com.example.classroom.presentation.navigation


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.classroom.App
import com.example.classroom.common.validator.UserDataValidator
import com.example.classroom.data.repository.LoginRepositoryImpl
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.presentation.SplashScreen
import com.example.classroom.presentation.screens.auth.AuthViewModel
import com.example.classroom.presentation.screens.auth.signIn.SignInScreen
import com.example.classroom.presentation.screens.auth.signUp.SignUpScreen

@Composable
fun Navigation(
    isUserLogged: Boolean,
    darkTheme: Boolean,
    changeTheme: () -> Unit,
) {


    val navController = rememberNavController()
    val focusManager = LocalFocusManager.current

    NavHost(
        navController = navController,
//        startDestination = Destination.HOME.screenRoute,
        startDestination =
        if (isUserLogged) {
                Destination.HOME.screenRoute
        }else {
            Destination.LOGIN.screenRoute
        },
        builder = {
            /** Configuracion de vista de splash*/
            composable(
                route = Destination.SPLASH.screenRoute
            ){
                Box(modifier = Modifier.fillMaxSize()){
                    SplashScreen(navController)
                }
            }
            /** Configuracion de vista de inicio de sesion*/
            composable(
                route = Destination.LOGIN.screenRoute
            ){
//                val loginUseCase = LoginUserUseCase(repositoryBundle)
//                val insertUseCase = InsertUserInfoUseCase(repositoryBundle)
//                val repositoryImp = LoginRepositoryImp(App.appModule.apiService, db.appDao)
                SignInScreen(
                    viewModel = AuthViewModel(
                        userDataValidator = UserDataValidator()
                    ),
                    focusManager = focusManager,
                    navController = navController,
                    darkTheme = false)
            }
            /** Configuracion de vista de Home*/
            composable(
                route = Destination.HOME.screenRoute
            ){
                Box(modifier = Modifier.fillMaxSize()){
//                    DashboardScreen(navController, dashboardViewmodel)
                }
            }
            /** Configuracion de vista de Registro*/
            composable(
                route = Destination.REGISTRO.screenRoute
            ){
                SignUpScreen(
                    viewModel = AuthViewModel(
                        userDataValidator = UserDataValidator()
                    ),
                    focusManager = focusManager,
                    navController = navController,
                    darkTheme = false)
            }
        }
    )
}