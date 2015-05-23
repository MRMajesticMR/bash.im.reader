package ru.majestic.bashimreader.cache;

import java.util.List;

import ru.majestic.bashimreader.cache.listeners.QuotesCacheStatusListener;
import ru.majestic.bashimreader.data.Quote;
import android.os.AsyncTask;

public abstract class IQuotesCacherSkeleton implements IQuotesCacher {

   private QuotesCacheStatusListener quotesCacheStatusListener;
   
   @Override
   public void saveQuotes(final List<Quote> quotes) {
      new AsyncTask<Void, Integer, Void>() {

         @Override
         protected Void doInBackground(Void... params) {
            saveQuotesAsynk(quotes);
            return null;
         }

         @Override
         protected void onPostExecute(Void result) {
            quotesCacheStatusListener.onQuotesSaved();
         }
      }.execute();
   }

   @Override
   public void loadSavedQuotes() {
      new AsyncTask<Void, Integer, Void>() {

         @Override
         protected Void doInBackground(Void... params) {
            loadQuotesAsynk();
            return null;
         }

         @Override
         protected void onPostExecute(Void result) {
            quotesCacheStatusListener.onQuotesSaved();
         }
      }.execute();
   }

   @Override
   public void setQuotesCacheStatusListener(QuotesCacheStatusListener quotesCacheStatusListener) {
      this.quotesCacheStatusListener = quotesCacheStatusListener;
   }
   
   protected abstract void          saveQuotesAsynk   (List<Quote> quotes);
   protected abstract List<Quote>   loadQuotesAsynk   ();
}
