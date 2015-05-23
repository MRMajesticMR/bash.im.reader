package ru.majestic.bashimreader.datebase.tables;

import ru.majestic.bashimreader.datebase.exceptions.ChangeTableException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ComicsTable implements ITable {

   private static final String DEBUG_TAG = "DATEBASE_BASH";

   public static final String TABLE_NAME = "COMICS_TABLE";

   public static final String ROW_TITLE = "ROW_TITLE";
   public static final String ROW_AUTHOR = "ROW_AUTHOR";
   public static final String ROW_QUOTE_NUMBER = "ROW_QUOTE_NUMBER";
   public static final String ROW_DATE = "ROW_DATE";
   
   @Override
   public void createTable(SQLiteDatabase db) throws ChangeTableException {
      Log.d(DEBUG_TAG, "Starting create table " + TABLE_NAME);

      try {
         db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + 
               "(" + "_id INTEGER PRIMARY KEY, " + 
               ROW_TITLE + " TEXT, " + 
               ROW_AUTHOR + " TEXT, " + 
               ROW_QUOTE_NUMBER + " INTEGER, " + 
               ROW_DATE + " TEXT" + ");");
         Log.d(DEBUG_TAG, "Table " + TABLE_NAME + " created.");
      } catch (SQLException e) {
         throw new ChangeTableException(TABLE_NAME);
      }

   }

   @Override
   public void deleteTable(SQLiteDatabase db) throws ChangeTableException {
      Log.d(DEBUG_TAG, "Starting drop table " + TABLE_NAME);
      try {
         db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
         Log.d(DEBUG_TAG, "Table " + TABLE_NAME + " droped.");
      } catch (SQLException e) {
         throw new ChangeTableException(TABLE_NAME);
      }
   }

}
