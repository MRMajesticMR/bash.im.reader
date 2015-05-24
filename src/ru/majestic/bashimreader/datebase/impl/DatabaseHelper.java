package ru.majestic.bashimreader.datebase.impl;

import ru.majestic.bashimreader.datebase.IDatabaseHelper;
import ru.majestic.bashimreader.datebase.ILikedQuotesDatabaseHelper;
import ru.majestic.bashimreader.datebase.INewQuotesDatabaseHelper;
import ru.majestic.bashimreader.datebase.exceptions.ChangeTableException;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper implements IDatabaseHelper {

   public static final int       DB_VERSION  = 2;
   public static final String    DB_NAME     = "Bash.im.db";
   
   private static DatabaseHelper instance;
   
   private ILikedQuotesDatabaseHelper  likedQuotesDatabaseHelper;
   private INewQuotesDatabaseHelper    newQuotesDatabaseHelper;
   
   private SQLiteOpenHelper dbHelper;
   
   private DatabaseHelper() {
            
   }
   
   public static DatabaseHelper getInstance() {
      if(instance == null)
         instance = new DatabaseHelper();
      return instance;
   }
   
   @Override
   public void init(Context context) {
      dbHelper = new SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
         
         @Override
         public void onCreate(SQLiteDatabase db) {
            db.beginTransaction();
            try {
               likedQuotesDatabaseHelper.getHelperTable().createTable(db);
               newQuotesDatabaseHelper.getHelperTable().createTable(db);
               
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
               likedQuotesDatabaseHelper.getHelperTable().deleteTable(db);
               newQuotesDatabaseHelper.getHelperTable().deleteTable(db);
               
               db.setTransactionSuccessful();
            } catch (Exception e) {
               Log.e("ERROR", e.toString());
            } finally {
               db.endTransaction();
            }
            onCreate(db);
         }
      };
      
      likedQuotesDatabaseHelper  = new LikedQuotesDatabaseHelper(dbHelper);
      newQuotesDatabaseHelper    = new NewQuotesDatabaseHelper(dbHelper);
   }      

   @Override
   public ILikedQuotesDatabaseHelper getLikedQuotesDatabaseHelper() {
      return likedQuotesDatabaseHelper;
   }

   @Override
   public INewQuotesDatabaseHelper getNewQuotesDatabaseHelper() {
      return newQuotesDatabaseHelper;
   }   

}
