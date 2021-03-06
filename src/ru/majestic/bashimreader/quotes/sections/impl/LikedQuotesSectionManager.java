package ru.majestic.bashimreader.quotes.sections.impl;

import java.util.List;

import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.datebase.ILikedQuotesDatabaseHelper;
import ru.majestic.bashimreader.parsers.IQuotesPageParser;
import ru.majestic.bashimreader.parsers.impl.NullQuotesPageParser;
import ru.majestic.bashimreader.quotes.sections.IQuoteSectionManagerSkeleton;
import android.os.AsyncTask;

public class LikedQuotesSectionManager extends IQuoteSectionManagerSkeleton {

   private ILikedQuotesDatabaseHelper quotesDatabaseHelper;

   
   public void setQuotesDatabaseHelper(ILikedQuotesDatabaseHelper quotesDatabaseHelper) {
      this.quotesDatabaseHelper = quotesDatabaseHelper;
   }
   
   @Override
   public void loadNextPage() {      
      new AsyncTask<Void, Integer, List<Quote>>() {
         @Override
         protected List<Quote> doInBackground(Void... params) {
            return quotesDatabaseHelper.getLikedQuotes();
         }
         
         @Override
         protected void onPostExecute(List<Quote> quotes) {
            if(quotes == null)
               onNewQuotesReadyListener.onLoadNewQuotesError();
            else
               onNewQuotesReadyListener.onNewQuoteReady(quotes);
        }
      }.execute();
   }
   
   @Override
   public boolean isNeedLoadMorePage() {
      return false;
   }
   
   
   //THIS METHODS NOT USED!
   @Override
   protected String generateNextPageDownloadUrl() {
      return null;
   }

   @Override
   protected IQuotesPageParser getQuotesPageParser() {
      return new NullQuotesPageParser();
   }
   ///////////////////////////////
}
