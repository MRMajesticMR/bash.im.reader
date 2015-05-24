package ru.majestic.bashimreader.datebase;

import java.util.ArrayList;
import java.util.List;

import ru.majestic.bashimreader.data.Comics;
import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.datebase.exceptions.ChangeTableException;
import ru.majestic.bashimreader.datebase.tables.ComicsTable;
import ru.majestic.bashimreader.datebase.tables.LikedQuotesTable;
import ru.majestic.bashimreader.datebase.tables.NewQuotesTable;
import ru.majestic.bashimreader.datebase.tables.ITable;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class QuotesDatebaseManager extends SQLiteOpenHelper {

	public enum Type {
		NEW, LIKED
	};	

	public static final int DB_VERSION = 2;
	public static final String DB_NAME = "Bash.im.db";

	private final List<ITable> tables = new ArrayList<ITable>();		

	public QuotesDatebaseManager(final Context context) {
		super(context, DB_NAME, null, DB_VERSION);
//		tables.add(new LikedQuotesTable());
//		tables.add(new NewQuotesTable());
		tables.add(new ComicsTable());
	}

	public synchronized final void deleteCache(final Type type) {
		switch (type) {
		case NEW:
			try {
				tables.get(1).deleteTable(getWritableDatabase());
				tables.get(1).createTable(getWritableDatabase());
			} catch (ChangeTableException e) {
				Log.e("DATABASE", "New quotes table delete error. " + e.toString());
			}
			break;
		case LIKED:
			try {
				tables.get(0).deleteTable(getWritableDatabase());
				tables.get(0).createTable(getWritableDatabase());
			} catch (ChangeTableException e) {
				Log.e("DATABASE", "Liked quotes table delete error. " + e.toString());
			}
			break;
		}
	}

//	public synchronized final void saveLikedQuotes(final Quote quote) throws Exception {
//		final SQLiteDatabase db = getWritableDatabase();
//		db.beginTransaction();
//		try {
//			db.execSQL("INSERT INTO " + LikedQuotesTable.TABLE_NAME + " VALUES (" + String.valueOf(quote.getId()) + ", \""
//					+ quote.getContent() + "\", " + quote.getRating() + ", \"" + quote.getDateString() + "\");");
//			db.setTransactionSuccessful();
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			db.endTransaction();
//		}
//	}

//	public synchronized final List<Quote> getLikedQuotes() {
//		final List<Quote> items = new ArrayList<Quote>();
//		final SQLiteDatabase db = getReadableDatabase();
//		final Cursor cursor;
//		cursor = db.rawQuery("SELECT * FROM " + LikedQuotesTable.TABLE_NAME + " ORDER BY " + LikedQuotesTable.ROW_DATE + " DESC;", null);
//		cursor.move(-1);
//		while (cursor.moveToNext()) {
//			items.add(cursorToQuote(cursor));
//		}
//		cursor.close();
//		db.close();
//		return items;
//	}
	
	  public synchronized final void saveDownloadedQuotesList(List<Quote> downloadedQuotes) {
	      final SQLiteDatabase db = getWritableDatabase();
	      for(int i = 0; i < downloadedQuotes.size(); i++) {
	         final Quote quote = downloadedQuotes.get(i);
	         
   	      db.beginTransaction();
   	      try {
   	         db.execSQL("INSERT INTO " + NewQuotesTable.TABLE_NAME + " VALUES (" + String.valueOf(quote.getId()) + ", \""
   	               + quote.getContent() + "\", " + quote.getRating() + ", \"" + quote.getDateString() + "\");");
   	         db.setTransactionSuccessful();   	         
   	         Log.d("QUOTES", "Saved");
   	      } catch (Exception e) {
   	         Log.d("QUOTES", "Quotes not saved");   	         
   	      } finally {
   	         db.endTransaction();
   	      }
	      }
	      db.close();
	   }

	public synchronized final List<Quote> getNewQuotes() {
		final List<Quote> items = new ArrayList<Quote>();
		final SQLiteDatabase db = getReadableDatabase();
		final Cursor cursor;
		cursor = db.rawQuery("SELECT * FROM " + NewQuotesTable.TABLE_NAME + " ORDER BY _id DESC;", null);
		cursor.move(-1);
		while (cursor.moveToNext()) {
			items.add(cursorToQuote(cursor));
		}
		cursor.close();
		return items;
	}
	
	public synchronized final void saveNewComics(final Comics pComics) throws Exception {
	   final SQLiteDatabase db = getWritableDatabase();
      db.beginTransaction();
      try {
         db.execSQL("INSERT INTO " + ComicsTable.TABLE_NAME + " VALUES (" + 
                     "NULL, " +
               		"\"" + pComics.getTitle() + "\", " +
               		"\"" + pComics.getAuthorName() + "\", " +
               		"" + pComics.getQuoteNumber() + ", " +
               		"\"" + pComics.getDate() + "\");");
         db.setTransactionSuccessful();
         Log.d("COMICS", "Saved");
      } catch (Exception e) {
         throw e;
      } finally {
         db.endTransaction();
         db.close();
      }
	}

	private final Quote cursorToQuote(final Cursor cursor) {
		return new Quote(cursor.getInt(cursor.getColumnIndex("_id")), cursor.getInt(cursor.getColumnIndex(LikedQuotesTable.ROW_RATING)),
				cursor.getString(cursor.getColumnIndex(LikedQuotesTable.ROW_TEXT)), cursor.getString(cursor
						.getColumnIndex(LikedQuotesTable.ROW_DATE)));
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.beginTransaction();
		try {
			for (ITable table : tables) {
				table.createTable(db);
			}
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
			for (ITable table : tables) {
				table.deleteTable(db);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e("ERROR", e.toString());
		} finally {
			db.endTransaction();
		}
		onCreate(db);
	}

}
