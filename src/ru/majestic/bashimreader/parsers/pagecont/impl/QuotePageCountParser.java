package ru.majestic.bashimreader.parsers.pagecont.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import ru.majestic.bashimreader.parsers.listeners.OnQuotesPageCountParsedListener;
import ru.majestic.bashimreader.parsers.pagecont.IQuotesPageCountParser;
import android.os.AsyncTask;
import android.util.Log;

public class QuotePageCountParser implements IQuotesPageCountParser {

   private OnQuotesPageCountParsedListener onQuotesPageCountParsedListener;
   
   @Override
   public void parse(String pageContent) {
      new AsyncTask<String, Integer, Integer>() {

         @Override
         protected Integer doInBackground(String... params) {
            return getPageCountFromContent(params[0]);
         }
         
         @Override
         protected void onPostExecute(Integer pageCount) {
            Log.i("NEW_QUOTES_MANAGER", "Max page count: " + pageCount);
            onQuotesPageCountParsedListener.onQuotesPageCountParsed(pageCount);
        }
      }.execute(pageContent);      
   }

   @Override
   public void setOnQuotesPageCountParsedListener(OnQuotesPageCountParsedListener onQuotesPageCountParsedListener) {
      this.onQuotesPageCountParsedListener = onQuotesPageCountParsedListener;
   }
   
   private int getPageCountFromContent(String pageContent) {
      Document doc = Jsoup.parse(pageContent);
      final Elements element = doc.getElementsByClass("page");

      return Integer.valueOf(element.get(0).attr("max"));
   }

}
