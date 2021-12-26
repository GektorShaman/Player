package com.player.dependency


import com.player.viewmodels.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginViewModelModule = module {
    viewModel { UserViewModel(get(), get()) }
}