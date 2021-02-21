package com.example.goaltrackerapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static final String USER_TABLE = "USER_TABLE";
    public static final String COLUMN_USER_ID = "USER_ID";
    public static final String COLUMN_USER_NAME = "USER_NAME";
    public static final String COLUMN_USER_EMAIL = "USER_EMAIL";
    public static final String COLUMN_USER_PROFILEURL = "USER_PROFILEURL";
    public static final String GOALS_TABLE = "GOALS_TABLE";
    public static final String COLUMN_GOALS_ID = "GOALS_ID";
    public static final String COLUMN_GOALS_TITLE = "GOALS_TITLE";
    public static final String COLUMN_GOALS_DESC = "GOALS_DESC";
    public static final String COLUMN_GOALS_DATE_END = "GOALS_DATE_END";
    public static final String COLUMN_GOALS_TIME_END = "GOALS_TIME_END";
    public static final String COLUMN_GOALS_NOTIFICATION = "GOALS_NOTIFICATION";
    public static final String COLUMN_MOTIVATION_ID = "MOTIVATION_ID";
    public static final String COLUMN_MOTIVATION_NAME = "MOTIVATION_NAME";
    public static final String MOTIVATIONS_TABLE = "MOTIVATIONS_TABLE";
    public static final String GOALS_ALLOC_TABLE = "GOALS_ALLOC_TABLE";
    public static final String COLUMN_MOTIVATION_AUTHOR = "MOTIVATION_AUTHOR";
    public static final String COLUMN_GOALS_COMPLETED = "GOALS_COMPLETED"; // 0 MEANS NOT STARTED  **** 1 MEANS COMPLETE **** 2 MEANS IN PROGRESS

    public DataBaseHelper(@Nullable Context context) {
        super(context, "GTDB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating the tables in the database
        String createTableUsersString = "CREATE TABLE " + USER_TABLE + " (" + COLUMN_USER_ID + " TEXT PRIMARY KEY, " + COLUMN_USER_NAME + " TEXT, " + COLUMN_USER_EMAIL + " TEXT, " + COLUMN_USER_PROFILEURL + " TEXT)";
        db.execSQL(createTableUsersString);

        String createTableGoalsString = "CREATE TABLE " + GOALS_TABLE + " (" + COLUMN_GOALS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_GOALS_TITLE + " TEXT, " + COLUMN_GOALS_DESC + " TEXT, " + COLUMN_GOALS_DATE_END + " TEXT, " + COLUMN_GOALS_TIME_END + " TEXT, " + COLUMN_GOALS_NOTIFICATION + " INTEGER, " + COLUMN_GOALS_COMPLETED + " INTEGER)";
        db.execSQL(createTableGoalsString);

        String createTableGoalsAllocString = "CREATE TABLE " + GOALS_ALLOC_TABLE + " (" + COLUMN_USER_ID + " TEXT, " + COLUMN_GOALS_ID + " INTEGER, PRIMARY KEY (USER_ID, GOALS_ID))";
        db.execSQL(createTableGoalsAllocString);

        String createTableMotivationsString = "CREATE TABLE " + MOTIVATIONS_TABLE + " (" + COLUMN_MOTIVATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_MOTIVATION_NAME + " TEXT, " + COLUMN_MOTIVATION_AUTHOR + " TEXT)";
        db.execSQL(createTableMotivationsString);

    } // creating the empty database on creation

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // users

    public boolean addUser(UserModel userModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_ID, userModel.getId());
        cv.put(COLUMN_USER_NAME, userModel.getName());
        cv.put(COLUMN_USER_EMAIL, userModel.getEmail());
        cv.put(COLUMN_USER_PROFILEURL, userModel.getProfileurl());

        long insert = db.insert(USER_TABLE, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    } // ADDS USER TO DATABASE

    // goals

    public boolean addGoal(GoalModel goalModel, String userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_GOALS_TITLE, goalModel.getTitle());
        cv.put(COLUMN_GOALS_DESC, goalModel.getDesc());
        cv.put(COLUMN_GOALS_DATE_END, goalModel.getDateend());
        cv.put(COLUMN_GOALS_TIME_END, goalModel.getTimeend());
        cv.put(COLUMN_GOALS_NOTIFICATION, goalModel.getNotification());
        cv.put(COLUMN_GOALS_COMPLETED, goalModel.getCompleteness());

        long insert = db.insert(GOALS_TABLE, null, cv);
        if (insert == -1) {
            return false;
        } else {
            // IF THIS HAS ADDED TO THE DATABASE, CREATE THE ENTRY FOR THE GOALALLOC TABLE
            GoalAllocationModel goalAllocationModel = new GoalAllocationModel(userid, getLastGoalID());
            addGoalAlloc(goalAllocationModel);
            return true;
        }
    } // INITIATES THE CREATION OF A GOAL AND ADDS ALL DATA

    public boolean addGoalAlloc(GoalAllocationModel goalAllocationModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_ID, goalAllocationModel.getUserid());
        cv.put(COLUMN_GOALS_ID, goalAllocationModel.getGoalid());

        long insert = db.insert(GOALS_ALLOC_TABLE, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }

    } // UPDATES GOAL ALLOCATION TABLE UPON GOAL CREATE

    public int getLastGoalID() {
        int goalID = 0;

        String queryString = "SELECT MAX(GOALS_ID) FROM " + GOALS_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            goalID = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return goalID;
    } // THIS WILL LINK THE CURRENTLY UPLOADED GOAL ID AFTER ITS BEEN MADE

    public List<String> getGoalsTitleForUser(String userID) {
        List<String> userGoals = new ArrayList<>();

        String queryString = "SELECT GOALS_TITLE FROM GOALS_TABLE INNER JOIN GOALS_ALLOC_TABLE on GOALS_ALLOC_TABLE.GOALS_ID = GOALS_TABLE.GOALS_ID WHERE GOALS_ALLOC_TABLE.USER_ID = '" + userID + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                userGoals.add(cursor.getString(0));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return userGoals;
    } // GET ALL GOAL TITLES FROM THE USER

    public GoalModel getGoalData(String goal_Title) {
        GoalModel goal = null;
        String queryString = "SELECT * FROM GOALS_TABLE WHERE GOALS_TITLE == '" + goal_Title + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int goalsId = cursor.getInt(0);
                String goalsTitle = cursor.getString(1);
                String goalsDesc = cursor.getString(2);
                String goalsDate = cursor.getString(3);
                String goalsTime = cursor.getString(4);
                int goalsNotif = cursor.getInt(5);
                int goalsCompl = cursor.getInt(6);

                goal = new GoalModel(goalsId, goalsTitle, goalsDesc, goalsDate, goalsTime, goalsNotif, goalsCompl);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return goal;
    } // GET DATA ABOUT GIVEN GOAL TITLE

    public boolean completeGoal(String goal_Title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_GOALS_COMPLETED, 1);

        long update = db.update(GOALS_TABLE, cv, "GOALS_TITLE == ?", new String[]{goal_Title});
        if (update == -1) {
            return false;
        } else {
            return true;
        }
    } // UPDATE THE GOAL TO BE COMPLETED

    public boolean inProgressGoal(String goal_Title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_GOALS_COMPLETED, 2);

        long update = db.update(GOALS_TABLE, cv, "GOALS_TITLE == ?", new String[]{goal_Title});
        if (update == -1) {
            return false;
        } else {
            return true;
        }

    } // UPDATE THE GOAL TO BE SET IN PROGRESS

    // goal breakdown -

    public String getCountOfInProgress(String userID) {
        String count;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor QCount = db.rawQuery("SELECT COUNT(*) FROM GOALS_ALLOC_TABLE INNER JOIN GOALS_TABLE ON GOALS_ALLOC_TABLE.GOALS_ID = GOALS_TABLE.GOALS_ID WHERE GOALS_TABLE.GOALS_COMPLETED = 2 AND GOALS_ALLOC_TABLE.USER_ID = '" + userID + "'", null);
        QCount.moveToFirst();
        count = Integer.toString(QCount.getInt(0));
        QCount.close();
        return count;
    }

    public String getCountOfCompleted(String userID) {
        String count;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor QCount = db.rawQuery("SELECT COUNT(*) FROM GOALS_ALLOC_TABLE INNER JOIN GOALS_TABLE ON GOALS_ALLOC_TABLE.GOALS_ID = GOALS_TABLE.GOALS_ID WHERE GOALS_TABLE.GOALS_COMPLETED = 1 AND GOALS_ALLOC_TABLE.USER_ID= '" + userID + "'", null);
        QCount.moveToFirst();
        count = Integer.toString(QCount.getInt(0));
        QCount.close();
        return count;
    }

    public String getCountOfNotStarted(String userID) {
        String count;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor QCount = db.rawQuery("SELECT COUNT(*) FROM GOALS_ALLOC_TABLE INNER JOIN GOALS_TABLE ON GOALS_ALLOC_TABLE.GOALS_ID = GOALS_TABLE.GOALS_ID WHERE GOALS_TABLE.GOALS_COMPLETED = 0 AND GOALS_ALLOC_TABLE.USER_ID = '" + userID + "'", null);
        QCount.moveToFirst();
        count = Integer.toString(QCount.getInt(0));
        QCount.close();
        return count;
    }

    public String totalGoals(String userID) {
        //if (userID == null) return "2";
        String count;
        SQLiteDatabase db = getReadableDatabase();

        long val = DatabaseUtils.queryNumEntries(db, "GOALS_ALLOC_TABLE",
                "USER_ID=?", new String[]{userID});
        db.close();

        count = Long.toString(val);
        return count;
    }

    public List<String> getCompletedGoals(String userID) {
        List<String> userGoals = new ArrayList<>();

        String queryString = "SELECT GOALS_TITLE FROM GOALS_TABLE INNER JOIN GOALS_ALLOC_TABLE on GOALS_ALLOC_TABLE.GOALS_ID = GOALS_TABLE.GOALS_ID WHERE GOALS_TABLE.GOALS_COMPLETED = 1 AND GOALS_ALLOC_TABLE.USER_ID = '" + userID + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                userGoals.add(cursor.getString(0));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return userGoals;
    }

    public List<String> getInProgressGoals(String userID) {
        List<String> userGoals = new ArrayList<>();

        String queryString = "SELECT GOALS_TITLE FROM GOALS_TABLE INNER JOIN GOALS_ALLOC_TABLE on GOALS_ALLOC_TABLE.GOALS_ID = GOALS_TABLE.GOALS_ID WHERE GOALS_TABLE.GOALS_COMPLETED = 2 AND GOALS_ALLOC_TABLE.USER_ID = '" + userID + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                userGoals.add(cursor.getString(0));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return userGoals;
    }

    public List<String> getToDoGoals(String userID) {
        List<String> userGoals = new ArrayList<>();

        String queryString = "SELECT GOALS_TITLE FROM GOALS_TABLE INNER JOIN GOALS_ALLOC_TABLE on GOALS_ALLOC_TABLE.GOALS_ID = GOALS_TABLE.GOALS_ID WHERE GOALS_TABLE.GOALS_COMPLETED = 0 AND GOALS_ALLOC_TABLE.USER_ID = '" + userID + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                userGoals.add(cursor.getString(0));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return userGoals;
    }

    // motivations -

    public boolean areThereMotivations(){
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM MOTIVATIONS_TABLE";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0){
            // if not empty
            mcursor.close();
            db.close();
            return true;
        }else{
            // if empty
            mcursor.close();
            db.close();
            return false;
        }
    }

    public void loadMotivations(){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

       // cv.put(COLUMN_MOTIVATION_NAME, "THIS IS A TEST MOTIVATION QUOTE NUMBER 1");
       // cv.put(COLUMN_MOTIVATION_AUTHOR, "MIKE OXLONG");

       // long insert = db.insert(MOTIVATIONS_TABLE, null, cv);

       // cv.clear();

       // db = this.getWritableDatabase();

        cv.put(COLUMN_MOTIVATION_NAME, "BELIEVE IN YOURSELF BECAUSE NOONE ELSE WILL");
        cv.put(COLUMN_MOTIVATION_AUTHOR, "MIKE OXHAM");

        long insert = db.insert(MOTIVATIONS_TABLE, null, cv);

        cv.clear();
        db = this.getWritableDatabase();

        cv.put(COLUMN_MOTIVATION_NAME, "YOU CAN DO WHAT YOU CANT AND CANT WHAT YOU DO");
        cv.put(COLUMN_MOTIVATION_AUTHOR, "BEN DOVERSON");

        insert = db.insert(MOTIVATIONS_TABLE, null, cv);

        cv.clear();
        db = this.getWritableDatabase();

        cv.put(COLUMN_MOTIVATION_NAME, "THIS IS A TEST MOTIVATION QUOTE NUMBER 3");
        cv.put(COLUMN_MOTIVATION_AUTHOR, "ED SHEERAN");

        insert = db.insert(MOTIVATIONS_TABLE, null, cv);

        cv.clear();
        db = this.getWritableDatabase();

        cv.put(COLUMN_MOTIVATION_NAME, "THIS IS A TEST MOTIVATION QUOTE NUMBER 4");
        cv.put(COLUMN_MOTIVATION_AUTHOR, "JAMES CHARLES");

        insert = db.insert(MOTIVATIONS_TABLE, null, cv);

        cv.clear();



    }

    public List<String> getMotivations(){
        List<String> motivations = new ArrayList<>();

        String queryString = "SELECT MOTIVATION_NAME, MOTIVATION_AUTHOR FROM MOTIVATIONS_TABLE";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                motivations.add(cursor.getString(0));
                motivations.add(cursor.getString(1));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return motivations;
    }







    // settings -

}
