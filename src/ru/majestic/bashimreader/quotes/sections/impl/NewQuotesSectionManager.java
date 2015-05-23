package ru.majestic.bashimreader.quotes.sections.impl;

import ru.majestic.bashimreader.cache.IQuotesCacher;
import ru.majestic.bashimreader.cache.impl.EmptyQuotesCacher;
import ru.majestic.bashimreader.parsers.IQuotesPageParser;
import ru.majestic.bashimreader.parsers.impl.NewQuotesPageParser;
import ru.majestic.bashimreader.parsers.pagecont.IQuotesPageCountParser;
import ru.majestic.bashimreader.parsers.pagecont.impl.QuotePageCountParser;
import ru.majestic.bashimreader.quotes.sections.IQuoteSectionManagerSkeleton;
import ru.majestic.bashimreader.utils.QuotesDictionary;

public class NewQuotesSectionManager extends IQuoteSectionManagerSkeleton {   
   
   @Override
   protected String generateNextPageDownloadUrl() {      
      return QuotesDictionary.URL_QUOTES_NEW + QuotesDictionary.PREFIX_NEW_QUOTES_PAGE + getNextPage();
   }

   @Override
   protected IQuotesPageParser getQuotesPageParser() {
      return new NewQuotesPageParser();
   }
   
   @Override
   protected int changeNextPage(int currentPage) {
      return --currentPage;
   }
   
   @Override
   protected IQuotesPageCountParser getQuotesPageCountParser() {
      return new QuotePageCountParser();
   }
   
   @Override
   public void onQuotesPageCountParsed(int pageCount) {
      this.nextPage        = pageCount;
      this.maxPageCount    = pageCount;
      
      this.quotesPageParser.parsePage(lastLoadPageContent);
   }

   @Override
   protected IQuotesCacher getQuotesCacher() {
      return new EmptyQuotesCacher();
   }

}
