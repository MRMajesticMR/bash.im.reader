package ru.majestic.bashimreader.parsers;

import ru.majestic.bashimreader.parsers.listeners.OnQuotesPagesParsedListener;

public interface IQuotesPageParser {

   
   public void parsePage                     (String pageContent);
   public void setOnQuotesPageParsedListener (OnQuotesPagesParsedListener onQuotesPagesParsedListener);
   
}
