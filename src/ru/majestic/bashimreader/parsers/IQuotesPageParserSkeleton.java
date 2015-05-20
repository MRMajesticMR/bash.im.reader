package ru.majestic.bashimreader.parsers;

import java.util.List;

import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.parsers.listeners.OnQuotesPagesParsedListener;
import android.os.AsyncTask;

public abstract class IQuotesPageParserSkeleton implements IQuotesPageParser {
   
   protected OnQuotesPagesParsedListener onQuotesPagesParsedListener;   
   
   public void setOnQuotesPageParsedListener (OnQuotesPagesParsedListener onQuotesPagesParsedListener) {
      this.onQuotesPagesParsedListener = onQuotesPagesParsedListener;
   }

   @Override
   public void parsePage(String pageContent) {
      new AsyncTask<String, Integer, List<Quote>>() {

         @Override
         protected List<Quote> doInBackground(String... params) {
            try {
               return parsePageContent(params[0]);
            } catch (IllegalArgumentException e) {
               return null;
            }
         }
         
         @Override
         protected void onPostExecute(List<Quote> quotes) {
            if(quotes == null)
               onQuotesPagesParsedListener.onQuotePageParseError();
            else
               onQuotesPagesParsedListener.onQuotesPageParsed(quotes);
        }
      }.execute(pageContent);
   }
   
   protected abstract List<Quote> parsePageContent(String pageContent) throws IllegalArgumentException;

}
