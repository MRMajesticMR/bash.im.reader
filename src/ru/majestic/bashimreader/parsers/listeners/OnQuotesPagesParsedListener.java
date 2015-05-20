package ru.majestic.bashimreader.parsers.listeners;

import java.util.List;

import ru.majestic.bashimreader.data.Quote;

public interface OnQuotesPagesParsedListener {

   public void onQuotesPageParsed      (List<Quote> quotes);
   public void onQuotePageParseError   ();
   
}
