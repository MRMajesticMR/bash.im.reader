package ru.majestic.bashimreader.datebase.impl;

import java.util.List;

import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.datebase.IQuotesDatabaseHelper;
import ru.majestic.bashimreader.datebase.tables.ITable;
import ru.majestic.bashimreader.datebase.tables.LikedQuotesTable;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class QuotesDatabaseHelper implements IQuotesDatabaseHelper {

   private static final String LOG_TAG = QuotesDatabaseHelper.class.getSimpleName();
   
   private SQLiteOpenHelper dbHelper;
   
   public QuotesDatabaseHelper(SQLiteOpenHelper dbHelper) {
      this.dbHelper = dbHelper;
   }
   
   @Override
   public ITable getHelperTable() {
      return new LikedQuotesTable();
   }
   
   @Override
   public void saveLikedQuote(Quote quote) {
      final SQLiteDatabase db = dbHelper.getWritableDatabase();
      db.beginTransaction();
      try {
         db.execSQL("INSERT INTO " + LikedQuotesTable.TABLE_NAME + " VALUES (" + String.valueOf(quote.getId()) + ", \""
               + quote.getContent() + "\", " + quote.getRating() + ", \"" + quote.getDateString() + "\");");
         db.setTransactionSuccessful();
      } catch (Exception e) {
         Log.e(LOG_TAG, "Save liked quote error: " + e.toString());
      } finally {
         db.endTransaction();
      }      
   }

   @Override
   public boolean isQuoteAlreadyLiked(Quote quote) {
      final SQLiteDatabase db = dbHelper.getReadableDatabase();
      int rowCount;
      final Cursor cursor;
      cursor = db.rawQuery("SELECT _id FROM " + LikedQuotesTable.TABLE_NAME + " WHERE _id = " + quote.getId() + ";", null);
      rowCount = cursor.getCount();
      cursor.close();
      db.close();
      return rowCount > 0;            
   }

   @Override
   public List<Quote> getLikedQuotes() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void clearLikedQuotes() {
      // TODO Auto-generated method stub      
   }   

}
