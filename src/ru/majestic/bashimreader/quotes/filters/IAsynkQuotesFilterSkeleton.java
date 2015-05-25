package ru.majestic.bashimreader.quotes.filters;

import java.util.LinkedList;
import java.util.List;

import android.os.AsyncTask;
import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.quotes.filters.listeners.OnQuotesFilteredListener;

public abstract class IAsynkQuotesFilterSkeleton implements IQuotesFilter {

   private OnQuotesFilteredListener onQuotesFilteredListener;      
   
   @Override
   public void startFilter(final List<Quote> quotes) {
      new AsyncTask<Void, Integer, List<Quote>>() {

         @Override
         protected List<Quote> doInBackground(Void... params) {
            List<Quote> filteredQuotes = new LinkedList<Quote>();
            for(Quote quote: quotes) {
               if(isQuoteAccessed(quote))
                  filteredQuotes.add(quote);
            }
            return filteredQuotes;
         }
         
         @Override
         protected void onPostExecute(List<Quote> filteredQuotes) {
            onQuotesFilteredListener.onQuotesFiltered(filteredQuotes);
        }
      }.execute();
   }

   @Override
   public void setOnQuotesFilteredListener(OnQuotesFilteredListener onQuotesFilteredListener) {
      this.onQuotesFilteredListener = onQuotesFilteredListener;
   }
   
   protected abstract boolean isQuoteAccessed(Quote quote);

}
