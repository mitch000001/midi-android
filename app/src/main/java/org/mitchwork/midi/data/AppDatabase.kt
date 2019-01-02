package org.mitchwork.midi.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = arrayOf(
        MidiDevice::class,
        ControlChange::class,
        ProgramChange::class
    ),
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun midiDeviceDao(): MidiDeviceDao
    abstract fun controlChangeDao(): ControlChangeDao
    abstract fun programChangeDao(): ProgramChangeDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(applicationContext: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java, "midi_db"
                ).build()
            }
            return instance!!
        }
    }
}