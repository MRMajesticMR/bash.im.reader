package ru.majestic.bashimreader.quotes.sections.impl;

import ru.majestic.bashimreader.cache.IQuotesCacher;
import ru.majestic.bashimreader.cache.impl.EmptyQuotesCacher;
import ru.majestic.bashimreader.parsers.IQuotesPageParser;
import ru.majestic.bashimreader.parsers.impl.RandomQuotesPageParser;
import ru.majestic.bashimreader.parsers.pagecont.IQuotesPageCountParser;
import ru.majestic.bashimreader.parsers.pagecont.impl.EmptyQuotePageCountParser;
import ru.majestic.bashimreader.quotes.sections.IQuoteSectionManagerSkeleton;
import ru.majestic.bashimreader.utils.QuotesDictionary;

public class RandomQuotesSectionManager extends IQuoteSectionManagerSkeleton {   
   
   @Override
   protected String generateNextPageDownloadUrl() {      
      return QuotesDictionary.URL_QUOTES_RANDOM;
   }

   @Override
   protected IQuotesPageParser getQuotesPageParser() {
      return new RandomQuotesPageParser();
   }
   
   @Override
   protected IQuotesPageCountParser getQuotesPageCountParser() {
      return new EmptyQuotePageCountParser();
   }

   @Override
   protected IQuotesCacher getQuotesCacher() {
      return new EmptyQuotesCacher();
   }
}
