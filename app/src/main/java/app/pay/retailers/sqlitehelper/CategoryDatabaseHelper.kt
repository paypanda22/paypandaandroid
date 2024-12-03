package app.pay.retailers.sqlitehelper


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper


import android.database.sqlite.SQLiteDatabase as SQLiteDatabase1


// SQLite Helper class to manage Category data
class CategoryDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "category_db" // Database name
        private const val DATABASE_VERSION = 1 // Database version

        // Table and column names
        const val TABLE_NAME = "category_data"
        const val COLUMN_ID = "_id"
        const val COLUMN_ICON = "icon"
        const val COLUMN_SERVICE_CATEGORY = "service_category"
        const val COLUMN_SERVICE_NAME = "service_name"
    }

    override fun onCreate(db: SQLiteDatabase1) {
        // SQL statement to create the category table
        val CREATE_CATEGORY_TABLE = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID TEXT PRIMARY KEY, "
                + "$COLUMN_ICON TEXT, "
                + "$COLUMN_SERVICE_CATEGORY TEXT, "
                + "$COLUMN_SERVICE_NAME TEXT)")

        db.execSQL(CREATE_CATEGORY_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase1, oldVersion: Int, newVersion: Int) {
        // Drop the old table if it exists and create a new one
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertCategory(dataList: List<app.pay.retailers.responsemodels.allservices.Data>): Long {
        val db = this.writableDatabase
        var result: Long = -1 // Initialize result variable

        for (data in dataList) { // Loop through each Data object in the list
            val contentValues = ContentValues()
            contentValues.put(COLUMN_ID, data._id)
            contentValues.put(COLUMN_ICON, data.icon)
            contentValues.put(COLUMN_SERVICE_CATEGORY, data.service_category)
            contentValues.put(COLUMN_SERVICE_NAME, data.service_name)

            // Insert each data item into the table
            result = db.insert(TABLE_NAME, null, contentValues)
        }

        return result
    }



    // Retrieve all category data from SQLite
    fun getAllCategories(): List<app.pay.retailers.responsemodels.allservices.Data> {
        val categoryList: MutableList<app.pay.retailers.responsemodels.allservices.Data> = mutableListOf()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val category = app.pay.retailers.responsemodels.allservices.Data(
                    _id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    icon = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ICON)),
                    service_category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_CATEGORY)),
                    service_name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_NAME))
                )
                categoryList.add(category)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return categoryList
    }

    // Delete all records from the category table
    fun deleteAllCategories() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
    }

}
