package ru.majestic.bashimreader.quotes.liked.listeners;

import java.util.List;

import ru.majestic.bashimreader.data.Quote;

public interface LikedQuotesHandlerListener {

   public void onLikedQuoteSaved       ();
   public void onLoadLikedQuotesError  ();
   public void onLoadedLikedQuotes     (List<Quote> quotes);
   
}
