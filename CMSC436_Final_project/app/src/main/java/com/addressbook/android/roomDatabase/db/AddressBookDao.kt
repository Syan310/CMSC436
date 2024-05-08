package com.addressbook.android.roomDatabase.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.addressbook.android.roomDatabase.db.AddressBook

@Dao
interface AddressBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddressBook(addressBook: AddressBook)

    @Update
    suspend fun updateAddressBook(addressBook: AddressBook)

    @Delete
    suspend fun deleteAddressBook(addressBook: AddressBook)

    @Query("SELECT * FROM AddressBookTable ORDER BY name")
    fun getAllAddressBook() :LiveData<List<AddressBook>>

    @Query("DELETE FROM AddressBookTable")
    suspend fun clearAddress()

    @Query("DELETE FROM AddressBookTable WHERE id=:id")
    suspend fun deleteAddressBook(id: Long)

}