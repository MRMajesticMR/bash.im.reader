package ru.majestic.bashimreader.quotes.filters;

import java.util.List;

import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.quotes.filters.listeners.OnQuotesFilteredListener;

public interface IQuotesFilter {

   public void startFilter(List<Quote> quotes);
   
   public void setOnQuotesFilteredListener(OnQuotesFilteredListener onQuotesFilteredListener);
   
}
