package com.example.siddhi.contactsapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.siddhi.contactsapp.helper.Constants;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String USER_TABLE = "userTable";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_EMAIL_ID = "emailId";
    private static final String KEY_MOBILE_NO = "mobileNo";
    private static final String KEY_PROFILE_IMAGE = "profileImage";
    private static final String KEY_FULL_NAME = "fullName";
    private static final String KEY_DEVICE_ID = "deviceId";
    private static final String KEY_PASSWORD ="password";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_JOB_TITLE = "jobTitle";
    private static final String KEY_WORK_ADDRESS = "workAddress";
    private static final String KEY_WORK_PHONE = "workPhone";
    private static final String KEY_HOME_ADDRESS = "homeAddress";


    private static final String CONTACT_TABLE = "contactTable";
    private static final String KEY_CONTACT_NAME = "contactName";
    private static final String KEY_CONTACT_EMAIL_ID = "contactEmailId";
    private static final String KEY_CONTACT_MOBILE_NO = "contactMobileNo";
    private static final String KEY_CONTACT_PROFILE_IMAGE = "contactProfileImage";
    private static final String KEY_CONTACT_ID = "contactId";
    private static final String KEY_CONTACT_FULL_NAME = "fullName";
    private static final String KEY_CONTACT_JOB_TITLE = "jobTitle";
    private static final String KEY_CONTACT_WORK_ADDRESS = "workAddress";
    private static final String KEY_CONTACT_WORK_PHONE = "workPhone";
    private static final String KEY_CONTACT_HOME_ADDRESS = "homeAddress";

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
        this.context = context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        createTable(db);

    }

    public void createDatabase(){
        context.deleteDatabase(Constants.DATABASE_NAME + ".db");
        SQLiteDatabase db = this.getReadableDatabase();
    }


    public void createTable(SQLiteDatabase db){
        String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE + "("
                + KEY_USER_ID + " TEXT,"
                + KEY_USER_NAME + " TEXT,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_MOBILE_NO + " TEXT,"
                + KEY_EMAIL_ID + " TEXT,"
                + KEY_PROFILE_IMAGE + " TEXT,"
                + KEY_FULL_NAME + " TEXT,"
                + KEY_DEVICE_ID + " TEXT,"
                + KEY_JOB_TITLE + " TEXT,"
                + KEY_HOME_ADDRESS + " TEXT,"
                + KEY_WORK_ADDRESS + " TEXT,"
                + KEY_WORK_PHONE + " TEXT " + ")";

        db.execSQL(CREATE_USER_TABLE);

        String CREATE_CONTACT_TABLE = "CREATE TABLE " + CONTACT_TABLE + "("
                + KEY_CONTACT_ID + " TEXT,"
                + KEY_CONTACT_NAME + " TEXT,"
                + KEY_CONTACT_MOBILE_NO + " TEXT,"
                + KEY_CONTACT_EMAIL_ID + " TEXT,"
                + KEY_CONTACT_PROFILE_IMAGE + " TEXT,"
                + KEY_CONTACT_FULL_NAME + " TEXT,"
                + KEY_CONTACT_JOB_TITLE+ " TEXT,"
                + KEY_CONTACT_WORK_ADDRESS + " TEXT,"
                + KEY_CONTACT_WORK_PHONE + " TEXT,"
                + KEY_CONTACT_HOME_ADDRESS + " TEXT " + ")";

        db.execSQL(CREATE_CONTACT_TABLE);

    }

  /*  public void createTable(SQLiteDatabase db){
        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT,"
                + KEY_START_HOURS + " TEXT,"
                + KEY_START_MINS + " TEXT,"
                + KEY_END_HOURS + " TEXT,"
                + KEY_END_MINS + " TEXT,"
                + KEY_DAY_OF_WEEK + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_NOTIFICATION_TIME + " DATE" + ")";

        db.execSQL(CREATE_EVENTS_TABLE);
    }*/
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CONTACT_TABLE);

        context.deleteDatabase(Constants.DATABASE_NAME + ".db");

        createTable(db);

        // Create tables again
        //onCreate(db);
    }

}
