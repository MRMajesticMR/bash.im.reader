package ru.majestic.bashimreader.quotes.sections.impl;

import ru.majestic.bashimreader.parsers.IQuotesPageParser;
import ru.majestic.bashimreader.parsers.impl.AbyssQuotesPageParser;
import ru.majestic.bashimreader.quotes.sections.IQuoteSectionManagerSkeleton;
import ru.majestic.bashimreader.utils.QuotesDictionary;

public class AbyssQuotesSectionManager extends IQuoteSectionManagerSkeleton {   
   
   @Override
   protected String generateNextPageDownloadUrl() {      
      return QuotesDictionary.URL_ABYSS;
   }

   @Override
   protected IQuotesPageParser getQuotesPageParser() {
      return new AbyssQuotesPageParser();
   }

}
