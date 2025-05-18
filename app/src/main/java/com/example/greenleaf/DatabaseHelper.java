package com.example.greenleaf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "GreenLeaf.db";
    private static final int DATABASE_VERSION = 2; // Incremented version to force table creation

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_PLANTS = "plants";
    private static final String TABLE_CART = "cart";
    private static final String TABLE_FAVORITES = "favorites";
    private static final String TABLE_ORDERS = "orders";
    // Common Column
    private static final String KEY_ID = "id";

    // Users Table Columns
    private static final String KEY_USER_FULLNAME = "fullname";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_PHONE = "phone";
    private static final String KEY_USER_PASSWORD = "password";

    // Plants Table Columns
    private static final String KEY_PLANT_NAME = "name";
    private static final String KEY_PLANT_PRICE = "price";
    private static final String KEY_PLANT_DESCRIPTION = "description";
    private static final String KEY_PLANT_IMAGE = "image";
    private static final String KEY_PLANT_CATEGORY = "category";

    // Cart Table Columns
    private static final String KEY_CART_PLANT_ID = "plant_id";
    private static final String KEY_CART_QUANTITY = "quantity";

    // Favorites Table Columns
    private static final String KEY_FAVORITE_PLANT_ID = "plant_id";
    private static final String KEY_FAVORITE_USER_ID = "user_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createAllTables(db);
        addSamplePlants(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropAllTables(db);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // Verify tables exist
        if (!tableExists(db, TABLE_PLANTS)) {
            Log.w("DatabaseHelper", "Plants table missing, recreating...");
            createAllTables(db);
            addSamplePlants(db);
        }
    }

    private void createAllTables(SQLiteDatabase db) {
        try {
            // Users table
            db.execSQL("CREATE TABLE " + TABLE_USERS + "(" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_USER_FULLNAME + " TEXT," +
                    KEY_USER_EMAIL + " TEXT UNIQUE," +
                    KEY_USER_PHONE + " TEXT," +
                    KEY_USER_PASSWORD + " TEXT)");

            // Plants table
            db.execSQL("CREATE TABLE " + TABLE_PLANTS + "(" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_PLANT_NAME + " TEXT," +
                    KEY_PLANT_PRICE + " REAL," +
                    KEY_PLANT_DESCRIPTION + " TEXT," +
                    KEY_PLANT_IMAGE + " TEXT," +
                    KEY_PLANT_CATEGORY + " TEXT)");

            // Cart table
            db.execSQL("CREATE TABLE " + TABLE_CART + "(" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_CART_PLANT_ID + " INTEGER," +
                    KEY_CART_QUANTITY + " INTEGER)");

            // Favorites table
            db.execSQL("CREATE TABLE " + TABLE_FAVORITES + "(" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_FAVORITE_PLANT_ID + " INTEGER," +
                    KEY_FAVORITE_USER_ID + " INTEGER)");



            Log.d("DatabaseHelper", "All tables created successfully");
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error creating tables", e);
        }
    }

    private void dropAllTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);

    }

    private boolean tableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                new String[]{tableName});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // User methods
    public boolean addUser(String fullname, String email, String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_FULLNAME, fullname);
        values.put(KEY_USER_EMAIL, email);
        values.put(KEY_USER_PHONE, phone);
        values.put(KEY_USER_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{KEY_ID},
                KEY_USER_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_ID};
        String selection = KEY_USER_EMAIL + "=? AND " + KEY_USER_PASSWORD + "=?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public int getUserId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_ID};
        String selection = KEY_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return userId;
    }

    // Plant methods
    // Add this method to check if plants exist
    public boolean hasPlants() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_PLANTS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    // Modify addSamplePlants to return boolean
    public boolean addSamplePlants() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            addPlant(db, "Snake Plant", 24.99, "Great for beginners", "snake_plant", "Beginner");
            addPlant(db, "Monstera", 34.99, "Tropical plant", "monstera", "Popular");
            return true;
        } catch (Exception e) {
            Log.e("Database", "Error adding plants", e);
            return false;
        }
    }
    private void addSamplePlants(SQLiteDatabase db) {
        addPlant(db, "Snake Plant", 24.99, "Great for beginners, purifies air", "snake_plant", "Beginner");
        addPlant(db, "Monstera", 34.99, "Beautiful tropical plant with split leaves", "monstera", "Popular");
        addPlant(db, "Fiddle Leaf Fig", 49.99, "Popular indoor tree with large leaves", "fiddle_leaf", "Popular");
        addPlant(db, "Peace Lily", 19.99, "Elegant plant with white flowers", "peace_lily", "Flowering");
        addPlant(db, "Aloe Vera", 14.99, "Healing properties, easy to care for", "aloe_vera", "Medicinal");
        addPlant(db, "Spider Plant", 16.99, "Easy to grow, produces baby plants", "spider_plant", "Beginner");
        addPlant(db, "Rubber Plant", 29.99, "Hardy plant with glossy leaves", "rubber_plant", "Popular");
        addPlant(db, "ZZ Plant", 22.99, "Thrives in low light conditions", "zz_plant", "Low Light");
    }

    private void addPlant(SQLiteDatabase db, String name, double price, String description, String image, String category) {
        ContentValues values = new ContentValues();
        values.put(KEY_PLANT_NAME, name);
        values.put(KEY_PLANT_PRICE, price);
        values.put(KEY_PLANT_DESCRIPTION, description);
        values.put(KEY_PLANT_IMAGE, image);
        values.put(KEY_PLANT_CATEGORY, category);
        db.insert(TABLE_PLANTS, null, values);
    }

    public Cursor getAllPlants() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PLANTS,
                new String[]{KEY_ID, KEY_PLANT_NAME, KEY_PLANT_PRICE,
                        KEY_PLANT_DESCRIPTION, KEY_PLANT_IMAGE, KEY_PLANT_CATEGORY},
                null, null, null, null, null);
    }

    public Cursor getPlantsByCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PLANTS,
                new String[]{KEY_ID, KEY_PLANT_NAME, KEY_PLANT_PRICE, KEY_PLANT_DESCRIPTION, KEY_PLANT_IMAGE, KEY_PLANT_CATEGORY},
                KEY_PLANT_CATEGORY + "=?",
                new String[]{category},
                null, null, null);
    }

    public List<String> getPlantCategories() {
        List<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + KEY_PLANT_CATEGORY + " FROM " + TABLE_PLANTS, null);

        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categories;
    }

    public Plant getPlantById(int plantId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PLANTS,
                new String[]{KEY_ID, KEY_PLANT_NAME, KEY_PLANT_PRICE, KEY_PLANT_DESCRIPTION, KEY_PLANT_IMAGE, KEY_PLANT_CATEGORY},
                KEY_ID + "=?",
                new String[]{String.valueOf(plantId)},
                null, null, null);

        Plant plant = null;
        if (cursor.moveToFirst()) {
            plant = new Plant(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5));
        }
        cursor.close();
        return plant;
    }

    // Cart methods
    public boolean addToCart(int plantId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if item already exists in cart
        Cursor cursor = db.query(TABLE_CART,
                new String[]{KEY_ID, KEY_CART_QUANTITY},
                KEY_CART_PLANT_ID + "=?",
                new String[]{String.valueOf(plantId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            // Update quantity if exists
            int quantity = cursor.getInt(1) + 1;
            ContentValues values = new ContentValues();
            values.put(KEY_CART_QUANTITY, quantity);

            int rowsAffected = db.update(TABLE_CART, values,
                    KEY_CART_PLANT_ID + "=?",
                    new String[]{String.valueOf(plantId)});
            cursor.close();
            db.close();
            return rowsAffected > 0;
        } else {
            // Add new item to cart
            ContentValues values = new ContentValues();
            values.put(KEY_CART_PLANT_ID, plantId);
            values.put(KEY_CART_QUANTITY, 1);

            long result = db.insert(TABLE_CART, null, values);
            cursor.close();
            db.close();
            return result != -1;
        }
    }

    public List<CartItem> getCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT cart." + KEY_ID + ", cart." + KEY_CART_PLANT_ID + ", " +
                "cart." + KEY_CART_QUANTITY + ", plants." + KEY_PLANT_NAME + ", " +
                "plants." + KEY_PLANT_PRICE + ", plants." + KEY_PLANT_IMAGE + " " +
                "FROM " + TABLE_CART + " cart " +
                "INNER JOIN " + TABLE_PLANTS + " plants ON cart." + KEY_CART_PLANT_ID +
                " = plants." + KEY_ID;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                CartItem item = new CartItem(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(3),
                        cursor.getDouble(4),
                        cursor.getString(5),
                        cursor.getInt(2));
                cartItems.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartItems;
    }

    public boolean removeFromCart(int cartId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_CART, KEY_ID + "=?",
                new String[]{String.valueOf(cartId)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean updateCartItemQuantity(int cartId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CART_QUANTITY, quantity);

        int rowsAffected = db.update(TABLE_CART, values,
                KEY_ID + "=?",
                new String[]{String.valueOf(cartId)});
        db.close();
        return rowsAffected > 0;
    }

    public double getCartTotal() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(plants." + KEY_PLANT_PRICE + " * cart." + KEY_CART_QUANTITY + ") as total " +
                "FROM " + TABLE_CART + " cart " +
                "INNER JOIN " + TABLE_PLANTS + " plants ON cart." + KEY_CART_PLANT_ID +
                " = plants." + KEY_ID;
        Cursor cursor = db.rawQuery(query, null);

        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }

    public int getCartItemCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + KEY_CART_QUANTITY + ") FROM " + TABLE_CART, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    // Favorites methods
    public boolean addFavorite(int plantId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FAVORITE_PLANT_ID, plantId);
        values.put(KEY_FAVORITE_USER_ID, userId);

        long result = db.insert(TABLE_FAVORITES, null, values);
        db.close();
        return result != -1;
    }

    public boolean removeFavorite(int plantId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_FAVORITES,
                KEY_FAVORITE_PLANT_ID + "=? AND " + KEY_FAVORITE_USER_ID + "=?",
                new String[]{String.valueOf(plantId), String.valueOf(userId)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean isFavorite(int plantId, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITES,
                new String[]{KEY_ID},
                KEY_FAVORITE_PLANT_ID + "=? AND " + KEY_FAVORITE_USER_ID + "=?",
                new String[]{String.valueOf(plantId), String.valueOf(userId)},
                null, null, null);

        boolean isFavorite = cursor.getCount() > 0;
        cursor.close();
        return isFavorite;
    }

    public List<Plant> getFavorites(int userId) {
        List<Plant> favorites = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT plants.* FROM " + TABLE_PLANTS + " plants " +
                "INNER JOIN " + TABLE_FAVORITES + " favorites ON plants." + KEY_ID +
                " = favorites." + KEY_FAVORITE_PLANT_ID + " " +
                "WHERE favorites." + KEY_FAVORITE_USER_ID + " = " + userId;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Plant plant = new Plant(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));
                favorites.add(plant);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return favorites;
    }
    // Add this method to your DatabaseHelper class
    public boolean clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_CART, null, null);
        db.close();
        return rowsAffected > 0;
    }
}