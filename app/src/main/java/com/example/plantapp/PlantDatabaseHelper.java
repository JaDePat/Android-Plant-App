import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.plantapp.objects.Plant;

import java.util.ArrayList;
import java.util.List;

public class PlantDatabaseHelper extends SQLiteOpenHelper{
    private static final String TAG = "PlantDatabaseHelper";
    // Database Info
    private static final String DATABASE_NAME = "plantsDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_PLANTS = "plants";

    // Plant Table Columns
    private static final String KEY_PLANT_ID = "ID";
    private static final String KEY_PLANT_NAME = "NAME";
    private static final String KEY_PLANT_SCIENTIFIC_NAME = "SCIENTIFIC_NAME";
    private static final String KEY_PLANT_LIGHT = "LIGHT";
    private static final String KEY_PLANT_WATER = "WATER";
    private static final String KEY_PLANT_FERTILIZER = "FERTILIZER";
    private static final String KEY_PLANT_TEMPERATURE = "TEMPERATURE";
    private static final String KEY_PLANT_HUMIDITY = "HUMIDITY";
    private static final String KEY_PLANT_FLOWERING = "FLOWERING";
    private static final String KEY_PLANT_PESTS = "PESTS";
    private static final String KEY_PLANT_DISEASES = "DISEASES";
    private static final String KEY_PLANT_SOIL = "SOIL";
    private static final String KEY_PLANT_POT_SIZE = "POT_SIZE";
    private static final String KEY_PLANT_PRUNING = "PRUNING";
    private static final String KEY_PLANT_PROPAGATION = "PROPAGATION";
    private static final String KEY_PLANT_POISONOUS_PLANT_INFO = "POISONOUS_PLANT_INFO";

    private static PlantDatabaseHelper sInstance;

    public static synchronized PlantDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new PlantDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private PlantDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PLANT_TABLE = "CREATE TABLE " + TABLE_PLANTS +
                "(" +
                KEY_PLANT_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_PLANT_NAME + " TEXT," +
                KEY_PLANT_SCIENTIFIC_NAME + " TEXT," +
                KEY_PLANT_LIGHT  + " TEXT," +
                KEY_PLANT_WATER + " TEXT," +
                KEY_PLANT_FERTILIZER + " TEXT," +
                KEY_PLANT_TEMPERATURE + " TEXT," +
                KEY_PLANT_HUMIDITY  + " TEXT," +
                KEY_PLANT_FLOWERING + " TEXT," +
                KEY_PLANT_PESTS + " TEXT," +
                KEY_PLANT_DISEASES + " TEXT," +
                KEY_PLANT_SOIL + " TEXT," +
                KEY_PLANT_POT_SIZE + " TEXT," +
                KEY_PLANT_PRUNING + " TEXT," +
                KEY_PLANT_PROPAGATION + " TEXT," +
                KEY_PLANT_POISONOUS_PLANT_INFO + " TEXT" +
                ")";

