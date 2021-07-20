package com.mdevs.naivas.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.mdevs.naivas.customer.classes.CartDetails;
import com.mdevs.naivas.helperclasses.User;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Naivas";
    private static final int DATABASE_VERSION = 1;

    //Database tables
    private static final String USER_TABLE = "userProfile";
    private static final String CART_TABLE = "naivasCart";

    //table fields for userProfile
    private static final String USER_ID = "id";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String GENDER = "gender";
    private static final String EMAIL_ADDRESS = "emailAddress";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String USER_TYPE = "userType";

    //table fields for CartTable
    public static final String PRODUCT_ID = "id";
    public static final String PRODUCT_IMAGE = "image";
    public static final String PRODUCT_NAME = "name";
    public static final String PRODUCT_PRICE = "price";
    public static final String PRODUCT_CATEGORY = "category";
    public static final String PRODUCT_QUANTITY = "quantity";
    public static final String PRODUCT_TOTAL_QUANTITY = "total_quantity";
    public static final String PRODUCT_TOTAL_PRICE = "total_price";


    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table using SQLite Statements
        //UserProfile SQLite Statements
        String PRIMARY_USER_ID = "primary_id";
        String userProfileSQl = "CREATE TABLE " + USER_TABLE + "(" + PRIMARY_USER_ID + " INTEGER PRIMARY KEY, " +
                USER_ID + " INTEGER, " +
                FIRST_NAME + " TEXT, " +
                LAST_NAME + " TEXT, " +
                GENDER + " TEXT, " +
                EMAIL_ADDRESS + " VARCHAR, " +
                PHONE_NUMBER + " VARCHAR, " +
                USER_TYPE + " VARCHAR " + ");";

        db.execSQL(userProfileSQl);

        String cartSQL = "CREATE TABLE " + CART_TABLE + "("
                + PRODUCT_ID + " INTEGER, "
                + PRODUCT_NAME + " TEXT, "
                + PRODUCT_CATEGORY + " TEXT, "
                + PRODUCT_PRICE + " VARCHAR, "
                + PRODUCT_IMAGE + " VARCHAR, "
                + PRODUCT_QUANTITY + " INTEGER, "
                + PRODUCT_TOTAL_PRICE + " INTEGER, "
                + PRODUCT_TOTAL_QUANTITY + " INTEGER " + ");";

        db.execSQL(cartSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //if the tables already exist
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CART_TABLE);
        // Create the table after dropping the existing ones
        onCreate(db);

    }


    public void addUser(User user) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USER_ID, String.valueOf(user.getId()));
        cv.put(FIRST_NAME, user.getFirstname());
        cv.put(LAST_NAME, user.getLastname());
        cv.put(GENDER, user.getGender());
        cv.put(EMAIL_ADDRESS, user.getEmail());
        cv.put(PHONE_NUMBER, user.getPhonenumber());
        cv.put(USER_TYPE, user.getUsertype());

        //insert row in myDb
        myDb.insert(USER_TABLE, null, cv);
        myDb.close();
    }

    //get User Details From myDb using their id
    public User getUser(int id) {
        SQLiteDatabase myDb = this.getReadableDatabase();
        //Cursor userCursor= myDb.query(USER_TABLE,new String[]{FIRST_NAME,LAST_NAME,EMAIL_ADDRESS,PHONE_NUMBER},null,null,null,
        //null,null);

        String sql = "SELECT * FROM " + USER_TABLE + " WHERE " + USER_ID + " = '" + id + "'";
        Cursor userCursor = myDb.rawQuery(sql, null);

        if (userCursor != null)
            userCursor.moveToFirst();

        assert userCursor != null;
        User user = new User(
                userCursor.getInt(userCursor.getColumnIndex(USER_ID)),
                userCursor.getString(userCursor.getColumnIndex(FIRST_NAME)),
                userCursor.getString(userCursor.getColumnIndex(LAST_NAME)),
                userCursor.getString(userCursor.getColumnIndex(GENDER)),
                userCursor.getString(userCursor.getColumnIndex(EMAIL_ADDRESS)),
                userCursor.getString(userCursor.getColumnIndex(PHONE_NUMBER)),
                userCursor.getString(userCursor.getColumnIndex(USER_TYPE))

        );
        userCursor.close();
        myDb.close();

        return user;


    }


    //add Items to Cart
    public void addToCart(CartDetails products) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PRODUCT_ID, products.getId());
        cv.put(PRODUCT_NAME, products.getName());
        cv.put(PRODUCT_CATEGORY, products.getCategory());
        cv.put(PRODUCT_PRICE, products.getPrice());
        cv.put(PRODUCT_IMAGE, products.getImage());
        cv.put(PRODUCT_QUANTITY, products.getSelectedQuantity());
        cv.put(PRODUCT_TOTAL_QUANTITY, products.getQuantity());
        cv.put(PRODUCT_TOTAL_PRICE, products.getTotalPrice());

        myDb.insert(CART_TABLE, null, cv);
        myDb.close();
    }

    public ArrayList<CartDetails> getCartDetails() {
        ArrayList<CartDetails> cartList = new ArrayList<>();
        SQLiteDatabase myDb = this.getReadableDatabase();
        String sql = "SELECT * FROM " + CART_TABLE;

        Cursor cursor = myDb.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                CartDetails cartDetails = new CartDetails();
                cartDetails.setId(cursor.getString(cursor.getColumnIndex(PRODUCT_ID)));
                cartDetails.setName(cursor.getString(cursor.getColumnIndex(PRODUCT_NAME)));
                cartDetails.setCategory(cursor.getString(cursor.getColumnIndex(PRODUCT_CATEGORY)));
                cartDetails.setPrice(cursor.getString(cursor.getColumnIndex(PRODUCT_PRICE)));
                cartDetails.setImage(cursor.getString(cursor.getColumnIndex(PRODUCT_IMAGE)));
                cartDetails.setSelectedQuantity(cursor.getString(cursor.getColumnIndex(PRODUCT_QUANTITY)));
                cartDetails.setQuantity(cursor.getString(cursor.getColumnIndex(PRODUCT_TOTAL_QUANTITY)));
                cartDetails.setTotalPrice(cursor.getString(cursor.getColumnIndex(PRODUCT_TOTAL_PRICE)));

                cartList.add(cartDetails);
            } while (cursor.moveToNext());
        }

        cursor.close();
        myDb.close();

        return cartList;
    }

    public void deleteCartItems() {
        //this method will be called out once the user logs out of their account
        SQLiteDatabase myDb = this.getWritableDatabase();
        String sQL = "DELETE FROM " + CART_TABLE;
        myDb.execSQL(sQL);
        myDb.close();
    }

    public void deleteOneItem(int id) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        String sql = "DELETE FROM " + CART_TABLE + " WHERE id = " + id;
        myDb.execSQL(sql);
        myDb.close();
    }


    public boolean checkIfRowExists(CartDetails cartDetails) {
        boolean exists;
        SQLiteDatabase myDb = this.getReadableDatabase();
        String checkQuery = "SELECT * FROM " + CART_TABLE + " WHERE id = " + cartDetails.getId();
        Cursor cursor = myDb.rawQuery(checkQuery, null);
        exists = cursor.getCount() > 0;

        cursor.close();

        return exists;
    }

}
