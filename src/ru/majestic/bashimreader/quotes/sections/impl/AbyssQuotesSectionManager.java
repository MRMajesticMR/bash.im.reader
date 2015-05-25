package ru.majestic.bashimreader.quotes.sections.impl;

import java.util.List;

import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.parsers.IQuotesPageParser;
import ru.majestic.bashimreader.parsers.impl.AbyssQuotesPageParser;
import ru.majestic.bashimreader.quotes.filters.IQuotesFilter;
import ru.majestic.bashimreader.quotes.filters.impl.RepeatedQuotesFilter;
import ru.majestic.bashimreader.quotes.sections.IQuoteSectionManagerSkeleton;
import ru.majestic.bashimreader.utils.QuotesDictionary;

public class AbyssQuotesSectionManager extends IQuoteSectionManagerSkeleton {   
   
   private boolean needMoreQuotes;
   
   public AbyssQuotesSectionManager() {
      this.needMoreQuotes = true;
   }
   
   @Override
   public boolean isNeedLoadMorePage() {
      return needMoreQuotes;
   }
   
   @Override
   protected String generateNextPageDownloadUrl() {      
      return QuotesDictionary.URL_ABYSS;
   }

   @Override
   protected IQuotesPageParser getQuotesPageParser() {
      return new AbyssQuotesPageParser();
   }
   
   @Override
   protected IQuotesFilter getQuotesFilter() {
      return new RepeatedQuotesFilter(quotes);
   }
   
   @Override
   public void onQuotesFiltered(List<Quote> filteredQuotes) {
      if(filteredQuotes.size() == 0) {
         needMoreQuotes = false;
      }
      
      super.onQuotesFiltered(filteredQuotes);
   } 
   
   @Override
   public void reset() {
      super.reset();
      
      needMoreQuotes = true;
   }

}
