package ru.majestic.bashimreader.quotes.sections.impl;

import ru.majestic.bashimreader.parsers.IQuotesPageParser;
import ru.majestic.bashimreader.parsers.impl.AbyssTopQuotesPageParser;
import ru.majestic.bashimreader.quotes.sections.IQuoteSectionManagerSkeleton;
import ru.majestic.bashimreader.utils.QuotesDictionary;

public class AbyssTopQuotesSectionManager extends IQuoteSectionManagerSkeleton {   
   
   @Override
   protected String generateNextPageDownloadUrl() {      
      return QuotesDictionary.URL_ABYSS_TOP;
   }

   @Override
   protected IQuotesPageParser getQuotesPageParser() {
      return new AbyssTopQuotesPageParser();
   }
   
   public boolean isNeedLoadMorePage() {
      return false;
   }

}
