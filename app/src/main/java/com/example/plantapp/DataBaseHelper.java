package com.example.plantapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.plantapp.fragments.SearchFragment;
import com.example.plantapp.objects.Plant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "plants.db";
    private static final String DATABASE_DIRECTORY = "/data/data/com.example.plantapp/" +
            "databases/";
    private static String DATABASE_PATH = DATABASE_DIRECTORY + DATABASE_NAME;
    private static String OLD_DATABASE_PATH = DATABASE_DIRECTORY + "old_" + DATABASE_NAME;

    private final Context myContext;

    private boolean createDatabase = false;
    private boolean upgradeDatabase = false;

    // PLANT_TABLE
    public static final String PLANT_TABLE = "PLANT_TABLE";
    public static final String COLUMN_PLANT_ID = "ID";
    public static final String COLUMN_PLANT_NAME = "NAME";
    public static final String COLUMN_PLANT_SCI_NAME = "SCIENTIFIC_NAME";
    public static final String COLUMN_PLANT_LIGHT = "LIGHT";
    public static final String COLUMN_PLANT_WATER = "WATER";
    public static final String COLUMN_PLANT_FERTILIZER = "FERTILIZER";
    public static final String COLUMN_PLANT_TEMPERATURE = "TEMPERATURE";
    public static final String COLUMN_PLANT_HUMIDITY = "HUMIDITY";
    public static final String COLUMN_PLANT_FLOWERING = "FLOWERING";
    public static final String COLUMN_PLANT_PESTS = "PESTS";
    public static final String COLUMN_PLANT_DISEASES = "DISEASES";
    public static final String COLUMN_PLANT_SOIL = "SOIL";
    public static final String COLUMN_PLANT_POT_SIZE = "POT_SIZE";
    public static final String COLUMN_PLANT_PRUNING = "PRUNING";
    public static final String COLUMN_PLANT_PROPAGATION = "PROPAGATION";
    public static final String COLUMN_PLANT_POISON = "POISONOUS_PLANT_INFO";

    // USER_TABLE - keeping this table in case we want to provide a more "personalized"
    //              experience by using the user's name
    public static final String USER_TABLE = "USER_TABLE";
    public static final String COLUMN_USER_NAME = "USER_NAME";

    // WISHLIST_TABLE
    public static final String WISHLIST_TABLE = "WISHLIST_TABLE";
    public static final String COLUMN_WISHLIST_ID = "WISHLIST_ID";
    public static final String COLUMN_WISHLIST_PLANT_ID = "PLANT_ID"; // references PLANT_TABLE

    // PLANTS_OWNED_TABLE
    public static final String PLANTS_OWNED_TABLE = "PLANTS_OWNED_TABLE";
    public static final String COLUMN_PLANTS_OWNED_ID = "PLANTS_OWNED_ID";
    public static final String COLUMN_PLANTS_OWNED_PLANT_ID = "PLANT_ID"; // references PLANT_TABLE

    /*
    * Constructor takes and keeps a reference of the passed context in order to
    * access the application assets and resources
    *
    * */
    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        myContext = context;
        // Get the path of the database that is based on the context.
        DATABASE_PATH = myContext.getDatabasePath(DATABASE_NAME).getAbsolutePath();
    }

    /*
    * Upgrade the database in internal storage if it exists but is not current.
    * Create a new empty database in internal storage if it does not exist.
    * */
    public void initializeDataBase() {
        /*
        * Creates or updates the database in internal storage if it is needed
        * before opening the database. In all cases opening the database copies
        * the database in internal storage to the cache.
        * */
        getWritableDatabase();

        if (createDatabase) {
            /*
            * If the database is created by the copy method, then the creation
            * code needs to go here. This method consists of copying the new
            * database from assets into internal storage and then caching it.
            * */
            try {
                /*
                * Write over the empty data that created in internal
                * storage with the one in assets and then cache it.
                * */
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        } else if (upgradeDatabase) {
            /*
            * If the database is upgraded by the copy and reload method, then
            * the upgrade code needs to go here. This method consists of
            * renaming the old database in internal storage, create an empty
            * new database in internal storage, copying the database from
            * assets to the new database in internal storage, caching the new
            * database from internal storage, loading the data from the old
            * database into the new database in the cache and then deleting the
            * old database from internal storage.
            * */
            try {
                FileHelper.copyFile(DATABASE_PATH, OLD_DATABASE_PATH);
                copyDataBase();
                SQLiteDatabase old_db = SQLiteDatabase.openDatabase(OLD_DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
                SQLiteDatabase new_db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
                /*
                * Add code to load data into the new database from the old
                * database and then delete the old database from internal
                * storage after all data has been transferred
                * */
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /*
    * Copies your database from your local assets-folder to the just created
    * empty database in the system folder, from where it can be accessed and
    * handled. This is done by transferring bytestream.
    * */
    private void copyDataBase() throws IOException {
        /*
        * Close SQLiteOpenHelper so it will commit the created empty database
        * to internal storage.
        * */
        close();

        /*
        * Open the database in the assets folder as the input stream.
        */
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

        /*
         * Open the empty db in internal storage as the output stream.
         */
        OutputStream myOutput = new FileOutputStream(DATABASE_PATH);

        /*
        * Copy over the empty db in internal storage with the database in the
        * assets folder.
        * */
        FileHelper.copyFile(myInput, myOutput);

        /*
        * Access the copied database so SQLiteHelper will cache it and mark it
        * as created.
        * */
        getWritableDatabase().close();
    }

    /*
    * This is where the creation of tables and the initial population of the
    * tables should happen, if a database is being created from scratch instead
    * of being copied from the application package assets. Copying a database
    * from the application package assets to internal storage inside this
    * method will result in a corrupted database.
    * NOTE: This method is normally only called when a database has not already
    * been created. when the database has been copied, then this method is
    * called the first time a reference to the database is retrieved after the
    * database is copied since the database last cached by SQLiteOpenHelper is
    * different than the database in internal storage.
    * */
    @Override
    public void onCreate(SQLiteDatabase db) {

        /*
        * Signal that a new database needs to be copied. The copy process must
        * be performed after the database in the cache has been closed causing
        * it to be committed to internal storage. Otherwise the database in
        * internal storage will not have the same creation timestamp as the one
        * in the cache causing the database in internal storage to be marked as
        * corrupted.
        * */
        createDatabase = true;

        /*String createTableStatement = "CREATE TABLE " + PLANT_TABLE + " (" + COLUMN_PLANT_ID + " INTEGER PRIMARY KEY," +
                COLUMN_PLANT_NAME + " TEXT, " + COLUMN_PLANT_SCI_NAME + " TEXT, " +
                COLUMN_PLANT_LIGHT + " TEXT, " + COLUMN_PLANT_WATER + " TEXT, " +
                COLUMN_PLANT_FERTILIZER + " TEXT, " + COLUMN_PLANT_TEMPERATURE + " TEXT, " +
                COLUMN_PLANT_HUMIDITY + " TEXT, " + COLUMN_PLANT_FLOWERING + " TEXT, " +
                COLUMN_PLANT_PESTS + " TEXT, " + COLUMN_PLANT_DISEASES + " TEXT, " +
                COLUMN_PLANT_SOIL + " TEXT, " + COLUMN_PLANT_POT_SIZE + " TEXT, " +
                COLUMN_PLANT_PRUNING + " TEXT, " + COLUMN_PLANT_PROPAGATION + " TEXT, " +
                COLUMN_PLANT_POISON + " TEXT)";

        db.execSQL(createTableStatement);*/
    }


    /*
    * Called only if version number was changed and the database has already
    * been created. Copying a database from the application package assets to
    * the internal data system inside this method will result in a corrupted
    * database in the internal data system.
    * */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        * Signal that the database needs to be upgraded for the copy method of
        * creation. The copy process must be performed after the database has
        * been opened or the database will be corrupted.
        * */
        upgradeDatabase = true;

        /*
        * Code to update the database via execution of sql statements goes
        * here.
        * */
    }

    /*
    * Called everytime the database is opened by getReadableDatabase or
    * getWritableDatabase. This is called after onCreate or onUpgrade is
    * called.
    * */
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public List<String> getPlantNamesLike(String whatUserTyped) {
        //List<PlantModel> returnList = new ArrayList<>();
        List<String> returnList = new ArrayList<>();
        String[] nameArg = {whatUserTyped + "%"};

        // get data from the database
        String queryString = "SELECT " + COLUMN_PLANT_NAME + " FROM " + PLANT_TABLE +
                " WHERE " + COLUMN_PLANT_NAME + " LIKE ?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, nameArg);

        if (cursor.moveToFirst()) {

            do{

                String plantName = cursor.getString(0);

                //PlantModel newPlant = new PlantModel(plantName);

                //returnList.add(newPlant);

                returnList.add(plantName);

            } while (cursor.moveToNext());
        }
        else {
            // failure. do not add anything to list
            returnList.add("No matching plant found");
        }

        cursor.close();
        db.close();

        return returnList;
    }

    public List<Plant> getPlants()
    {
        List<Plant> plants = new ArrayList<>();

        String queryString = "SELECT * FROM " + PLANT_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                Plant currentPlant = new Plant();
                currentPlant.setID(cursor.getInt(0));
                currentPlant.setName(cursor.getString(1));
                currentPlant.setScientific_Name(cursor.getString(2));
                currentPlant.setLight(cursor.getString(3));
                currentPlant.setWater(cursor.getString(4));
                currentPlant.setFertilizer(cursor.getString(5));
                currentPlant.setTemperature(cursor.getString(6));
                currentPlant.setHumidity(cursor.getString(7));
                currentPlant.setFlowering(cursor.getString(8));
                currentPlant.setPests(cursor.getString(9));
                currentPlant.setDiseases(cursor.getString(10));
                currentPlant.setSoil(cursor.getString(11));
                currentPlant.setPot_size(cursor.getString(12));
                currentPlant.setPruning(cursor.getString(13));
                currentPlant.setPropagation(cursor.getString(14));
                currentPlant.setPoisonous_plant_info(cursor.getString(15));

                plants.add(currentPlant);
            } while (cursor.moveToNext());
        }

        return plants;
    }

    public List<Plant> getOwnedPlants()
    {
        List<Plant> ownedPlants = new ArrayList<>();
        String queryString = "SELECT * FROM  PLANT_TABLE  INNER JOIN  PLANTS_OWNED_TABLE ON  PLANTS_OWNED_TABLE.PLANT_ID  = PLANT_TABLE.ID";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);
        android.util.Log.d("CURSOR", String.format("size = %d", cursor.getCount()));
        if(cursor.moveToFirst()) {
            do {
                Plant currentPlant = new Plant();
                currentPlant.setID(cursor.getInt(0));
                currentPlant.setName(cursor.getString(1));
                currentPlant.setScientific_Name(cursor.getString(2));
                currentPlant.setLight(cursor.getString(3));
                currentPlant.setWater(cursor.getString(4));
                currentPlant.setFertilizer(cursor.getString(5));
                currentPlant.setTemperature(cursor.getString(6));
                currentPlant.setHumidity(cursor.getString(7));
                currentPlant.setFlowering(cursor.getString(8));
                currentPlant.setPests(cursor.getString(9));
                currentPlant.setDiseases(cursor.getString(10));
                currentPlant.setSoil(cursor.getString(11));
                currentPlant.setPot_size(cursor.getString(12));
                currentPlant.setPruning(cursor.getString(13));
                currentPlant.setPropagation(cursor.getString(14));
                currentPlant.setPoisonous_plant_info(cursor.getString(15));

                ownedPlants.add(currentPlant);
            } while (cursor.moveToNext());
        }
        return ownedPlants;
    }

    public List<Plant> getWishlistPlants()
    {
        List<Plant> wishlistPlants = new ArrayList<>();
        String queryString = "SELECT * FROM  PLANT_TABLE  INNER JOIN  WISHLIST_TABLE ON WISHLIST_TABLE.PLANT_ID  = PLANT_TABLE.ID";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);
        android.util.Log.d("CURSOR", String.format("size = %d", cursor.getCount()));
        if(cursor.moveToFirst()) {
            do {
                // TODO: make this code less repetitive
                Plant currentPlant = new Plant();
                currentPlant.setID(cursor.getInt(0));
                currentPlant.setName(cursor.getString(1));
                currentPlant.setScientific_Name(cursor.getString(2));
                currentPlant.setLight(cursor.getString(3));
                currentPlant.setWater(cursor.getString(4));
                currentPlant.setFertilizer(cursor.getString(5));
                currentPlant.setTemperature(cursor.getString(6));
                currentPlant.setHumidity(cursor.getString(7));
                currentPlant.setFlowering(cursor.getString(8));
                currentPlant.setPests(cursor.getString(9));
                currentPlant.setDiseases(cursor.getString(10));
                currentPlant.setSoil(cursor.getString(11));
                currentPlant.setPot_size(cursor.getString(12));
                currentPlant.setPruning(cursor.getString(13));
                currentPlant.setPropagation(cursor.getString(14));
                currentPlant.setPoisonous_plant_info(cursor.getString(15));

                wishlistPlants.add(currentPlant);
            } while (cursor.moveToNext());
        }
        return wishlistPlants;
    }
    public void deleteFromShelf(String get_ID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String deleteString = "DELETE FROM PLANTS_OWNED_TABLE WHERE PLANT_ID ='"+get_ID+"'";
        db.execSQL(deleteString);
        db.setTransactionSuccessful();
       // db.execSQL("DROP TABLE IF EXISTS PLANTS_OWNED_TABLE");
        db.endTransaction();

        Toast.makeText(myContext, "ere i am", Toast.LENGTH_SHORT).show();
    }
}
