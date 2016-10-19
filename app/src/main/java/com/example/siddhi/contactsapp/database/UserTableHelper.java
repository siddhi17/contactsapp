package com.example.siddhi.contactsapp.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.siddhi.contactsapp.User;
import com.example.siddhi.contactsapp.helper.Constants;

import java.util.ArrayList;

public class UserTableHelper extends SQLiteOpenHelper {


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


    public UserTableHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);

        // createTable(db);
        // onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_USER_ID, user.getmUserId());
        values.put(KEY_USER_NAME, user.getmUserName());
        values.put(KEY_PASSWORD, user.getmPassword());
        values.put(KEY_MOBILE_NO, user.getmMobileNo());
        values.put(KEY_EMAIL_ID, user.getmEmailId());
        values.put(KEY_PROFILE_IMAGE, user.getmProfileImage());
        values.put(KEY_FULL_NAME, user.getmFullName());
        values.put(KEY_DEVICE_ID, user.getmDeviceId());
        values.put(KEY_JOB_TITLE, user.getmJobTitle());
        values.put(KEY_HOME_ADDRESS, user.getmHomeAddress());
        values.put(KEY_WORK_ADDRESS, user.getmWorkAddress());
        values.put(KEY_WORK_PHONE, user.getmWorkPhone());

        db.insert(USER_TABLE, null, values);

        db.close();
    }

    public User getUser(String id) {

        SQLiteDatabase db = this.getReadableDatabase();

        User user = new User();


        Cursor cursor = db.query(USER_TABLE, new String[]{KEY_USER_ID,
                        KEY_USER_NAME, KEY_PASSWORD,KEY_MOBILE_NO,KEY_EMAIL_ID, KEY_PROFILE_IMAGE, KEY_FULL_NAME,
                        KEY_DEVICE_ID,KEY_JOB_TITLE,KEY_HOME_ADDRESS,KEY_WORK_ADDRESS,KEY_WORK_PHONE}, KEY_USER_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        //cursor.moveToFirst();
        if( cursor != null && cursor.moveToFirst() ) {

            user = new User(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),
                    cursor.getString(4),cursor.getString(5), cursor.getString(6),cursor.getString(7),
                    cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getString(11));
        }

        return user;
    }

    public ArrayList<User> getAllContacts() {
        ArrayList<User> conList = new ArrayList<User>();

        String selectQuery = "SELECT * FROM " + USER_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                User user = new User();

                user.setmUserId(cursor.getString(0));
                user.setmUserName(cursor.getString(1));
                user.setmPassword(cursor.getString(2));
                user.setmMobileNo(cursor.getString(3));
                user.setmEmailId(cursor.getString(4));
                user.setmProfileImage(cursor.getString(5));
                user.setmFullName(cursor.getString(6));
                user.setmDeviceId(cursor.getString(7));
                user.setmJobTitle(cursor.getString(8));
                user.setmHomeAddress(cursor.getString(9));
                user.setmWorkAddress(cursor.getString(10));
                user.setmWorkPhone(cursor.getString(11));

                conList.add(user);
            } while (cursor.moveToNext());
        }

        return conList;
    }


  /*  public ArrayList<Task> getAllTask(int id) {
        ArrayList<Task> conList = new ArrayList<Task>();

        String selectQuery = "SELECT  * FROM " + TASK_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                Task task = new Task();

                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setTitle(cursor.getString(1));
                task.setTaskPriority(Integer.parseInt(cursor.getString(2)));
                task.setAlertDate(cursor.getString(3));
                task.setAlertTime(cursor.getString(4));
                task.setDueDate(cursor.getString(5));

                task.setDueTime(cursor.getString(6));
                task.setList(Integer.parseInt(cursor.getString(7)));

                conList.add(task);
            } while (cursor.moveToNext());
        }

        return conList;
    }*/


 /*   public ListData<EventData> getNotificationEvents(Integer tableId) {
        ListData<EventData> conList = new ArrayList<EventData>();

        String selectQuery = "SELECT  * FROM " + TABLE + " WHERE " + KEY_TIME_TABLE_ID + " == " + tableId ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                EventData event = new EventData();

                event.setId(Integer.parseInt(cursor.getString(0)));
                event.setTitle(cursor.getString(1));
                //  event.setStartHours(cursor.getInt(2));
                //  event.setStartMins(cursor.getInt(3));
                //  event.setEndHours(cursor.getInt(4));
                // event.setEndMins(cursor.getInt(5));
                event.setFromDate(cursor.getString(2));
                event.setToDate(cursor.getString(3));
                event.setDayOfWeek(cursor.getString(4));
                event.setLocation(cursor.getString(5));
                event.setNotificationTime(cursor.getString(6));
                event.setTableId(Integer.parseInt(cursor.getString(7)));
                event.setNotificationId(Integer.parseInt(cursor.getString(8)));
//                event.setColor(Integer.parseInt(cursor.getString(8)));
                event.setNotification(cursor.getString(9));

                conList.add(event);
            } while (cursor.moveToNext());
        }

        return conList;
    }

    public ListData<EventData> getTimeTableEvents(String day,int tableId) {
        ListData<EventData> conList = new ArrayList<EventData>();

        String selectQuery = "SELECT  * FROM " + TABLE + " WHERE " + KEY_DAY_OF_WEEK + " = '" + day + "'" + " AND " +

                KEY_TIME_TABLE_ID + " IN " +

        " ( " + " SELECT " + KEY_TIME_TABLE_ID + " FROM " + TABLE_TIME_TABLE + " WHERE " + KEY_STATUS + " == " + " 1 " +
                " AND " + KEY_TIME_TABLE_ID + " == " + tableId +  " ) ";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                EventData event = new EventData();
                event.setId(Integer.parseInt(cursor.getString(0)));
                event.setTitle(cursor.getString(1));
                event.setFromDate(cursor.getString(2));
                event.setToDate(cursor.getString(3));
                event.setDayOfWeek(cursor.getString(4));
                event.setLocation(cursor.getString(5));
                event.setNotificationTime(cursor.getString(6));
                event.setTableId(Integer.parseInt(cursor.getString(7)));
                event.setNotificationId(Integer.parseInt(cursor.getString(8)));
//                event.setColor(Integer.parseInt(cursor.getString(8)));
                event.setNotification(cursor.getString(9));
                conList.add(event);
            } while (cursor.moveToNext());
        }

        return conList;
    }
*/

 /*   public int updateStatus(Task task)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TASK_STATUS, task.getStatus());

        // updating row
        return db.update(TASK_TABLE, values, KEY_TASK_ID + " = ?",
                new String[]{String.valueOf(task.getId())});

    }*/


    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_USER_NAME, user.getmUserName());
        values.put(KEY_EMAIL_ID, user.getmEmailId());
        values.put(KEY_PASSWORD, user.getmPassword());
        values.put(KEY_MOBILE_NO, user.getmMobileNo());
        values.put(KEY_DEVICE_ID, user.getmDeviceId());
        values.put(KEY_JOB_TITLE, user.getmJobTitle());
        values.put(KEY_HOME_ADDRESS, user.getmHomeAddress());
        values.put(KEY_WORK_ADDRESS, user.getmWorkAddress());
        values.put(KEY_WORK_PHONE, user.getmWorkPhone());
        values.put(KEY_PROFILE_IMAGE, user.getmProfileImage());
        values.put(KEY_FULL_NAME, user.getmFullName());


        // updating row
        return db.update(USER_TABLE, values, KEY_USER_ID + " = ?",
                new String[]{String.valueOf(user.getmUserId())});

    }

   /* public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TASK_TABLE, KEY_TASK_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        db.close();
    }*/

    public int updateProfileImage(String userId,String profileImage) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(KEY_PROFILE_IMAGE,profileImage);
        return db.update(USER_TABLE, cv, KEY_PROFILE_IMAGE + "= ?", new String[] {KEY_USER_ID});
    }
}