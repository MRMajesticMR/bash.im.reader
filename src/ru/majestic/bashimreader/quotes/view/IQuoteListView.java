package ru.majestic.bashimreader.quotes.view;

import java.util.List;

import ru.majestic.bashimreader.data.Quote;

public interface IQuoteListView {
   
   public void addQuotes   (List<Quote> quotes);
   public void clear       ();
   
}
