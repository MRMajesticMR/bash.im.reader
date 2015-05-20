package ru.majestic.bashimreader.quotes.sections.impl;

import ru.majestic.bashimreader.parsers.IQuotesPageParser;
import ru.majestic.bashimreader.parsers.impl.NewQuotesPageParser;
import ru.majestic.bashimreader.quotes.sections.IQuoteSectionManagerSkeleton;
import ru.majestic.bashimreader.utils.QuotesDictionary;

public class NewQuotesSectionManager extends IQuoteSectionManagerSkeleton {

   @Override
   protected String generateNextPageDownloadUrl() {      
      if (getNextPage() == 0)
         return QuotesDictionary.URL_QUOTES_NEW;
      else
         return QuotesDictionary.URL_QUOTES_NEW + QuotesDictionary.PREFIX_NEW_QUOTES_PAGE + getNextPage();
   }

   @Override
   protected IQuotesPageParser getQuotesPageParser() {
      return new NewQuotesPageParser();
   }

}
