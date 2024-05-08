package com.addressbook.android.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.addressbook.android.R
import com.addressbook.android.databinding.ActivityAddressBookListingBinding
import com.addressbook.android.main.respository.AddressBookRepository
import com.addressbook.android.main.respository.AddressViewFactory
import com.addressbook.android.main.viewModel.AddressViewModel
import com.addressbook.android.roomDatabase.db.AddressBook
import com.addressbook.android.roomDatabase.db.AddressBookDatabase
import com.addressbook.android.util.*
import java.io.Serializable


// An AppCompatActivity that displays a list of address book entries.
// It manages adding, editing, and deleting entries, and interacts with AddressBookAdapter and AddressViewModel.
class AddressBookListingActivity : AppCompatActivity(), AddressBookAdapter.OnAddressViewActionListener, View.OnClickListener {

    private var addressableList: List<AddressBook> = ArrayList()
    private var adapterAddressBookList: AddressBookAdapter? = null
    private lateinit var binding: ActivityAddressBookListingBinding

    private lateinit var viewModel: AddressViewModel
    private lateinit var addressBookDatabase: AddressBookDatabase
    private lateinit var addressBookRepository: AddressBookRepository
    private lateinit var factory: AddressViewFactory
    private val currentContext = this@AddressBookListingActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBookListingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        setToolbar()
        setUpList()
    }

    private fun setToolbar() {

        binding.toolbar.apply {
            setSupportActionBar(toolbar)
            toolbarTitle.text = getString(R.string.lbl_address_book)
            toolbarLeft.show()
            toolbarRight.show()
            toolbarLeft.text = getString(R.string.action_logout)
            toolbarRight.text = getString(R.string.action_add)

            toolbarRight.setOnClickListener(currentContext)
            toolbarLeft.setOnClickListener(currentContext)
        }
    }

    private fun setUpList() {
        addressBookDatabase = AddressBookDatabase(this)
        addressBookRepository = AddressBookRepository(addressBookDatabase)
        factory = AddressViewFactory(addressBookRepository)
        viewModel = ViewModelProvider(this, factory)[AddressViewModel::class.java]

        viewModel.getAllAddress().observe(this) {
            addressableList = it
            setAdapter()
        }
    }

    private fun setAdapter() {
        if (addressableList.isNotEmpty()) {
            if (adapterAddressBookList == null) {
                adapterAddressBookList = AddressBookAdapter(this)
            }
            adapterAddressBookList!!.setData(addressableList)

            binding.rvAddressList.apply {
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(DividerItemDecoration(this@AddressBookListingActivity, RecyclerView.VERTICAL))
                setHasFixedSize(true)
                isNestedScrollingEnabled = false
                isFocusable = true
                adapter = adapterAddressBookList
            }
        }
        handleEmptyList()
    }

    private fun handleEmptyList() {
        binding.tvNoList.handleVisibleHide(addressableList.isEmpty())
        binding.rvAddressList.handleVisibleHide(addressableList.isNotEmpty())
    }

    private fun saveAddressBook() {
        openActivity(EditRemoveAddressBookActivity::class.java)
    }

    private fun editAddressBook(addressBook: AddressBook) {
        openActivityWithIntent(EditRemoveAddressBookActivity::class.java, Constant.Key_editAddressBook, addressBook as Serializable)
    }

    override fun onAddressDeleted(addressBook: AddressBook) {
        val dialog = AlertDialog.Builder(this, R.style.ThemeOverlay_AppCompat_Dialog)
        dialog.setTitle(resources.getString(R.string.delete_address))
            .setMessage(resources.getString(R.string.are_you_sure))
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.deleteProduct(addressBook)
            }.setNegativeButton(R.string.cancel, null).create().show()
    }

    override fun onAddressEdited(addressBook: AddressBook) {
        editAddressBook(addressBook)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.toolbar_right -> {
                saveAddressBook()
            }

            R.id.toolbar_left -> {
                SharedPrefsHelper.setUserDetails(null)
                Globals.logoutProcess(currentContext)
            }
        }
    }
}
