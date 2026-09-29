package com.techquantum.tqdkhata.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.techquantum.tqdkhata.data.model.ClientEntity
import com.techquantum.tqdkhata.data.model.ClientResourceEntity
import com.techquantum.tqdkhata.data.model.ReminderEntity

@Database(
    entities = [ClientEntity::class, ReminderEntity::class, ClientResourceEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun clientDao(): ClientDao
    abstract fun reminderDao(): ReminderDao
    abstract fun clientResourceDao(): ClientResourceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Add businessType column to clients table
                db.execSQL("ALTER TABLE clients ADD COLUMN businessType TEXT DEFAULT NULL")

                // Create client_resources table for photos and videos
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS client_resources (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        clientId INTEGER NOT NULL,
                        filePath TEXT NOT NULL,
                        resourceType TEXT NOT NULL,
                        title TEXT,
                        createdAt INTEGER NOT NULL,
                        FOREIGN KEY(clientId) REFERENCES clients(id) ON DELETE CASCADE
                    )
                """)
                db.execSQL("CREATE INDEX IF NOT EXISTS index_client_resources_clientId ON client_resources(clientId)")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tqd_khata_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
