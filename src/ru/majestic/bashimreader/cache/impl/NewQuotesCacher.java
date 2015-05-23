package ru.majestic.bashimreader.cache.impl;

import java.util.List;

import ru.majestic.bashimreader.cache.IQuotesCacherSkeleton;
import ru.majestic.bashimreader.data.Quote;

public class NewQuotesCacher extends IQuotesCacherSkeleton {

   @Override
   public boolean hasQuotes() {
      return false;
   }

   @Override
   protected void saveQuotesAsynk(List<Quote> quotes) {
      
   }

   @Override
   protected List<Quote> loadQuotesAsynk() {
      return null;
   }

}
