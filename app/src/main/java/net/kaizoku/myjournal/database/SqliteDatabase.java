package net.kaizoku.myjournal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import net.kaizoku.myjournal.database.model.Note;
import java.util.ArrayList;
import java.util.List;

public class SqliteDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "note";
    private static final String TABLE_NOTES = "notes";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";

    public SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE    TABLE " + TABLE_NOTES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_TITLE + " TEXT," + COLUMN_CONTENT + " TEXT" + ")";
        db.execSQL(CREATE_NOTES_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    public List<Note> listNotes(){
        String sql = "select * from " + TABLE_NOTES;
        SQLiteDatabase db = this.getReadableDatabase();
        List<Note> storeNotes = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                storeNotes.add(new Note(id, title, content));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeNotes;
    }

    public void addNote(Note note){
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_CONTENT, note.getContent());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NOTES, null, values);
    }

    public void updateNote(Note note){
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_CONTENT, note.getContent());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_NOTES, values, COLUMN_ID    + "    = ?", new String[] { String.valueOf(note.getId())});
    }

    public Note findNote(String title){
        String query = "Select * FROM "    + TABLE_NOTES + " WHERE " + COLUMN_TITLE + " = " + "title";
        SQLiteDatabase db = this.getWritableDatabase();
        Note mNote = null;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            int id = Integer.parseInt(cursor.getString(0));
            String noteTitle = cursor.getString(1);
            String noteContent = cursor.getString(2);
            mNote = new Note(id, noteTitle, noteContent);
        }
        cursor.close();
        return mNote;
    }

    public void deleteNote(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, COLUMN_ID    + "    = ?", new String[] { String.valueOf(id)});
    }
}
