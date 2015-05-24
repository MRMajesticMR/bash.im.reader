package ru.majestic.bashimreader.datebase.impl;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.datebase.INewQuotesDatabaseHelper;
import ru.majestic.bashimreader.datebase.tables.ITable;
import ru.majestic.bashimreader.datebase.tables.LikedQuotesTable;
import ru.majestic.bashimreader.datebase.tables.NewQuotesTable;

public class NewQuotesDatabaseHelper implements INewQuotesDatabaseHelper {

   private SQLiteOpenHelper dbHelper;
   
   public NewQuotesDatabaseHelper(SQLiteOpenHelper dbHelper) {
      this.dbHelper = dbHelper;
   }
   
   @Override
   public ITable getHelperTable() {
      return new NewQuotesTable();
   }

   @Override
   public void saveNewQuote(Quote quote) {
      final SQLiteDatabase db = dbHelper.getWritableDatabase();

      db.beginTransaction();
      try {
         db.execSQL("INSERT INTO " + NewQuotesTable.TABLE_NAME + " VALUES (" + String.valueOf(quote.getId()) + ", \"" + quote.getContent()
               + "\", " + quote.getRating() + ", \"" + quote.getDateString() + "\");");
         db.setTransactionSuccessful();
      } catch (Exception e) {
         Log.d("QUOTES", "Quotes not saved");
      } finally {
         db.endTransaction();
      }
      db.close();

   }

   @Override
   public boolean isQuoteAlreadySaved(Quote quote) {
      final SQLiteDatabase db = dbHelper.getReadableDatabase();
      int rowCount;
      final Cursor cursor;
      cursor = db.rawQuery("SELECT _id FROM " + NewQuotesTable.TABLE_NAME + " WHERE _id = " + quote.getId() + ";", null);
      rowCount = cursor.getCount();
      cursor.close();
      db.close();
      return rowCount > 0;
   }

   @Override
   public List<Quote> getNewQuotes() {
      final List<Quote> items = new ArrayList<Quote>();
      final SQLiteDatabase db = dbHelper.getReadableDatabase();
      final Cursor cursor;
      cursor = db.rawQuery("SELECT * FROM " + NewQuotesTable.TABLE_NAME + " ORDER BY " + LikedQuotesTable.ROW_DATE + " DESC;", null);
      cursor.move(-1);
      while (cursor.moveToNext()) {
         items.add(cursorToQuote(cursor));
      }
      cursor.close();
      db.close();
      return items;
   }

   @Override
   public void clearNewQuotes() {
      // TODO Auto-generated method stub
      
   }

   @Override
   public boolean hasQuotes() {
      final SQLiteDatabase db = dbHelper.getReadableDatabase();
      int rowCount;
      final Cursor cursor;
      cursor = db.rawQuery("SELECT _id FROM " + NewQuotesTable.TABLE_NAME + ";", null);
      rowCount = cursor.getCount();
      cursor.close();
      db.close();
      return rowCount > 0;
   }
   
   private Quote cursorToQuote(Cursor cursor) {
      return new Quote(cursor.getInt(cursor.getColumnIndex("_id")), cursor.getInt(cursor.getColumnIndex(LikedQuotesTable.ROW_RATING)),
            cursor.getString(cursor.getColumnIndex(LikedQuotesTable.ROW_TEXT)), cursor.getString(cursor
                  .getColumnIndex(LikedQuotesTable.ROW_DATE)));
   }

}
