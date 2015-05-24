package ru.majestic.bashimreader.quotes.sections.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ru.majestic.bashimreader.parsers.IQuotesPageParser;
import ru.majestic.bashimreader.parsers.impl.AbyssBestQuotesParser;
import ru.majestic.bashimreader.quotes.sections.IQuoteSectionManagerSkeleton;
import ru.majestic.bashimreader.utils.QuotesDictionary;

public class AbyssBestQuotesSectionManager extends IQuoteSectionManagerSkeleton {   
   
   private static final long DAY_MILLISECONDS = 86400000;
   
   private Date abyssBestDate;
   
   public AbyssBestQuotesSectionManager() {
      abyssBestDate = Calendar.getInstance().getTime();
   }      
   
   @Override
   protected String generateNextPageDownloadUrl() {      
      return QuotesDictionary.URL_ABYSS_BEST + new SimpleDateFormat("yyyyMMdd").format(abyssBestDate);
   }

   @Override
   protected int changeNextPage(int currentPage) {
      abyssBestDate.setTime(abyssBestDate.getTime() - DAY_MILLISECONDS);
      return super.changeNextPage(currentPage);
   }
   
   @Override
   protected IQuotesPageParser getQuotesPageParser() {
      return new AbyssBestQuotesParser();
   }            

}
