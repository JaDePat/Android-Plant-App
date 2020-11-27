package com.example.plantapp;

import android.content.ContentValues;
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

        cursor.close();
        db.close();

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

        db.close();
        cursor.close();

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

        cursor.close();
        db.close();

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

    public ArrayList<Plant> plantWizard(ArrayList<String> lightSelectedChips,
                                             ArrayList<String> humiditySelectedChips,
                                             ArrayList<String> poisonSelectedChips,
                                             int minTemp,
                                             int maxTemp) {
        // int _id = 451;
        // List to hold what the names that we wish to return
        // List<String> returnList = new ArrayList<>();
        ArrayList<Plant> returnList = new ArrayList<>();
        ArrayList<Plant> compoundReturnList = new ArrayList<>();

        // String array to hold temperatures from temperature column
        String[] temperatureArray;

        // Boolean values for what is in the selectedChip arrays
        boolean containsBright = false;
        boolean containsMedium = false;
        boolean containsLow = false;
        boolean containsDirect = false;
        boolean containsIndirect = false;

        boolean containsHumidityVeryHigh = false;
        boolean containsHumidityHigh = false;
        boolean containsHumidityModerate = false;
        boolean containsHumidityLow = false;

        boolean containsPoisonous = false;
        boolean containsNonPoisonous = false;

        boolean validTemperatures = false;

        int minTempFromArray = 50;
        int maxTempFromArray = 90;

        boolean maxSixtyCheck = maxTemp > 59;
        boolean maxSixtyFiveCheck = maxTemp > 64;
        boolean maxFiftyCheck = maxTemp > 49;

        // Set the boolean values
        if (lightSelectedChips.contains("Bright")) {
            containsBright = true;
        }

        if (lightSelectedChips.contains("Medium")) {
            containsMedium = true;
        }

        if (lightSelectedChips.contains("Low")) {
            containsLow = true;
        }

        if (lightSelectedChips.contains("Direct")) {
            containsDirect = true;
        }

        if (lightSelectedChips.contains("Indirect")) {
            containsIndirect = true;
        }

        if (humiditySelectedChips.contains("Very High")) {
            containsHumidityVeryHigh = true;
        }

        if (humiditySelectedChips.contains("High")) {
            containsHumidityHigh = true;
        }

        if (humiditySelectedChips.contains("Moderate")) {
            containsHumidityModerate = true;
        }

        if (humiditySelectedChips.contains("Low")) {
            containsHumidityLow = true;
        }

        if (poisonSelectedChips.contains("Poisonous")) {
            containsPoisonous = true;
        }

        if (poisonSelectedChips.contains("Non-Poisonous")) {
            containsNonPoisonous = true;
        }

        // Select the name, light, scientific_name, and humidity columns
        String queryString = "SELECT * FROM " + PLANT_TABLE + " ORDER BY " + COLUMN_PLANT_NAME;

        // Execute the query and put the results into a cursor
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        // Loop through cursor and add plants that we wish to display to the ArrayList declared
        // above
        if (cursor.moveToFirst()) {

            do {

                // String that holds the contents of the "LIGHT" column
                String plantLight = cursor.getString(3);

                // String that holds the "NAME" of the plant
                String plantName = cursor.getString(1);

                // String that holds the "SCIENTIFIC_NAME" of the plant
                String plantSciName = cursor.getString(2);

                // String that holds the "HUMIDITY" of the plant
                String plantHumidity = cursor.getString(7);
                if (plantHumidity == null) {
                    plantHumidity = "";
                }

                // String that holds the "POISONOUS_PLANT_INFO" of the plant
                String plantPoisonInfo = cursor.getString(15);
                if (plantPoisonInfo == null) {
                    plantPoisonInfo = "";
                }

                // String that holds the "TEMPERATURE" of the plant
                String plantTemperature = cursor.getString(6);

                // PlantModel to hold the current plant that we're on
                Plant currentPlant = new Plant(cursor.getInt(0), plantName, plantSciName,
                        plantLight, cursor.getString(4), cursor.getString(5),
                        plantTemperature, plantHumidity, cursor.getString(8),
                        cursor.getString(9), cursor.getString(10),
                        cursor.getString(11), cursor.getString(12),
                        cursor.getString(13), cursor.getString(14),
                        plantPoisonInfo);

                // Check if the user selected 'Bright' and check for bright in the "LIGHT" column
                if (containsBright) {
                    if (plantLight.contains(" bright ")) {
                        returnList.add(currentPlant);
                        compoundReturnList.add(currentPlant);
                    }
                }

                // Check if the user selected 'Medium' and add the plants that need 'Medium'
                // light to the ArrayList
                //boolean mediumLoopCheck = false;
                if (containsMedium) {
                    if (plantLight.contains(" medium ")) {
                        // if the return list is empty then add the current plant
                        returnList.add(currentPlant);
                        compoundReturnList.add(currentPlant);
                    }
                }

                // Check if the user selected 'Low' and add the plants that need 'Low' light
                // to the ArrayList
                if (containsLow) {
                    if (!plantName.equals("Dracaena Compacta") &&
                            !plantName.equals("Ficus Tree") &&
                            !plantName.equals("Fiddle Leaf Fig Plant") &&
                            plantLight.contains(" low ")) {

                        // if the return list is empty then add the current plant
                        returnList.add(currentPlant);
                        compoundReturnList.add(currentPlant);
                    }
                }

                // Check if the user selected 'Direct' and add the plants that need 'Direct' light
                // to the ArrayList
                if (containsDirect) {
                    if (!plantName.equals("Arrowhead Plant") &&
                            !plantName.equals("Caladium Plant") &&
                            !plantName.equals("Chinese Evergreen Plant - Amelia") &&
                            !plantName.equals("Christmas Cactus Plant") &&
                            !plantName.equals("Chrysanthemum Plant") &&
                            !plantName.equals("Lucky Bamboo Plant") &&
                            !plantName.equals("Orchid - Cymbidium") &&
                            !plantName.equals("Orchid Plant - Phalaenopsis") &&
                            !plantName.equals("Parlor Palm") &&
                            !plantName.equals("Philodendron Congo Plant") &&
                            !plantName.equals("Philodendron Heartleaf") &&
                            !plantName.equals("Spider Plant") &&
                            !plantName.equals("Split Leaf Philodendron") &&
                            plantLight.contains(" direct ") &&
                            !plantLight.contains(" no direct ") &&
                            !plantLight.contains(" Avoid direct ") &&
                            !plantLight.contains(" Too much direct ") &&
                            !plantLight.contains(" Too much light or direct ")) {

                        // if the return list is empty then add the current plant
                        returnList.add(currentPlant);
                        compoundReturnList.add(currentPlant);
                    }
                }

                // Check if the user selected 'Indirect' and add the plants that need 'Indirect'
                // light to the ArrayList
                if (containsIndirect) {
                    if (plantLight.contains(" indirect ")) {
                        returnList.add(currentPlant);
                        compoundReturnList.add(currentPlant);
                    }
                }

                // Check if the user selected 'Very High' humidity and make sure the current plant
                // fits the amount of light selected
                boolean humidityVeryHighLoopCheck = false;
                if (containsHumidityVeryHigh) {
                    //if (!returnList.isEmpty()) {
                    if(!compoundReturnList.isEmpty()) {
                        for (Plant plant : compoundReturnList) {
                            if ((plant.getName() + plant.getScientific_Name()).equals(
                                    (currentPlant.getName() + currentPlant.getScientific_Name())
                            )) {
                                if (!(!plantName.equals("Geranium Plant") &&
                                        (plantHumidity.contains("Very high ") ||
                                                plantHumidity.contains(" very high ")))) {
                                    compoundReturnList.remove(currentPlant);
                                }
                                humidityVeryHighLoopCheck = true;
                                break;
                            }
                        }

                        if (!humidityVeryHighLoopCheck &&
                                !plantName.equals("Geranium Plant") &&
                                (plantHumidity.contains("Very high ") ||
                                        plantHumidity.contains(" very high "))) {

                            //returnList.add(currentPlant);
                            if (lightSelectedChips.isEmpty()) {
                                compoundReturnList.add(currentPlant);
                            }
                        }
                    } else if (!plantName.equals("Geranium Plant") &&
                            plantHumidity.contains("Very high ") ||
                            plantHumidity.contains(" very high ")) {
                        //returnList.add(currentPlant);
                        if (lightSelectedChips.isEmpty()) {
                            compoundReturnList.add(currentPlant);
                        }
                    }
                }

                // Check if the user selected 'High' humidity and make sure the current plant
                // fits the amount of light selected
                boolean humidityHighLoopCheck = false;
                if (containsHumidityHigh) {
                    //if (!returnList.isEmpty()) {
                    if(!compoundReturnList.isEmpty()) {
                        for (Plant plant : compoundReturnList) {
                            if ((plant.getName() + plant.getScientific_Name()).equals(
                                    (currentPlant.getName() + currentPlant.getScientific_Name())
                            )) {
                                if (plantName.equals("Agave Plant") ||
                                        plantName.equals("Geranium Plant") ||
                                        plantHumidity.contains(" very high humidity")) {
                                    compoundReturnList.remove(currentPlant);
                                }

                                if (!plantHumidity.contains(" high humidity") &&
                                        !plantHumidity.contains(" higher ") &&
                                        !plantHumidity.contains(" plenty of humidity") &&
                                        !plantHumidity.contains(" more humid ") &&
                                        !plantHumidity.contains("High humidity")) {

                                    if (!plantName.equals("Peperomia Plant") &&
                                            !plantName.equals("Peperomia Plant - Caperata") &&
                                            !plantName.equals("Terrarium")) {

                                        compoundReturnList.remove(currentPlant);
                                    }
                                }

                                humidityHighLoopCheck = true;
                                break;
                            }
                        }

                        if (!humidityHighLoopCheck &&
                                !plantName.equals("Agave Plant") &&
                                !plantName.equals("Geranium Plant") &&
                                !plantHumidity.contains(" very high humidity")) {

                            if (plantName.equals("Peperomia Plant") ||
                                    plantName.equals("Peperomia Plant - Caperata") ||
                                    plantName.equals("Terrarium")) {

                                //returnList.add(currentPlant);
                                if (lightSelectedChips.isEmpty()) {
                                    compoundReturnList.add(currentPlant);
                                }
                            }

                            if (plantHumidity.contains(" high humidity") ||
                                    plantHumidity.contains(" higher ") ||
                                    plantHumidity.contains(" plenty of humidity") ||
                                    plantHumidity.contains(" more humid ") ||
                                    plantHumidity.contains("High humidity")) {

                                //returnList.add(currentPlant);
                                if (lightSelectedChips.isEmpty()) {
                                    compoundReturnList.add(currentPlant);
                                }
                            }
                        }
                    } else if (!plantName.equals("Agave Plant") &&
                            !plantName.equals("Geranium Plant") &&
                            !plantHumidity.contains(" very high humidity")) {

                        if (plantHumidity.contains(" high humidity") ||
                                plantHumidity.contains(" higher ") ||
                                plantHumidity.contains(" plenty of humidity") ||
                                plantHumidity.contains(" more humid ") ||
                                plantHumidity.contains("High humidity")) {

                            //returnList.add(currentPlant);
                            if (lightSelectedChips.isEmpty()) {
                                compoundReturnList.add(currentPlant);
                            }
                        }
                    }
                }

                // Check if the user selected 'Moderate' humidity and make sure the current
                // fits the amount of light selected
                boolean humidityModerateLoopCheck = false;
                if (containsHumidityModerate) {
                    //if (!returnList.isEmpty()) {
                    if(!compoundReturnList.isEmpty()) {
                        for (Plant plant : compoundReturnList) {
                            if ((plant.getName() + plant.getScientific_Name()).equals(
                                    (currentPlant.getName() + currentPlant.getScientific_Name())
                            )) {

                                if (plantName.equals("Gardenia Plant")) {
                                    compoundReturnList.remove(currentPlant);
                                }

                                if (!plantHumidity.contains(" moderate ") &&
                                        !plantHumidity.contains("Moderate ") &&
                                        !plantHumidity.contains(" household humidity") &&
                                        !plantHumidity.contains(" average ") &&
                                        !plantHumidity.contains(" medium ") &&
                                        !plantHumidity.contains("Average ") &&
                                        !plantHumidity.contains(" normal ") &&
                                        !plantHumidity.contains("Medium ")) {

                                    compoundReturnList.remove(currentPlant);
                                }

                                humidityModerateLoopCheck = true;
                                break;
                            }
                        }

                        if (!humidityModerateLoopCheck && !plantName.equals("Gardenia Plant")) {
                            if (plantHumidity.contains(" moderate ") ||
                                    plantHumidity.contains("Moderate ") ||
                                    plantHumidity.contains(" household humidity") ||
                                    plantHumidity.contains(" average ") ||
                                    plantHumidity.contains(" medium ") ||
                                    plantHumidity.contains("Average ") ||
                                    plantHumidity.contains(" normal ") ||
                                    plantHumidity.contains("Medium ")) {

                                //returnList.add(currentPlant);
                                if (lightSelectedChips.isEmpty()) {
                                    compoundReturnList.add(currentPlant);
                                }
                            }
                        }
                    } else if (plantHumidity.contains(" moderate ") ||
                            plantHumidity.contains("Moderate ") ||
                            plantHumidity.contains(" household humidity") ||
                            plantHumidity.contains(" average ") ||
                            plantHumidity.contains(" medium ") ||
                            plantHumidity.contains("Average ") ||
                            plantHumidity.contains(" normal ") ||
                            plantHumidity.contains("Medium ")) {

                        //returnList.add(currentPlant);
                        if (lightSelectedChips.isEmpty()) {
                            compoundReturnList.add(currentPlant);
                        }
                    }
                }

                // Check if the user selected 'Low' humidity and make sure the current
                // fits the amount of light selected
                boolean humidityLowLoopCheck = false;
                if (containsHumidityLow) {
                    //if (!returnList.isEmpty()) {
                    if(!compoundReturnList.isEmpty()) {
                        for (Plant plant : compoundReturnList) {
                            if ((plant.getName() + plant.getScientific_Name()).equals(
                                    (currentPlant.getName() + currentPlant.getScientific_Name())
                            )) {

                                if (plantName.equals("Aralia Plant") ||
                                        plantName.equals("Aralia Plant - Balfour") ||
                                        plantName.equals("Boston Fern") ||
                                        plantName.equals("Geranium Plant") ||
                                        plantName.equals("Kangaroo Paw Fern") ||
                                        plantName.equals("Kimberly Queen Fern") ||
                                        plantName.equals("Persian Shield Plant")) {

                                    compoundReturnList.remove(currentPlant);
                                }

                                if (!plantHumidity.contains(" low humidity") &&
                                        !plantHumidity.contains("Low humidity") &&
                                        !plantHumidity.contains(" very little ") &&
                                        !plantHumidity.contains(" lower ")) {

                                    compoundReturnList.remove(currentPlant);
                                }

                                humidityLowLoopCheck = true;
                                break;
                            }
                        }

                        if (!humidityLowLoopCheck &&
                                !plantName.equals("Aralia Plant") &&
                                !plantName.equals("Aralia Plant - Balfour") &&
                                !plantName.equals("Boston Fern") &&
                                !plantName.equals("Geranium Plant") &&
                                !plantName.equals("Kangaroo Paw Fern") &&
                                !plantName.equals("Kimberly Queen Fern") &&
                                !plantName.equals("Persian Shield Plant")) {

                            if (plantHumidity.contains(" low humidity") ||
                                    plantHumidity.contains("Low humidity") ||
                                    plantHumidity.contains(" very little ") ||
                                    plantHumidity.contains(" lower ")) {

                                //returnList.add(currentPlant);
                                if (lightSelectedChips.isEmpty()) {
                                    compoundReturnList.add(currentPlant);
                                }
                            }
                        }
                    } else if (plantHumidity.contains(" low humidity") ||
                            plantHumidity.contains("Low humidity") ||
                            plantHumidity.contains(" very little ") ||
                            plantHumidity.contains(" lower ")) {

                        //returnList.add(currentPlant);
                        if (lightSelectedChips.isEmpty()) {
                            compoundReturnList.add(currentPlant);
                        }
                    }
                }

                // Check if the user selected 'Poisonous' and make sure the current plant
                // fits the amount of light and humidity selected
                boolean poisonPoisonousLoopCheck = false;
                if (containsPoisonous) {
                    if (!compoundReturnList.isEmpty()) {
                        for (Plant plant : compoundReturnList) {
                            if ((plant.getName() + plant.getScientific_Name()).equals(
                                    (currentPlant.getName() + currentPlant.getScientific_Name())
                            )) {
                                if (plantPoisonInfo.equals("") ||
                                        plantPoisonInfo.contains("- poisonous") ||
                                        plantPoisonInfo.contains("-poisonous") ||
                                        plantPoisonInfo.contains("- Poisonous") ||
                                        plantPoisonInfo.contains("-Poisonous")) {

                                    compoundReturnList.remove(currentPlant);
                                }

                                poisonPoisonousLoopCheck = true;
                                break;
                            }
                        }

                        if (!poisonPoisonousLoopCheck &&
                                !plantPoisonInfo.equals("") &&
                                !plantPoisonInfo.contains("- poisonous") &&
                                !plantPoisonInfo.contains("-poisonous") &&
                                !plantPoisonInfo.contains("- Poisonous") &&
                                !plantPoisonInfo.contains("-Poisonous")) {

                            //returnList.add(currentPlant);
                            if (lightSelectedChips.isEmpty() && humiditySelectedChips.isEmpty()) {
                                compoundReturnList.add(currentPlant);
                            }
                        }
                    }
                    else if (!plantPoisonInfo.equals("") &&
                            !plantPoisonInfo.contains("- poisonous") &&
                            !plantPoisonInfo.contains("-poisonous") &&
                            !plantPoisonInfo.contains("- Poisonous") &&
                            !plantPoisonInfo.contains("-Poisonous")) {

                        //returnList.add(currentPlant);
                        if (lightSelectedChips.isEmpty() && humiditySelectedChips.isEmpty()) {
                            compoundReturnList.add(currentPlant);
                        }
                    }

                    /*if (!returnList.isEmpty()) {
                        for (PlantModel plant : returnList) {
                            if ((plant.getName() + plant.getScientific_name()).equals(
                                    (currentPlant.getName() + currentPlant.getScientific_name())
                            )) {
                                poisonPoisonousLoopCheck = true;
                                break;
                            }
                        }

                        if (!poisonPoisonousLoopCheck &&
                                !plantPoisonInfo.equals("") &&
                                !plantPoisonInfo.contains("- poisonous") &&
                                !plantPoisonInfo.contains("-poisonous") &&
                                !plantPoisonInfo.contains("- Poisonous") &&
                                !plantPoisonInfo.contains("-Poisonous")) {

                            returnList.add(currentPlant);
                            if (lightSelectedChips.isEmpty() && humiditySelectedChips.isEmpty()) {
                                compoundReturnList.add(currentPlant);
                            }
                        }
                    } else if (!plantPoisonInfo.equals("") &&
                            !plantPoisonInfo.contains("- poisonous") &&
                            !plantPoisonInfo.contains("-poisonous") &&
                            !plantPoisonInfo.contains("- Poisonous") &&
                            !plantPoisonInfo.contains("-Poisonous")) {

                        returnList.add(currentPlant);
                        if (lightSelectedChips.isEmpty() && humiditySelectedChips.isEmpty()) {
                            compoundReturnList.add(currentPlant);
                        }
                    }*/
                }

                // Check if the user selected 'Non-Poisonous' and make sure the current plant
                // fits the amount of light and humidity selected
                boolean poisonNonPoisonousLoopCheck = false;
                if (containsNonPoisonous) {
                    if (!compoundReturnList.isEmpty()) {
                        for (Plant plant : compoundReturnList) {
                            if ((plant.getName() + plant.getScientific_Name()).equals(
                                    (currentPlant.getName() + currentPlant.getScientific_Name())
                            )) {
                                if (!plantPoisonInfo.contains("Non-") &&
                                        !plantPoisonInfo.contains("Non -") &&
                                        !plantPoisonInfo.contains("non-") &&
                                        !plantPoisonInfo.contains("non -")) {

                                    compoundReturnList.remove(currentPlant);
                                }

                                poisonNonPoisonousLoopCheck = true;
                                break;
                            }
                        }

                        if (!poisonNonPoisonousLoopCheck) {
                            if (plantPoisonInfo.contains("Non-") ||
                                    plantPoisonInfo.contains("Non -") ||
                                    plantPoisonInfo.contains("non-") ||
                                    plantPoisonInfo.contains("non -")) {

                                //returnList.add(currentPlant);
                                if (lightSelectedChips.isEmpty() && humiditySelectedChips.isEmpty()) {
                                    compoundReturnList.add(currentPlant);
                                }
                            }
                        }
                    }
                    else if (plantPoisonInfo.contains("Non-") ||
                            plantPoisonInfo.contains("Non -") ||
                            plantPoisonInfo.contains("non-") ||
                            plantPoisonInfo.contains("non -")) {

                        //returnList.add(currentPlant);
                        if (lightSelectedChips.isEmpty() && humiditySelectedChips.isEmpty()) {
                            compoundReturnList.add(currentPlant);
                        }
                    }

                    /*if (!returnList.isEmpty()) {
                        for (PlantModel plant : returnList) {
                            if ((plant.getName() + plant.getScientific_name()).equals(
                                    (currentPlant.getName() + currentPlant.getScientific_name())
                            )) {
                                poisonNonPoisonousLoopCheck = true;
                                break;
                            }
                        }

                        if (!poisonNonPoisonousLoopCheck) {
                            if (plantPoisonInfo.contains("Non-") ||
                                    plantPoisonInfo.contains("Non -") ||
                                    plantPoisonInfo.contains("non-") ||
                                    plantPoisonInfo.contains("non -")) {

                                returnList.add(currentPlant);
                                if (lightSelectedChips.isEmpty() && humiditySelectedChips.isEmpty()) {
                                    compoundReturnList.add(currentPlant);
                                }
                            }
                        }
                    } else if (plantPoisonInfo.contains("Non-") ||
                            plantPoisonInfo.contains("Non -") ||
                            plantPoisonInfo.contains("non-") ||
                            plantPoisonInfo.contains("non -")) {

                        returnList.add(currentPlant);
                        if (lightSelectedChips.isEmpty() && humiditySelectedChips.isEmpty()) {
                            compoundReturnList.add(currentPlant);
                        }
                    }*/
                }

                // loop check for the temperature test
                boolean temperatureLoopCheck = false;

                // Removes plants that don't fit the temperature range provided by the user
                if (!compoundReturnList.isEmpty()) {
                    for (Plant plant : compoundReturnList) {
                        if (!lightSelectedChips.isEmpty() || !humiditySelectedChips.isEmpty() || !poisonSelectedChips.isEmpty()) {
                            if ((plant.getName() + plant.getScientific_Name()).equals(
                                    (currentPlant.getName() + currentPlant.getScientific_Name())
                            )) {
                                if (plantTemperature != null) {
                                    temperatureArray = plantTemperature.split("\\D+");

                                    if (plantName.equals("Begonia Rex") && !maxSixtyCheck) {
                                        compoundReturnList.remove(currentPlant);
                                    } else if (plantName.equals("Bromeliad - Silver Vase") && !maxSixtyCheck) {
                                        compoundReturnList.remove(currentPlant);
                                    } else if (plantName.equals("Chrysanthemum Plant") && !maxSixtyCheck) {
                                        compoundReturnList.remove(currentPlant);
                                    } else if (plantName.equals("Desert Rose Plant") && !maxSixtyCheck) {
                                        compoundReturnList.remove(currentPlant);
                                    } else if (plantName.equals("Dracaena Reflexa Song of India") && !maxSixtyFiveCheck) {
                                        compoundReturnList.remove(currentPlant);
                                    } else if (plantName.equals("Hawaiian Schefflera - Gold Capella") && !maxSixtyFiveCheck) {
                                        compoundReturnList.remove(currentPlant);
                                    } else if (plantName.equals("Hawaiian Schefflera Plant") && !maxSixtyFiveCheck) {
                                        compoundReturnList.remove(currentPlant);
                                    } else if (plantName.equals("Kimberly Queen Fern") && !maxSixtyCheck) {
                                        compoundReturnList.remove(currentPlant);
                                    } else if (plantName.equals("Maidenhair Fern") && !maxFiftyCheck) {
                                        compoundReturnList.remove(currentPlant);
                                    } else if (plantName.equals("Moses in the Cradle Plant") && !maxSixtyCheck) {
                                        compoundReturnList.remove(currentPlant);
                                    } else if (plantName.equals("Stromanthe Tricolor Plant") && !maxSixtyCheck) {
                                        compoundReturnList.remove(currentPlant);
                                    } else if (plantName.equals("Terrarium") && !maxSixtyCheck) {
                                        compoundReturnList.remove(currentPlant);
                                    } else if (temperatureArray.length > 2 &&
                                            !plantName.equals("Bromeliad - Silver Vase") &&
                                            !plantName.equals("Chrysanthemum Plant") &&
                                            !plantName.equals("Dracaena Reflexa Song of India") &&
                                            !plantName.equals("Kimberly Queen Fern") &&
                                            !plantName.equals("Moses in the Cradle Plant")) {
                                        minTempFromArray = Integer.parseInt(temperatureArray[1]);

                                        if (maxTemp < minTempFromArray) {
                                            compoundReturnList.remove(currentPlant);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Returns plants that fit the temperature range given by the user if they
                // only give a temperature range
                if (!returnList.isEmpty()) {
                    for (Plant plant : returnList) {
                        if ((plant.getName() + plant.getScientific_Name()).equals(
                                (currentPlant.getName() + currentPlant.getScientific_Name())
                        )) {
                            temperatureLoopCheck = true;
                            break;
                        }
                    }

                    if (!temperatureLoopCheck) {
                        if (plantTemperature != null) {
                            temperatureArray = plantTemperature.split("\\D+");

                            if (plantName.equals("Begonia Rex") && maxSixtyCheck) {
                                returnList.add(currentPlant);
                            } else if (plantName.equals("Bromeliad - Silver Vase") && maxSixtyCheck) {
                                returnList.add(currentPlant);
                            } else if (plantName.equals("Chrysanthemum Plant") && maxSixtyCheck) {
                                returnList.add(currentPlant);
                            } else if (plantName.equals("Desert Rose Plant") && maxSixtyCheck) {
                                returnList.add(currentPlant);
                            } else if (plantName.equals("Dracaena Reflexa Song of India") && maxSixtyFiveCheck) {
                                returnList.add(currentPlant);
                            } else if (plantName.equals("Hawaiian Schefflera - Gold Capella") && maxSixtyFiveCheck) {
                                returnList.add(currentPlant);
                            } else if (plantName.equals("Hawaiian Schefflera Plant") && maxSixtyFiveCheck) {
                                returnList.add(currentPlant);
                            } else if (plantName.equals("Kimberly Queen Fern") && maxSixtyCheck) {
                                returnList.add(currentPlant);
                            } else if (plantName.equals("Maidenhair Fern") && maxFiftyCheck) {
                                returnList.add(currentPlant);
                            } else if (plantName.equals("Moses in the Cradle Plant") && maxSixtyCheck) {
                                returnList.add(currentPlant);
                            } else if (plantName.equals("Stromanthe Tricolor Plant") && maxSixtyCheck) {
                                returnList.add(currentPlant);
                            } else if (plantName.equals("Terrarium") && maxSixtyCheck) {
                                returnList.add(currentPlant);
                            } else if (temperatureArray.length > 2 &&
                                    !plantName.equals("Bromeliad - Silver Vase") &&
                                    !plantName.equals("Chrysanthemum Plant") &&
                                    !plantName.equals("Dracaena Reflexa Song of India") &&
                                    !plantName.equals("Kimberly Queen Fern") &&
                                    !plantName.equals("Moses in the Cradle Plant")) {
                                minTempFromArray = Integer.parseInt(temperatureArray[1]);

                                if (maxTemp >= minTempFromArray) {
                                    returnList.add(currentPlant);
                                }
                            }
                        }
                    }
                } else if (plantTemperature != null) {
                    temperatureArray = plantTemperature.split("\\D+");
                    if (temperatureArray.length > 2) {
                        minTempFromArray = Integer.parseInt(temperatureArray[1]);
                        if (maxTemp >= minTempFromArray) {
                            returnList.add(currentPlant);
                        }
                    }
                }

            } while (cursor.moveToNext());

        } else {
            // failure
            returnList.add(new Plant(450, "Something went wrong", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", ""));
        }

        // clean up
        cursor.close();
        db.close();

        //return returnList;
        if (lightSelectedChips.isEmpty() && humiditySelectedChips.isEmpty() && poisonSelectedChips.isEmpty()) {
            return returnList;
        } else {
            return compoundReturnList;
        }
    }

}
