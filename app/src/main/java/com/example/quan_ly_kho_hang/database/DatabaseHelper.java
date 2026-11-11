package com.example.quan_ly_kho_hang.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quan_ly_kho_hang.model.Product;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "warehouse.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_PRODUCT_ID = "id";
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_PRICE = "price";
    public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
    public static final String COLUMN_PRODUCT_CATEGORY = "category";
    public static final String COLUMN_PRODUCT_ACTIVE = "active";
    public static final String COLUMN_PRODUCT_DESCRIPTION = "description";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableUsers = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, "
                + COLUMN_PASSWORD + " TEXT NOT NULL);";
        db.execSQL(createTableUsers);

        String createTableProducts = "CREATE TABLE " + TABLE_PRODUCTS + " ("
                + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL, "
                + COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL, "
                + COLUMN_PRODUCT_CATEGORY + " TEXT NOT NULL, "
                + COLUMN_PRODUCT_ACTIVE + " INTEGER NOT NULL, "
                + COLUMN_PRODUCT_DESCRIPTION + " TEXT);";
        db.execSQL(createTableProducts);

        ContentValues userValues = new ContentValues();
        userValues.put(COLUMN_USERNAME, "admin");
        userValues.put(COLUMN_PASSWORD, "123456");
        db.insert(TABLE_USERS, null, userValues);

        insertSampleProduct(db, "Chuột không dây", 150000, 20, "Điện tử", 1, "Chuột không dây kết nối 2.4GHz");
        insertSampleProduct(db, "Bàn phím cơ", 450000, 3, "Điện tử", 1, "Bàn phím cơ switch xanh, có đèn nền");
        insertSampleProduct(db, "Sổ tay A5", 25000, 40, "Văn phòng phẩm", 1, "Sổ tay kẻ ngang A5 bìa cứng");
    }

    private void insertSampleProduct(SQLiteDatabase db, String name, int price, int quantity,
                                    String category, int active, String description) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, name);
        values.put(COLUMN_PRODUCT_PRICE, price);
        values.put(COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(COLUMN_PRODUCT_CATEGORY, category);
        values.put(COLUMN_PRODUCT_ACTIVE, active);
        values.put(COLUMN_PRODUCT_DESCRIPTION, description);
        db.insert(TABLE_PRODUCTS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_USER_ID + " FROM " + TABLE_USERS
                + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) {
            cursor.close();
        }
        return exists;
    }

    public long insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        return db.insert(TABLE_USERS, null, values);
    }

    public long insertProduct(Product p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, p.getName());
        values.put(COLUMN_PRODUCT_PRICE, p.getPrice());
        values.put(COLUMN_PRODUCT_QUANTITY, p.getQuantity());
        values.put(COLUMN_PRODUCT_CATEGORY, p.getCategory());
        values.put(COLUMN_PRODUCT_ACTIVE, p.getActive());
        values.put(COLUMN_PRODUCT_DESCRIPTION, p.getDescription());
        return db.insert(TABLE_PRODUCTS, null, values);
    }

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " ORDER BY " + COLUMN_PRODUCT_ID + " DESC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_QUANTITY));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_CATEGORY));
                int active = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ACTIVE));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DESCRIPTION));

                list.add(new Product(id, name, price, quantity, category, active, desc));
            }
            cursor.close();
        }
        return list;
    }

    public Product getProductById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCT_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
        Product product = null;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_QUANTITY));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_CATEGORY));
                int active = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ACTIVE));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DESCRIPTION));

                product = new Product(id, name, price, quantity, category, active, desc);
            }
            cursor.close();
        }
        return product;
    }

    public int updateQuantity(int id, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_QUANTITY, quantity);
        return db.update(TABLE_PRODUCTS, values, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PRODUCTS, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public long getTotalValue() {
        SQLiteDatabase db = this.getReadableDatabase();
        long total = 0;
        String query = "SELECT SUM(" + COLUMN_PRODUCT_PRICE + " * " + COLUMN_PRODUCT_QUANTITY + ") FROM " + TABLE_PRODUCTS;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                total = cursor.getLong(0);
            }
            cursor.close();
        }
        return total;
    }

    public int countLowStock() {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        String query = "SELECT COUNT(*) FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCT_QUANTITY + " < 5";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }
}
