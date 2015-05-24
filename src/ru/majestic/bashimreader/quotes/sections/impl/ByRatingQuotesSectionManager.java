package ru.majestic.bashimreader.quotes.sections.impl;

import ru.majestic.bashimreader.parsers.IQuotesPageParser;
import ru.majestic.bashimreader.parsers.impl.ByRatingQuotesPageParser;
import ru.majestic.bashimreader.parsers.pagecont.IQuotesPageCountParser;
import ru.majestic.bashimreader.parsers.pagecont.impl.QuotePageCountParser;
import ru.majestic.bashimreader.quotes.sections.IQuoteSectionManagerSkeleton;
import ru.majestic.bashimreader.utils.QuotesDictionary;

public class ByRatingQuotesSectionManager extends IQuoteSectionManagerSkeleton {   
   
   @Override
   protected String generateNextPageDownloadUrl() {      
      return QuotesDictionary.URL_QUOTES_BY_RATING + "/" + getNextPage();     
   }

   @Override
   protected IQuotesPageParser getQuotesPageParser() {
      return new ByRatingQuotesPageParser();
   }
   
   @Override
   protected IQuotesPageCountParser getQuotesPageCountParser() {
      return new QuotePageCountParser();
   }

}
