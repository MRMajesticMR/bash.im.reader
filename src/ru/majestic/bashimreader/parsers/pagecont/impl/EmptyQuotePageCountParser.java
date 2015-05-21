package ru.majestic.bashimreader.parsers.pagecont.impl;

import ru.majestic.bashimreader.parsers.listeners.OnQuotesPageCountParsedListener;
import ru.majestic.bashimreader.parsers.pagecont.IQuotesPageCountParser;

public class EmptyQuotePageCountParser implements IQuotesPageCountParser {

   private OnQuotesPageCountParsedListener onQuotesPageCountParsedListener;
   
   @Override
   public void parse(String pageContent) {
      onQuotesPageCountParsedListener.onQuotesPageCountParsed(-1);      
   }

   @Override
   public void setOnQuotesPageCountParsedListener(OnQuotesPageCountParsedListener onQuotesPageCountParsedListener) {
      this.onQuotesPageCountParsedListener = onQuotesPageCountParsedListener;
   }

}
