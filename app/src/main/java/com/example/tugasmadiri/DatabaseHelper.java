package com.example.tugasmadiri;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.Display;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private final static String DATABASE_NAME = "note";
    private final static String TABLE_NAME = "note_table";
    public final static String Col_1 = "ID";
    public final static String Col_2 = "Title";
    public final static String Col_3 = "Description";
    public final static String Col_4 = "TimeAdded";


    public DatabaseHelper(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
//        db.execSQL("CREATE TABLE IF NOT EXISTS "
//                +TABLE_NAME+
//                "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
//                "TITLE TEXT," +
//                "DESCRIPTION TEXT)");

        String CREATE_BABY_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + Col_1 + " INTEGER PRIMARY KEY,"
                + Col_2 + " TEXT,"
                + Col_3 + " TEXT,"
                + Col_4 + " LONG);";

        db.execSQL(CREATE_BABY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //it drops table if same name table already exists!
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);

    }


    public void addItem(ModelNote noteModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Col_2, noteModel.getTitle());
        values.put(Col_3, noteModel.getDescription());
        values.put(Col_4, System.currentTimeMillis());//timestamp of the system

        //Inset the row
        db.insert(TABLE_NAME, null, values);

        Log.d("DBHandler", "added Item: ");
    }

    //Get a note
    public ModelNote getNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{Col_1,
                        Col_2,
                        Col_3,
                        Col_4},
                Col_1 + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        ModelNote noteModel = new ModelNote();
        if (cursor != null) {
            noteModel.setId((cursor.getString(cursor.getColumnIndex(Col_1))));
            noteModel.setTitle(cursor.getString(cursor.getColumnIndex(Col_2)));
            noteModel.setDescription(cursor.getString(cursor.getColumnIndex(Col_3)));

            //convert Timestamp to something readable
            DateFormat dateFormat = DateFormat.getDateInstance();
            String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Col_4)))
                    .getTime()); // Feb 23, 2020

            noteModel.setDataNoteAdded(formattedDate);


        }

        return noteModel;
    }

    //Get all Items
    public List<ModelNote> getAllNotes() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<ModelNote> noteModelList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{Col_1,
                        Col_2,
                        Col_3,
                        Col_4},
                null, null, null, null,
                Col_4 + " DESC");

        if (cursor.moveToFirst()) {
            do {
                ModelNote noteModel = new ModelNote();
                noteModel.setId((cursor.getString(cursor.getColumnIndex(Col_1))));
                noteModel.setTitle(cursor.getString(cursor.getColumnIndex(Col_2)));
                noteModel.setDescription(cursor.getString(cursor.getColumnIndex(Col_3)));

                //convert Timestamp to something readable
                DateFormat dateFormat = DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Col_4)))
                        .getTime()); // Feb 23, 2020

                noteModel.setDataNoteAdded(formattedDate);

                //Add to arraylist
                noteModelList.add(noteModel);
            } while (cursor.moveToNext());
        }
        return noteModelList;

    }

    //Todo: Add updateItem
    public int updateItem(ModelNote noteModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Col_2, noteModel.getTitle());
        values.put(Col_3, noteModel.getDescription());
        values.put(Col_4, System.currentTimeMillis());//timestamp of the system

        //update row
        return db.update(TABLE_NAME, values,
                Col_1 + "=?",
                new String[]{String.valueOf(noteModel.getId())});

    }


    //Todo: Add Delete Item
    public void deleteItem(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,
                Col_1 + "=?",
                new String[]{String.valueOf(id)});

        //close
        db.close();

    }

    //Todo: getItemCount
    public int getItemsCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();

    }
}
