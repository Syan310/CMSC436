package com.addressbook.android.api

import android.app.Application
import androidx.lifecycle.AndroidViewModel

//all viewModel class extend this viewModel and override its repository
abstract class NetworkViewModel(application: Application) : AndroidViewModel(application) {
    abstract val repository: BaseRepository
}