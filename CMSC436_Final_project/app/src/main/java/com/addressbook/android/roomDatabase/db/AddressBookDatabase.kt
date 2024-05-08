package com.addressbook.android.roomDatabase.db

import android.content.Context
import androidx.room.*

@Database(
    entities = [AddressBook::class],
    version = 1,
    exportSchema = true
)
abstract class AddressBookDatabase : RoomDatabase() {

    abstract fun getAddressBookDao(): AddressBookDao

    companion object{

        private const val DB_NAME = "addressbook_database.db"
        @Volatile private var instance: AddressBookDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also{
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AddressBookDatabase::class.java,
            DB_NAME
        ).build()
    }
}