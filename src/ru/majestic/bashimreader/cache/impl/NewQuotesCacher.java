package ru.majestic.bashimreader.cache.impl;

import java.util.List;

import ru.majestic.bashimreader.cache.IQuotesCacherSkeleton;
import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.datebase.INewQuotesDatabaseHelper;

public class NewQuotesCacher extends IQuotesCacherSkeleton {

   private INewQuotesDatabaseHelper newQuotesDatabaseHelper;
   
   public NewQuotesCacher(INewQuotesDatabaseHelper newQuotesDatabaseHelper) {
      this.newQuotesDatabaseHelper = newQuotesDatabaseHelper;
   }
   
   @Override
   public boolean hasQuotes() {
      return newQuotesDatabaseHelper.hasQuotes();
   }

   @Override
   protected void saveQuotesAsynk(List<Quote> quotes) {
      for(Quote quote: quotes) {
         if(!newQuotesDatabaseHelper.isQuoteAlreadySaved(quote))
            newQuotesDatabaseHelper.saveNewQuote(quote);
      }
   }

   @Override
   protected List<Quote> loadQuotesAsynk() {
      return newQuotesDatabaseHelper.getNewQuotes();
   }

}
