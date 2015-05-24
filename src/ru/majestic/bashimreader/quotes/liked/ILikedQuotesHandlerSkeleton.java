package ru.majestic.bashimreader.quotes.liked;

import java.util.List;

import android.os.AsyncTask;
import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.quotes.liked.listeners.LikedQuotesHandlerListener;

public abstract class ILikedQuotesHandlerSkeleton implements ILikedQuotesHandler {

   private LikedQuotesHandlerListener likedQuotesHandlerListener;
   
   @Override
   public void saveLikedQuote(Quote quote) {
      new AsyncTask<Quote, Void, Void>() {

         @Override
         protected Void doInBackground(Quote... params) {
            saveLikedQuoteAsynk(params[0]);
            return null;
         }
         
         @Override
         protected void onPostExecute(Void result) {
            likedQuotesHandlerListener.onLikedQuoteSaved();
        }
      }.execute(quote);      
   }

   @Override
   public void loadLikedQuotes() {
      new AsyncTask<Void, Integer, List<Quote>>() {

         @Override
         protected List<Quote> doInBackground(Void... params) {
            return loadLikedQuotesAsynk();
         }
         
         @Override
         protected void onPostExecute(List<Quote> quotes) {
            if(quotes == null)
               likedQuotesHandlerListener.onLoadLikedQuotesError();
            else
               likedQuotesHandlerListener.onLoadedLikedQuotes(quotes);
        }
      }.execute();      

   }
   
   @Override
   public void clearLikedQuotes() {
      // TODO Auto-generated method stub     
   }
   
   @Override
   public void setLikedQuotesHandlerListener(LikedQuotesHandlerListener likedQuotesHandlerListener) {
      this.likedQuotesHandlerListener = likedQuotesHandlerListener;
   }

   protected abstract void          saveLikedQuoteAsynk     (Quote quote);
   protected abstract List<Quote>   loadLikedQuotesAsynk    ();
   protected abstract void          clearLikedQuotesAsynk   ();
   
}
