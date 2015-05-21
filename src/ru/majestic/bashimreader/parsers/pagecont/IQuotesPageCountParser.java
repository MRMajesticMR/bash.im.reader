package ru.majestic.bashimreader.parsers.pagecont;

import ru.majestic.bashimreader.parsers.listeners.OnQuotesPageCountParsedListener;

public interface IQuotesPageCountParser {

   public void parse                               (String pageContent);
   public void setOnQuotesPageCountParsedListener  (OnQuotesPageCountParsedListener onQuotesPageCountParsedListener);   
   
}
