package ru.majestic.bashimreader.quotes.filters.listeners;

import java.util.List;

import ru.majestic.bashimreader.data.Quote;

public interface OnQuotesFilteredListener {

   public void onQuotesFiltered(List<Quote> filteredQuotes);
   
}
