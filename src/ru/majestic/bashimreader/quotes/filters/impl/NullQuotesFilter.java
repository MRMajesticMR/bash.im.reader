package ru.majestic.bashimreader.quotes.filters.impl;

import java.util.List;

import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.quotes.filters.IQuotesFilter;
import ru.majestic.bashimreader.quotes.filters.listeners.OnQuotesFilteredListener;

public class NullQuotesFilter implements IQuotesFilter {

   private OnQuotesFilteredListener onQuotesFilteredListener; 
   
   @Override
   public void startFilter(List<Quote> quotes) {
      onQuotesFilteredListener.onQuotesFiltered(quotes);
   }

   @Override
   public void setOnQuotesFilteredListener(OnQuotesFilteredListener onQuotesFilteredListener) {
      this.onQuotesFilteredListener = onQuotesFilteredListener;      
   }

}
