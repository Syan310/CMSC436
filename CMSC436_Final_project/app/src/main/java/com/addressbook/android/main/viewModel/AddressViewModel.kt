package com.addressbook.android.main.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.addressbook.android.main.respository.AddressBookRepository
import com.addressbook.android.roomDatabase.db.AddressBook
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddressViewModel(private val repository: AddressBookRepository) : ViewModel() {

    private var list: LiveData<List<AddressBook>> = repository.getAllAddress()

    fun insertAddressBook(addressBook: AddressBook) {
        viewModelScope.launch {
            repository.insertAddressBook(addressBook)
        }
    }

    fun updateAddressBook(addressBook: AddressBook) {
        viewModelScope.launch {
            repository.updateAddressBook(addressBook)
        }
    }

    fun deleteProduct(addressBook: AddressBook) {
        viewModelScope.launch {
            repository.deleteAddressBook(addressBook)
        }
    }

    fun clearProduct() {
        viewModelScope.launch {
            repository.clearAddress()
        }
    }

    suspend fun deleteProductById(id: Int) = repository.deleteAddressBookbyId(id)

    fun getAllAddress(): LiveData<List<AddressBook>> = list
}