        db.execSQL(CREATE_PLANT_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANTS);
            onCreate(db);
        }
    }

    // Insert a plant into the database
    public void addPlant(Plant plant) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The plant might already exist in the database (i.e. the same plant created multiple plants).
            long ID = addOrUpdatePlant(plant);

            ContentValues values = new ContentValues();
            values.put(KEY_PLANT_ID, ID);
            values.put(KEY_PLANT_NAME, plant.NAME);
            values.put(KEY_PLANT_SCIENTIFIC_NAME, plant.SCIENTIFIC_NAME);
            values.put(KEY_PLANT_LIGHT, plant.LIGHT);
            values.put(KEY_PLANT_WATER, plant.WATER);
            values.put(KEY_PLANT_FERTILIZER, plant.FERTILIZER);
            values.put(KEY_PLANT_TEMPERATURE, plant.TEMPERATURE);
            values.put(KEY_PLANT_HUMIDITY, plant.HUMIDITY);
            values.put(KEY_PLANT_FLOWERING, plant.FLOWERING);
            values.put(KEY_PLANT_PESTS, plant.PESTS);
            values.put(KEY_PLANT_DISEASES, plant.DISEASES);
            values.put(KEY_PLANT_SOIL, plant.SOIL);
            values.put(KEY_PLANT_POT_SIZE, plant.POT_SIZE);
            values.put(KEY_PLANT_PRUNING, plant.PRUNING);
            values.put(KEY_PLANT_PROPAGATION, plant.PROPAGATION);
            values.put(KEY_PLANT_POISONOUS_PLANT_INFO, plant.POISONOUS_PLANT_INFO);
            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_PLANTS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add plant to database");
        } finally {
            db.endTransaction();
        }
    }

    // Insert or update a plant in the database
    // Since SQLite doesn't support "upsert" we need to fall back on an attempt to UPDATE (in case the
    // plant already exists) optionally followed by an INSERT (in case the plant does not already exist).
    // Unfortunately, there is a bug with the insertOnConflict method
    // (https://code.google.com/p/android/issues/detail?id=13045) so we need to fall back to the more
    // verbose option of querying for the plant's primary key if we did an update.

    public long addOrUpdatePlant(Plant plant) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long ID = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_PLANT_NAME, plant.SCIENTIFIC_NAME);

            // First try to update the plant in case the plant already exists in the database
            // Use the scientific name since it is unique
            int rows = db.update(TABLE_PLANTS, values, KEY_PLANT_SCIENTIFIC_NAME + "= ?", new String[]{plant.SCIENTIFIC_NAME});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the plant we just updated
                String plantsSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_PLANT_ID, TABLE_PLANTS, KEY_PLANT_SCIENTIFIC_NAME);
                Cursor cursor = db.rawQuery(plantsSelectQuery, new String[]{String.valueOf(plant.SCIENTIFIC_NAME)});
                try {
                    if (cursor.moveToFirst()) {
                        ID = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // plant with this scientific name did not already exist, so insert new plant
                ID = db.insertOrThrow(TABLE_PLANTS, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update plant");
        } finally {
            db.endTransaction();
        }
        return ID;
    }

    // Get all plants in the database
    public List<Plant> getAllPlants() {
        List<Plant> plants = new ArrayList<>();

        // SELECT * FROM PLANTS
=
        String PLANTS_SELECT_QUERY =
                String.format("SELECT * FROM %s LEFT OUTER JOIN %s ON %s.%s = %s.%s",
                        TABLE_PLANTS);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(PLANTS_SELECT_QUERY, null);
        try {
            // moveToFirst() - method moves the cursor to the first row. It allows to perform a test whether the query returned an empty set or not
            if (cursor.moveToFirst()) {
                do {
                    Plant newPlant = new Plant();
                    newPlant.NAME = cursor.getString(cursor.getColumnIndex(KEY_PLANT_NAME));
                    newPlant.SCIENTIFIC_NAME = cursor.getString(cursor.getColumnIndex(KEY_PLANT_SCIENTIFIC_NAME));
                    newPlant.LIGHT =  cursor.getString(cursor.getColumnIndex(KEY_PLANT_LIGHT));
                    newPlant.WATER = cursor.getString(cursor.getColumnIndex(KEY_PLANT_WATER));
                    newPlant.FERTILIZER = cursor.getString(cursor.getColumnIndex(KEY_PLANT_FERTILIZER));
                    newPlant.TEMPERATURE = cursor.getString(cursor.getColumnIndex(KEY_PLANT_TEMPERATURE));
                    newPlant.HUMIDITY = cursor.getString(cursor.getColumnIndex(KEY_PLANT_HUMIDITY));
                    newPlant.FLOWERING = cursor.getString(cursor.getColumnIndex(KEY_PLANT_FLOWERING));
                    newPlant.PESTS = cursor.getString(cursor.getColumnIndex(KEY_PLANT_PESTS));
                    newPlant.DISEASES = cursor.getString(cursor.getColumnIndex(KEY_PLANT_DISEASES));
                    newPlant.SOIL = cursor.getString(cursor.getColumnIndex(KEY_PLANT_SOIL));
                    newPlant.POT_SIZE = cursor.getString(cursor.getColumnIndex(KEY_PLANT_POT_SIZE));
                    newPlant.PRUNING = cursor.getString(cursor.getColumnIndex(KEY_PLANT_PRUNING));
                    newPlant.PROPAGATION = cursor.getString(cursor.getColumnIndex(KEY_PLANT_PROPAGATION));
                    newPlant.POISONOUS_PLANT_INFO = cursor.getString(cursor.getColumnIndex(KEY_PLANT_POISONOUS_PLANT_INFO));

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get plants from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return plants;
    }

    // Delete all plants in the database
    public void deleteAllPlants () {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_PLANTS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all plants");
        } finally {
            db.endTransaction();
        }
    }
}


