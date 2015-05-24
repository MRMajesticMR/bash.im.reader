package ru.majestic.bashimreader.quotes.liked.impl;

import java.util.List;

import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.datebase.IQuotesDatabaseHelper;
import ru.majestic.bashimreader.quotes.liked.ILikedQuotesHandlerSkeleton;

public class LikedQuotesHandler extends ILikedQuotesHandlerSkeleton {

   private IQuotesDatabaseHelper quotesDatabaseHelper;
   
   public LikedQuotesHandler(IQuotesDatabaseHelper quotesDatabaseHelper) {
      this.quotesDatabaseHelper = quotesDatabaseHelper;
   }
   
   @Override
   protected void saveLikedQuoteAsynk(Quote quote) {
      if(!quotesDatabaseHelper.isQuoteAlreadyLiked(quote))
         quotesDatabaseHelper.saveLikedQuote(quote);
   }

   @Override
   protected List<Quote> loadLikedQuotesAsynk() {
      return quotesDatabaseHelper.getLikedQuotes();
   }

   @Override
   protected void clearLikedQuotesAsynk() {
      quotesDatabaseHelper.clearLikedQuotes();      
   }

}
