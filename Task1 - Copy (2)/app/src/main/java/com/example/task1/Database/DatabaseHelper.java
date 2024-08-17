package com.example.task1.Database;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.task1.Model.Category;
import com.example.task1.Model.Expense;
import com.example.task1.Model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "expense_manager.db";
    private static final int DATABASE_VERSION = 4;

    // Table and column names
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_PHONE = "phone";

    public static final String TABLE_CATEGORIES = "categories";
    public static final String COLUMN_CATEGORY_ID = "id";
    public static final String COLUMN_CATEGORY_USER_ID = "user_id";
    public static final String COLUMN_CATEGORY_NAME = "name";
    public static final String COLUMN_CATEGORY_AMOUNT = "amount";

    public static final String TABLE_EXPENSES = "expenses";
    public static final String COLUMN_EXPENSE_ID = "id";
    public static final String COLUMN_EXPENSE_NAME = "name";
    public static final String COLUMN_EXPENSE_USER_ID = "user_id";
    public static final String COLUMN_EXPENSE_AMOUNT = "amount";
    public static final String COLUMN_EXPENSE_CATEGORY_ID = "category_id";
    public static final String COLUMN_EXPENSE_DATE = "date";

    public static final String TABLE_SESSION = "session";
    public static final String COLUMN_SESSION_USER_ID = "user_id";
    public static final String COLUMN_SESSION_NAME = "name";
    public static final String COLUMN_SESSION_EMAIL = "email";
    public static final String COLUMN_SESSION_PHONE = "phone";
    public static final String COLUMN_SESSION_PASSWORD = "password";

    private static final String TABLE_CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_NAME + " TEXT, " +
                    COLUMN_USER_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_USER_PASSWORD + " TEXT, " +
                    COLUMN_USER_PHONE + " TEXT " + ");";

    private static final String TABLE_CREATE_CATEGORIES =
            "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                    COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CATEGORY_USER_ID + " INTEGER, " +
                    COLUMN_CATEGORY_NAME + " TEXT, " +
                    COLUMN_CATEGORY_AMOUNT + " REAL, " + // New column
                    "FOREIGN KEY(" + COLUMN_CATEGORY_USER_ID + ") REFERENCES " +
                    TABLE_USERS + "(" + COLUMN_USER_ID + ")" +
                    ");";

    private static final String TABLE_CREATE_EXPENSES =
            "CREATE TABLE " + TABLE_EXPENSES + "("
                    + COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_EXPENSE_NAME + " TEXT,"
                    + COLUMN_EXPENSE_USER_ID + " INTEGER,"
                    + COLUMN_EXPENSE_AMOUNT + " REAL,"
                    + COLUMN_EXPENSE_CATEGORY_ID + " INTEGER,"
                    + COLUMN_EXPENSE_DATE + " TEXT,"
                    + "FOREIGN KEY(" + COLUMN_EXPENSE_CATEGORY_ID + ") REFERENCES "
                    + TABLE_CATEGORIES + "(" + COLUMN_CATEGORY_ID + "))";

    private static final String TABLE_CREATE_SESSION =
            "CREATE TABLE " + TABLE_SESSION + " (" +
                    COLUMN_SESSION_USER_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_SESSION_NAME + " TEXT, " +
                    COLUMN_SESSION_EMAIL + " TEXT, " +
                    COLUMN_SESSION_PHONE + " TEXT, " +
                    COLUMN_SESSION_PASSWORD + " TEXT" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_USERS);
        db.execSQL(TABLE_CREATE_CATEGORIES);
        db.execSQL(TABLE_CREATE_EXPENSES);
        db.execSQL(TABLE_CREATE_SESSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION);
        onCreate(db);
    }

    // Add these methods to manage session data

    public void saveSession(int userId, String name, String email, String phone, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SESSION_USER_ID, userId);
        values.put(COLUMN_SESSION_NAME, name);
        values.put(COLUMN_SESSION_EMAIL, email);
        values.put(COLUMN_SESSION_PHONE, phone);
        values.put(COLUMN_SESSION_PASSWORD, password);
        db.replace(TABLE_SESSION, null, values);  // Use replace to update or insert new session
        db.close();
    }

    public Cursor getSession() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_SESSION, new String[]{COLUMN_SESSION_USER_ID, COLUMN_SESSION_NAME, COLUMN_SESSION_EMAIL, COLUMN_SESSION_PHONE, COLUMN_SESSION_PASSWORD},
                null, null, null, null, null);
    }

    public void clearSession() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_SESSION, null, null);
        db.close();
    }
    public boolean isSessionActive() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SESSION, null, null, null, null, null, null);
        boolean sessionExists = cursor.moveToFirst();
        cursor.close();
        return sessionExists;
    }

    // Method to get session data
    public User getSessionUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SESSION, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndex(COLUMN_SESSION_USER_ID));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_SESSION_NAME));
            String email = cursor.getString(cursor.getColumnIndex(COLUMN_SESSION_EMAIL));
            String phone = cursor.getString(cursor.getColumnIndex(COLUMN_SESSION_PHONE));
            String password = cursor.getString(cursor.getColumnIndex(COLUMN_SESSION_PASSWORD));
            cursor.close();
            return new User(userId, name, email, phone, password);
        }
        cursor.close();
        return null;
    }





    //Budget Tab
    public void addCategory(String name, double amount, int userId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, name);
        values.put(COLUMN_CATEGORY_AMOUNT, amount);
        values.put(COLUMN_CATEGORY_USER_ID, userId);
        db.insert(TABLE_CATEGORIES, null, values);
        db.close();
    }

    public List<Category> getCategories(int userId) {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORIES,
                new String[]{COLUMN_CATEGORY_ID, COLUMN_CATEGORY_NAME, COLUMN_CATEGORY_AMOUNT},
                COLUMN_CATEGORY_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));
                double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_CATEGORY_AMOUNT));
                categories.add(new Category(id, name, amount));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categories;
    }



    // Add a new expense
    // Get all expenses for a specific user
    public List<Expense> getExpensesByUser(int userId) {
        List<Expense> expenses = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_EXPENSES,
                new String[]{COLUMN_EXPENSE_ID, COLUMN_EXPENSE_NAME, COLUMN_EXPENSE_USER_ID, COLUMN_EXPENSE_AMOUNT, COLUMN_EXPENSE_CATEGORY_ID, COLUMN_EXPENSE_DATE},
                COLUMN_EXPENSE_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, COLUMN_EXPENSE_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_EXPENSE_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_NAME));
                double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_EXPENSE_AMOUNT));
                int categoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_EXPENSE_CATEGORY_ID));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_DATE));

                expenses.add(new Expense( userId, name, amount, categoryId, date));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return expenses;
    }

    // Add a new expense
    public double getTotalExpensesByCategoryId(int categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_EXPENSE_AMOUNT + ") FROM " + TABLE_EXPENSES + " WHERE " + COLUMN_EXPENSE_CATEGORY_ID + "=?", new String[]{String.valueOf(categoryId)});

        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getDouble(0);
        }
        return 0.0;
    }

    // Lấy ngân sách của danh mục theo ID
    public double getCategoryAmount(int categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORIES, new String[]{COLUMN_CATEGORY_AMOUNT}, COLUMN_CATEGORY_ID + "=?", new String[]{String.valueOf(categoryId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getDouble(cursor.getColumnIndex(COLUMN_CATEGORY_AMOUNT));
        }
        return 0.0;
    }

    // Thêm chi tiêu
    public void addExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPENSE_NAME, expense.getName());
        values.put(COLUMN_EXPENSE_AMOUNT, expense.getAmount());
        values.put(COLUMN_EXPENSE_USER_ID, expense.getUserId());
        values.put(COLUMN_EXPENSE_CATEGORY_ID, expense.getCategoryId());
        values.put(COLUMN_EXPENSE_DATE, expense.getDate());
        db.insert(TABLE_EXPENSES, null, values);
    }


    // Get a specific expense by ID
    public Expense getExpenseById(int expenseId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_EXPENSES,
                new String[]{COLUMN_EXPENSE_ID, COLUMN_EXPENSE_NAME, COLUMN_EXPENSE_USER_ID, COLUMN_EXPENSE_AMOUNT, COLUMN_EXPENSE_CATEGORY_ID, COLUMN_EXPENSE_DATE},
                COLUMN_EXPENSE_ID + "=?",
                new String[]{String.valueOf(expenseId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_EXPENSE_ID));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_NAME));
            int userId = cursor.getInt(cursor.getColumnIndex(COLUMN_EXPENSE_USER_ID));
            double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_EXPENSE_AMOUNT));
            int categoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_EXPENSE_CATEGORY_ID));
            String date = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_DATE));

            cursor.close();
            return new Expense( userId, name, amount, categoryId, date);
        }

        return null;
    }

    // Update an existing expense
    public int updateExpense(Expense expense) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPENSE_NAME, expense.getName());
        values.put(COLUMN_EXPENSE_USER_ID, expense.getUserId());
        values.put(COLUMN_EXPENSE_AMOUNT, expense.getAmount());
        values.put(COLUMN_EXPENSE_CATEGORY_ID, expense.getCategoryId());
        values.put(COLUMN_EXPENSE_DATE, expense.getDate());

        int rowsAffected = db.update(TABLE_EXPENSES, values, COLUMN_EXPENSE_ID + "=?",
                new String[]{String.valueOf(expense.getId())});
        db.close();
        return rowsAffected;
    }

    // Delete an expense
    public void deleteExpense(int expenseId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_EXPENSES, COLUMN_EXPENSE_ID + "=?",
                new String[]{String.valueOf(expenseId)});
        db.close();
    }



    // DatabaseHelper.java

    // Method to retrieve all category names
    // DatabaseHelper.java

