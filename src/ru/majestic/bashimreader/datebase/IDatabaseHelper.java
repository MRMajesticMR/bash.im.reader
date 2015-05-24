package ru.majestic.bashimreader.datebase;

import android.content.Context;

public interface IDatabaseHelper {
   
   public void                         init                         (Context context);
   
   public ILikedQuotesDatabaseHelper   getLikedQuotesDatabaseHelper ();
   public INewQuotesDatabaseHelper     getNewQuotesDatabaseHelper   ();
   
}
