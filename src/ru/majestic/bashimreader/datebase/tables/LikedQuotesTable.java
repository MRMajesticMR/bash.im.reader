package ru.majestic.bashimreader.datebase.tables;

import ru.majestic.bashimreader.datebase.exceptions.ChangeTableException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LikedQuotesTable implements ITable {
   
   private static final String DEBUG_TAG = "DATEBASE_BASH";
   
   public static final String TABLE_NAME = "LIKED_QUOTES";
   
   public static final String ROW_TEXT = "TEXT";
   public static final String ROW_RATING = "RATING";
   public static final String ROW_DATE = "DATE";

   @Override
   public void createTable(SQLiteDatabase db) throws ChangeTableException {
      Log.d(DEBUG_TAG, "Starting create table " + TABLE_NAME);
      
      try{
         db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME+ "(" +
               "_id INTEGER PRIMARY KEY, " +
               ROW_TEXT + " TEXT, " +
               ROW_RATING + " INTEGER, " +
               ROW_DATE + " TEXT" +               
                     ");");
         Log.d(DEBUG_TAG, "Table " + TABLE_NAME + " created.");
      } catch (SQLException e){
         throw new ChangeTableException(TABLE_NAME);
      }
      
   }

   @Override
   public void deleteTable(SQLiteDatabase db) throws ChangeTableException {
      Log.d(DEBUG_TAG, "Starting drop table " + TABLE_NAME);
      try{
         db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
         Log.d(DEBUG_TAG, "Table " + TABLE_NAME + " droped.");
      } catch (SQLException e){
         throw new ChangeTableException(TABLE_NAME);
      }      
   }

}
