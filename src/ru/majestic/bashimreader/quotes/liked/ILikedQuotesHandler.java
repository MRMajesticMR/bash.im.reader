package ru.majestic.bashimreader.quotes.liked;

import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.quotes.liked.listeners.LikedQuotesHandlerListener;

public interface ILikedQuotesHandler {

   public void          saveLikedQuote    (Quote quote);
   public void          loadLikedQuotes   ();
   public void          clearLikedQuotes  ();
   
   public void setLikedQuotesHandlerListener(LikedQuotesHandlerListener likedQuotesHandlerListener);
   
}
