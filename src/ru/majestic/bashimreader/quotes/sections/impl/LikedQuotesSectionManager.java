package ru.majestic.bashimreader.quotes.sections.impl;

import java.util.List;

import android.os.AsyncTask;
import ru.majestic.bashimreader.cache.IQuotesCacher;
import ru.majestic.bashimreader.cache.impl.EmptyQuotesCacher;
import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.datebase.IQuotesDatabaseHelper;
import ru.majestic.bashimreader.parsers.IQuotesPageParser;
import ru.majestic.bashimreader.parsers.impl.NullQuotesPageParser;
import ru.majestic.bashimreader.quotes.sections.IQuoteSectionManagerSkeleton;

public class LikedQuotesSectionManager extends IQuoteSectionManagerSkeleton {

   private IQuotesDatabaseHelper quotesDatabaseHelper;

   
   public void setQuotesDatabaseHelper(IQuotesDatabaseHelper quotesDatabaseHelper) {
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

   @Override
   protected IQuotesCacher getQuotesCacher() {
      return new EmptyQuotesCacher();
   }
   ///////////////////////////////
}
