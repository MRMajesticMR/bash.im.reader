package ru.majestic.bashimreader.quotes.sections.impl;

import ru.majestic.bashimreader.parsers.IQuotesPageParser;
import ru.majestic.bashimreader.parsers.impl.BestQuotesPageParser;
import ru.majestic.bashimreader.quotes.sections.IQuoteSectionManagerSkeleton;
import ru.majestic.bashimreader.utils.QuotesDictionary;

public class BestQuotesSectionManager extends IQuoteSectionManagerSkeleton {   
      
   @Override
   protected String generateNextPageDownloadUrl() {
      return QuotesDictionary.URL_QUOTES_BEST;
   }

   @Override
   protected IQuotesPageParser getQuotesPageParser() {
      return new BestQuotesPageParser();
   }   
   
   @Override
   public void loadNextPage() {
      newQuotesPrepearing = true;
      pageLoader.load(generateNextPageDownloadUrl());
   }
   
   @Override
   public boolean isNeedLoadMorePage() {
      return false;
   }

}
