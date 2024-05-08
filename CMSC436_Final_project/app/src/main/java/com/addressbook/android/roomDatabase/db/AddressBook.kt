package com.addressbook.android.roomDatabase.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "AddressBookTable")
class AddressBook : Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null

    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "email")
    var email: String? = null

    @ColumnInfo(name = "contact_number")
    var contact_number: String? = null

    @ColumnInfo(name = "isactive")
    var isactive: Boolean? = null

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END
}