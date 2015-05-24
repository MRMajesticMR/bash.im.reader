package ru.majestic.bashimreader.datebase.impl;

import ru.majestic.bashimreader.datebase.IDatabaseHelper;
import ru.majestic.bashimreader.datebase.IQuotesDatabaseHelper;
import ru.majestic.bashimreader.datebase.exceptions.ChangeTableException;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper implements IDatabaseHelper {

   public static final int       DB_VERSION  = 2;
   public static final String    DB_NAME     = "Bash.im.db";
   
   private IQuotesDatabaseHelper quotesDatabaseHelper;
   
   public DatabaseHelper(Context context) {
      super(context, DB_NAME, null, DB_VERSION);
      
      quotesDatabaseHelper = new QuotesDatabaseHelper(this);
   }
   
   @Override
   public void onCreate(SQLiteDatabase db) {
      db.beginTransaction();
      try {
         quotesDatabaseHelper.getHelperTable().createTable(db);
         
         db.setTransactionSuccessful();
      } catch (ChangeTableException e) {
         Log.e("ERROR", e.toString());
      } finally {
         db.endTransaction();
      }
      
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.beginTransaction();
      try {
         quotesDatabaseHelper.getHelperTable().deleteTable(db);
         
         db.setTransactionSuccessful();
      } catch (Exception e) {
         Log.e("ERROR", e.toString());
      } finally {
         db.endTransaction();
      }
      onCreate(db);
   }

   @Override
   public IQuotesDatabaseHelper getQuotesDatabaseHelper() {
      return quotesDatabaseHelper;
   }

}
