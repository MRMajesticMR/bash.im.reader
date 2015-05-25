package ru.majestic.bashimreader.quotes.filters.impl;

import java.util.List;

import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.quotes.filters.IAsynkQuotesFilterSkeleton;

public class RepeatedQuotesFilter extends IAsynkQuotesFilterSkeleton {
   
   
   private List<Quote> allQuotes;
   
   public RepeatedQuotesFilter(List<Quote> allQuotes) {
      this.allQuotes = allQuotes;
   }

   @Override
   protected boolean isQuoteAccessed(Quote quote) {

      for(Quote quoteFromAll: allQuotes) {
         if(quoteFromAll.getId() == quote.getId())
            return false;
      }
      
      return true;
   }
}
