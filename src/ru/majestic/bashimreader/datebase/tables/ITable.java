package ru.majestic.bashimreader.datebase.tables;

import ru.majestic.bashimreader.datebase.exceptions.ChangeTableException;
import android.database.sqlite.SQLiteDatabase;

public interface ITable {
   
   public void createTable(SQLiteDatabase db) throws ChangeTableException;
   public void deleteTable(SQLiteDatabase db) throws ChangeTableException;

}