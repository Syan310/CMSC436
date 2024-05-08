package com.addressbook.android.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.addressbook.android.R
import com.addressbook.android.databinding.ActivityEditRemoveAddressBookBinding
import com.addressbook.android.main.respository.AddressBookRepository
import com.addressbook.android.main.respository.AddressViewFactory
import com.addressbook.android.main.viewModel.AddressViewModel
import com.addressbook.android.roomDatabase.db.AddressBook
import com.addressbook.android.roomDatabase.db.AddressBookDatabase
import com.addressbook.android.util.*

//An activity for adding a new address book entry or editing an existing one.
// It includes validation of user inputs and communicates with AddressViewModel for database operations.
class EditRemoveAddressBookActivity : AppCompatActivity(), View.OnClickListener {

    private var isUpdate = false
    private var extra: Bundle? = null
    private var mAddressBook: AddressBook? = null
    private val currentContext = this@EditRemoveAddressBookActivity

    private lateinit var binding: ActivityEditRemoveAddressBookBinding
    private lateinit var viewModel: AddressViewModel
    private lateinit var addressBookDatabase: AddressBookDatabase
    private lateinit var addressBookRepository: AddressBookRepository
    private lateinit var factory: AddressViewFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRemoveAddressBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        setSupportActionBar(binding.toolbar.toolbar)

        binding.toolbar.apply {
            toolbarTitle.text = getString(R.string.lbl_detail)
            toolbarLeft.show()
            toolbarRight.show()
            toolbarLeft.text = getString(R.string.lbl_address_book)
            toolbarRight.text = getString(R.string.action_logout)
        }

        addressBookDatabase = AddressBookDatabase(this)
        addressBookRepository = AddressBookRepository(addressBookDatabase)
        factory = AddressViewFactory(addressBookRepository)
        viewModel = ViewModelProvider(this, factory)[AddressViewModel::class.java]

        extra = intent.extras
        if (extra != null && extra!!.containsKey(Constant.Key_editAddressBook)) {
            isUpdate = true
            mAddressBook = extra!!.getSerializable(Constant.Key_editAddressBook) as AddressBook
        }

        // set the text in button based on adding or editing addressBook
        binding.apply {
            if (isUpdate) {
                // set the value from intent bundle
                edtName.setText(mAddressBook?.name)
                edtEmail.setText(mAddressBook?.email)
                edtContactNo.setText(mAddressBook?.contact_number)
                mAddressBook?.isactive?.let { switchActive.isChecked = it }
                btnSaveUpdate.text = getString(R.string.action_update)
            } else {
                btnSaveUpdate.text = getString(R.string.action_save)
            }

            toolbar.toolbarLeft.setOnClickListener(currentContext)
            toolbar.toolbarRight.setOnClickListener(currentContext)
            btnSaveUpdate.setOnClickListener(currentContext)
        }
    }


    private fun insertAddressBook() {
        if (isValid()) {
            val addressBook = AddressBook()
            addressBook.apply {
                name = binding.edtName.text.toString().trim()
                email = binding.edtEmail.text.toString().trim()
                contact_number = binding.edtContactNo.text.toString().trim()
                isactive = binding.switchActive.isChecked
            }.also {
                viewModel.insertAddressBook(addressBook).also { finish() }
            }

        }
    }


    private fun updateAddressBook() {
        if (isValid()) {
            val addressBook = AddressBook()
            addressBook.apply {
                name = binding.edtName.text.toString().trim()
                email = binding.edtEmail.text.toString().trim()
                contact_number = binding.edtContactNo.text.toString().trim()
                isactive = binding.switchActive.isChecked
                id = mAddressBook?.id
            }.also {
                viewModel.updateAddressBook(addressBook).also { finish() }
                val resultIntent = Intent()
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    private fun isValid(): Boolean {
        if (UtilsValidation.validateEmptyEditText(binding.edtName)) {
            toast(getString(R.string.toast_err_name))
            requestFocus(binding.edtName)
            return false
        }
        if (UtilsValidation.validateEmptyEditText(binding.edtEmail)) {
            toast(getString(R.string.toast_err_email))
            requestFocus(binding.edtEmail)
            return false
        }
        if (UtilsValidation.validateEmail(binding.edtEmail)) {
            toast(getString(R.string.toast_err_enter_valid_email))
            requestFocus(binding.edtEmail)
            return false
        }
        if (UtilsValidation.validateEmptyEditText(binding.edtContactNo)) {
            toast(getString(R.string.toast_err_contact_no))
            requestFocus(binding.edtContactNo)
            return false
        }
        if (UtilsValidation.validatePhoneNumber(binding.edtContactNo)) {
            toast(getString(R.string.toast_err_enter_valid_contact_number))
            requestFocus(binding.edtContactNo)
            return false
        }
        return true
    }

    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.toolbar_left -> {
                Globals.hideKeyboard(currentContext)
                onBackPressed()
            }

            R.id.toolbar_right -> {
                Globals.hideKeyboard(currentContext)
                SharedPrefsHelper.setUserDetails(null)
                Globals.logoutProcess(currentContext)
            }

            R.id.btn_save_update -> {
                Globals.hideKeyboard(currentContext)
                if (isUpdate) {
                    updateAddressBook()
                } else {
                    insertAddressBook()
                }
            }
        }
    }
}
