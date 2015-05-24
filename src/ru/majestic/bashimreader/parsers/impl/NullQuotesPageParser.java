package ru.majestic.bashimreader.parsers.impl;

import java.util.List;

import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.parsers.IQuotesPageParserSkeleton;

public class NullQuotesPageParser extends IQuotesPageParserSkeleton {      

   @Override
   protected List<Quote> parsePageContent(String pageContent) throws IllegalArgumentException {
      return null;
   }   


}
