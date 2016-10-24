package com.example.siddhi.contactsapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.siddhi.contactsapp.Contact;
import com.example.siddhi.contactsapp.User;
import com.example.siddhi.contactsapp.helper.Constants;

import java.util.ArrayList;

/**
 * Created by Siddhi on 9/29/2016.
 */
public class ContactTableHelper extends SQLiteOpenHelper{

    private static final String CONTACT_TABLE = "contactTable";
    private static final String KEY_CONTACT_NAME = "contactName";
    private static final String KEY_CONTACT_PASS = "contactPass";
    private static final String KEY_CONTACT_EMAIL_ID = "contactEmailId";
    private static final String KEY_CONTACT_MOBILE_NO = "contactMobileNo";
    private static final String KEY_CONTACT_PROFILE_IMAGE = "contactProfileImage";
    private static final String KEY_CONTACT_ID = "contactId";
    private static final String KEY_CONTACT_FULL_NAME = "fullName";
    private static final String KEY_CONTACT_JOB_TITLE = "jobTitle";
    private static final String KEY_CONTACT_WORK_ADDRESS = "workAddress";
    private static final String KEY_CONTACT_WORK_PHONE = "workPhone";
    private static final String KEY_CONTACT_HOME_ADDRESS = "homeAddress";

    public ContactTableHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + CONTACT_TABLE);

        // createTable(db);
        // onCreate(db);
    }

    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_CONTACT_ID, contact.getContactId());
        values.put(KEY_CONTACT_NAME, contact.getmUserName());
        values.put(KEY_CONTACT_PASS, contact.getmPass());
        values.put(KEY_CONTACT_MOBILE_NO, contact.getmMobileNo());
        values.put(KEY_CONTACT_EMAIL_ID, contact.getmEmailId());
        values.put(KEY_CONTACT_FULL_NAME,contact.getmFullName());
        values.put(KEY_CONTACT_JOB_TITLE, contact.getmJobTitle());
        values.put(KEY_CONTACT_HOME_ADDRESS, contact.getmHomeAddress());
        values.put(KEY_CONTACT_WORK_ADDRESS, contact.getmWorkAddress());
        values.put(KEY_CONTACT_WORK_PHONE, contact.getmWorkPhone());
        values.put(KEY_CONTACT_PROFILE_IMAGE, contact.getmProfileImage());

        db.insert(CONTACT_TABLE, null, values);

        db.close();
    }

    public Contact getContacts(String id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Contact contact = new Contact();

        Cursor cursor = db.query(CONTACT_TABLE, new String[]{KEY_CONTACT_ID,
                        KEY_CONTACT_NAME, KEY_CONTACT_PASS, KEY_CONTACT_MOBILE_NO,KEY_CONTACT_EMAIL_ID, KEY_CONTACT_PROFILE_IMAGE,KEY_CONTACT_FULL_NAME
        ,KEY_CONTACT_JOB_TITLE,KEY_CONTACT_WORK_ADDRESS,KEY_CONTACT_WORK_PHONE,KEY_CONTACT_HOME_ADDRESS}, KEY_CONTACT_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        //cursor.moveToFirst();
        if( cursor != null && cursor.moveToFirst() ) {

           contact = new Contact(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),
                    cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),
                   cursor.getString(9),cursor.getString(10));
        }

        return contact;
    }

    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> conList = new ArrayList<Contact>();

        String selectQuery = "SELECT * FROM " + CONTACT_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                Contact contact = new Contact();

                contact.setContactId(cursor.getString(0));
                contact.setmUserName(cursor.getString(1));
                contact.setmPass(cursor.getString(2));
                contact.setmMobileNo(cursor.getString(3));
                contact.setmEmailId(cursor.getString(4));
                contact.setmProfileImage(cursor.getString(5));
                contact.setmFullName(cursor.getString(6));
                contact.setmJobTitle(cursor.getString(7));
                contact.setmWorkAddress(cursor.getString(8));
                contact.setmWorkPhone(cursor.getString(9));
                contact.setmHomeAddress(cursor.getString(10));


                conList.add(contact);
            } while (cursor.moveToNext());
        }

        return conList;
    }
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_CONTACT_NAME, contact.getmUserName());
        values.put(KEY_CONTACT_PASS, contact.getmPass());
        values.put(KEY_CONTACT_EMAIL_ID, contact.getmEmailId());
        values.put(KEY_CONTACT_MOBILE_NO, contact.getmMobileNo());;
        values.put(KEY_CONTACT_JOB_TITLE, contact.getmJobTitle());
        values.put(KEY_CONTACT_HOME_ADDRESS, contact.getmHomeAddress());
        values.put(KEY_CONTACT_WORK_ADDRESS, contact.getmWorkAddress());
        values.put(KEY_CONTACT_WORK_PHONE, contact.getmWorkPhone());
        values.put(KEY_CONTACT_PROFILE_IMAGE, contact.getmProfileImage());
        values.put(KEY_CONTACT_FULL_NAME, contact.getmFullName());


        // updating row
        return db.update(CONTACT_TABLE, values, KEY_CONTACT_ID + " = ?",
                new String[]{String.valueOf(contact.getContactId())});

    }

    public void deleteContact(String contactId) {
        //Open the database
        SQLiteDatabase database = this.getWritableDatabase();

        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        database.execSQL("DELETE FROM " + CONTACT_TABLE + " WHERE " + KEY_CONTACT_ID + "= '" + contactId + "'");

        //Close the database
        database.close();
    }

    public void deleteAllContacts()
    {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("delete from "+ CONTACT_TABLE);
        database.close();

    }
}