//    public List<String> getAllCategoryNames(int userId) {
//        List<String> categories = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT name FROM Categories WHERE user_id = ?";
//        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
//
//        if (cursor.moveToFirst()) {
//            do {
//                String categoryName = cursor.getString(0);
//                categories.add(categoryName);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//        return categories;
//    }


    // Method to get category ID by name
    public int getCategoryIdByName(String categoryName) {
        int categoryId = -1; // Default value if not found
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_CATEGORY_ID + " FROM " + TABLE_CATEGORIES + " WHERE " + COLUMN_CATEGORY_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{categoryName});

        if (cursor.moveToFirst()) {
            categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID));
        }
        cursor.close();
        db.close();

        return categoryId;
    }


    public String getCategoryNameById(int categoryId) {
        SQLiteDatabase db = getReadableDatabase();
        String categoryName = null;
        Cursor cursor = db.query(TABLE_CATEGORIES,
                new String[]{COLUMN_CATEGORY_NAME},
                COLUMN_CATEGORY_ID + "=?",
                new String[]{String.valueOf(categoryId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            categoryName = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));
            cursor.close();
        }
        db.close();
        return categoryName;
    }


    public List<Expense> getAllExpensesByUserId(int userId) {
        List<Expense> expenses = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_EXPENSES,
                new String[]{COLUMN_EXPENSE_ID, COLUMN_EXPENSE_NAME, COLUMN_EXPENSE_AMOUNT, COLUMN_EXPENSE_CATEGORY_ID, COLUMN_EXPENSE_DATE},
                COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_EXPENSE_ID));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_NAME));
            double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_EXPENSE_AMOUNT));
            int categoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_EXPENSE_CATEGORY_ID));
            String date = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_DATE));

            expenses.add(new Expense( userId, name, amount, categoryId, date));
        }
        cursor.close();
        db.close();
        return expenses;
    }

    // Method to get user ID from the session
    public int getUserId() {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1; // Default value if no session exists

        // Query to get the session
        Cursor cursor = db.query(TABLE_SESSION, new String[]{COLUMN_SESSION_USER_ID},
                null, null, null, null, null);

        // Check if a session exists and retrieve user ID
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex(COLUMN_SESSION_USER_ID));
            cursor.close();
        }

        db.close();
        return userId;
    }

    // DatabaseHelper.java

    public List<String> getAllCategoryNames(int userId) {
        List<String> categoryNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM categories WHERE user_id = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                String categoryName = cursor.getString(0);
                categoryNames.add(categoryName);
                // Log để kiểm tra
                Log.d("DatabaseHelper", "Category found: " + categoryName);
            } while (cursor.moveToNext());
        } else {
            Log.d("DatabaseHelper", "No categories found for userId: " + userId);
        }
        cursor.close();
        db.close();
        return categoryNames;
    }



    // Phương thức lấy categoryId từ tên danh mục
    public int getCategoryIdByName(int userId, String categoryName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT id FROM Categories WHERE user_id = ? AND name = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), categoryName});

        if (cursor.moveToFirst()) {
            int categoryId = cursor.getInt(0);
            cursor.close();
            db.close();
            return categoryId;
        }
        cursor.close();
        db.close();
        return -1; // Nếu không tìm thấy categoryId
    }


}
